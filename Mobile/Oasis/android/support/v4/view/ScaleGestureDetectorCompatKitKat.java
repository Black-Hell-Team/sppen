package android.support.v4.view;

import android.view.ScaleGestureDetector;

class ScaleGestureDetectorCompatKitKat {
	private ScaleGestureDetectorCompatKitKat() {
		super();
	}

	public static boolean isQuickScaleEnabled(Object scaleGestureDetector) {
		return ((ScaleGestureDetector) scaleGestureDetector).isQuickScaleEnabled();
	}

	public static void setQuickScaleEnabled(Object scaleGestureDetector, boolean enabled) {
		((ScaleGestureDetector) scaleGestureDetector).setQuickScaleEnabled(enabled);
	}
}
