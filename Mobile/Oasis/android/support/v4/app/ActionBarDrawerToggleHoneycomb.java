package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb {
	private static final String TAG = "ActionBarDrawerToggleHoneycomb";
	private static final int[] THEME_ATTRS;

	private static class SetIndicatorInfo {
		public Method setHomeActionContentDescription;
		public Method setHomeAsUpIndicator;
		public ImageView upIndicatorView;

		SetIndicatorInfo(Activity activity) {
			super();
			try {
				Class[] r8_Class_A = new Class[1];
				r8_Class_A[0] = Drawable.class;
				setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", r8_Class_A);
				r8_Class_A = new Class[1];
				r8_Class_A[0] = Integer.TYPE;
				setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", r8_Class_A);
			} catch (NoSuchMethodException e) {
				View home = activity.findViewById(16908332);
				if (home != null) {
					ViewGroup parent = (ViewGroup) home.getParent();
					if (parent.getChildCount() == 2) {
						View up;
						View first = parent.getChildAt(0);
						View second = parent.getChildAt(1);
						if (first.getId() == 16908332) {
							up = second;
						} else {
							up = first;
						}
						if (up instanceof ImageView) {
							upIndicatorView = (ImageView) up;
							return;
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
			}
		}
	}


	static {
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16843531;
		THEME_ATTRS = r0_int_A;
	}

	ActionBarDrawerToggleHoneycomb() {
		super();
	}

	public static Drawable getThemeUpIndicator(Activity activity) {
		TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);
		a.recycle();
		return a.getDrawable(0);
	}

	public static Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
		if (info == null) {
			info = new SetIndicatorInfo(activity);
		}
		SetIndicatorInfo sii = (SetIndicatorInfo) info;
		if (sii.setHomeAsUpIndicator != null) {
			ActionBar actionBar;
			try {
				actionBar = activity.getActionBar();
				Object[] r4_Object_A = new Object[1];
				r4_Object_A[0] = Integer.valueOf(contentDescRes);
				sii.setHomeActionContentDescription.invoke(actionBar, r4_Object_A);
				if (VERSION.SDK_INT <= TimeUtils.HUNDRED_DAY_FIELD_LEN) {
					actionBar.setSubtitle(actionBar.getSubtitle());
					return info;
				} else {
					return info;
				}
			} catch (Exception e) {
				Log.w(TAG, "Couldn't set content description via JB-MR2 API", e);
				return info;
			}
		} else {
			return info;
		}
	}

	public static Object setActionBarUpIndicator(Object info, Activity activity, Drawable drawable, int contentDescRes) {
		if (info == null) {
			info = new SetIndicatorInfo(activity);
		}
		SetIndicatorInfo sii = (SetIndicatorInfo) info;
		if (sii.setHomeAsUpIndicator != null) {
			try {
				ActionBar actionBar = activity.getActionBar();
				Object[] r4_Object_A = new Object[1];
				r4_Object_A[0] = drawable;
				sii.setHomeAsUpIndicator.invoke(actionBar, r4_Object_A);
				r4_Object_A = new Object[1];
				r4_Object_A[0] = Integer.valueOf(contentDescRes);
				sii.setHomeActionContentDescription.invoke(actionBar, r4_Object_A);
				return info;
			} catch (Exception e) {
				Log.w(TAG, "Couldn't set home-as-up indicator via JB-MR2 API", e);
				return info;
			}
		} else if (sii.upIndicatorView != null) {
			sii.upIndicatorView.setImageDrawable(drawable);
			return info;
		} else {
			Log.w(TAG, "Couldn't set home-as-up indicator");
			return info;
		}
	}
}
