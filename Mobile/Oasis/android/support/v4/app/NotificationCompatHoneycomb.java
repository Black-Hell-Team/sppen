package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

class NotificationCompatHoneycomb {
	NotificationCompatHoneycomb() {
		super();
	}

	static Notification add(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon) {
		boolean r1z;
		Builder r2_Builder = new Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS);
		if ((n.flags & 2) != 0) {
			r1z = true;
		} else {
			r1z = false;
		}
		r2_Builder = r2_Builder.setOngoing(r1z);
		if ((n.flags & 8) != 0) {
			r1z = true;
		} else {
			r1z = false;
		}
		r2_Builder = r2_Builder.setOnlyAlertOnce(r1z);
		if ((n.flags & 16) != 0) {
			r1z = true;
		} else {
			r1z = false;
		}
		r2_Builder = r2_Builder.setAutoCancel(r1z).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent);
		if ((n.flags & 128) != 0) {
			r1z = true;
		} else {
			r1z = false;
		}
		return r2_Builder.setFullScreenIntent(fullScreenIntent, r1z).setLargeIcon(largeIcon).setNumber(number).getNotification();
	}
}
