package android.support.v4.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

class ViewCompatJB {
	ViewCompatJB() {
		super();
	}

	public static Object getAccessibilityNodeProvider(View view) {
		return view.getAccessibilityNodeProvider();
	}

	public static int getImportantForAccessibility(View view) {
		return view.getImportantForAccessibility();
	}

	public static int getMinimumHeight(View view) {
		return view.getMinimumHeight();
	}

	public static int getMinimumWidth(View view) {
		return view.getMinimumWidth();
	}

	public static ViewParent getParentForAccessibility(View view) {
		return view.getParentForAccessibility();
	}

	public static boolean hasTransientState(View view) {
		return view.hasTransientState();
	}

	public static boolean performAccessibilityAction(View view, int action, Bundle arguments) {
		return view.performAccessibilityAction(action, arguments);
	}

	public static void postInvalidateOnAnimation(View view) {
		view.postInvalidateOnAnimation();
	}

	public static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
		view.postInvalidate(left, top, right, bottom);
	}

	public static void postOnAnimation(View view, Runnable action) {
		view.postOnAnimation(action);
	}

	public static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
		view.postOnAnimationDelayed(action, delayMillis);
	}

	public static void setHasTransientState(View view, boolean hasTransientState) {
		view.setHasTransientState(hasTransientState);
	}

	public static void setImportantForAccessibility(View view, int mode) {
		view.setImportantForAccessibility(mode);
	}
}
