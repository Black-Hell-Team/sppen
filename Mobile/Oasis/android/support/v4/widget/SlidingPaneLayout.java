package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
	private static final int DEFAULT_FADE_COLOR = -858993460;
	private static final int DEFAULT_OVERHANG_SIZE = 32;
	static final SlidingPanelLayoutImpl IMPL;
	private static final int MIN_FLING_VELOCITY = 400;
	private static final String TAG = "SlidingPaneLayout";
	private boolean mCanSlide;
	private int mCoveredFadeColor;
	private final ViewDragHelper mDragHelper;
	private boolean mFirstLayout;
	private float mInitialMotionX;
	private float mInitialMotionY;
	private boolean mIsUnableToDrag;
	private final int mOverhangSize;
	private PanelSlideListener mPanelSlideListener;
	private int mParallaxBy;
	private float mParallaxOffset;
	private final ArrayList<DisableLayerRunnable> mPostedRunnables;
	private boolean mPreservedOpenState;
	private Drawable mShadowDrawableLeft;
	private Drawable mShadowDrawableRight;
	private float mSlideOffset;
	private int mSlideRange;
	private View mSlideableView;
	private int mSliderFadeColor;
	private final Rect mTmpRect;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private class DisableLayerRunnable implements Runnable {
		final View mChildView;
		final /* synthetic */ SlidingPaneLayout this$0;

		DisableLayerRunnable(SlidingPaneLayout r1_SlidingPaneLayout, View childView) {
			super();
			this$0 = r1_SlidingPaneLayout;
			mChildView = childView;
		}

		public void run() {
			if (mChildView.getParent() == this$0) {
				ViewCompat.setLayerType(mChildView, 0, null);
				this$0.invalidateChildRegion(mChildView);
			}
			this$0.mPostedRunnables.remove(this);
		}
	}

	public static class LayoutParams extends MarginLayoutParams {
		private static final int[] ATTRS;
		Paint dimPaint;
		boolean dimWhenOffset;
		boolean slideable;
		public float weight;

		static {
			int[] r0_int_A = new int[1];
			r0_int_A[0] = 16843137;
			ATTRS = r0_int_A;
		}

		public LayoutParams() {
			super(-1, -1);
			weight = 0.0f;
		}

		public LayoutParams(int width, int height) {
			super(width, height);
			weight = 0.0f;
		}

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			weight = 0.0f;
			TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
			weight = a.getFloat(0, AutoScrollHelper.RELATIVE_UNSPECIFIED);
			a.recycle();
		}

		public LayoutParams(SlidingPaneLayout.LayoutParams source) {
			super(source);
			weight = 0.0f;
			weight = source.weight;
		}

		public LayoutParams(android.view.ViewGroup.LayoutParams source) {
			super(source);
			weight = 0.0f;
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
			weight = 0.0f;
		}
	}

	public static interface PanelSlideListener {
		public void onPanelClosed(View r1_View);

		public void onPanelOpened(View r1_View);

		public void onPanelSlide(View r1_View, float r2f);
	}

	static class SavedState extends BaseSavedState {
		public static final Creator<SlidingPaneLayout.SavedState> CREATOR;
		boolean isOpen;

		static class AnonymousClass_1 implements Creator<SlidingPaneLayout.SavedState> {
			AnonymousClass_1() {
				super();
			}

			public SlidingPaneLayout.SavedState createFromParcel(Parcel in) {
				return new SlidingPaneLayout.SavedState(in, null);
			}

			public SlidingPaneLayout.SavedState[] newArray(int size) {
				return new SlidingPaneLayout.SavedState[size];
			}
		}


		static {
			CREATOR = new AnonymousClass_1();
		}

		private SavedState(Parcel in) {
			boolean r0z;
			super(in);
			if (in.readInt() != 0) {
				r0z = true;
			} else {
				r0z = false;
			}
			isOpen = r0z;
		}

		/* synthetic */ SavedState(Parcel x0, SlidingPaneLayout.AnonymousClass_1 x1) {
			this(x0);
		}

		SavedState(Parcelable superState) {
			super(superState);
		}

		public void writeToParcel(Parcel out, int flags) {
			int r0i;
			super.writeToParcel(out, flags);
			if (isOpen) {
				r0i = 1;
			} else {
				r0i = 0;
			}
			out.writeInt(r0i);
		}
	}

	static interface SlidingPanelLayoutImpl {
		public void invalidateChildRegion(SlidingPaneLayout r1_SlidingPaneLayout, View r2_View);
	}

	class AccessibilityDelegate extends AccessibilityDelegateCompat {
		private final Rect mTmpRect;
		final /* synthetic */ SlidingPaneLayout this$0;

		AccessibilityDelegate(SlidingPaneLayout r2_SlidingPaneLayout) {
			super();
			this$0 = r2_SlidingPaneLayout;
			mTmpRect = new Rect();
		}

		private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
			Rect rect = mTmpRect;
			src.getBoundsInParent(rect);
			dest.setBoundsInParent(rect);
			src.getBoundsInScreen(rect);
			dest.setBoundsInScreen(rect);
			dest.setVisibleToUser(src.isVisibleToUser());
			dest.setPackageName(src.getPackageName());
			dest.setClassName(src.getClassName());
			dest.setContentDescription(src.getContentDescription());
			dest.setEnabled(src.isEnabled());
			dest.setClickable(src.isClickable());
			dest.setFocusable(src.isFocusable());
			dest.setFocused(src.isFocused());
			dest.setAccessibilityFocused(src.isAccessibilityFocused());
			dest.setSelected(src.isSelected());
			dest.setLongClickable(src.isLongClickable());
			dest.addAction(src.getActions());
			dest.setMovementGranularities(src.getMovementGranularities());
		}

		public boolean filter(View child) {
			return this$0.isDimmed(child);
		}

		public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
			super.onInitializeAccessibilityEvent(host, event);
			event.setClassName(SlidingPaneLayout.class.getName());
		}

		public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
			AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
			super.onInitializeAccessibilityNodeInfo(host, superNode);
			copyNodeInfoNoChildren(info, superNode);
			superNode.recycle();
			info.setClassName(SlidingPaneLayout.class.getName());
			info.setSource(host);
			ViewParent parent = ViewCompat.getParentForAccessibility(host);
			if (parent instanceof View) {
				info.setParent((View) parent);
			}
			int i = 0;
			while (i < this$0.getChildCount()) {
				View child = this$0.getChildAt(i);
				if (filter(child) || child.getVisibility() != 0) {
					i++;
				} else {
					ViewCompat.setImportantForAccessibility(child, 1);
					info.addChild(child);
					i++;
				}
			}
		}

		public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
			if (!filter(child)) {
				return super.onRequestSendAccessibilityEvent(host, child, event);
			} else {
				return false;
			}
		}
	}

	private class DragHelperCallback extends Callback {
		final /* synthetic */ SlidingPaneLayout this$0;

		private DragHelperCallback(SlidingPaneLayout r1_SlidingPaneLayout) {
			super();
			this$0 = r1_SlidingPaneLayout;
		}

		/* synthetic */ DragHelperCallback(SlidingPaneLayout x0, SlidingPaneLayout.AnonymousClass_1 x1) {
			this(x0);
		}

		public int clampViewPositionHorizontal(View child, int left, int dx) {
			SlidingPaneLayout.LayoutParams lp = (SlidingPaneLayout.LayoutParams) this$0.mSlideableView.getLayoutParams();
			int startBound;
			if (this$0.isLayoutRtlSupport()) {
				startBound = this$0.getWidth() - ((this$0.getPaddingRight() + lp.rightMargin) + this$0.mSlideableView.getWidth());
				return Math.max(Math.min(left, startBound), startBound - this$0.mSlideRange);
			} else {
				startBound = this$0.getPaddingLeft() + lp.leftMargin;
				return Math.min(Math.max(left, startBound), startBound + this$0.mSlideRange);
			}
		}

		public int clampViewPositionVertical(View child, int top, int dy) {
			return child.getTop();
		}

		public int getViewHorizontalDragRange(View child) {
			return this$0.mSlideRange;
		}

		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
			this$0.mDragHelper.captureChildView(this$0.mSlideableView, pointerId);
		}

		public void onViewCaptured(View capturedChild, int activePointerId) {
			this$0.setAllChildrenVisible();
		}

		public void onViewDragStateChanged(int state) {
			if (this$0.mDragHelper.getViewDragState() == 0) {
				if (this$0.mSlideOffset == 0.0f) {
					this$0.updateObscuredViewsVisibility(this$0.mSlideableView);
					this$0.dispatchOnPanelClosed(this$0.mSlideableView);
					this$0.mPreservedOpenState = false;
				} else {
					this$0.dispatchOnPanelOpened(this$0.mSlideableView);
					this$0.mPreservedOpenState = true;
				}
			}
		}

		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			this$0.onPanelDragged(left);
			this$0.invalidate();
		}

		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			int left;
			SlidingPaneLayout.LayoutParams lp = (SlidingPaneLayout.LayoutParams) releasedChild.getLayoutParams();
			if (this$0.isLayoutRtlSupport()) {
				int startToRight = this$0.getPaddingRight() + lp.rightMargin;
				if (xvel >= 0.0f) {
					if (xvel != 0.0f || this$0.mSlideOffset <= 0.5f) {
						left = (this$0.getWidth() - startToRight) - this$0.mSlideableView.getWidth();
					} else {
						startToRight += this$0.mSlideRange;
					}
				} else {
					startToRight += this$0.mSlideRange;
				}
				left = (this$0.getWidth() - startToRight) - this$0.mSlideableView.getWidth();
			} else {
				left = this$0.getPaddingLeft() + lp.leftMargin;
				if (xvel <= 0.0f) {
					if (xvel != 0.0f || this$0.mSlideOffset <= 0.5f) {
						this$0.mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
						this$0.invalidate();
					}
				}
				left += this$0.mSlideRange;
			}
			this$0.mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
			this$0.invalidate();
		}

		public boolean tryCaptureView(View child, int pointerId) {
			if (this$0.mIsUnableToDrag) {
				return false;
			} else {
				return ((SlidingPaneLayout.LayoutParams) child.getLayoutParams()).slideable;
			}
		}
	}

	public static class SimplePanelSlideListener implements SlidingPaneLayout.PanelSlideListener {
		public SimplePanelSlideListener() {
			super();
		}

		public void onPanelClosed(View panel) {
		}

		public void onPanelOpened(View panel) {
		}

		public void onPanelSlide(View panel, float slideOffset) {
		}
	}

	static class SlidingPanelLayoutImplBase implements SlidingPaneLayout.SlidingPanelLayoutImpl {
		SlidingPanelLayoutImplBase() {
			super();
		}

		public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
			ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
		}
	}

	static class SlidingPanelLayoutImplJB extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
		private Method mGetDisplayList;
		private Field mRecreateDisplayList;

		SlidingPanelLayoutImplJB() {
			super();
			try {
				mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) false);
			} catch (NoSuchMethodException e) {
				Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
			}
			try {
				mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
				mRecreateDisplayList.setAccessible(true);
			} catch (NoSuchFieldException e_2) {
				Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e_2);
				return;
			}
		}

		public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
			if (mGetDisplayList == null || mRecreateDisplayList == null) {
				child.invalidate();
			} else {
				try {
					mRecreateDisplayList.setBoolean(child, true);
					mGetDisplayList.invoke(child, (Object[]) false);
				} catch (Exception e) {
					Log.e(TAG, "Error refreshing display list state", e);
				}
				super.invalidateChildRegion(parent, child);
			}
		}
	}

	static class SlidingPanelLayoutImplJBMR1 extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
		SlidingPanelLayoutImplJBMR1() {
			super();
		}

		public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
			ViewCompat.setLayerPaint(child, ((SlidingPaneLayout.LayoutParams) child.getLayoutParams()).dimPaint);
		}
	}


	static {
		int deviceVersion = VERSION.SDK_INT;
		if (deviceVersion >= 17) {
			IMPL = new SlidingPanelLayoutImplJBMR1();
		} else if (deviceVersion >= 16) {
			IMPL = new SlidingPanelLayoutImplJB();
		} else {
			IMPL = new SlidingPanelLayoutImplBase();
		}
	}

	public SlidingPaneLayout(Context context) {
		this(context, null);
	}

	public SlidingPaneLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mSliderFadeColor = -858993460;
		mFirstLayout = true;
		mTmpRect = new Rect();
		mPostedRunnables = new ArrayList();
		float density = context.getResources().getDisplayMetrics().density;
		mOverhangSize = (int) ((32.0f * density) + 0.5f);
		setWillNotDraw(false);
		ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate(this));
		ViewCompat.setImportantForAccessibility(this, 1);
		mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback(this, null));
		mDragHelper.setMinVelocity(400.0f * density);
	}

	private boolean closePane(View pane, int initialVelocity) {
		boolean r0z = false;
		if (mFirstLayout || smoothSlideTo(AutoScrollHelper.RELATIVE_UNSPECIFIED, initialVelocity)) {
			mPreservedOpenState = true;
			r0z = true;
		} else {
			return r0z;
		}
		return r0z;
	}

	private void dimChildView(View v, float mag, int fadeColor) {
		LayoutParams lp = (LayoutParams) v.getLayoutParams();
		if (mag <= 0.0f || fadeColor == 0) {
			if (ViewCompat.getLayerType(v) != 0) {
				if (lp.dimPaint != null) {
					lp.dimPaint.setColorFilter(null);
				}
				DisableLayerRunnable dlr = new DisableLayerRunnable(this, v);
				mPostedRunnables.add(dlr);
				ViewCompat.postOnAnimation(this, dlr);
			}
		} else {
			int color = (((int) (((float) ((-16777216 & fadeColor) >>> 24)) * mag)) << 24) | (16777215 & fadeColor);
			if (lp.dimPaint == null) {
				lp.dimPaint = new Paint();
			}
			lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_OVER));
			if (ViewCompat.getLayerType(v) != 2) {
				ViewCompat.setLayerType(v, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, lp.dimPaint);
			}
			invalidateChildRegion(v);
		}
	}

	private void invalidateChildRegion(View v) {
		IMPL.invalidateChildRegion(this, v);
	}

	private boolean isLayoutRtlSupport() {
		if (ViewCompat.getLayoutDirection(this) == 1) {
			return true;
		} else {
			return false;
		}
	}

	private void onPanelDragged(int newLeft) {
		if (mSlideableView == null) {
			mSlideOffset = 0.0f;
		} else {
			int newStart;
			int paddingStart;
			int lpMargin;
			boolean isLayoutRtl = isLayoutRtlSupport();
			LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();
			int childWidth = mSlideableView.getWidth();
			if (isLayoutRtl) {
				newStart = (getWidth() - newLeft) - childWidth;
			} else {
				newStart = newLeft;
			}
			if (isLayoutRtl) {
				paddingStart = getPaddingRight();
			} else {
				paddingStart = getPaddingLeft();
			}
			if (isLayoutRtl) {
				lpMargin = lp.rightMargin;
			} else {
				lpMargin = lp.leftMargin;
			}
			mSlideOffset = ((float) (newStart - (paddingStart + lpMargin))) / ((float) mSlideRange);
			if (mParallaxBy != 0) {
				parallaxOtherViews(mSlideOffset);
			}
			if (lp.dimWhenOffset) {
				dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
			}
			dispatchOnPanelSlide(mSlideableView);
		}
	}

	private boolean openPane(View pane, int initialVelocity) {
		if (mFirstLayout || smoothSlideTo(1.0f, initialVelocity)) {
			mPreservedOpenState = true;
			return true;
		} else {
			return false;
		}
	}

	private void parallaxOtherViews(float slideOffset) {
		boolean dimViews;
		boolean isLayoutRtl = isLayoutRtlSupport();
		LayoutParams slideLp = (LayoutParams) mSlideableView.getLayoutParams();
		if (slideLp.dimWhenOffset) {
			int r9i;
			if (isLayoutRtl) {
				r9i = slideLp.rightMargin;
			} else {
				r9i = slideLp.leftMargin;
			}
			if (r9i <= 0) {
				dimViews = true;
			}
			dimViews = false;
		} else {
			dimViews = false;
		}
		int i = 0;
		while (i < getChildCount()) {
			View v = getChildAt(i);
			if (v == mSlideableView) {
				i++;
			} else {
				mParallaxOffset = slideOffset;
				int dx = ((int) ((1.0f - mParallaxOffset) * ((float) mParallaxBy))) - ((int) ((1.0f - slideOffset) * ((float) mParallaxBy)));
				if (isLayoutRtl) {
					dx = -dx;
				}
				v.offsetLeftAndRight(dx);
				if (dimViews) {
					float r9f;
					if (isLayoutRtl) {
						r9f = mParallaxOffset - 1.0f;
					} else {
						r9f = 1.0f - mParallaxOffset;
					}
					dimChildView(v, r9f, mCoveredFadeColor);
				}
				i++;
			}
		}
	}

	private static boolean viewIsOpaque(View v) {
		if (ViewCompat.isOpaque(v)) {
			return true;
		} else if (VERSION.SDK_INT >= 18) {
			return false;
		} else {
			Drawable bg = v.getBackground();
			if (bg != null) {
				if (bg.getOpacity() != -1) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	protected boolean canScroll(android.view.View r12_v, boolean r13_checkV, int r14_dx, int r15_x, int r16_y) {
		r11_this = this;
		r0 = r12_v instanceof android.view.ViewGroup;
		if (r0 == 0) goto L_0x0059;
	L_0x0004:
		r7 = r12_v;
		r7 = (android.view.ViewGroup) r7;
		r9 = r12_v.getScrollX();
		r10 = r12_v.getScrollY();
		r6 = r7_group.getChildCount();
		r8 = r6_count + -1;
	L_0x0015:
		if (r8_i < 0) goto L_0x0059;
	L_0x0017:
		r1 = r7_group.getChildAt(r8_i);
		r0 = r15_x + r9_scrollX;
		r2 = r1_child.getLeft();
		if (r0 < r2) goto L_0x0056;
	L_0x0023:
		r0 = r15_x + r9_scrollX;
		r2 = r1_child.getRight();
		if (r0 >= r2) goto L_0x0056;
	L_0x002b:
		r0 = r16_y + r10_scrollY;
		r2 = r1_child.getTop();
		if (r0 < r2) goto L_0x0056;
	L_0x0033:
		r0 = r16_y + r10_scrollY;
		r2 = r1_child.getBottom();
		if (r0 >= r2) goto L_0x0056;
	L_0x003b:
		r2 = 1;
		r0 = r15_x + r9_scrollX;
		r3 = r1_child.getLeft();
		r4 = r0 - r3;
		r0 = r16_y + r10_scrollY;
		r3 = r1_child.getTop();
		r5 = r0 - r3;
		r0 = r11;
		r3 = r14_dx;
		r0 = r0.canScroll(r1_child, r2, r3, r4, r5);
		if (r0 == 0) goto L_0x0056;
	L_0x0054:
		r0 = 1;
	L_0x0055:
		return r0;
	L_0x0056:
		r8_i++;
		goto L_0x0015;
	L_0x0059:
		if (r13_checkV == 0) goto L_0x006b;
	L_0x005b:
		r0 = r11.isLayoutRtlSupport();
		if (r0 == 0) goto L_0x0069;
	L_0x0061:
		r0 = android.support.v4.view.ViewCompat.canScrollHorizontally(r12_v, r14_dx);
		if (r0 == 0) goto L_0x006b;
	L_0x0067:
		r0 = 1;
		goto L_0x0055;
	L_0x0069:
		r14_dx = -r14_dx;
		goto L_0x0061;
	L_0x006b:
		r0 = 0;
		goto L_0x0055;
	}
	*/
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) v;
			int scrollX = v.getScrollX();
			int scrollY = v.getScrollY();
			int i = group.getChildCount() - 1;
			while (i >= 0) {
				View child = group.getChildAt(i);
				if (x + scrollX < child.getLeft() || x + scrollX >= child.getRight() || y + scrollY < child.getTop() || y + scrollY >= child.getBottom() || !canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
					i--;
				}
			}
		}
		if (checkV) {
			if (isLayoutRtlSupport()) {
				if (!ViewCompat.canScrollHorizontally(v, dx)) {
					return true;
				}
			} else {
				dx = -dx;
				if (!ViewCompat.canScrollHorizontally(v, dx)) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Deprecated
	public boolean canSlide() {
		return mCanSlide;
	}

	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		if (!(p instanceof LayoutParams) || !super.checkLayoutParams(p)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean closePane() {
		return closePane(mSlideableView, 0);
	}

	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			if (!mCanSlide) {
				mDragHelper.abort();
			} else {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}
	}

	void dispatchOnPanelClosed(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelClosed(panel);
		}
		sendAccessibilityEvent(DEFAULT_OVERHANG_SIZE);
	}

	void dispatchOnPanelOpened(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelOpened(panel);
		}
		sendAccessibilityEvent(DEFAULT_OVERHANG_SIZE);
	}

	void dispatchOnPanelSlide(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
		}
	}

	public void draw(Canvas c) {
		Drawable shadowDrawable;
		View shadowView;
		super.draw(c);
		if (isLayoutRtlSupport()) {
			shadowDrawable = mShadowDrawableRight;
		} else {
			shadowDrawable = mShadowDrawableLeft;
		}
		if (getChildCount() > 1) {
			shadowView = getChildAt(1);
		} else {
			shadowView = null;
		}
		if (shadowView == null || shadowDrawable == null) {
		} else {
			int left;
			int right;
			int top = shadowView.getTop();
			int bottom = shadowView.getBottom();
			int shadowWidth = shadowDrawable.getIntrinsicWidth();
			if (isLayoutRtlSupport()) {
				left = shadowView.getRight();
				right = left + shadowWidth;
			} else {
				right = shadowView.getLeft();
				left = right - shadowWidth;
			}
			shadowDrawable.setBounds(left, top, right, bottom);
			shadowDrawable.draw(c);
		}
	}

	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		LayoutParams lp = (LayoutParams) child.getLayoutParams();
		int save = canvas.save(CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		boolean result;
		Bitmap cache;
		if (!mCanSlide || lp.slideable || mSlideableView == null) {
			if (VERSION.SDK_INT < 11) {
				result = super.drawChild(canvas, child, drawingTime);
			} else if (!lp.dimWhenOffset || mSlideOffset <= 0.0f) {
				if (!child.isDrawingCacheEnabled()) {
					child.setDrawingCacheEnabled(false);
				}
				result = super.drawChild(canvas, child, drawingTime);
			} else {
				if (child.isDrawingCacheEnabled()) {
					child.setDrawingCacheEnabled(true);
				}
				cache = child.getDrawingCache();
				if (cache != null) {
					canvas.drawBitmap(cache, (float) child.getLeft(), (float) child.getTop(), lp.dimPaint);
					result = false;
				} else {
					Log.e(TAG, "drawChild: child view " + child + " returned null drawing cache");
					result = super.drawChild(canvas, child, drawingTime);
				}
			}
			canvas.restoreToCount(save);
			return result;
		} else {
			canvas.getClipBounds(mTmpRect);
			if (isLayoutRtlSupport()) {
				mTmpRect.left = Math.max(mTmpRect.left, mSlideableView.getRight());
			} else {
				mTmpRect.right = Math.min(mTmpRect.right, mSlideableView.getLeft());
			}
			canvas.clipRect(mTmpRect);
			if (VERSION.SDK_INT < 11) {
				if (!lp.dimWhenOffset || mSlideOffset <= 0.0f) {
					if (!child.isDrawingCacheEnabled()) {
						result = super.drawChild(canvas, child, drawingTime);
					} else {
						child.setDrawingCacheEnabled(false);
						result = super.drawChild(canvas, child, drawingTime);
					}
				} else if (child.isDrawingCacheEnabled()) {
					cache = child.getDrawingCache();
					if (cache != null) {
						Log.e(TAG, "drawChild: child view " + child + " returned null drawing cache");
						result = super.drawChild(canvas, child, drawingTime);
					} else {
						canvas.drawBitmap(cache, (float) child.getLeft(), (float) child.getTop(), lp.dimPaint);
						result = false;
					}
				} else {
					child.setDrawingCacheEnabled(true);
					cache = child.getDrawingCache();
					if (cache != null) {
						canvas.drawBitmap(cache, (float) child.getLeft(), (float) child.getTop(), lp.dimPaint);
						result = false;
					} else {
						Log.e(TAG, "drawChild: child view " + child + " returned null drawing cache");
						result = super.drawChild(canvas, child, drawingTime);
					}
				}
			} else {
				result = super.drawChild(canvas, child, drawingTime);
			}
			canvas.restoreToCount(save);
			return result;
		}
	}

	protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams();
	}

	public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
		if (p instanceof MarginLayoutParams) {
			return new LayoutParams((MarginLayoutParams) p);
		} else {
			return new LayoutParams(p);
		}
	}

	public int getCoveredFadeColor() {
		return mCoveredFadeColor;
	}

	public int getParallaxDistance() {
		return mParallaxBy;
	}

	public int getSliderFadeColor() {
		return mSliderFadeColor;
	}

	boolean isDimmed(View child) {
		if (child == null) {
			return false;
		} else {
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			if (mCanSlide) {
				if (lp.dimWhenOffset) {
					if (mSlideOffset > 0.0f) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public boolean isOpen() {
		if (!mCanSlide || mSlideOffset == 1.0f) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSlideable() {
		return mCanSlide;
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mFirstLayout = true;
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mFirstLayout = true;
		int i = 0;
		while (i < mPostedRunnables.size()) {
			((DisableLayerRunnable) mPostedRunnables.get(i)).run();
			i++;
		}
		mPostedRunnables.clear();
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		float x;
		float y;
		float adx;
		float ady;
		if (mCanSlide || action != 0 || getChildCount() <= 1) {
			if (!mCanSlide) {
				if (!mIsUnableToDrag || action == 0) {
					if (action == 3 || action == 1) {
						mDragHelper.cancel();
						return false;
					} else {
						interceptTap = false;
						switch(action) {
						case WearableExtender.SIZE_DEFAULT:
							mIsUnableToDrag = false;
							x = ev.getX();
							y = ev.getY();
							mInitialMotionX = x;
							mInitialMotionY = y;
							if (!mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)) {
								if (!isDimmed(mSlideableView)) {
									interceptTap = true;
								}
							}
							break;
						case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
							adx = Math.abs(ev.getX() - mInitialMotionX);
							ady = Math.abs(ev.getY() - mInitialMotionY);
							if (adx <= ((float) mDragHelper.getTouchSlop())) {
								if (ady <= adx) {
									mDragHelper.cancel();
									mIsUnableToDrag = true;
									return false;
								}
							}
						}
						if (mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap) {
							return true;
						} else {
							return false;
						}
					}
				}
			}
			mDragHelper.cancel();
			return super.onInterceptTouchEvent(ev);
		} else {
			View secondChild = getChildAt(1);
			if (secondChild != null) {
				boolean r9z;
				if (!mDragHelper.isViewUnder(secondChild, (int) ev.getX(), (int) ev.getY())) {
					r9z = true;
				} else {
					r9z = false;
				}
				mPreservedOpenState = r9z;
			}
			if (!mCanSlide) {
				mDragHelper.cancel();
				return super.onInterceptTouchEvent(ev);
			} else if (!mIsUnableToDrag || action == 0) {
				if (action == 3 || action == 1) {
					mDragHelper.cancel();
					return false;
				} else {
					interceptTap = false;
					switch(action) {
					case WearableExtender.SIZE_DEFAULT:
						mIsUnableToDrag = false;
						x = ev.getX();
						y = ev.getY();
						mInitialMotionX = x;
						mInitialMotionY = y;
						if (!mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)) {
						} else if (!isDimmed(mSlideableView)) {
						} else {
							interceptTap = true;
						}
						break;
					case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
						adx = Math.abs(ev.getX() - mInitialMotionX);
						ady = Math.abs(ev.getY() - mInitialMotionY);
						if (adx <= ((float) mDragHelper.getTouchSlop())) {
						} else if (ady <= adx) {
						} else {
							mDragHelper.cancel();
							mIsUnableToDrag = true;
							return false;
						}
					}
					if (mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				mDragHelper.cancel();
				return super.onInterceptTouchEvent(ev);
			}
		}
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingStart;
		int paddingEnd;
		boolean isLayoutRtl = isLayoutRtlSupport();
		if (isLayoutRtl) {
			mDragHelper.setEdgeTrackingEnabled(CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		} else {
			mDragHelper.setEdgeTrackingEnabled(1);
		}
		int width = r - l;
		if (isLayoutRtl) {
			paddingStart = getPaddingRight();
		} else {
			paddingStart = getPaddingLeft();
		}
		if (isLayoutRtl) {
			paddingEnd = getPaddingLeft();
		} else {
			paddingEnd = getPaddingRight();
		}
		int paddingTop = getPaddingTop();
		int childCount = getChildCount();
		int xStart = paddingStart;
		int nextXStart = xStart;
		if (mFirstLayout) {
			float r25f;
			if (!mCanSlide || !mPreservedOpenState) {
				r25f = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			} else {
				r25f = 1.0f;
			}
			mSlideOffset = r25f;
		}
		int i = 0;
		while (i < childCount) {
			View child = getChildAt(i);
			if (child.getVisibility() == 8) {
				i++;
			} else {
				int childRight;
				int childLeft;
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				int childWidth = child.getMeasuredWidth();
				int offset = 0;
				if (lp.slideable) {
					int lpMargin;
					boolean r25z;
					int range = (Math.min(nextXStart, (width - paddingEnd) - mOverhangSize) - xStart) - (lp.leftMargin + lp.rightMargin);
					mSlideRange = range;
					if (isLayoutRtl) {
						lpMargin = lp.rightMargin;
					} else {
						lpMargin = lp.leftMargin;
					}
					if (((xStart + lpMargin) + range) + (childWidth / 2) > width - paddingEnd) {
						r25z = true;
					} else {
						r25z = false;
					}
					lp.dimWhenOffset = r25z;
					int pos = (int) (((float) range) * mSlideOffset);
					xStart += pos + lpMargin;
					mSlideOffset = ((float) pos) / ((float) mSlideRange);
				} else if (!mCanSlide || mParallaxBy == 0) {
					xStart = nextXStart;
				} else {
					offset = (int) ((1.0f - mSlideOffset) * ((float) mParallaxBy));
					xStart = nextXStart;
				}
				if (isLayoutRtl) {
					childRight = (width - xStart) + offset;
					childLeft = childRight - childWidth;
				} else {
					childLeft = xStart - offset;
					childRight = childLeft + childWidth;
				}
				child.layout(childLeft, paddingTop, childRight, paddingTop + child.getMeasuredHeight());
				nextXStart += child.getWidth();
				i++;
			}
		}
		if (mFirstLayout) {
			if (mCanSlide) {
				if (mParallaxBy != 0) {
					parallaxOtherViews(mSlideOffset);
				}
				if (((LayoutParams) mSlideableView.getLayoutParams()).dimWhenOffset) {
					dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
				}
			} else {
				i = 0;
				while (i < childCount) {
					dimChildView(getChildAt(i), 0.0f, mSliderFadeColor);
					i++;
				}
			}
			updateObscuredViewsVisibility(mSlideableView);
		}
		mFirstLayout = false;
	}

	/* JADX WARNING: inconsistent code */
	/*
	protected void onMeasure(int r32_widthMeasureSpec, int r33_heightMeasureSpec) {
		r31_this = this;
		r25 = android.view.View.MeasureSpec.getMode(r32_widthMeasureSpec);
		r27 = android.view.View.MeasureSpec.getSize(r32_widthMeasureSpec);
		r12 = android.view.View.MeasureSpec.getMode(r33_heightMeasureSpec);
		r13 = android.view.View.MeasureSpec.getSize(r33_heightMeasureSpec);
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r25_widthMode;
		r1 = r29;
		if (r0 == r1) goto L_0x008e;
	L_0x0018:
		r29 = r31.isInEditMode();
		if (r29 == 0) goto L_0x0086;
	L_0x001e:
		r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r0 = r25_widthMode;
		r1 = r29;
		if (r0 != r1) goto L_0x007f;
	L_0x0026:
		r25_widthMode = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
	L_0x0028:
		r16_layoutHeight = 0;
		r18_maxLayoutHeight = -1;
		switch(r12_heightMode) {
			case -2147483648: goto L_0x00b5;
			case 1073741824: goto L_0x00a5;
			default: goto L_0x002f;
		}
	L_0x002f:
		r23_weightSum = 0;
		r4_canSlide = 0;
		r29 = r31.getPaddingLeft();
		r29 = r27_widthSize - r29;
		r30 = r31.getPaddingRight();
		r24 = r29 - r30;
		r26_widthRemaining = r24_widthAvailable;
		r6 = r31.getChildCount();
		r29 = 2;
		r0 = r29;
		if (r6_childCount <= r0) goto L_0x0051;
	L_0x004a:
		r29 = "SlidingPaneLayout";
		r30 = "onMeasure: More than two child views are not supported.";
		android.util.Log.e(r29, r30);
	L_0x0051:
		r29 = 0;
		r0 = r29;
		r1 = r31;
		r1.mSlideableView = r0;
		r15 = 0;
	L_0x005a:
		if (r15_i >= r6_childCount) goto L_0x019f;
	L_0x005c:
		r0 = r31;
		r5 = r0.getChildAt(r15_i);
		r17 = r5_child.getLayoutParams();
		r17 = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r17;
		r29 = r5_child.getVisibility();
		r30 = 8;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x00c3;
	L_0x0074:
		r29 = 0;
		r0 = r29;
		r1 = r17_lp;
		r1.dimWhenOffset = r0;
	L_0x007c:
		r15_i++;
		goto L_0x005a;
	L_0x007f:
		if (r25_widthMode != 0) goto L_0x0028;
	L_0x0081:
		r25_widthMode = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r27_widthSize = 300; // 0x12c float:4.2E-43 double:1.48E-321;
		goto L_0x0028;
	L_0x0086:
		r29 = new java.lang.IllegalStateException;
		r30 = "Width must have an exact value or MATCH_PARENT";
		r29.<init>(r30);
		throw r29;
	L_0x008e:
		if (r12_heightMode != 0) goto L_0x0028;
	L_0x0090:
		r29 = r31.isInEditMode();
		if (r29 == 0) goto L_0x009d;
	L_0x0096:
		if (r12_heightMode != 0) goto L_0x0028;
	L_0x0098:
		r12_heightMode = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r13_heightSize = 300; // 0x12c float:4.2E-43 double:1.48E-321;
		goto L_0x0028;
	L_0x009d:
		r29 = new java.lang.IllegalStateException;
		r30 = "Height must not be UNSPECIFIED";
		r29.<init>(r30);
		throw r29;
	L_0x00a5:
		r29 = r31.getPaddingTop();
		r29 = r13_heightSize - r29;
		r30 = r31.getPaddingBottom();
		r18_maxLayoutHeight = r29 - r30;
		r16_layoutHeight = r18_maxLayoutHeight;
		goto L_0x002f;
	L_0x00b5:
		r29 = r31.getPaddingTop();
		r29 = r13_heightSize - r29;
		r30 = r31.getPaddingBottom();
		r18_maxLayoutHeight = r29 - r30;
		goto L_0x002f;
	L_0x00c3:
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r30 = 0;
		r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
		if (r29 <= 0) goto L_0x00df;
	L_0x00cf:
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r23_weightSum += r29;
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		if (r29 == 0) goto L_0x007c;
	L_0x00df:
		r0 = r17_lp;
		r0 = r0.leftMargin;
		r29 = r0;
		r0 = r17_lp;
		r0 = r0.rightMargin;
		r30 = r0;
		r14 = r29 + r30;
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		r30 = -2;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x0152;
	L_0x00fb:
		r29 = r24_widthAvailable - r14_horizontalMargin;
		r30 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r10 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
	L_0x0103:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -2;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x0176;
	L_0x0111:
		r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
	L_0x011b:
		r5_child.measure(r10_childWidthSpec, r8_childHeightSpec);
		r9 = r5_child.getMeasuredWidth();
		r7 = r5_child.getMeasuredHeight();
		r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r0 = r29;
		if (r12_heightMode != r0) goto L_0x0136;
	L_0x012c:
		r0 = r16_layoutHeight;
		if (r7_childHeight <= r0) goto L_0x0136;
	L_0x0130:
		r0 = r18_maxLayoutHeight;
		r16_layoutHeight = java.lang.Math.min(r7_childHeight, r0);
	L_0x0136:
		r26_widthRemaining -= r9_childWidth;
		if (r26_widthRemaining >= 0) goto L_0x019c;
	L_0x013a:
		r29 = 1;
	L_0x013c:
		r0 = r29;
		r1 = r17_lp;
		r1.slideable = r0;
		r4_canSlide |= r29;
		r0 = r17_lp;
		r0 = r0.slideable;
		r29 = r0;
		if (r29 == 0) goto L_0x007c;
	L_0x014c:
		r0 = r31;
		r0.mSlideableView = r5_child;
		goto L_0x007c;
	L_0x0152:
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		r30 = -1;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x0169;
	L_0x0160:
		r29 = r24_widthAvailable - r14_horizontalMargin;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r10_childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x0103;
	L_0x0169:
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r10_childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x0103;
	L_0x0176:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -1;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x018f;
	L_0x0184:
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
		goto L_0x011b;
	L_0x018f:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x011b;
	L_0x019c:
		r29 = 0;
		goto L_0x013c;
	L_0x019f:
		if (r4_canSlide != 0) goto L_0x01a7;
	L_0x01a1:
		r29 = 0;
		r29 = (r23_weightSum > r29 ? 1 : (r23_weightSum == r29 ? 0 : -1));
		if (r29 <= 0) goto L_0x0323;
	L_0x01a7:
		r0 = r31;
		r0 = r0.mOverhangSize;
		r29 = r0;
		r11 = r24_widthAvailable - r29;
		r15_i = 0;
	L_0x01b0:
		if (r15_i >= r6_childCount) goto L_0x0323;
	L_0x01b2:
		r0 = r31;
		r5_child = r0.getChildAt(r15_i);
		r29 = r5_child.getVisibility();
		r30 = 8;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x01c7;
	L_0x01c4:
		r15_i++;
		goto L_0x01b0;
	L_0x01c7:
		r17_lp = r5_child.getLayoutParams();
		r17_lp = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r17_lp;
		r29 = r5_child.getVisibility();
		r30 = 8;
		r0 = r29;
		r1 = r30;
		if (r0 == r1) goto L_0x01c4;
	L_0x01d9:
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		if (r29 != 0) goto L_0x023d;
	L_0x01e1:
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r30 = 0;
		r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
		if (r29 <= 0) goto L_0x023d;
	L_0x01ed:
		r22 = 1;
	L_0x01ef:
		if (r22_skippedFirstPass == 0) goto L_0x0240;
	L_0x01f1:
		r20 = 0;
	L_0x01f3:
		if (r4_canSlide == 0) goto L_0x0276;
	L_0x01f5:
		r0 = r31;
		r0 = r0.mSlideableView;
		r29 = r0;
		r0 = r29;
		if (r5_child == r0) goto L_0x0276;
	L_0x01ff:
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		if (r29 >= 0) goto L_0x01c4;
	L_0x0207:
		r0 = r20_measuredWidth;
		if (r0 > r11_fixedPanelWidthLimit) goto L_0x0217;
	L_0x020b:
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r30 = 0;
		r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
		if (r29 <= 0) goto L_0x01c4;
	L_0x0217:
		if (r22_skippedFirstPass == 0) goto L_0x026b;
	L_0x0219:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -2;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x0245;
	L_0x0227:
		r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
	L_0x0231:
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r29;
		r10_childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(r11_fixedPanelWidthLimit, r0);
		r5_child.measure(r10_childWidthSpec, r8_childHeightSpec);
		goto L_0x01c4;
	L_0x023d:
		r22_skippedFirstPass = 0;
		goto L_0x01ef;
	L_0x0240:
		r20_measuredWidth = r5_child.getMeasuredWidth();
		goto L_0x01f3;
	L_0x0245:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -1;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x025e;
	L_0x0253:
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
		goto L_0x0231;
	L_0x025e:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x0231;
	L_0x026b:
		r29 = r5_child.getMeasuredHeight();
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x0231;
	L_0x0276:
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r30 = 0;
		r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
		if (r29 <= 0) goto L_0x01c4;
	L_0x0282:
		r0 = r17_lp;
		r0 = r0.width;
		r29 = r0;
		if (r29 != 0) goto L_0x02ef;
	L_0x028a:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -2;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x02c9;
	L_0x0298:
		r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
	L_0x02a2:
		if (r4_canSlide == 0) goto L_0x02fa;
	L_0x02a4:
		r0 = r17_lp;
		r0 = r0.leftMargin;
		r29 = r0;
		r0 = r17_lp;
		r0 = r0.rightMargin;
		r30 = r0;
		r14_horizontalMargin = r29 + r30;
		r21 = r24_widthAvailable - r14_horizontalMargin;
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r21_newWidth;
		r1 = r29;
		r10_childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
		r0 = r20_measuredWidth;
		r1 = r21_newWidth;
		if (r0 == r1) goto L_0x01c4;
	L_0x02c4:
		r5_child.measure(r10_childWidthSpec, r8_childHeightSpec);
		goto L_0x01c4;
	L_0x02c9:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = -1;
		r0 = r29;
		r1 = r30;
		if (r0 != r1) goto L_0x02e2;
	L_0x02d7:
		r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r18_maxLayoutHeight;
		r1 = r29;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
		goto L_0x02a2;
	L_0x02e2:
		r0 = r17_lp;
		r0 = r0.height;
		r29 = r0;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x02a2;
	L_0x02ef:
		r29 = r5_child.getMeasuredHeight();
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r8_childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		goto L_0x02a2;
	L_0x02fa:
		r29 = 0;
		r0 = r29;
		r1 = r26_widthRemaining;
		r28 = java.lang.Math.max(r0, r1);
		r0 = r17_lp;
		r0 = r0.weight;
		r29 = r0;
		r0 = r28_widthToDistribute;
		r0 = (float) r0;
		r30 = r0;
		r29 *= r30;
		r29 /= r23_weightSum;
		r0 = r29;
		r3 = (int) r0;
		r29 = r20_measuredWidth + r3_addedWidth;
		r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r10_childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
		r5_child.measure(r10_childWidthSpec, r8_childHeightSpec);
		goto L_0x01c4;
	L_0x0323:
		r20_measuredWidth = r27_widthSize;
		r29 = r31.getPaddingTop();
		r29 += r16_layoutHeight;
		r30 = r31.getPaddingBottom();
		r19 = r29 + r30;
		r0 = r31;
		r1 = r20_measuredWidth;
		r2 = r19_measuredHeight;
		r0.setMeasuredDimension(r1, r2);
		r0 = r31;
		r0.mCanSlide = r4_canSlide;
		r0 = r31;
		r0 = r0.mDragHelper;
		r29 = r0;
		r29 = r29.getViewDragState();
		if (r29 == 0) goto L_0x0355;
	L_0x034a:
		if (r4_canSlide != 0) goto L_0x0355;
	L_0x034c:
		r0 = r31;
		r0 = r0.mDragHelper;
		r29 = r0;
		r29.abort();
	L_0x0355:
		return;
	}
	*/
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View child;
		LayoutParams lp;
		int childWidthSpec;
		int childHeightSpec;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthMode != 1073741824) {
			if (isInEditMode()) {
				if (widthMode == -2147483648) {
				} else if (widthMode == 0) {
					widthSize = 300;
				}
			} else {
				throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
			}
		} else if (heightMode == 0) {
			if (isInEditMode()) {
				if (heightMode == 0) {
					heightMode = ExploreByTouchHelper.INVALID_ID;
					heightSize = 300;
				}
			} else {
				throw new IllegalStateException("Height must not be UNSPECIFIED");
			}
		}
		int layoutHeight = 0;
		int maxLayoutHeight = -1;
		switch(heightMode) {
		case ExploreByTouchHelper.INVALID_ID:
			maxLayoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
			break;
		case 1073741824:
			maxLayoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
			layoutHeight = maxLayoutHeight;
			break;
		}
		float weightSum = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		boolean canSlide = false;
		int widthAvailable = (widthSize - getPaddingLeft()) - getPaddingRight();
		int widthRemaining = widthAvailable;
		int childCount = getChildCount();
		if (childCount > 2) {
			Log.e(TAG, "onMeasure: More than two child views are not supported.");
		}
		mSlideableView = null;
		int i = 0;
		while (i < childCount) {
			child = getChildAt(i);
			lp = (LayoutParams) child.getLayoutParams();
			if (child.getVisibility() == 8) {
				lp.dimWhenOffset = false;
			} else {
				int horizontalMargin;
				if (lp.weight > 0.0f) {
					weightSum += lp.weight;
					if (lp.width != 0) {
						horizontalMargin = lp.leftMargin + lp.rightMargin;
					}
				}
				horizontalMargin = lp.leftMargin + lp.rightMargin;
				if (lp.width == -2) {
					childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, ExploreByTouchHelper.INVALID_ID);
				} else if (lp.width == -1) {
					childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, 1073741824);
				} else {
					childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, 1073741824);
				}
				if (lp.height == -2) {
					childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, -2147483648);
				} else if (lp.height == -1) {
					childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, 1073741824);
				} else {
					childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
				}
				child.measure(childWidthSpec, childHeightSpec);
				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				boolean r29z;
				if (heightMode != -2147483648 || childHeight <= layoutHeight) {
					widthRemaining -= childWidth;
					if (widthRemaining >= 0) {
						r29z = true;
					} else {
						r29z = false;
					}
					lp.slideable = r29z;
					canSlide |= r29z;
					if (!lp.slideable) {
						mSlideableView = child;
					}
				} else {
					layoutHeight = Math.min(childHeight, maxLayoutHeight);
					widthRemaining -= childWidth;
					if (widthRemaining >= 0) {
						r29z = false;
					} else {
						r29z = true;
					}
					lp.slideable = r29z;
					canSlide |= r29z;
					if (!lp.slideable) {
						i++;
					} else {
						mSlideableView = child;
					}
				}
			}
			i++;
		}
		if (canSlide || weightSum > 0.0f) {
			int fixedPanelWidthLimit = widthAvailable - mOverhangSize;
			i = 0;
			while (i < childCount) {
				child = getChildAt(i);
				if (child.getVisibility() == 8) {
					i++;
				} else {
					lp = (LayoutParams) child.getLayoutParams();
					if (child.getVisibility() != 8) {
						boolean skippedFirstPass;
						int measuredWidth;
						if (lp.width != 0 || lp.weight <= 0.0f) {
							skippedFirstPass = false;
						} else {
							skippedFirstPass = true;
						}
						if (skippedFirstPass) {
							measuredWidth = 0;
						} else {
							measuredWidth = child.getMeasuredWidth();
						}
						if (!canSlide || child == mSlideableView) {
							if (lp.weight > 0.0f) {
								if (lp.width == 0) {
									if (lp.height == -2) {
										childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, -2147483648);
									} else if (lp.height == -1) {
										childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, 1073741824);
									} else {
										childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
									}
								} else {
									childHeightSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), 1073741824);
								}
								if (canSlide) {
									int newWidth = widthAvailable - (lp.leftMargin + lp.rightMargin);
									childWidthSpec = MeasureSpec.makeMeasureSpec(newWidth, 1073741824);
									if (measuredWidth != newWidth) {
										child.measure(childWidthSpec, childHeightSpec);
									}
								} else {
									child.measure(MeasureSpec.makeMeasureSpec(measuredWidth + ((int) ((lp.weight * ((float) Math.max(0, widthRemaining))) / weightSum)), 1073741824), childHeightSpec);
								}
							}
						} else if (lp.width < 0) {
							if (measuredWidth > fixedPanelWidthLimit || lp.weight > 0.0f) {
								childHeightSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), 1073741824);
								child.measure(MeasureSpec.makeMeasureSpec(fixedPanelWidthLimit, 1073741824), childHeightSpec);
							}
						}
					}
					i++;
				}
			}
		} else {
			setMeasuredDimension(widthSize, (getPaddingTop() + layoutHeight) + getPaddingBottom());
			mCanSlide = canSlide;
		}
		setMeasuredDimension(widthSize, (getPaddingTop() + layoutHeight) + getPaddingBottom());
		mCanSlide = canSlide;
		if (mDragHelper.getViewDragState() == 0 || canSlide) {
		} else {
			mDragHelper.abort();
		}
	}

	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		if (ss.isOpen) {
			openPane();
		} else {
			closePane();
		}
		mPreservedOpenState = ss.isOpen;
	}

	protected Parcelable onSaveInstanceState() {
		boolean r2z;
		SavedState ss = new SavedState(super.onSaveInstanceState());
		if (isSlideable()) {
			r2z = isOpen();
		} else {
			r2z = mPreservedOpenState;
		}
		ss.isOpen = r2z;
		return ss;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw) {
			mFirstLayout = true;
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (!mCanSlide) {
			return super.onTouchEvent(ev);
		} else {
			mDragHelper.processTouchEvent(ev);
			switch((ev.getAction() & 255)) {
			case WearableExtender.SIZE_DEFAULT:
				mInitialMotionX = ev.getX();
				mInitialMotionY = ev.getY();
				return true;
			case CursorAdapter.FLAG_AUTO_REQUERY:
				if (isDimmed(mSlideableView)) {
					float x = ev.getX();
					float y = ev.getY();
					float dx = x - mInitialMotionX;
					float dy = y - mInitialMotionY;
					int slop = mDragHelper.getTouchSlop();
					if ((dx * dx) + (dy * dy) < ((float) (slop * slop))) {
						if (mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)) {
							closePane(mSlideableView, 0);
							return true;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
			return true;
		}
	}

	public boolean openPane() {
		return openPane(mSlideableView, 0);
	}

	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		if (isInTouchMode() || mCanSlide) {
		} else {
			boolean r0z;
			if (child == mSlideableView) {
				r0z = true;
			} else {
				r0z = false;
			}
			mPreservedOpenState = r0z;
		}
	}

	void setAllChildrenVisible() {
		int i = 0;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (child.getVisibility() == 4) {
				child.setVisibility(0);
			}
			i++;
		}
	}

	public void setCoveredFadeColor(int color) {
		mCoveredFadeColor = color;
	}

	public void setPanelSlideListener(PanelSlideListener listener) {
		mPanelSlideListener = listener;
	}

	public void setParallaxDistance(int parallaxBy) {
		mParallaxBy = parallaxBy;
		requestLayout();
	}

	@Deprecated
	public void setShadowDrawable(Drawable d) {
		setShadowDrawableLeft(d);
	}

	public void setShadowDrawableLeft(Drawable d) {
		mShadowDrawableLeft = d;
	}

	public void setShadowDrawableRight(Drawable d) {
		mShadowDrawableRight = d;
	}

	@Deprecated
	public void setShadowResource(int resId) {
		setShadowDrawable(getResources().getDrawable(resId));
	}

	public void setShadowResourceLeft(int resId) {
		setShadowDrawableLeft(getResources().getDrawable(resId));
	}

	public void setShadowResourceRight(int resId) {
		setShadowDrawableRight(getResources().getDrawable(resId));
	}

	public void setSliderFadeColor(int color) {
		mSliderFadeColor = color;
	}

	@Deprecated
	public void smoothSlideClosed() {
		closePane();
	}

	@Deprecated
	public void smoothSlideOpen() {
		openPane();
	}

	boolean smoothSlideTo(float slideOffset, int velocity) {
		if (!mCanSlide) {
			return false;
		} else {
			int x;
			LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();
			if (isLayoutRtlSupport()) {
				x = (int) (((float) getWidth()) - ((((float) (getPaddingRight() + lp.rightMargin)) + (((float) mSlideRange) * slideOffset)) + ((float) mSlideableView.getWidth())));
			} else {
				x = (int) (((float) (getPaddingLeft() + lp.leftMargin)) + (((float) mSlideRange) * slideOffset));
			}
			if (mDragHelper.smoothSlideViewTo(mSlideableView, x, mSlideableView.getTop())) {
				setAllChildrenVisible();
				ViewCompat.postInvalidateOnAnimation(this);
				return true;
			} else {
				return false;
			}
		}
	}

	void updateObscuredViewsVisibility(View panel) {
		int startBound;
		int endBound;
		int left;
		boolean isLayoutRtl = isLayoutRtlSupport();
		if (isLayoutRtl) {
			startBound = getWidth() - getPaddingRight();
		} else {
			startBound = getPaddingLeft();
		}
		if (isLayoutRtl) {
			endBound = getPaddingLeft();
		} else {
			endBound = getWidth() - getPaddingRight();
		}
		int topBound = getPaddingTop();
		int bottomBound = getHeight() - getPaddingBottom();
		if (panel == null || !viewIsOpaque(panel)) {
			bottom = 0;
			top = 0;
			right = 0;
			left = 0;
		} else {
			left = panel.getLeft();
			right = panel.getRight();
			top = panel.getTop();
			bottom = panel.getBottom();
		}
		int i = 0;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (child == panel) {
				return;
			} else {
				int r19i;
				int vis;
				if (isLayoutRtl) {
					r19i = endBound;
				} else {
					r19i = startBound;
				}
				int clampedChildLeft = Math.max(r19i, child.getLeft());
				int clampedChildTop = Math.max(topBound, child.getTop());
				if (isLayoutRtl) {
					r19i = startBound;
				} else {
					r19i = endBound;
				}
				int clampedChildRight = Math.min(r19i, child.getRight());
				int clampedChildBottom = Math.min(bottomBound, child.getBottom());
				if (clampedChildLeft < left || clampedChildTop < top || clampedChildRight > right || clampedChildBottom > bottom) {
					vis = 0;
				} else {
					vis = TransportMediator.FLAG_KEY_MEDIA_PLAY;
				}
				child.setVisibility(vis);
				i++;
			}
		}
	}
}
