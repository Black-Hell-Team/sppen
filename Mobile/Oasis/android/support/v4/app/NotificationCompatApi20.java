package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompatBase.Action;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.widget.RemoteViews;
import java.util.ArrayList;

class NotificationCompatApi20 {
	public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
		private android.app.Notification.Builder b;

		public Builder(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int mProgressMax, int mProgress, boolean mProgressIndeterminate, boolean useChronometer, int priority, CharSequence subText, boolean localOnly, Bundle extras, String groupKey, boolean groupSummary, String sortKey) {
			boolean r3z;
			super();
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
			b = r4_android_app_Notification_Builder.setFullScreenIntent(fullScreenIntent, r3z).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(mProgressMax, mProgress, mProgressIndeterminate).setLocalOnly(localOnly).setExtras(extras).setGroup(groupKey).setGroupSummary(groupSummary).setSortKey(sortKey);
		}

		public void addAction(Action action) {
			android.app.Notification.Action.Builder actionBuilder = new android.app.Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
			if (action.getRemoteInputs() != null) {
				RemoteInput[] arr$ = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
				int i$ = 0;
				while (i$ < arr$.length) {
					actionBuilder.addRemoteInput(arr$[i$]);
					i$++;
				}
			}
			if (action.getExtras() != null) {
				actionBuilder.addExtras(action.getExtras());
			}
			b.addAction(actionBuilder.build());
		}

		public Notification build() {
			return b.build();
		}

		public android.app.Notification.Builder getBuilder() {
			return b;
		}
	}


	NotificationCompatApi20() {
		super();
	}

	public static Action getAction(Notification notif, int actionIndex, Factory actionFactory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		return getActionCompatFromAction(notif.actions[actionIndex], actionFactory, remoteInputFactory);
	}

	private static Action getActionCompatFromAction(android.app.Notification.Action action, Factory actionFactory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		return actionFactory.build(action.icon, action.title, action.actionIntent, action.getExtras(), RemoteInputCompatApi20.toCompat(action.getRemoteInputs(), remoteInputFactory));
	}

	private static android.app.Notification.Action getActionFromActionCompat(Action actionCompat) {
		android.app.Notification.Action.Builder actionBuilder = new android.app.Notification.Action.Builder(actionCompat.getIcon(), actionCompat.getTitle(), actionCompat.getActionIntent()).addExtras(actionCompat.getExtras());
		RemoteInput[] remoteInputCompats = actionCompat.getRemoteInputs();
		if (remoteInputCompats != null) {
			android.app.RemoteInput[] arr$ = RemoteInputCompatApi20.fromCompat(remoteInputCompats);
			int i$ = 0;
			while (i$ < arr$.length) {
				actionBuilder.addRemoteInput(arr$[i$]);
				i$++;
			}
		}
		return actionBuilder.build();
	}

	public static Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables, Factory actionFactory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
		if (parcelables == null) {
			return null;
		} else {
			Action[] actions = actionFactory.newArray(parcelables.size());
			int i = 0;
			while (i < actions.length) {
				actions[i] = getActionCompatFromAction((android.app.Notification.Action) parcelables.get(i), actionFactory, remoteInputFactory);
				i++;
			}
			return actions;
		}
	}

	public static String getGroup(Notification notif) {
		return notif.getGroup();
	}

	public static boolean getLocalOnly(Notification notif) {
		if ((notif.flags & 256) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public static ArrayList<Parcelable> getParcelableArrayListForActions(Action[] actions) {
		if (actions == null) {
			return null;
		} else {
			ArrayList<Parcelable> parcelables = new ArrayList(actions.length);
			Action[] arr$ = actions;
			int i$ = 0;
			while (i$ < arr$.length) {
				parcelables.add(getActionFromActionCompat(arr$[i$]));
				i$++;
			}
			return parcelables;
		}
	}

	public static String getSortKey(Notification notif) {
		return notif.getSortKey();
	}

	public static boolean isGroupSummary(Notification notif) {
		if ((notif.flags & 512) != 0) {
			return true;
		} else {
			return false;
		}
	}
}
