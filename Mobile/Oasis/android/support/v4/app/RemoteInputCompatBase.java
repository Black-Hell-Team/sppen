package android.support.v4.app;

import android.os.Bundle;

class RemoteInputCompatBase {
	public static abstract class RemoteInput {
		public static interface Factory {
			public RemoteInputCompatBase.RemoteInput build(String r1_String, CharSequence r2_CharSequence, CharSequence[] r3_CharSequence_A, boolean r4z, Bundle r5_Bundle);

			public RemoteInputCompatBase.RemoteInput[] newArray(int r1i);
		}


		public RemoteInput() {
			super();
		}

		protected abstract boolean getAllowFreeFormInput();

		protected abstract CharSequence[] getChoices();

		protected abstract Bundle getExtras();

		protected abstract CharSequence getLabel();

		protected abstract String getResultKey();
	}


	RemoteInputCompatBase() {
		super();
	}
}
