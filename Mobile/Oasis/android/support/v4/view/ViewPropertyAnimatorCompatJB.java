package android.support.v4.view;

import android.view.View;

class ViewPropertyAnimatorCompatJB {
	ViewPropertyAnimatorCompatJB() {
		super();
	}

	public static void withEndAction(View view, Runnable runnable) {
		view.animate().withEndAction(runnable);
	}

	public static void withLayer(View view) {
		view.animate().withLayer();
	}

	public static void withStartAction(View view, Runnable runnable) {
		view.animate().withStartAction(runnable);
	}
}
