package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.widget.CursorAdapter;

public class ConnectivityManagerCompat {
	private static final ConnectivityManagerCompatImpl IMPL;

	static interface ConnectivityManagerCompatImpl {
		public boolean isActiveNetworkMetered(ConnectivityManager r1_ConnectivityManager);
	}

	static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
		BaseConnectivityManagerCompatImpl() {
			super();
		}

		public boolean isActiveNetworkMetered(ConnectivityManager cm) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				return true;
			} else {
				switch(info.getType()) {
				case WearableExtender.SIZE_DEFAULT:
					return true;
				case CursorAdapter.FLAG_AUTO_REQUERY:
					return false;
				}
				return true;
			}
		}
	}

	static class GingerbreadConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
		GingerbreadConnectivityManagerCompatImpl() {
			super();
		}

		public boolean isActiveNetworkMetered(ConnectivityManager cm) {
			return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(cm);
		}
	}

	static class HoneycombMR2ConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
		HoneycombMR2ConnectivityManagerCompatImpl() {
			super();
		}

		public boolean isActiveNetworkMetered(ConnectivityManager cm) {
			return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(cm);
		}
	}

	static class JellyBeanConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
		JellyBeanConnectivityManagerCompatImpl() {
			super();
		}

		public boolean isActiveNetworkMetered(ConnectivityManager cm) {
			return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(cm);
		}
	}


	static {
		if (VERSION.SDK_INT >= 16) {
			IMPL = new JellyBeanConnectivityManagerCompatImpl();
		} else if (VERSION.SDK_INT >= 13) {
			IMPL = new HoneycombMR2ConnectivityManagerCompatImpl();
		} else if (VERSION.SDK_INT >= 8) {
			IMPL = new GingerbreadConnectivityManagerCompatImpl();
		} else {
			IMPL = new BaseConnectivityManagerCompatImpl();
		}
	}

	public ConnectivityManagerCompat() {
		super();
	}

	public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager cm, Intent intent) {
		NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
		if (info != null) {
			return cm.getNetworkInfo(info.getType());
		} else {
			return null;
		}
	}

	public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
		return IMPL.isActiveNetworkMetered(cm);
	}
}
