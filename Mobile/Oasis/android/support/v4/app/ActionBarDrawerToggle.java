package android.support.v4.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.InsetDrawable;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;

public class ActionBarDrawerToggle implements DrawerListener {
	private static final int ID_HOME = 16908332;
	private static final ActionBarDrawerToggleImpl IMPL;
	private static final float TOGGLE_DRAWABLE_OFFSET = 0.33333334f;
	private final Activity mActivity;
	private final Delegate mActivityImpl;
	private final int mCloseDrawerContentDescRes;
	private Drawable mDrawerImage;
	private final int mDrawerImageResource;
	private boolean mDrawerIndicatorEnabled;
	private final DrawerLayout mDrawerLayout;
	private final int mOpenDrawerContentDescRes;
	private Object mSetIndicatorInfo;
	private SlideDrawable mSlider;
	private Drawable mThemeImage;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private static interface ActionBarDrawerToggleImpl {
		public Drawable getThemeUpIndicator(Activity r1_Activity);

		public Object setActionBarDescription(Object r1_Object, Activity r2_Activity, int r3i);

		public Object setActionBarUpIndicator(Object r1_Object, Activity r2_Activity, Drawable r3_Drawable, int r4i);
	}

	public static interface Delegate {
		@Nullable
		public Drawable getThemeUpIndicator();

		public void setActionBarDescription(int r1i);

		public void setActionBarUpIndicator(Drawable r1_Drawable, int r2i);
	}

	public static interface DelegateProvider {
		@Nullable
		public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
	}

	private class SlideDrawable extends InsetDrawable implements Callback {
		private final boolean mHasMirroring;
		private float mOffset;
		private float mPosition;
		private final Rect mTmpRect;
		final /* synthetic */ ActionBarDrawerToggle this$0;

		private SlideDrawable(ActionBarDrawerToggle r4_ActionBarDrawerToggle, Drawable wrapped) {
			super(wrapped, 0);
			boolean r0z = false;
			this$0 = r4_ActionBarDrawerToggle;
			if (VERSION.SDK_INT > 18) {
				r0z = true;
			}
			mHasMirroring = r0z;
			mTmpRect = new Rect();
		}

		/* synthetic */ SlideDrawable(ActionBarDrawerToggle x0, Drawable x1, ActionBarDrawerToggle.AnonymousClass_1 x2) {
			this(x0, x1);
		}

		public void draw(Canvas canvas) {
			boolean isLayoutRTL;
			int flipRtl = 1;
			copyBounds(mTmpRect);
			canvas.save();
			if (ViewCompat.getLayoutDirection(this$0.mActivity.getWindow().getDecorView()) == 1) {
				isLayoutRTL = true;
			} else {
				isLayoutRTL = false;
			}
			if (isLayoutRTL) {
				flipRtl = -1;
			}
			int width = mTmpRect.width();
			canvas.translate((((-mOffset) * ((float) width)) * mPosition) * ((float) flipRtl), AutoScrollHelper.RELATIVE_UNSPECIFIED);
			if (!isLayoutRTL || mHasMirroring) {
				super.draw(canvas);
				canvas.restore();
			} else {
				canvas.translate((float) width, AutoScrollHelper.RELATIVE_UNSPECIFIED);
				canvas.scale(-1.0f, 1.0f);
				super.draw(canvas);
				canvas.restore();
			}
		}

		public float getPosition() {
			return mPosition;
		}

		public void setOffset(float offset) {
			mOffset = offset;
			invalidateSelf();
		}

		public void setPosition(float position) {
			mPosition = position;
			invalidateSelf();
		}
	}

	private static class ActionBarDrawerToggleImplBase implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
		private ActionBarDrawerToggleImplBase() {
			super();
		}

		/* synthetic */ ActionBarDrawerToggleImplBase(ActionBarDrawerToggle.AnonymousClass_1 x0) {
			this();
		}

		public Drawable getThemeUpIndicator(Activity activity) {
			return null;
		}

		public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
			return info;
		}

		public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
			return info;
		}
	}

	private static class ActionBarDrawerToggleImplHC implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
		private ActionBarDrawerToggleImplHC() {
			super();
		}

		/* synthetic */ ActionBarDrawerToggleImplHC(ActionBarDrawerToggle.AnonymousClass_1 x0) {
			this();
		}

		public Drawable getThemeUpIndicator(Activity activity) {
			return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(activity);
		}

		public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
			return ActionBarDrawerToggleHoneycomb.setActionBarDescription(info, activity, contentDescRes);
		}

		public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
			return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
		}
	}

	private static class ActionBarDrawerToggleImplJellybeanMR2 implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
		private ActionBarDrawerToggleImplJellybeanMR2() {
			super();
		}

		/* synthetic */ ActionBarDrawerToggleImplJellybeanMR2(ActionBarDrawerToggle.AnonymousClass_1 x0) {
			this();
		}

		public Drawable getThemeUpIndicator(Activity activity) {
			return ActionBarDrawerToggleJellybeanMR2.getThemeUpIndicator(activity);
		}

		public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
			return ActionBarDrawerToggleJellybeanMR2.setActionBarDescription(info, activity, contentDescRes);
		}

		public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
			return ActionBarDrawerToggleJellybeanMR2.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 18) {
			IMPL = new ActionBarDrawerToggleImplJellybeanMR2(null);
		} else if (version >= 11) {
			IMPL = new ActionBarDrawerToggleImplHC(null);
		} else {
			IMPL = new ActionBarDrawerToggleImplBase(null);
		}
	}

	public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
		super();
		mDrawerIndicatorEnabled = true;
		mActivity = activity;
		if (activity instanceof DelegateProvider) {
			mActivityImpl = ((DelegateProvider) activity).getDrawerToggleDelegate();
		} else {
			mActivityImpl = null;
		}
		mDrawerLayout = drawerLayout;
		mDrawerImageResource = drawerImageRes;
		mOpenDrawerContentDescRes = openDrawerContentDescRes;
		mCloseDrawerContentDescRes = closeDrawerContentDescRes;
		mThemeImage = getThemeUpIndicator();
		mDrawerImage = activity.getResources().getDrawable(drawerImageRes);
		mSlider = new SlideDrawable(this, mDrawerImage, null);
		mSlider.setOffset(TOGGLE_DRAWABLE_OFFSET);
	}

	Drawable getThemeUpIndicator() {
		if (mActivityImpl != null) {
			return mActivityImpl.getThemeUpIndicator();
		} else {
			return IMPL.getThemeUpIndicator(mActivity);
		}
	}

	public boolean isDrawerIndicatorEnabled() {
		return mDrawerIndicatorEnabled;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		mThemeImage = getThemeUpIndicator();
		mDrawerImage = mActivity.getResources().getDrawable(mDrawerImageResource);
		syncState();
	}

	public void onDrawerClosed(View drawerView) {
		mSlider.setPosition(AutoScrollHelper.RELATIVE_UNSPECIFIED);
		if (mDrawerIndicatorEnabled) {
			setActionBarDescription(mOpenDrawerContentDescRes);
		}
	}

	public void onDrawerOpened(View drawerView) {
		mSlider.setPosition(1.0f);
		if (mDrawerIndicatorEnabled) {
			setActionBarDescription(mCloseDrawerContentDescRes);
		}
	}

	public void onDrawerSlide(View drawerView, float slideOffset) {
		float r2f = 0.5f;
		float glyphOffset = mSlider.getPosition();
		if (slideOffset > 0.5f) {
			glyphOffset = Math.max(glyphOffset, Math.max(AutoScrollHelper.RELATIVE_UNSPECIFIED, slideOffset - r2f) * 2.0f);
		} else {
			glyphOffset = Math.min(glyphOffset, slideOffset * 2.0f);
		}
		mSlider.setPosition(glyphOffset);
	}

	public void onDrawerStateChanged(int newState) {
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null || item.getItemId() != 16908332 || !mDrawerIndicatorEnabled) {
			return false;
		} else {
			if (mDrawerLayout.isDrawerVisible((int)GravityCompat.START)) {
				mDrawerLayout.closeDrawer((int)GravityCompat.START);
			} else {
				mDrawerLayout.openDrawer((int)GravityCompat.START);
			}
			return true;
		}
	}

	void setActionBarDescription(int contentDescRes) {
		if (mActivityImpl != null) {
			mActivityImpl.setActionBarDescription(contentDescRes);
		} else {
			mSetIndicatorInfo = IMPL.setActionBarDescription(mSetIndicatorInfo, mActivity, contentDescRes);
		}
	}

	void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
		if (mActivityImpl != null) {
			mActivityImpl.setActionBarUpIndicator(upDrawable, contentDescRes);
		} else {
			mSetIndicatorInfo = IMPL.setActionBarUpIndicator(mSetIndicatorInfo, mActivity, upDrawable, contentDescRes);
		}
	}

	public void setDrawerIndicatorEnabled(boolean enable) {
		if (enable != mDrawerIndicatorEnabled) {
			if (enable) {
				int r0i;
				Drawable r1_Drawable = mSlider;
				if (mDrawerLayout.isDrawerOpen((int)GravityCompat.START)) {
					r0i = mCloseDrawerContentDescRes;
				} else {
					r0i = mOpenDrawerContentDescRes;
				}
				setActionBarUpIndicator(r1_Drawable, r0i);
			} else {
				setActionBarUpIndicator(mThemeImage, 0);
			}
			mDrawerIndicatorEnabled = enable;
		}
	}

	public void syncState() {
		if (mDrawerLayout.isDrawerOpen((int)GravityCompat.START)) {
			mSlider.setPosition(1.0f);
		} else {
			mSlider.setPosition(AutoScrollHelper.RELATIVE_UNSPECIFIED);
		}
		if (mDrawerIndicatorEnabled) {
			int r0i;
			Drawable r1_Drawable = mSlider;
			if (mDrawerLayout.isDrawerOpen((int)GravityCompat.START)) {
				r0i = mCloseDrawerContentDescRes;
			} else {
				r0i = mOpenDrawerContentDescRes;
			}
			setActionBarUpIndicator(r1_Drawable, r0i);
		}
	}
}
