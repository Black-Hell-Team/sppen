package android.support.v4.view;

import android.view.ViewGroup;

class ViewGroupCompatJellybeanMR2 {
	ViewGroupCompatJellybeanMR2() {
		super();
	}

	public static int getLayoutMode(ViewGroup group) {
		return group.getLayoutMode();
	}

	public static void setLayoutMode(ViewGroup group, int mode) {
		group.setLayoutMode(mode);
	}
}
