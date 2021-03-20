package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatBase.Action;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;

class NotificationCompatKitKat {
	public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
		private android.app.Notification.Builder b;
		private List<Bundle> mActionExtrasList;
		private Bundle mExtras;

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
				mExtras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
			}
			if (groupKey != null) {
				mExtras.putString(NotificationCompatExtras.EXTRA_GROUP_KEY, groupKey);
				if (groupSummary) {
					mExtras.putBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY, true);
				} else {
					mExtras.putBoolean(NotificationManagerCompat.EXTRA_USE_SIDE_CHANNEL, true);
				}
			}
			if (sortKey != null) {
				mExtras.putString(NotificationCompatExtras.EXTRA_SORT_KEY, sortKey);
			}
		}

		public void addAction(Action action) {
			mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(b, action));
		}

		public Notification build() {
			SparseArray<Bundle> actionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
			if (actionExtrasMap != null) {
				mExtras.putSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS, actionExtrasMap);
			}
			b.setExtras(mExtras);
			return b.build();
		}

		public android.app.Notification.Builder getBuilder() {
			return b;
		}
	}


	NotificationCompatKitKat() {
		super();
	}

	public static Action getAction(Notification notif, int actionIndex, Factory factory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		android.app.Notification.Action action = notif.actions[actionIndex];
		Bundle actionExtras = null;
		SparseArray<Bundle> actionExtrasMap = notif.extras.getSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
		if (actionExtrasMap != null) {
			actionExtras = actionExtrasMap.get(actionIndex);
		}
		return NotificationCompatJellybean.readAction(factory, remoteInputFactory, action.icon, action.title, action.actionIntent, actionExtras);
	}

	public static int getActionCount(Notification notif) {
		if (notif.actions != null) {
			return notif.actions.length;
		} else {
			return 0;
		}
	}

	public static Bundle getExtras(Notification notif) {
		return notif.extras;
	}

	public static String getGroup(Notification notif) {
		return notif.extras.getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
	}

	public static boolean getLocalOnly(Notification notif) {
		return notif.extras.getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
	}

	public static String getSortKey(Notification notif) {
		return notif.extras.getString(NotificationCompatExtras.EXTRA_SORT_KEY);
	}

	public static boolean isGroupSummary(Notification notif) {
		return notif.extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
	}
}
