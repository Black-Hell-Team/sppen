package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager {
	private static final boolean DEBUG = false;
	static final int MSG_EXEC_PENDING_BROADCASTS = 1;
	private static final String TAG = "LocalBroadcastManager";
	private static LocalBroadcastManager mInstance;
	private static final Object mLock;
	private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
	private final Context mAppContext;
	private final Handler mHandler;
	private final ArrayList<BroadcastRecord> mPendingBroadcasts;
	private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;

	class AnonymousClass_1 extends Handler {
		final /* synthetic */ LocalBroadcastManager this$0;

		AnonymousClass_1(LocalBroadcastManager r1_LocalBroadcastManager, Looper x0) {
			super(x0);
			this$0 = r1_LocalBroadcastManager;
		}

		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_EXEC_PENDING_BROADCASTS:
				this$0.executePendingBroadcasts();
			}
			super.handleMessage(msg);
		}
	}

	private static class BroadcastRecord {
		final Intent intent;
		final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;

		BroadcastRecord(Intent _intent, ArrayList<LocalBroadcastManager.ReceiverRecord> _receivers) {
			super();
			intent = _intent;
			receivers = _receivers;
		}
	}

	private static class ReceiverRecord {
		boolean broadcasting;
		final IntentFilter filter;
		final BroadcastReceiver receiver;

		ReceiverRecord(IntentFilter _filter, BroadcastReceiver _receiver) {
			super();
			filter = _filter;
			receiver = _receiver;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder(128);
			builder.append("Receiver{");
			builder.append(receiver);
			builder.append(" filter=");
			builder.append(filter);
			builder.append("}");
			return builder.toString();
		}
	}


	static {
		mLock = new Object();
	}

	private LocalBroadcastManager(Context context) {
		super();
		mReceivers = new HashMap();
		mActions = new HashMap();
		mPendingBroadcasts = new ArrayList();
		mAppContext = context;
		mHandler = new AnonymousClass_1(this, context.getMainLooper());
	}

	private void executePendingBroadcasts() {
		while (true) {
			HashMap r6_HashMap = mReceivers;
			synchronized(r6_HashMap) {
				int N;
				try {
					N = mPendingBroadcasts.size();
					if (N <= 0) {
						return;
					} else {
						BroadcastRecord[] brs = new BroadcastRecord[N];
						mPendingBroadcasts.toArray(brs);
						mPendingBroadcasts.clear();
						int i = 0;
						while (i < brs.length) {
							BroadcastRecord br = brs[i];
							int j = 0;
							while (j < br.receivers.size()) {
								((ReceiverRecord) br.receivers.get(j)).receiver.onReceive(mAppContext, br.intent);
								j++;
							}
							i++;
						}
					}
				} catch (Throwable th) {
					while (true) {
						return th;
					}
				}
			}
		}
	}

	public static LocalBroadcastManager getInstance(Context context) {
		LocalBroadcastManager r0_LocalBroadcastManager;
		synchronized(mLock) {
			if (mInstance == null) {
				mInstance = new LocalBroadcastManager(context.getApplicationContext());
			}
			r0_LocalBroadcastManager = mInstance;
		}
		return r0_LocalBroadcastManager;
	}

	public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		synchronized(mReceivers) {
			ReceiverRecord entry = new ReceiverRecord(filter, receiver);
			ArrayList<IntentFilter> filters = (ArrayList) mReceivers.get(receiver);
			if (filters == null) {
				filters = new ArrayList(1);
				mReceivers.put(receiver, filters);
			}
			filters.add(filter);
			int i = 0;
			while (i < filter.countActions()) {
				String action = filter.getAction(i);
				ArrayList<ReceiverRecord> entries = (ArrayList) mActions.get(action);
				if (entries == null) {
					entries = new ArrayList(1);
					mActions.put(action, entries);
				}
				entries.add(entry);
				i++;
			}
		}
	}

	public boolean sendBroadcast(Intent intent) {
		boolean r1z;
		HashMap r15_HashMap = mReceivers;
		synchronized(r15_HashMap) {
			String action;
			String type;
			Uri data;
			String scheme;
			Set<String> categories;
			boolean debug;
			ArrayList<ReceiverRecord> entries;
			try {
				action = intent.getAction();
				type = intent.resolveTypeIfNeeded(mAppContext.getContentResolver());
				data = intent.getData();
				scheme = intent.getScheme();
				categories = intent.getCategories();
				if ((intent.getFlags() & 8) != 0) {
					debug = true;
				} else {
					debug = DEBUG;
				}
				if (debug) {
					Log.v(TAG, "Resolving type " + type + " scheme " + scheme + " of intent " + intent);
				}
				entries = (ArrayList) mActions.get(intent.getAction());
				if (entries != null) {
					if (debug) {
						Log.v(TAG, "Action list: " + entries);
					}
					ArrayList<ReceiverRecord> receivers = null;
					int i = 0;
					while (i < entries.size()) {
						ReceiverRecord receiver = (ReceiverRecord) entries.get(i);
						if (debug) {
							Log.v(TAG, "Matching against filter " + receiver.filter);
						}
						if (receiver.broadcasting) {
							if (debug) {
								Log.v(TAG, "  Filter's target already added");
							}
						} else {
							int match = receiver.filter.match(action, type, scheme, data, categories, TAG);
							if (match >= 0) {
								if (debug) {
									Log.v(TAG, "  Filter matched!  match=0x" + Integer.toHexString(match));
								}
								if (receivers == null) {
									receivers = new ArrayList();
								}
								receivers.add(receiver);
								receiver.broadcasting = true;
							} else if (debug) {
								String reason;
								switch(match) {
								case -4:
									reason = "category";
									Log.v(TAG, "  Filter did not match: " + reason);
									break;
								case -3:
									reason = "action";
									Log.v(TAG, "  Filter did not match: " + reason);
									break;
								case PagerAdapter.POSITION_NONE:
									reason = "data";
									Log.v(TAG, "  Filter did not match: " + reason);
									break;
								case WearableExtender.UNSET_ACTION_INDEX:
									reason = "type";
									Log.v(TAG, "  Filter did not match: " + reason);
									break;
								}
								reason = "unknown reason";
								Log.v(TAG, "  Filter did not match: " + reason);
							}
						}
						i++;
					}
					if (receivers != null) {
						i = 0;
						while (i < receivers.size()) {
							((ReceiverRecord) receivers.get(i)).broadcasting = false;
							i++;
						}
						mPendingBroadcasts.add(new BroadcastRecord(intent, receivers));
						if (!mHandler.hasMessages(MSG_EXEC_PENDING_BROADCASTS)) {
							mHandler.sendEmptyMessage(MSG_EXEC_PENDING_BROADCASTS);
						}
						r1z = true;
					}
					r1z = DEBUG;
				} else {
					r1z = DEBUG;
				}
			} catch (Throwable th) {
				return th;
			}
		}
		return r1z;
	}

	public void sendBroadcastSync(Intent intent) {
		if (sendBroadcast(intent)) {
			executePendingBroadcasts();
		}
	}

	public void unregisterReceiver(BroadcastReceiver receiver) {
		HashMap r8_HashMap = mReceivers;
		synchronized(r8_HashMap) {
			ArrayList<IntentFilter> filters = (ArrayList) mReceivers.remove(receiver);
			if (filters == null) {
			} else {
				int i = 0;
				while (i < filters.size()) {
					IntentFilter filter = (IntentFilter) filters.get(i);
					int j = 0;
					while (j < filter.countActions()) {
						String action = filter.getAction(j);
						ArrayList<ReceiverRecord> receivers = (ArrayList) mActions.get(action);
						if (receivers != null) {
							int k = 0;
							while (k < receivers.size()) {
								if (((ReceiverRecord) receivers.get(k)).receiver == receiver) {
									receivers.remove(k);
									k--;
								}
							}
							if (receivers.size() <= 0) {
								mActions.remove(action);
							}
						}
						j++;
					}
					i++;
				}
			}
		}
	}
}
