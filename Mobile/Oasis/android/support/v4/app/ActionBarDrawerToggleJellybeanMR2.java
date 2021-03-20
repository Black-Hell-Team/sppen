package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

class ActionBarDrawerToggleJellybeanMR2 {
	private static final String TAG = "ActionBarDrawerToggleImplJellybeanMR2";
	private static final int[] THEME_ATTRS;

	static {
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16843531;
		THEME_ATTRS = r0_int_A;
	}

	ActionBarDrawerToggleJellybeanMR2() {
		super();
	}

	public static Drawable getThemeUpIndicator(Activity activity) {
		TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);
		a.recycle();
		return a.getDrawable(0);
	}

	public static Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
		ActionBar actionBar = activity.getActionBar();
		if (actionBar != null) {
			actionBar.setHomeActionContentDescription(contentDescRes);
		}
		return info;
	}

	public static Object setActionBarUpIndicator(Object info, Activity activity, Drawable drawable, int contentDescRes) {
		ActionBar actionBar = activity.getActionBar();
		if (actionBar != null) {
			actionBar.setHomeAsUpIndicator(drawable);
			actionBar.setHomeActionContentDescription(contentDescRes);
		}
		return info;
	}
}
