package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public class DrawableCompat {
	static final DrawableImpl IMPL;

	static interface DrawableImpl {
		public boolean isAutoMirrored(Drawable r1_Drawable);

		public void jumpToCurrentState(Drawable r1_Drawable);

		public void setAutoMirrored(Drawable r1_Drawable, boolean r2z);
	}

	static class BaseDrawableImpl implements DrawableCompat.DrawableImpl {
		BaseDrawableImpl() {
			super();
		}

		public boolean isAutoMirrored(Drawable drawable) {
			return false;
		}

		public void jumpToCurrentState(Drawable drawable) {
		}

		public void setAutoMirrored(Drawable drawable, boolean mirrored) {
		}
	}

	static class HoneycombDrawableImpl extends DrawableCompat.BaseDrawableImpl {
		HoneycombDrawableImpl() {
			super();
		}

		public void jumpToCurrentState(Drawable drawable) {
			DrawableCompatHoneycomb.jumpToCurrentState(drawable);
		}
	}

	static class KitKatDrawableImpl extends DrawableCompat.HoneycombDrawableImpl {
		KitKatDrawableImpl() {
			super();
		}

		public boolean isAutoMirrored(Drawable drawable) {
			return DrawableCompatKitKat.isAutoMirrored(drawable);
		}

		public void setAutoMirrored(Drawable drawable, boolean mirrored) {
			DrawableCompatKitKat.setAutoMirrored(drawable, mirrored);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 19) {
			IMPL = new KitKatDrawableImpl();
		} else if (version >= 11) {
			IMPL = new HoneycombDrawableImpl();
		} else {
			IMPL = new BaseDrawableImpl();
		}
	}

	public DrawableCompat() {
		super();
	}

	public static boolean isAutoMirrored(Drawable drawable) {
		return IMPL.isAutoMirrored(drawable);
	}

	public static void jumpToCurrentState(Drawable drawable) {
		IMPL.jumpToCurrentState(drawable);
	}

	public static void setAutoMirrored(Drawable drawable, boolean mirrored) {
		IMPL.setAutoMirrored(drawable, mirrored);
	}
}
