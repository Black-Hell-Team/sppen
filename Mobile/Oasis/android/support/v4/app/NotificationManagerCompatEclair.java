package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;

class NotificationManagerCompatEclair {
	NotificationManagerCompatEclair() {
		super();
	}

	static void cancelNotification(NotificationManager notificationManager, String tag, int id) {
		notificationManager.cancel(tag, id);
	}

	public static void postNotification(NotificationManager notificationManager, String tag, int id, Notification notification) {
		notificationManager.notify(tag, id, notification);
	}
}
