package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

class NotificationCompatIceCreamSandwich {
	NotificationCompatIceCreamSandwich() {
		super();
	}

	static Notification add(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int mProgressMax, int mProgress, boolean mProgressIndeterminate) {
		boolean r4z;
		Builder r5_Builder = new Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS);
		if ((n.flags & 2) != 0) {
			r4z = true;
		} else {
			r4z = false;
		}
		r5_Builder = r5_Builder.setOngoing(r4z);
		if ((n.flags & 8) != 0) {
			r4z = true;
		} else {
			r4z = false;
		}
		r5_Builder = r5_Builder.setOnlyAlertOnce(r4z);
		if ((n.flags & 16) != 0) {
			r4z = true;
		} else {
			r4z = false;
		}
		r5_Builder = r5_Builder.setAutoCancel(r4z).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent);
		if ((n.flags & 128) != 0) {
			r4z = true;
		} else {
			r4z = false;
		}
		return r5_Builder.setFullScreenIntent(fullScreenIntent, r4z).setLargeIcon(largeIcon).setNumber(number).setProgress(mProgressMax, mProgress, mProgressIndeterminate).getNotification();
	}
}
