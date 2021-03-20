package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.CursorAdapter;

class ConnectivityManagerCompatGingerbread {
	ConnectivityManagerCompatGingerbread() {
		super();
	}

	public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			return true;
		} else {
			switch(info.getType()) {
			case WearableExtender.SIZE_DEFAULT:
			case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
			case WearableExtender.SIZE_MEDIUM:
			case TransportMediator.FLAG_KEY_MEDIA_PLAY:
			case WearableExtender.SIZE_FULL_SCREEN:
			case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
				return true;
			case CursorAdapter.FLAG_AUTO_REQUERY:
				return false;
			}
			return true;
		}
	}
}
