package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.support.v4.app.INotificationSideChannel.Stub;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NotificationManagerCompat {
	public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
	public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
	private static final Impl IMPL;
	private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
	private static final int SIDE_CHANNEL_BIND_FLAGS;
	private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
	private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
	private static final String TAG = "NotifManCompat";
	private static Set<String> sEnabledNotificationListenerPackages;
	private static String sEnabledNotificationListeners;
	private static final Object sEnabledNotificationListenersLock;
	private static final Object sLock;
	private static SideChannelManager sSideChannelManager;
	private final Context mContext;
	private final NotificationManager mNotificationManager;

	static interface Impl {
		public void cancelNotification(NotificationManager r1_NotificationManager, String r2_String, int r3i);

		public int getSideChannelBindFlags();

		public void postNotification(NotificationManager r1_NotificationManager, String r2_String, int r3i, Notification r4_Notification);
	}

	private static class ServiceConnectedEvent {
		final ComponentName componentName;
		final IBinder iBinder;

		public ServiceConnectedEvent(ComponentName componentName, IBinder iBinder) {
			super();
			this.componentName = componentName;
			this.iBinder = iBinder;
		}
	}

	private static class SideChannelManager implements Callback, ServiceConnection {
		private static final String KEY_BINDER = "binder";
		private static final int MSG_QUEUE_TASK = 0;
		private static final int MSG_RETRY_LISTENER_QUEUE = 3;
		private static final int MSG_SERVICE_CONNECTED = 1;
		private static final int MSG_SERVICE_DISCONNECTED = 2;
		private Set<String> mCachedEnabledPackages;
		private final Context mContext;
		private final Handler mHandler;
		private final HandlerThread mHandlerThread;
		private final Map<ComponentName, ListenerRecord> mRecordMap;

		private static class ListenerRecord {
			public boolean bound;
			public final ComponentName componentName;
			public int retryCount;
			public INotificationSideChannel service;
			public LinkedList<NotificationManagerCompat.Task> taskQueue;

			public ListenerRecord(ComponentName componentName) {
				super();
				bound = false;
				taskQueue = new LinkedList();
				retryCount = 0;
				this.componentName = componentName;
			}
		}


		public SideChannelManager(Context context) {
			super();
			mRecordMap = new HashMap();
			mCachedEnabledPackages = new HashSet();
			mContext = context;
			mHandlerThread = new HandlerThread("NotificationManagerCompat");
			mHandlerThread.start();
			mHandler = new Handler(mHandlerThread.getLooper(), this);
		}

		private boolean ensureServiceBound(ListenerRecord record) {
			if (record.bound) {
				return true;
			} else {
				record.bound = mContext.bindService(new Intent(ACTION_BIND_SIDE_CHANNEL).setComponent(record.componentName), this, SIDE_CHANNEL_BIND_FLAGS);
				if (record.bound) {
					record.retryCount = 0;
				} else {
					Log.w(TAG, "Unable to bind to listener " + record.componentName);
					mContext.unbindService(this);
				}
				return record.bound;
			}
		}

		private void ensureServiceUnbound(ListenerRecord record) {
			if (record.bound) {
				mContext.unbindService(this);
				record.bound = false;
			}
			record.service = null;
		}

		private void handleQueueTask(NotificationManagerCompat.Task task) {
			updateListenerMap();
			Iterator i$ = mRecordMap.values().iterator();
			while (i$.hasNext()) {
				ListenerRecord record = (ListenerRecord) i$.next();
				record.taskQueue.add(task);
				processListenerQueue(record);
			}
		}

		private void handleRetryListenerQueue(ComponentName componentName) {
			ListenerRecord record = (ListenerRecord) mRecordMap.get(componentName);
			if (record != null) {
				processListenerQueue(record);
			}
		}

		private void handleServiceConnected(ComponentName componentName, IBinder iBinder) {
			ListenerRecord record = (ListenerRecord) mRecordMap.get(componentName);
			if (record != null) {
				record.service = Stub.asInterface(iBinder);
				record.retryCount = 0;
				processListenerQueue(record);
			}
		}

		private void handleServiceDisconnected(ComponentName componentName) {
			ListenerRecord record = (ListenerRecord) mRecordMap.get(componentName);
			if (record != null) {
				ensureServiceUnbound(record);
			}
		}

		private void processListenerQueue(ListenerRecord record) {
			if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
				Log.d(TAG, "Processing component " + record.componentName + ", " + record.taskQueue.size() + " queued tasks");
			}
			if (record.taskQueue.isEmpty()) {
			} else if (!ensureServiceBound(record) || record.service == null) {
				scheduleListenerRetry(record);
			} else {
				while (true) {
					NotificationManagerCompat.Task task = record.taskQueue.peek();
					if (task == null) {
						if (!record.taskQueue.isEmpty()) {
							scheduleListenerRetry(record);
							return;
						} else {
							return;
						}
					} else {
						try {
							if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
								Log.d(TAG, "Sending task " + task);
							}
							task.send(record.service);
							record.taskQueue.remove();
						} catch (DeadObjectException e) {
							if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
								Log.d(TAG, "Remote service has died: " + record.componentName);
							}
						} catch (RemoteException e_2) {
							Log.w(TAG, "RemoteException communicating with " + record.componentName, e_2);
						}
					}
				}
			}
		}

		private void scheduleListenerRetry(ListenerRecord record) {
			if (mHandler.hasMessages(MSG_RETRY_LISTENER_QUEUE, record.componentName)) {
			} else {
				record.retryCount++;
				if (record.retryCount > 6) {
					Log.w(TAG, "Giving up on delivering " + record.taskQueue.size() + " tasks to " + record.componentName + " after " + record.retryCount + " retries");
					record.taskQueue.clear();
				} else {
					int delayMs = (1 << (record.retryCount - 1)) * 1000;
					if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
						Log.d(TAG, "Scheduling retry for " + delayMs + " ms");
					}
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RETRY_LISTENER_QUEUE, record.componentName), (long) delayMs);
				}
			}
		}

		private void updateListenerMap() {
			Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(mContext);
			if (enabledPackages.equals(mCachedEnabledPackages)) {
			} else {
				ComponentName componentName;
				mCachedEnabledPackages = enabledPackages;
				Set<ComponentName> enabledComponents = new HashSet();
				Iterator i$ = mContext.getPackageManager().queryIntentServices(new Intent().setAction(ACTION_BIND_SIDE_CHANNEL), TransportMediator.FLAG_KEY_MEDIA_PLAY).iterator();
				while (i$.hasNext()) {
					ResolveInfo resolveInfo = (ResolveInfo) i$.next();
					if (enabledPackages.contains(resolveInfo.serviceInfo.packageName)) {
						componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
						if (resolveInfo.serviceInfo.permission != null) {
							Log.w(TAG, "Permission present on component " + componentName + ", not adding listener record.");
						} else {
							enabledComponents.add(componentName);
						}
					}
				}
				i$ = enabledComponents.iterator();
				while (i$.hasNext()) {
					componentName = i$.next();
					if (!mRecordMap.containsKey(componentName)) {
						if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
							Log.d(TAG, "Adding listener record for " + componentName);
						}
						mRecordMap.put(componentName, new ListenerRecord(componentName));
					}
				}
				Iterator<Entry<ComponentName, ListenerRecord>> it = mRecordMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<ComponentName, ListenerRecord> entry = (Entry) it.next();
					if (!enabledComponents.contains(entry.getKey())) {
						if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
							Log.d(TAG, "Removing listener record for " + entry.getKey());
						}
						ensureServiceUnbound((ListenerRecord) entry.getValue());
						it.remove();
					}
				}
			}
		}

		public boolean handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_QUEUE_TASK:
				handleQueueTask((NotificationManagerCompat.Task) msg.obj);
				return true;
			case MSG_SERVICE_CONNECTED:
				NotificationManagerCompat.ServiceConnectedEvent event = (NotificationManagerCompat.ServiceConnectedEvent) msg.obj;
				handleServiceConnected(event.componentName, event.iBinder);
				return true;
			case MSG_SERVICE_DISCONNECTED:
				handleServiceDisconnected((ComponentName) msg.obj);
				return true;
			case MSG_RETRY_LISTENER_QUEUE:
				handleRetryListenerQueue((ComponentName) msg.obj);
				return true;
			}
			return false;
		}

		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
				Log.d(TAG, "Connected to service " + componentName);
			}
			mHandler.obtainMessage(MSG_SERVICE_CONNECTED, new NotificationManagerCompat.ServiceConnectedEvent(componentName, iBinder)).sendToTarget();
		}

		public void onServiceDisconnected(ComponentName componentName) {
			if (Log.isLoggable(TAG, MSG_RETRY_LISTENER_QUEUE)) {
				Log.d(TAG, "Disconnected from service " + componentName);
			}
			mHandler.obtainMessage(MSG_SERVICE_DISCONNECTED, componentName).sendToTarget();
		}

		public void queueTask(NotificationManagerCompat.Task task) {
			mHandler.obtainMessage(MSG_QUEUE_TASK, task).sendToTarget();
		}
	}

	private static interface Task {
		public void send(INotificationSideChannel r1_INotificationSideChannel) throws RemoteException;
	}

	private static class CancelTask implements NotificationManagerCompat.Task {
		final boolean all;
		final int id;
		final String packageName;
		final String tag;

		public CancelTask(String packageName) {
			super();
			this.packageName = packageName;
			id = 0;
			tag = null;
			all = true;
		}

		public CancelTask(String packageName, int id, String tag) {
			super();
			this.packageName = packageName;
			this.id = id;
			this.tag = tag;
			all = false;
		}

		public void send(INotificationSideChannel service) throws RemoteException {
			if (all) {
				service.cancelAll(packageName);
			} else {
				service.cancel(packageName, id, tag);
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("CancelTask[");
			sb.append("packageName:").append(packageName);
			sb.append(", id:").append(id);
			sb.append(", tag:").append(tag);
			sb.append(", all:").append(all);
			sb.append("]");
			return sb.toString();
		}
	}

	static class ImplBase implements NotificationManagerCompat.Impl {
		ImplBase() {
			super();
		}

		public void cancelNotification(NotificationManager notificationManager, String tag, int id) {
			notificationManager.cancel(id);
		}

		public int getSideChannelBindFlags() {
			return 1;
		}

		public void postNotification(NotificationManager notificationManager, String tag, int id, Notification notification) {
			notificationManager.notify(id, notification);
		}
	}

	private static class NotifyTask implements NotificationManagerCompat.Task {
		final int id;
		final Notification notif;
		final String packageName;
		final String tag;

		public NotifyTask(String packageName, int id, String tag, Notification notif) {
			super();
			this.packageName = packageName;
			this.id = id;
			this.tag = tag;
			this.notif = notif;
		}

		public void send(INotificationSideChannel service) throws RemoteException {
			service.notify(packageName, id, tag, notif);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("NotifyTask[");
			sb.append("packageName:").append(packageName);
			sb.append(", id:").append(id);
			sb.append(", tag:").append(tag);
			sb.append("]");
			return sb.toString();
		}
	}

	static class ImplEclair extends NotificationManagerCompat.ImplBase {
		ImplEclair() {
			super();
		}

		public void cancelNotification(NotificationManager notificationManager, String tag, int id) {
			NotificationManagerCompatEclair.cancelNotification(notificationManager, tag, id);
		}

		public void postNotification(NotificationManager notificationManager, String tag, int id, Notification notification) {
			NotificationManagerCompatEclair.postNotification(notificationManager, tag, id, notification);
		}
	}

	static class ImplIceCreamSandwich extends NotificationManagerCompat.ImplEclair {
		ImplIceCreamSandwich() {
			super();
		}

		public int getSideChannelBindFlags() {
			return 33;
		}
	}


	static {
		sEnabledNotificationListenersLock = new Object();
		sEnabledNotificationListenerPackages = new HashSet();
		sLock = new Object();
		if (VERSION.SDK_INT >= 14) {
			IMPL = new ImplIceCreamSandwich();
		} else if (VERSION.SDK_INT >= 5) {
			IMPL = new ImplEclair();
		} else {
			IMPL = new ImplBase();
		}
		SIDE_CHANNEL_BIND_FLAGS = IMPL.getSideChannelBindFlags();
	}

	private NotificationManagerCompat(Context context) {
		super();
		mContext = context;
		mNotificationManager = (NotificationManager) mContext.getSystemService("notification");
	}

	public static NotificationManagerCompat from(Context context) {
		return new NotificationManagerCompat(context);
	}

	public static Set<String> getEnabledListenerPackages(Context context) {
		String enabledNotificationListeners = Secure.getString(context.getContentResolver(), SETTING_ENABLED_NOTIFICATION_LISTENERS);
		if (enabledNotificationListeners == null || enabledNotificationListeners.equals(sEnabledNotificationListeners)) {
			return sEnabledNotificationListenerPackages;
		} else {
			String[] components = enabledNotificationListeners.split(":");
			Set<String> packageNames = new HashSet(components.length);
			String[] arr$ = components;
			int i$ = SIDE_CHANNEL_BIND_FLAGS;
			while (i$ < arr$.length) {
				ComponentName componentName = ComponentName.unflattenFromString(arr$[i$]);
				if (componentName != null) {
					packageNames.add(componentName.getPackageName());
				}
				i$++;
			}
			synchronized(sEnabledNotificationListenersLock) {
				sEnabledNotificationListenerPackages = packageNames;
				sEnabledNotificationListeners = enabledNotificationListeners;
			}
			return sEnabledNotificationListenerPackages;
		}
	}

	private void pushSideChannelQueue(Task task) {
		synchronized(sLock) {
			if (sSideChannelManager == null) {
				sSideChannelManager = new SideChannelManager(mContext.getApplicationContext());
			}
		}
		sSideChannelManager.queueTask(task);
	}

	private static boolean useSideChannelForNotification(Notification notification) {
		Bundle extras = NotificationCompat.getExtras(notification);
		if (extras == null || !extras.getBoolean(EXTRA_USE_SIDE_CHANNEL)) {
			return false;
		} else {
			return true;
		}
	}

	public void cancel(int id) {
		cancel(null, id);
	}

	public void cancel(String tag, int id) {
		IMPL.cancelNotification(mNotificationManager, tag, id);
		pushSideChannelQueue(new CancelTask(mContext.getPackageName(), id, tag));
	}

	public void cancelAll() {
		mNotificationManager.cancelAll();
		pushSideChannelQueue(new CancelTask(mContext.getPackageName()));
	}

	public void notify(int id, Notification notification) {
		notify(null, id, notification);
	}

	public void notify(String tag, int id, Notification notification) {
		if (useSideChannelForNotification(notification)) {
			pushSideChannelQueue(new NotifyTask(mContext.getPackageName(), id, tag, notification));
		} else {
			IMPL.postNotification(mNotificationManager, tag, id, notification);
		}
	}
}
