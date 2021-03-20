package android.support.v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;

class NotificationCompatBase {
	public static abstract class Action {
		public static interface Factory {
			public NotificationCompatBase.Action build(int r1i, CharSequence r2_CharSequence, PendingIntent r3_PendingIntent, Bundle r4_Bundle, RemoteInput[] r5_RemoteInput_A);

			public NotificationCompatBase.Action[] newArray(int r1i);
		}


		public Action() {
			super();
		}

		protected abstract PendingIntent getActionIntent();

		protected abstract Bundle getExtras();

		protected abstract int getIcon();

		protected abstract RemoteInput[] getRemoteInputs();

		protected abstract CharSequence getTitle();
	}


	NotificationCompatBase() {
		super();
	}
}
