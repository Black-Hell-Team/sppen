package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.BigTextStyle;
import android.app.Notification.InboxStyle;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompatBase.Action;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NotificationCompatJellybean {
	static final String EXTRA_ACTION_EXTRAS = "android.support.actionExtras";
	static final String EXTRA_GROUP_KEY = "android.support.groupKey";
	static final String EXTRA_GROUP_SUMMARY = "android.support.isGroupSummary";
	static final String EXTRA_LOCAL_ONLY = "android.support.localOnly";
	static final String EXTRA_REMOTE_INPUTS = "android.support.remoteInputs";
	static final String EXTRA_SORT_KEY = "android.support.sortKey";
	static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
	private static final String KEY_ACTION_INTENT = "actionIntent";
	private static final String KEY_EXTRAS = "extras";
	private static final String KEY_ICON = "icon";
	private static final String KEY_REMOTE_INPUTS = "remoteInputs";
	private static final String KEY_TITLE = "title";
	public static final String TAG = "NotificationCompat";
	private static Class<?> sActionClass;
	private static Field sActionIconField;
	private static Field sActionIntentField;
	private static Field sActionTitleField;
	private static boolean sActionsAccessFailed;
	private static Field sActionsField;
	private static final Object sActionsLock;
	private static Field sExtrasField;
	private static boolean sExtrasFieldAccessFailed;
	private static final Object sExtrasLock;

	public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
		private android.app.Notification.Builder b;
		private List<Bundle> mActionExtrasList;
		private final Bundle mExtras;

		public Builder(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int mProgressMax, int mProgress, boolean mProgressIndeterminate, boolean useChronometer, int priority, CharSequence subText, boolean localOnly, Bundle extras, String groupKey, boolean groupSummary, String sortKey) {
			boolean r3z;
			super();
			mActionExtrasList = new ArrayList();
			android.app.Notification.Builder r4_android_app_Notification_Builder = new android.app.Notification.Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS);
			if ((n.flags & 2) != 0) {
				r3z = true;
			} else {
				r3z = false;
			}
			r4_android_app_Notification_Builder = r4_android_app_Notification_Builder.setOngoing(r3z);
			if ((n.flags & 8) != 0) {
				r3z = true;
			} else {
				r3z = false;
			}
			r4_android_app_Notification_Builder = r4_android_app_Notification_Builder.setOnlyAlertOnce(r3z);
			if ((n.flags & 16) != 0) {
				r3z = true;
			} else {
				r3z = false;
			}
			r4_android_app_Notification_Builder = r4_android_app_Notification_Builder.setAutoCancel(r3z).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent);
			if ((n.flags & 128) != 0) {
				r3z = true;
			} else {
				r3z = false;
			}
			b = r4_android_app_Notification_Builder.setFullScreenIntent(fullScreenIntent, r3z).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(mProgressMax, mProgress, mProgressIndeterminate);
			mExtras = new Bundle();
			if (extras != null) {
				mExtras.putAll(extras);
			}
			if (localOnly) {
				mExtras.putBoolean(EXTRA_LOCAL_ONLY, true);
			}
			if (groupKey != null) {
				mExtras.putString(EXTRA_GROUP_KEY, groupKey);
				if (groupSummary) {
					mExtras.putBoolean(EXTRA_GROUP_SUMMARY, true);
				} else {
					mExtras.putBoolean(EXTRA_USE_SIDE_CHANNEL, true);
				}
			}
			if (sortKey != null) {
				mExtras.putString(EXTRA_SORT_KEY, sortKey);
			}
		}

		public void addAction(Action action) {
			mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(b, action));
		}

		public Notification build() {
			Notification notif = b.build();
			Bundle extras = NotificationCompatJellybean.getExtras(notif);
			Bundle mergeBundle = new Bundle(mExtras);
			Iterator i$ = mExtras.keySet().iterator();
			while (i$.hasNext()) {
				String key = (String) i$.next();
				if (extras.containsKey(key)) {
					mergeBundle.remove(key);
				}
			}
			extras.putAll(mergeBundle);
			SparseArray<Bundle> actionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
			if (actionExtrasMap != null) {
				NotificationCompatJellybean.getExtras(notif).putSparseParcelableArray(EXTRA_ACTION_EXTRAS, actionExtrasMap);
			}
			return notif;
		}

		public android.app.Notification.Builder getBuilder() {
			return b;
		}
	}


	static {
		sExtrasLock = new Object();
		sActionsLock = new Object();
	}

	NotificationCompatJellybean() {
		super();
	}

	public static void addBigPictureStyle(NotificationBuilderWithBuilderAccessor b, CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, Bitmap bigPicture, Bitmap bigLargeIcon, boolean bigLargeIconSet) {
		BigPictureStyle style = new BigPictureStyle(b.getBuilder()).setBigContentTitle(bigContentTitle).bigPicture(bigPicture);
		if (bigLargeIconSet) {
			style.bigLargeIcon(bigLargeIcon);
		}
		if (useSummary) {
			style.setSummaryText(summaryText);
		}
	}

	public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor b, CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, CharSequence bigText) {
		BigTextStyle style = new BigTextStyle(b.getBuilder()).setBigContentTitle(bigContentTitle).bigText(bigText);
		if (useSummary) {
			style.setSummaryText(summaryText);
		}
	}

	public static void addInboxStyle(NotificationBuilderWithBuilderAccessor b, CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, ArrayList<CharSequence> texts) {
		InboxStyle style = new InboxStyle(b.getBuilder()).setBigContentTitle(bigContentTitle);
		if (useSummary) {
			style.setSummaryText(summaryText);
		}
		Iterator i$ = texts.iterator();
		while (i$.hasNext()) {
			style.addLine((CharSequence) i$.next());
		}
	}

	public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> actionExtrasList) {
		SparseArray<Bundle> actionExtrasMap = null;
		int i = 0;
		while (i < actionExtrasList.size()) {
			Bundle actionExtras = (Bundle) actionExtrasList.get(i);
			if (actionExtras != null) {
				if (actionExtrasMap == null) {
					actionExtrasMap = new SparseArray();
				}
				actionExtrasMap.put(i, actionExtras);
			}
			i++;
		}
		return actionExtrasMap;
	}

	private static boolean ensureActionReflectionReadyLocked() {
		boolean r1z = true;
		if (sActionsAccessFailed) {
			return false;
		} else {
			try {
				if (sActionsField == null) {
					sActionClass = Class.forName("android.app.Notification$Action");
					sActionIconField = sActionClass.getDeclaredField(KEY_ICON);
					sActionTitleField = sActionClass.getDeclaredField(KEY_TITLE);
					sActionIntentField = sActionClass.getDeclaredField(KEY_ACTION_INTENT);
					sActionsField = Notification.class.getDeclaredField("actions");
					sActionsField.setAccessible(true);
				}
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "Unable to access notification actions", e);
				sActionsAccessFailed = true;
			} catch (NoSuchFieldException e_2) {
				Log.e(TAG, "Unable to access notification actions", e_2);
				sActionsAccessFailed = true;
			}
			if (!sActionsAccessFailed) {
				return r1z;
			} else {
				r1z = false;
				return r1z;
			}
		}
	}

	public static Action getAction(Notification notif, int actionIndex, Factory factory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		Action r0_Action;
		synchronized(sActionsLock) {
			Bundle actionExtras;
			Bundle extras;
			try {
				Object actionObject = getActionObjectsLocked(notif)[actionIndex];
				actionExtras = null;
				extras = getExtras(notif);
				if (extras != null) {
					SparseArray<Bundle> actionExtrasMap = extras.getSparseParcelableArray(EXTRA_ACTION_EXTRAS);
					if (actionExtrasMap != null) {
						actionExtras = actionExtrasMap.get(actionIndex);
					}
				}
				r0_Action = readAction(factory, remoteInputFactory, sActionIconField.getInt(actionObject), (CharSequence) sActionTitleField.get(actionObject), (PendingIntent) sActionIntentField.get(actionObject), actionExtras);
			} catch (IllegalAccessException e) {
				Log.e(TAG, "Unable to access notification actions", e);
				sActionsAccessFailed = true;
				r0_Action = null;
			}
		}
		return r0_Action;
	}

	public static int getActionCount(Notification notif) {
		int r1i;
		synchronized(sActionsLock) {
			Object[] actionObjects = getActionObjectsLocked(notif);
			if (actionObjects != null) {
				r1i = actionObjects.length;
			} else {
				r1i = 0;
			}
		}
		return r1i;
	}

	private static Action getActionFromBundle(Bundle bundle, Factory actionFactory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		return actionFactory.build(bundle.getInt(KEY_ICON), bundle.getCharSequence(KEY_TITLE), (PendingIntent) bundle.getParcelable(KEY_ACTION_INTENT), bundle.getBundle(KEY_EXTRAS), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, KEY_REMOTE_INPUTS), remoteInputFactory));
	}

	private static Object[] getActionObjectsLocked(Notification notif) {
		Object r3_Object = sActionsLock;
		synchronized(r3_Object) {
			try {
				if (!ensureActionReflectionReadyLocked()) {
					return null;
				} else {
					try {
						Object[] r1_Object_A = (Object[]) sActionsField.get(notif);
						return r1_Object_A;
					} catch (IllegalAccessException e) {
						Log.e(TAG, "Unable to access notification actions", e);
						sActionsAccessFailed = true;
						return null;
					}
				}
			} catch (Throwable th) {
				return th;
			}
		}
	}

	public static Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables, Factory actionFactory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		if (parcelables == null) {
			return null;
		} else {
			Action[] actions = actionFactory.newArray(parcelables.size());
			int i = 0;
			while (i < actions.length) {
				actions[i] = getActionFromBundle((Bundle) parcelables.get(i), actionFactory, remoteInputFactory);
				i++;
			}
			return actions;
		}
	}

	private static Bundle getBundleForAction(Action action) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_ICON, action.getIcon());
		bundle.putCharSequence(KEY_TITLE, action.getTitle());
		bundle.putParcelable(KEY_ACTION_INTENT, action.getActionIntent());
		bundle.putBundle(KEY_EXTRAS, action.getExtras());
		bundle.putParcelableArray(KEY_REMOTE_INPUTS, RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
		return bundle;
	}

	/* JADX WARNING: inconsistent code */
	/*
	public static android.os.Bundle getExtras(android.app.Notification r7_notif) {
		r3 = 0;
		r4 = sExtrasLock;
		monitor-enter(r4);
		r5 = sExtrasFieldAccessFailed;	 //Catch:{ all -> 0x004c }
		if (r5 == 0) goto L_0x000b;
	L_0x0008:
		monitor-exit(r4);	 //Catch:{ all -> 0x004c }
		r1 = r3;
	L_0x000a:
		return r1;
	L_0x000b:
		r5 = sExtrasField;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		if (r5 != 0) goto L_0x0036;
	L_0x000f:
		r5 = android.app.Notification.class;
		r6 = "extras";
		r2 = r5.getDeclaredField(r6);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r5 = android.os.Bundle.class;
		r6 = r2_extrasField.getType();	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r5 = r5.isAssignableFrom(r6);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		if (r5 != 0) goto L_0x0030;
	L_0x0023:
		r5 = "NotificationCompat";
		r6 = "Notification.extras field is not of type Bundle";
		android.util.Log.e(r5, r6);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r5 = 1;
		sExtrasFieldAccessFailed = r5;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		monitor-exit(r4);	 //Catch:{ all -> 0x004c }
		r1 = r3;
		goto L_0x000a;
	L_0x0030:
		r5 = 1;
		r2_extrasField.setAccessible(r5);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		sExtrasField = r2_extrasField;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
	L_0x0036:
		r5 = sExtrasField;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r1 = r5.get(r7_notif);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r1 = (android.os.Bundle) r1;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		if (r1_extras != 0) goto L_0x004a;
	L_0x0040:
		r1_extras = new android.os.Bundle;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r1_extras.<init>();	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r5 = sExtrasField;	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
		r5.set(r7_notif, r1_extras);	 //Catch:{ IllegalAccessException -> 0x004f, NoSuchFieldException -> 0x005d }
	L_0x004a:
		monitor-exit(r4);	 //Catch:{ all -> 0x004c }
		goto L_0x000a;
	L_0x004c:
		r3 = move-exception;
		monitor-exit(r4);	 //Catch:{ all -> 0x004c }
		throw r3;
	L_0x004f:
		r0 = move-exception;
		r5 = "NotificationCompat";
		r6 = "Unable to access notification extras";
		android.util.Log.e(r5, r6, r0_e);	 //Catch:{ all -> 0x004c }
	L_0x0057:
		r5 = 1;
		sExtrasFieldAccessFailed = r5;	 //Catch:{ all -> 0x004c }
		monitor-exit(r4);	 //Catch:{ all -> 0x004c }
		r1 = r3;
		goto L_0x000a;
	L_0x005d:
		r0_e = move-exception;
		r5 = "NotificationCompat";
		r6 = "Unable to access notification extras";
		android.util.Log.e(r5, r6, r0);	 //Catch:{ all -> 0x004c }
		goto L_0x0057;
	}
	*/
	public static Bundle getExtras(Notification notif) {
		Object r4_Object = sExtrasLock;
		synchronized(r4_Object) {
			try {
				if (sExtrasFieldAccessFailed) {
					return null;
				} else {
					Bundle extras;
					try {
						if (sExtrasField == null) {
							Field extrasField = Notification.class.getDeclaredField(KEY_EXTRAS);
							if (!Bundle.class.isAssignableFrom(extrasField.getType())) {
								Log.e(TAG, "Notification.extras field is not of type Bundle");
								sExtrasFieldAccessFailed = true;
								return null;
							} else {
								extrasField.setAccessible(true);
								sExtrasField = extrasField;
							}
						}
						extras = (Bundle) sExtrasField.get(notif);
						if (extras == null) {
							extras = new Bundle();
							sExtrasField.set(notif, extras);
						}
						return extras;
					} catch (IllegalAccessException e) {
						Log.e(TAG, "Unable to access notification extras", e);
					} catch (NoSuchFieldException e_2) {
						Log.e(TAG, "Unable to access notification extras", e_2);
					}
				}
			} catch (Throwable th) {
				return th;
			}
		}
	}

	public static String getGroup(Notification n) {
		return getExtras(n).getString(EXTRA_GROUP_KEY);
	}

	public static boolean getLocalOnly(Notification notif) {
		return getExtras(notif).getBoolean(EXTRA_LOCAL_ONLY);
	}

	public static ArrayList<Parcelable> getParcelableArrayListForActions(Action[] actions) {
		if (actions == null) {
			return null;
		} else {
			ArrayList<Parcelable> parcelables = new ArrayList(actions.length);
			Action[] arr$ = actions;
			int i$ = 0;
			while (i$ < arr$.length) {
				parcelables.add(getBundleForAction(arr$[i$]));
				i$++;
			}
			return parcelables;
		}
	}

	public static String getSortKey(Notification n) {
		return getExtras(n).getString(EXTRA_SORT_KEY);
	}

	public static boolean isGroupSummary(Notification n) {
		return getExtras(n).getBoolean(EXTRA_GROUP_SUMMARY);
	}

	public static Action readAction(Factory factory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory, int icon, CharSequence title, PendingIntent actionIntent, Bundle extras) {
		RemoteInput[] remoteInputs = null;
		if (extras != null) {
			remoteInputs = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(extras, EXTRA_REMOTE_INPUTS), remoteInputFactory);
		}
		return factory.build(icon, title, actionIntent, extras, remoteInputs);
	}

	public static Bundle writeActionAndGetExtras(android.app.Notification.Builder builder, Action action) {
		builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
		Bundle actionExtras = new Bundle(action.getExtras());
		if (action.getRemoteInputs() != null) {
			actionExtras.putParcelableArray(EXTRA_REMOTE_INPUTS, RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
		}
		return actionExtras;
	}
}
