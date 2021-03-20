package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public class MarginLayoutParamsCompat {
	static final MarginLayoutParamsCompatImpl IMPL;

	static interface MarginLayoutParamsCompatImpl {
		public int getLayoutDirection(MarginLayoutParams r1_MarginLayoutParams);

		public int getMarginEnd(MarginLayoutParams r1_MarginLayoutParams);

		public int getMarginStart(MarginLayoutParams r1_MarginLayoutParams);

		public boolean isMarginRelative(MarginLayoutParams r1_MarginLayoutParams);

		public void resolveLayoutDirection(MarginLayoutParams r1_MarginLayoutParams, int r2i);

		public void setLayoutDirection(MarginLayoutParams r1_MarginLayoutParams, int r2i);

		public void setMarginEnd(MarginLayoutParams r1_MarginLayoutParams, int r2i);

		public void setMarginStart(MarginLayoutParams r1_MarginLayoutParams, int r2i);
	}

	static class MarginLayoutParamsCompatImplBase implements MarginLayoutParamsCompat.MarginLayoutParamsCompatImpl {
		MarginLayoutParamsCompatImplBase() {
			super();
		}

		public int getLayoutDirection(MarginLayoutParams lp) {
			return 0;
		}

		public int getMarginEnd(MarginLayoutParams lp) {
			return lp.rightMargin;
		}

		public int getMarginStart(MarginLayoutParams lp) {
			return lp.leftMargin;
		}

		public boolean isMarginRelative(MarginLayoutParams lp) {
			return false;
		}

		public void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
		}

		public void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
		}

		public void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
			lp.rightMargin = marginEnd;
		}

		public void setMarginStart(MarginLayoutParams lp, int marginStart) {
			lp.leftMargin = marginStart;
		}
	}

	static class MarginLayoutParamsCompatImplJbMr1 implements MarginLayoutParamsCompat.MarginLayoutParamsCompatImpl {
		MarginLayoutParamsCompatImplJbMr1() {
			super();
		}

		public int getLayoutDirection(MarginLayoutParams lp) {
			return MarginLayoutParamsCompatJellybeanMr1.getLayoutDirection(lp);
		}

		public int getMarginEnd(MarginLayoutParams lp) {
			return MarginLayoutParamsCompatJellybeanMr1.getMarginEnd(lp);
		}

		public int getMarginStart(MarginLayoutParams lp) {
			return MarginLayoutParamsCompatJellybeanMr1.getMarginStart(lp);
		}

		public boolean isMarginRelative(MarginLayoutParams lp) {
			return MarginLayoutParamsCompatJellybeanMr1.isMarginRelative(lp);
		}

		public void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
			MarginLayoutParamsCompatJellybeanMr1.resolveLayoutDirection(lp, layoutDirection);
		}

		public void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
			MarginLayoutParamsCompatJellybeanMr1.setLayoutDirection(lp, layoutDirection);
		}

		public void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
			MarginLayoutParamsCompatJellybeanMr1.setMarginEnd(lp, marginEnd);
		}

		public void setMarginStart(MarginLayoutParams lp, int marginStart) {
			MarginLayoutParamsCompatJellybeanMr1.setMarginStart(lp, marginStart);
		}
	}


	static {
		if (VERSION.SDK_INT >= 17) {
			IMPL = new MarginLayoutParamsCompatImplJbMr1();
		} else {
			IMPL = new MarginLayoutParamsCompatImplBase();
		}
	}

	public MarginLayoutParamsCompat() {
		super();
	}

	public static int getLayoutDirection(MarginLayoutParams lp) {
		return IMPL.getLayoutDirection(lp);
	}

	public static int getMarginEnd(MarginLayoutParams lp) {
		return IMPL.getMarginEnd(lp);
	}

	public static int getMarginStart(MarginLayoutParams lp) {
		return IMPL.getMarginStart(lp);
	}

	public static boolean isMarginRelative(MarginLayoutParams lp) {
		return IMPL.isMarginRelative(lp);
	}

	public static void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
		IMPL.resolveLayoutDirection(lp, layoutDirection);
	}

	public static void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
		IMPL.setLayoutDirection(lp, layoutDirection);
	}

	public static void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
		IMPL.setMarginEnd(lp, marginEnd);
	}

	public static void setMarginStart(MarginLayoutParams lp, int marginStart) {
		IMPL.setMarginStart(lp, marginStart);
	}
}
