package android.support.v4.view;

import android.view.View;

public class ViewCompatKitKat {
	public ViewCompatKitKat() {
		super();
	}

	public static int getAccessibilityLiveRegion(View view) {
		return view.getAccessibilityLiveRegion();
	}

	public static void setAccessibilityLiveRegion(View view, int mode) {
		view.setAccessibilityLiveRegion(mode);
	}
}
