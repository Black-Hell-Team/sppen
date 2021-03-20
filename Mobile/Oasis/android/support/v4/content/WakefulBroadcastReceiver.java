package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
	private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
	private static final SparseArray<WakeLock> mActiveWakeLocks;
	private static int mNextId;

	static {
		mActiveWakeLocks = new SparseArray();
		mNextId = 1;
	}

	public WakefulBroadcastReceiver() {
		super();
	}

	public static boolean completeWakefulIntent(Intent intent) {
		int id = intent.getIntExtra(EXTRA_WAKE_LOCK_ID, 0);
		if (id == 0) {
			return false;
		} else {
			SparseArray r4_SparseArray = mActiveWakeLocks;
			synchronized(r4_SparseArray) {
				WakeLock wl = (WakeLock) mActiveWakeLocks.get(id);
				if (wl != null) {
					wl.release();
					mActiveWakeLocks.remove(id);
					return true;
				} else {
					Log.w("WakefulBroadcastReceiver", "No active wake lock id #" + id);
					return true;
				}
			}
		}
	}

	public static ComponentName startWakefulService(Context context, Intent intent) {
		ComponentName comp;
		SparseArray r5_SparseArray = mActiveWakeLocks;
		synchronized(r5_SparseArray) {
			int id = mNextId;
			mNextId++;
			if (mNextId <= 0) {
				mNextId = 1;
			}
			intent.putExtra(EXTRA_WAKE_LOCK_ID, id);
			comp = context.startService(intent);
			if (comp == null) {
				comp = null;
			} else {
				WakeLock wl = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "wake:" + comp.flattenToShortString());
				wl.setReferenceCounted(false);
				wl.acquire(60000);
				mActiveWakeLocks.put(id, wl);
			}
		}
		return comp;
	}
}
