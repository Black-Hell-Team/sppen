package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.Gravity;

public class GravityCompat {
	public static final int END = 8388613;
	static final GravityCompatImpl IMPL;
	public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
	public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
	public static final int START = 8388611;

	static interface GravityCompatImpl {
		public void apply(int r1i, int r2i, int r3i, Rect r4_Rect, int r5i, int r6i, Rect r7_Rect, int r8i);

		public void apply(int r1i, int r2i, int r3i, Rect r4_Rect, Rect r5_Rect, int r6i);

		public void applyDisplay(int r1i, Rect r2_Rect, Rect r3_Rect, int r4i);

		public int getAbsoluteGravity(int r1i, int r2i);
	}

	static class GravityCompatImplBase implements GravityCompat.GravityCompatImpl {
		GravityCompatImplBase() {
			super();
		}

		public void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
			Gravity.apply(gravity, w, h, container, xAdj, yAdj, outRect);
		}

		public void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
			Gravity.apply(gravity, w, h, container, outRect);
		}

		public void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
			Gravity.applyDisplay(gravity, display, inoutObj);
		}

		public int getAbsoluteGravity(int gravity, int layoutDirection) {
			return -8388609 & gravity;
		}
	}

	static class GravityCompatImplJellybeanMr1 implements GravityCompat.GravityCompatImpl {
		GravityCompatImplJellybeanMr1() {
			super();
		}

		public void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
			GravityCompatJellybeanMr1.apply(gravity, w, h, container, xAdj, yAdj, outRect, layoutDirection);
		}

		public void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
			GravityCompatJellybeanMr1.apply(gravity, w, h, container, outRect, layoutDirection);
		}

		public void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
			GravityCompatJellybeanMr1.applyDisplay(gravity, display, inoutObj, layoutDirection);
		}

		public int getAbsoluteGravity(int gravity, int layoutDirection) {
			return GravityCompatJellybeanMr1.getAbsoluteGravity(gravity, layoutDirection);
		}
	}


	static {
		if (VERSION.SDK_INT >= 17) {
			IMPL = new GravityCompatImplJellybeanMr1();
		} else {
			IMPL = new GravityCompatImplBase();
		}
	}

	public GravityCompat() {
		super();
	}

	public static void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
		IMPL.apply(gravity, w, h, container, xAdj, yAdj, outRect, layoutDirection);
	}

	public static void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
		IMPL.apply(gravity, w, h, container, outRect, layoutDirection);
	}

	public static void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
		IMPL.applyDisplay(gravity, display, inoutObj, layoutDirection);
	}

	public static int getAbsoluteGravity(int gravity, int layoutDirection) {
		return IMPL.getAbsoluteGravity(gravity, layoutDirection);
	}
}
