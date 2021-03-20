package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Interpolator;

class ViewPropertyAnimatorCompatICS {
	static class AnonymousClass_1 extends AnimatorListenerAdapter {
		final /* synthetic */ ViewPropertyAnimatorListener val$listener;
		final /* synthetic */ View val$view;

		AnonymousClass_1(ViewPropertyAnimatorListener r1_ViewPropertyAnimatorListener, View r2_View) {
			super();
			val$listener = r1_ViewPropertyAnimatorListener;
			val$view = r2_View;
		}

		public void onAnimationCancel(Animator animation) {
			val$listener.onAnimationCancel(val$view);
		}

		public void onAnimationEnd(Animator animation) {
			val$listener.onAnimationEnd(val$view);
		}

		public void onAnimationStart(Animator animation) {
			val$listener.onAnimationStart(val$view);
		}
	}


	ViewPropertyAnimatorCompatICS() {
		super();
	}

	public static void alpha(View view, float value) {
		view.animate().alpha(value);
	}

	public static void alphaBy(View view, float value) {
		view.animate().alphaBy(value);
	}

	public static void cancel(View view) {
		view.animate().cancel();
	}

	public static long getDuration(View view) {
		return view.animate().getDuration();
	}

	public static long getStartDelay(View view) {
		return view.animate().getStartDelay();
	}

	public static void rotation(View view, float value) {
		view.animate().rotation(value);
	}

	public static void rotationBy(View view, float value) {
		view.animate().rotationBy(value);
	}

	public static void rotationX(View view, float value) {
		view.animate().rotationX(value);
	}

	public static void rotationXBy(View view, float value) {
		view.animate().rotationXBy(value);
	}

	public static void rotationY(View view, float value) {
		view.animate().rotationY(value);
	}

	public static void rotationYBy(View view, float value) {
		view.animate().rotationYBy(value);
	}

	public static void scaleX(View view, float value) {
		view.animate().scaleX(value);
	}

	public static void scaleXBy(View view, float value) {
		view.animate().scaleXBy(value);
	}

	public static void scaleY(View view, float value) {
		view.animate().scaleY(value);
	}

	public static void scaleYBy(View view, float value) {
		view.animate().scaleYBy(value);
	}

	public static void setDuration(View view, long value) {
		view.animate().setDuration(value);
	}

	public static void setInterpolator(View view, Interpolator value) {
		view.animate().setInterpolator(value);
	}

	public static void setListener(View view, ViewPropertyAnimatorListener listener) {
		view.animate().setListener(new AnonymousClass_1(listener, view));
	}

	public static void setStartDelay(View view, long value) {
		view.animate().setStartDelay(value);
	}

	public static void start(View view) {
		view.animate().start();
	}

	public static void translationX(View view, float value) {
		view.animate().translationX(value);
	}

	public static void translationXBy(View view, float value) {
		view.animate().translationXBy(value);
	}

	public static void translationY(View view, float value) {
		view.animate().translationY(value);
	}

	public static void translationYBy(View view, float value) {
		view.animate().translationYBy(value);
	}

	public static void x(View view, float value) {
		view.animate().x(value);
	}

	public static void xBy(View view, float value) {
		view.animate().xBy(value);
	}

	public static void y(View view, float value) {
		view.animate().y(value);
	}

	public static void yBy(View view, float value) {
		view.animate().yBy(value);
	}
}
