package android.support.v4.view;

import android.os.Build.VERSION;

public class ScaleGestureDetectorCompat {
	static final ScaleGestureDetectorImpl IMPL;

	static /* synthetic */ class AnonymousClass_1 {
	}

	static interface ScaleGestureDetectorImpl {
		public boolean isQuickScaleEnabled(Object r1_Object);

		public void setQuickScaleEnabled(Object r1_Object, boolean r2z);
	}

	private static class BaseScaleGestureDetectorImpl implements ScaleGestureDetectorCompat.ScaleGestureDetectorImpl {
		private BaseScaleGestureDetectorImpl() {
			super();
		}

		/* synthetic */ BaseScaleGestureDetectorImpl(ScaleGestureDetectorCompat.AnonymousClass_1 x0) {
			this();
		}

		public boolean isQuickScaleEnabled(Object o) {
			return false;
		}

		public void setQuickScaleEnabled(Object o, boolean enabled) {
		}
	}

	private static class ScaleGestureDetectorCompatKitKatImpl implements ScaleGestureDetectorCompat.ScaleGestureDetectorImpl {
		private ScaleGestureDetectorCompatKitKatImpl() {
			super();
		}

		/* synthetic */ ScaleGestureDetectorCompatKitKatImpl(ScaleGestureDetectorCompat.AnonymousClass_1 x0) {
			this();
		}

		public boolean isQuickScaleEnabled(Object o) {
			return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(o);
		}

		public void setQuickScaleEnabled(Object o, boolean enabled) {
			ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(o, enabled);
		}
	}


	static {
		if (VERSION.SDK_INT >= 19) {
			IMPL = new ScaleGestureDetectorCompatKitKatImpl(null);
		} else {
			IMPL = new BaseScaleGestureDetectorImpl(null);
		}
	}

	private ScaleGestureDetectorCompat() {
		super();
	}

	public static boolean isQuickScaleEnabled(Object scaleGestureDetector) {
		return IMPL.isQuickScaleEnabled(scaleGestureDetector);
	}

	public static void setQuickScaleEnabled(Object scaleGestureDetector, boolean enabled) {
		IMPL.setQuickScaleEnabled(scaleGestureDetector, enabled);
	}
}
