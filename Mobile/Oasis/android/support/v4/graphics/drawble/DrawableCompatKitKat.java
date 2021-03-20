package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

class DrawableCompatKitKat {
	DrawableCompatKitKat() {
		super();
	}

	public static boolean isAutoMirrored(Drawable drawable) {
		return drawable.isAutoMirrored();
	}

	public static void setAutoMirrored(Drawable drawable, boolean mirrored) {
		drawable.setAutoMirrored(mirrored);
	}
}
