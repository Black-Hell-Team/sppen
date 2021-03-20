package android.support.v4.view;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class ViewCompat {
	public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
	public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
	public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
	private static final long FAKE_FRAME_TIME = 10;
	static final ViewCompatImpl IMPL;
	public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
	public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
	public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
	public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
	public static final int LAYER_TYPE_HARDWARE = 2;
	public static final int LAYER_TYPE_NONE = 0;
	public static final int LAYER_TYPE_SOFTWARE = 1;
	public static final int LAYOUT_DIRECTION_INHERIT = 2;
	public static final int LAYOUT_DIRECTION_LOCALE = 3;
	public static final int LAYOUT_DIRECTION_LTR = 0;
	public static final int LAYOUT_DIRECTION_RTL = 1;
	public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
	public static final int MEASURED_SIZE_MASK = 16777215;
	public static final int MEASURED_STATE_MASK = -16777216;
	public static final int MEASURED_STATE_TOO_SMALL = 16777216;
	public static final int OVER_SCROLL_ALWAYS = 0;
	public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
	public static final int OVER_SCROLL_NEVER = 2;
	private static final String TAG = "ViewCompat";

	@IntDef({0, 1, 2})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface AccessibilityLiveRegion {
	}

	@IntDef({0, 1, 2, 4})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface ImportantForAccessibility {
	}

	@IntDef({0, 1, 2})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface LayerType {
	}

	@IntDef({0, 1, 2, 3})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface LayoutDirectionMode {
	}

	@IntDef({0, 1, 1})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface OverScroll {
	}

	@IntDef({0, 1})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface ResolvedLayoutDirectionMode {
	}

	static interface ViewCompatImpl {
		public ViewPropertyAnimatorCompat animate(View r1_View);

		public boolean canScrollHorizontally(View r1_View, int r2i);

		public boolean canScrollVertically(View r1_View, int r2i);

		public void dispatchFinishTemporaryDetach(View r1_View);

		public void dispatchStartTemporaryDetach(View r1_View);

		public int getAccessibilityLiveRegion(View r1_View);

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View r1_View);

		public float getAlpha(View r1_View);

		public int getImportantForAccessibility(View r1_View);

		public int getLabelFor(View r1_View);

		public int getLayerType(View r1_View);

		public int getLayoutDirection(View r1_View);

		public int getMeasuredHeightAndState(View r1_View);

		public int getMeasuredState(View r1_View);

		public int getMeasuredWidthAndState(View r1_View);

		public int getMinimumHeight(View r1_View);

		public int getMinimumWidth(View r1_View);

		public int getOverScrollMode(View r1_View);

		public int getPaddingEnd(View r1_View);

		public int getPaddingStart(View r1_View);

		public ViewParent getParentForAccessibility(View r1_View);

		public float getPivotX(View r1_View);

		public float getPivotY(View r1_View);

		public float getRotation(View r1_View);

		public float getRotationX(View r1_View);

		public float getRotationY(View r1_View);

		public float getScaleX(View r1_View);

		public float getScaleY(View r1_View);

		public float getTranslationX(View r1_View);

		public float getTranslationY(View r1_View);

		public float getX(View r1_View);

		public float getY(View r1_View);

		public boolean hasTransientState(View r1_View);

		public boolean isOpaque(View r1_View);

		public void onInitializeAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public void onInitializeAccessibilityNodeInfo(View r1_View, AccessibilityNodeInfoCompat r2_AccessibilityNodeInfoCompat);

		public void onPopulateAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public boolean performAccessibilityAction(View r1_View, int r2i, Bundle r3_Bundle);

		public void postInvalidateOnAnimation(View r1_View);

		public void postInvalidateOnAnimation(View r1_View, int r2i, int r3i, int r4i, int r5i);

		public void postOnAnimation(View r1_View, Runnable r2_Runnable);

		public void postOnAnimationDelayed(View r1_View, Runnable r2_Runnable, long r3j);

		public int resolveSizeAndState(int r1i, int r2i, int r3i);

		public void setAccessibilityDelegate(View r1_View, AccessibilityDelegateCompat r2_AccessibilityDelegateCompat);

		public void setAccessibilityLiveRegion(View r1_View, int r2i);

		public void setAlpha(View r1_View, float r2f);

		public void setHasTransientState(View r1_View, boolean r2z);

		public void setImportantForAccessibility(View r1_View, int r2i);

		public void setLabelFor(View r1_View, int r2i);

		public void setLayerPaint(View r1_View, Paint r2_Paint);

		public void setLayerType(View r1_View, int r2i, Paint r3_Paint);

		public void setLayoutDirection(View r1_View, int r2i);

		public void setOverScrollMode(View r1_View, int r2i);

		public void setPaddingRelative(View r1_View, int r2i, int r3i, int r4i, int r5i);

		public void setPivotX(View r1_View, float r2f);

		public void setPivotY(View r1_View, float r2f);

		public void setRotation(View r1_View, float r2f);

		public void setRotationX(View r1_View, float r2f);

		public void setRotationY(View r1_View, float r2f);

		public void setScaleX(View r1_View, float r2f);

		public void setScaleY(View r1_View, float r2f);

		public void setTranslationX(View r1_View, float r2f);

		public void setTranslationY(View r1_View, float r2f);

		public void setX(View r1_View, float r2f);

		public void setY(View r1_View, float r2f);
	}

	static class BaseViewCompatImpl implements ViewCompat.ViewCompatImpl {
		private Method mDispatchFinishTemporaryDetach;
		private Method mDispatchStartTemporaryDetach;
		private boolean mTempDetachBound;
		WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap;

		BaseViewCompatImpl() {
			super();
			mViewPropertyAnimatorCompatMap = null;
		}

		private void bindTempDetach() {
			try {
				mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
				mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
			} catch (NoSuchMethodException e) {
				Log.e(TAG, "Couldn't find method", e);
			}
			mTempDetachBound = true;
		}

		public ViewPropertyAnimatorCompat animate(View view) {
			return new ViewPropertyAnimatorCompat(view);
		}

		public boolean canScrollHorizontally(View v, int direction) {
			return false;
		}

		public boolean canScrollVertically(View v, int direction) {
			return false;
		}

		public void dispatchFinishTemporaryDetach(View view) {
			if (!mTempDetachBound) {
				bindTempDetach();
			}
			if (mDispatchFinishTemporaryDetach != null) {
				try {
					mDispatchFinishTemporaryDetach.invoke(view, new Object[0]);
				} catch (Exception e) {
					Log.d(TAG, "Error calling dispatchFinishTemporaryDetach", e);
					return;
				}
			} else {
				view.onFinishTemporaryDetach();
			}
		}

		public void dispatchStartTemporaryDetach(View view) {
			if (!mTempDetachBound) {
				bindTempDetach();
			}
			if (mDispatchStartTemporaryDetach != null) {
				try {
					mDispatchStartTemporaryDetach.invoke(view, new Object[0]);
				} catch (Exception e) {
					Log.d(TAG, "Error calling dispatchStartTemporaryDetach", e);
					return;
				}
			} else {
				view.onStartTemporaryDetach();
			}
		}

		public int getAccessibilityLiveRegion(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
			return null;
		}

		public float getAlpha(View view) {
			return 1.0f;
		}

		long getFrameTime() {
			return FAKE_FRAME_TIME;
		}

		public int getImportantForAccessibility(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getLabelFor(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getLayerType(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getLayoutDirection(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getMeasuredHeightAndState(View view) {
			return view.getMeasuredHeight();
		}

		public int getMeasuredState(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getMeasuredWidthAndState(View view) {
			return view.getMeasuredWidth();
		}

		public int getMinimumHeight(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getMinimumWidth(View view) {
			return OVER_SCROLL_ALWAYS;
		}

		public int getOverScrollMode(View v) {
			return OVER_SCROLL_NEVER;
		}

		public int getPaddingEnd(View view) {
			return view.getPaddingRight();
		}

		public int getPaddingStart(View view) {
			return view.getPaddingLeft();
		}

		public ViewParent getParentForAccessibility(View view) {
			return view.getParent();
		}

		public float getPivotX(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getPivotY(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getRotation(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getRotationX(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getRotationY(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getScaleX(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getScaleY(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getTranslationX(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getTranslationY(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getX(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public float getY(View view) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public boolean hasTransientState(View view) {
			return false;
		}

		public boolean isOpaque(View view) {
			boolean r1z = false;
			Drawable bg = view.getBackground();
			if (bg == null || bg.getOpacity() != -1) {
				return r1z;
			} else {
				r1z = true;
				return r1z;
			}
		}

		public void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
		}

		public void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
		}

		public void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
		}

		public boolean performAccessibilityAction(View view, int action, Bundle arguments) {
			return false;
		}

		public void postInvalidateOnAnimation(View view) {
			view.invalidate();
		}

		public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
			view.invalidate(left, top, right, bottom);
		}

		public void postOnAnimation(View view, Runnable action) {
			view.postDelayed(action, getFrameTime());
		}

		public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
			view.postDelayed(action, getFrameTime() + delayMillis);
		}

		public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
			return View.resolveSize(size, measureSpec);
		}

		public void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
		}

		public void setAccessibilityLiveRegion(View view, int mode) {
		}

		public void setAlpha(View view, float value) {
		}

		public void setHasTransientState(View view, boolean hasTransientState) {
		}

		public void setImportantForAccessibility(View view, int mode) {
		}

		public void setLabelFor(View view, int id) {
		}

		public void setLayerPaint(View view, Paint p) {
		}

		public void setLayerType(View view, int layerType, Paint paint) {
		}

		public void setLayoutDirection(View view, int layoutDirection) {
		}

		public void setOverScrollMode(View v, int mode) {
		}

		public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
			view.setPadding(start, top, end, bottom);
		}

		public void setPivotX(View view, float value) {
		}

		public void setPivotY(View view, float value) {
		}

		public void setRotation(View view, float value) {
		}

		public void setRotationX(View view, float value) {
		}

		public void setRotationY(View view, float value) {
		}

		public void setScaleX(View view, float value) {
		}

		public void setScaleY(View view, float value) {
		}

		public void setTranslationX(View view, float value) {
		}

		public void setTranslationY(View view, float value) {
		}

		public void setX(View view, float value) {
		}

		public void setY(View view, float value) {
		}
	}

	static class EclairMr1ViewCompatImpl extends ViewCompat.BaseViewCompatImpl {
		EclairMr1ViewCompatImpl() {
			super();
		}

		public boolean isOpaque(View view) {
			return ViewCompatEclairMr1.isOpaque(view);
		}
	}

	static class GBViewCompatImpl extends ViewCompat.EclairMr1ViewCompatImpl {
		GBViewCompatImpl() {
			super();
		}

		public int getOverScrollMode(View v) {
			return ViewCompatGingerbread.getOverScrollMode(v);
		}

		public void setOverScrollMode(View v, int mode) {
			ViewCompatGingerbread.setOverScrollMode(v, mode);
		}
	}

	static class HCViewCompatImpl extends ViewCompat.GBViewCompatImpl {
		HCViewCompatImpl() {
			super();
		}

		public float getAlpha(View view) {
			return ViewCompatHC.getAlpha(view);
		}

		long getFrameTime() {
			return ViewCompatHC.getFrameTime();
		}

		public int getLayerType(View view) {
			return ViewCompatHC.getLayerType(view);
		}

		public int getMeasuredHeightAndState(View view) {
			return ViewCompatHC.getMeasuredHeightAndState(view);
		}

		public int getMeasuredState(View view) {
			return ViewCompatHC.getMeasuredState(view);
		}

		public int getMeasuredWidthAndState(View view) {
			return ViewCompatHC.getMeasuredWidthAndState(view);
		}

		public float getPivotX(View view) {
			return ViewCompatHC.getPivotX(view);
		}

		public float getPivotY(View view) {
			return ViewCompatHC.getPivotY(view);
		}

		public float getRotation(View view) {
			return ViewCompatHC.getRotation(view);
		}

		public float getRotationX(View view) {
			return ViewCompatHC.getRotationX(view);
		}

		public float getRotationY(View view) {
			return ViewCompatHC.getRotationY(view);
		}

		public float getScaleX(View view) {
			return ViewCompatHC.getScaleX(view);
		}

		public float getScaleY(View view) {
			return ViewCompatHC.getScaleY(view);
		}

		public float getTranslationX(View view) {
			return ViewCompatHC.getTranslationX(view);
		}

		public float getTranslationY(View view) {
			return ViewCompatHC.getTranslationY(view);
		}

		public float getX(View view) {
			return ViewCompatHC.getX(view);
		}

		public float getY(View view) {
			return ViewCompatHC.getY(view);
		}

		public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
			return ViewCompatHC.resolveSizeAndState(size, measureSpec, childMeasuredState);
		}

		public void setAlpha(View view, float value) {
			ViewCompatHC.setAlpha(view, value);
		}

		public void setLayerPaint(View view, Paint paint) {
			setLayerType(view, getLayerType(view), paint);
			view.invalidate();
		}

		public void setLayerType(View view, int layerType, Paint paint) {
			ViewCompatHC.setLayerType(view, layerType, paint);
		}

		public void setPivotX(View view, float value) {
			ViewCompatHC.setPivotX(view, value);
		}

		public void setPivotY(View view, float value) {
			ViewCompatHC.setPivotY(view, value);
		}

		public void setRotation(View view, float value) {
			ViewCompatHC.setRotation(view, value);
		}

		public void setRotationX(View view, float value) {
			ViewCompatHC.setRotationX(view, value);
		}

		public void setRotationY(View view, float value) {
			ViewCompatHC.setRotationY(view, value);
		}

		public void setScaleX(View view, float value) {
			ViewCompatHC.setScaleX(view, value);
		}

		public void setScaleY(View view, float value) {
			ViewCompatHC.setScaleY(view, value);
		}

		public void setTranslationX(View view, float value) {
			ViewCompatHC.setTranslationX(view, value);
		}

		public void setTranslationY(View view, float value) {
			ViewCompatHC.setTranslationY(view, value);
		}

		public void setX(View view, float value) {
			ViewCompatHC.setX(view, value);
		}

		public void setY(View view, float value) {
			ViewCompatHC.setY(view, value);
		}
	}

	static class ICSViewCompatImpl extends ViewCompat.HCViewCompatImpl {
		ICSViewCompatImpl() {
			super();
		}

		public ViewPropertyAnimatorCompat animate(View view) {
			if (mViewPropertyAnimatorCompatMap == null) {
				mViewPropertyAnimatorCompatMap = new WeakHashMap();
			}
			ViewPropertyAnimatorCompat vpa = (ViewPropertyAnimatorCompat) mViewPropertyAnimatorCompatMap.get(view);
			if (vpa == null) {
				vpa = new ViewPropertyAnimatorCompat(view);
				mViewPropertyAnimatorCompatMap.put(view, vpa);
			}
			return vpa;
		}

		public boolean canScrollHorizontally(View v, int direction) {
			return ViewCompatICS.canScrollHorizontally(v, direction);
		}

		public boolean canScrollVertically(View v, int direction) {
			return ViewCompatICS.canScrollVertically(v, direction);
		}

		public void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
			ViewCompatICS.onInitializeAccessibilityEvent(v, event);
		}

		public void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
			ViewCompatICS.onInitializeAccessibilityNodeInfo(v, info.getInfo());
		}

		public void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
			ViewCompatICS.onPopulateAccessibilityEvent(v, event);
		}

		public void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
			ViewCompatICS.setAccessibilityDelegate(v, delegate.getBridge());
		}
	}

	static class JBViewCompatImpl extends ViewCompat.ICSViewCompatImpl {
		JBViewCompatImpl() {
			super();
		}

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
			Object compat = ViewCompatJB.getAccessibilityNodeProvider(view);
			if (compat != null) {
				return new AccessibilityNodeProviderCompat(compat);
			} else {
				return null;
			}
		}

		public int getImportantForAccessibility(View view) {
			return ViewCompatJB.getImportantForAccessibility(view);
		}

		public int getMinimumHeight(View view) {
			return ViewCompatJB.getMinimumHeight(view);
		}

		public int getMinimumWidth(View view) {
			return ViewCompatJB.getMinimumWidth(view);
		}

		public ViewParent getParentForAccessibility(View view) {
			return ViewCompatJB.getParentForAccessibility(view);
		}

		public boolean hasTransientState(View view) {
			return ViewCompatJB.hasTransientState(view);
		}

		public boolean performAccessibilityAction(View view, int action, Bundle arguments) {
			return ViewCompatJB.performAccessibilityAction(view, action, arguments);
		}

		public void postInvalidateOnAnimation(View view) {
			ViewCompatJB.postInvalidateOnAnimation(view);
		}

		public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
			ViewCompatJB.postInvalidateOnAnimation(view, left, top, right, bottom);
		}

		public void postOnAnimation(View view, Runnable action) {
			ViewCompatJB.postOnAnimation(view, action);
		}

		public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
			ViewCompatJB.postOnAnimationDelayed(view, action, delayMillis);
		}

		public void setHasTransientState(View view, boolean hasTransientState) {
			ViewCompatJB.setHasTransientState(view, hasTransientState);
		}

		public void setImportantForAccessibility(View view, int mode) {
			if (mode == 4) {
				mode = OVER_SCROLL_NEVER;
			}
			ViewCompatJB.setImportantForAccessibility(view, mode);
		}
	}

	static class JbMr1ViewCompatImpl extends ViewCompat.JBViewCompatImpl {
		JbMr1ViewCompatImpl() {
			super();
		}

		public int getLabelFor(View view) {
			return ViewCompatJellybeanMr1.getLabelFor(view);
		}

		public int getLayoutDirection(View view) {
			return ViewCompatJellybeanMr1.getLayoutDirection(view);
		}

		public int getPaddingEnd(View view) {
			return ViewCompatJellybeanMr1.getPaddingEnd(view);
		}

		public int getPaddingStart(View view) {
			return ViewCompatJellybeanMr1.getPaddingStart(view);
		}

		public void setLabelFor(View view, int id) {
			ViewCompatJellybeanMr1.setLabelFor(view, id);
		}

		public void setLayerPaint(View view, Paint paint) {
			ViewCompatJellybeanMr1.setLayerPaint(view, paint);
		}

		public void setLayoutDirection(View view, int layoutDirection) {
			ViewCompatJellybeanMr1.setLayoutDirection(view, layoutDirection);
		}

		public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
			ViewCompatJellybeanMr1.setPaddingRelative(view, start, top, end, bottom);
		}
	}

	static class KitKatViewCompatImpl extends ViewCompat.JbMr1ViewCompatImpl {
		KitKatViewCompatImpl() {
			super();
		}

		public int getAccessibilityLiveRegion(View view) {
			return ViewCompatKitKat.getAccessibilityLiveRegion(view);
		}

		public void setAccessibilityLiveRegion(View view, int mode) {
			ViewCompatKitKat.setAccessibilityLiveRegion(view, mode);
		}

		public void setImportantForAccessibility(View view, int mode) {
			ViewCompatJB.setImportantForAccessibility(view, mode);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 19) {
			IMPL = new KitKatViewCompatImpl();
		} else if (version >= 17) {
			IMPL = new JbMr1ViewCompatImpl();
		} else if (version >= 16) {
			IMPL = new JBViewCompatImpl();
		} else if (version >= 14) {
			IMPL = new ICSViewCompatImpl();
		} else if (version >= 11) {
			IMPL = new HCViewCompatImpl();
		} else if (version >= 9) {
			IMPL = new GBViewCompatImpl();
		} else {
			IMPL = new BaseViewCompatImpl();
		}
	}

	public ViewCompat() {
		super();
	}

	public static ViewPropertyAnimatorCompat animate(View view) {
		return IMPL.animate(view);
	}

	public static boolean canScrollHorizontally(View v, int direction) {
		return IMPL.canScrollHorizontally(v, direction);
	}

	public static boolean canScrollVertically(View v, int direction) {
		return IMPL.canScrollVertically(v, direction);
	}

	public static void dispatchFinishTemporaryDetach(View view) {
		IMPL.dispatchFinishTemporaryDetach(view);
	}

	public static void dispatchStartTemporaryDetach(View view) {
		IMPL.dispatchStartTemporaryDetach(view);
	}

	public static int getAccessibilityLiveRegion(View view) {
		return IMPL.getAccessibilityLiveRegion(view);
	}

	public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
		return IMPL.getAccessibilityNodeProvider(view);
	}

	public static float getAlpha(View view) {
		return IMPL.getAlpha(view);
	}

	public static int getImportantForAccessibility(View view) {
		return IMPL.getImportantForAccessibility(view);
	}

	public static int getLabelFor(View view) {
		return IMPL.getLabelFor(view);
	}

	public static int getLayerType(View view) {
		return IMPL.getLayerType(view);
	}

	public static int getLayoutDirection(View view) {
		return IMPL.getLayoutDirection(view);
	}

	public static int getMeasuredHeightAndState(View view) {
		return IMPL.getMeasuredHeightAndState(view);
	}

	public static int getMeasuredState(View view) {
		return IMPL.getMeasuredState(view);
	}

	public static int getMeasuredWidthAndState(View view) {
		return IMPL.getMeasuredWidthAndState(view);
	}

	public static int getMinimumHeight(View view) {
		return IMPL.getMinimumHeight(view);
	}

	public static int getMinimumWidth(View view) {
		return IMPL.getMinimumWidth(view);
	}

	public static int getOverScrollMode(View v) {
		return IMPL.getOverScrollMode(v);
	}

	public static int getPaddingEnd(View view) {
		return IMPL.getPaddingEnd(view);
	}

	public static int getPaddingStart(View view) {
		return IMPL.getPaddingStart(view);
	}

	public static ViewParent getParentForAccessibility(View view) {
		return IMPL.getParentForAccessibility(view);
	}

	public static float getTranslationX(View view) {
		return IMPL.getTranslationX(view);
	}

	public static float getTranslationY(View view) {
		return IMPL.getTranslationY(view);
	}

	public static boolean hasTransientState(View view) {
		return IMPL.hasTransientState(view);
	}

	public static boolean isOpaque(View view) {
		return IMPL.isOpaque(view);
	}

	public static void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
		IMPL.onInitializeAccessibilityEvent(v, event);
	}

	public static void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
		IMPL.onInitializeAccessibilityNodeInfo(v, info);
	}

	public static void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
		IMPL.onPopulateAccessibilityEvent(v, event);
	}

	public static boolean performAccessibilityAction(View view, int action, Bundle arguments) {
		return IMPL.performAccessibilityAction(view, action, arguments);
	}

	public static void postInvalidateOnAnimation(View view) {
		IMPL.postInvalidateOnAnimation(view);
	}

	public static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
		IMPL.postInvalidateOnAnimation(view, left, top, right, bottom);
	}

	public static void postOnAnimation(View view, Runnable action) {
		IMPL.postOnAnimation(view, action);
	}

	public static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
		IMPL.postOnAnimationDelayed(view, action, delayMillis);
	}

	public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
		return IMPL.resolveSizeAndState(size, measureSpec, childMeasuredState);
	}

	public static void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
		IMPL.setAccessibilityDelegate(v, delegate);
	}

	public static void setAccessibilityLiveRegion(View view, int mode) {
		IMPL.setAccessibilityLiveRegion(view, mode);
	}

	public static void setAlpha(View view, float value) {
		IMPL.setAlpha(view, value);
	}

	public static void setHasTransientState(View view, boolean hasTransientState) {
		IMPL.setHasTransientState(view, hasTransientState);
	}

	public static void setImportantForAccessibility(View view, int mode) {
		IMPL.setImportantForAccessibility(view, mode);
	}

	public static void setLabelFor(View view, int labeledId) {
		IMPL.setLabelFor(view, labeledId);
	}

	public static void setLayerPaint(View view, Paint paint) {
		IMPL.setLayerPaint(view, paint);
	}

	public static void setLayerType(View view, int layerType, Paint paint) {
		IMPL.setLayerType(view, layerType, paint);
	}

	public static void setLayoutDirection(View view, int layoutDirection) {
		IMPL.setLayoutDirection(view, layoutDirection);
	}

	public static void setOverScrollMode(View v, int overScrollMode) {
		IMPL.setOverScrollMode(v, overScrollMode);
	}

	public static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
		IMPL.setPaddingRelative(view, start, top, end, bottom);
	}

	public static void setRotation(View view, float value) {
		IMPL.setRotation(view, value);
	}

	public static void setRotationX(View view, float value) {
		IMPL.setRotationX(view, value);
	}

	public static void setRotationY(View view, float value) {
		IMPL.setRotationY(view, value);
	}

	public static void setScaleX(View view, float value) {
		IMPL.setScaleX(view, value);
	}

	public static void setScaleY(View view, float value) {
		IMPL.setScaleY(view, value);
	}

	public static void setTranslationX(View view, float value) {
		IMPL.setTranslationX(view, value);
	}

	public static void setTranslationY(View view, float value) {
		IMPL.setTranslationY(view, value);
	}

	public static void setX(View view, float value) {
		IMPL.setX(view, value);
	}

	public static void setY(View view, float value) {
		IMPL.setY(view, value);
	}

	public float getPivotX(View view) {
		return IMPL.getPivotX(view);
	}

	public float getPivotY(View view) {
		return IMPL.getPivotY(view);
	}

	public float getRotation(View view) {
		return IMPL.getRotation(view);
	}

	public float getRotationX(View view) {
		return IMPL.getRotationX(view);
	}

	public float getRotationY(View view) {
		return IMPL.getRotationY(view);
	}

	public float getScaleX(View view) {
		return IMPL.getScaleX(view);
	}

	public float getScaleY(View view) {
		return IMPL.getScaleY(view);
	}

	public float getX(View view) {
		return IMPL.getX(view);
	}

	public float getY(View view) {
		return IMPL.getY(view);
	}

	public void setPivotX(View view, float value) {
		IMPL.setPivotX(view, value);
	}

	public void setPivotY(View view, float value) {
		IMPL.setPivotX(view, value);
	}
}
