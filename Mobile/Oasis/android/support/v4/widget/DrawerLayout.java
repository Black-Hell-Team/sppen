package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class DrawerLayout extends ViewGroup {
	private static final boolean ALLOW_EDGE_LOCK = false;
	private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
	private static final int DEFAULT_SCRIM_COLOR = -1728053248;
	private static final int[] LAYOUT_ATTRS;
	public static final int LOCK_MODE_LOCKED_CLOSED = 1;
	public static final int LOCK_MODE_LOCKED_OPEN = 2;
	public static final int LOCK_MODE_UNLOCKED = 0;
	private static final int MIN_DRAWER_MARGIN = 64;
	private static final int MIN_FLING_VELOCITY = 400;
	private static final int PEEK_DELAY = 160;
	public static final int STATE_DRAGGING = 1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_SETTLING = 2;
	private static final String TAG = "DrawerLayout";
	private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
	private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
	private boolean mChildrenCanceledTouch;
	private boolean mDisallowInterceptRequested;
	private int mDrawerState;
	private boolean mFirstLayout;
	private boolean mInLayout;
	private float mInitialMotionX;
	private float mInitialMotionY;
	private final ViewDragCallback mLeftCallback;
	private final ViewDragHelper mLeftDragger;
	private DrawerListener mListener;
	private int mLockModeLeft;
	private int mLockModeRight;
	private int mMinDrawerMargin;
	private final ViewDragCallback mRightCallback;
	private final ViewDragHelper mRightDragger;
	private int mScrimColor;
	private float mScrimOpacity;
	private Paint mScrimPaint;
	private Drawable mShadowLeft;
	private Drawable mShadowRight;
	private CharSequence mTitleLeft;
	private CharSequence mTitleRight;

	public static interface DrawerListener {
		public void onDrawerClosed(View r1_View);

		public void onDrawerOpened(View r1_View);

		public void onDrawerSlide(View r1_View, float r2f);

		public void onDrawerStateChanged(int r1i);
	}

	@IntDef({3, 5, 8388611, 8388613})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface EdgeGravity {
	}

	public static class LayoutParams extends MarginLayoutParams {
		public int gravity;
		boolean isPeeking;
		boolean knownOpen;
		float onScreen;

		public LayoutParams(int width, int height) {
			super(width, height);
			gravity = 0;
		}

		public LayoutParams(int width, int height, int gravity) {
			this(width, height);
			this.gravity = gravity;
		}

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			gravity = 0;
			TypedArray a = c.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
			gravity = a.getInt(STATE_IDLE, STATE_IDLE);
			a.recycle();
		}

		public LayoutParams(DrawerLayout.LayoutParams source) {
			super(source);
			gravity = 0;
			gravity = source.gravity;
		}

		public LayoutParams(android.view.ViewGroup.LayoutParams source) {
			super(source);
			gravity = 0;
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
			gravity = 0;
		}
	}

	@IntDef({0, 1, 2})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface LockMode {
	}

	protected static class SavedState extends BaseSavedState {
		public static final Creator<DrawerLayout.SavedState> CREATOR;
		int lockModeLeft;
		int lockModeRight;
		int openDrawerGravity;

		static class AnonymousClass_1 implements Creator<DrawerLayout.SavedState> {
			AnonymousClass_1() {
				super();
			}

			public DrawerLayout.SavedState createFromParcel(Parcel source) {
				return new DrawerLayout.SavedState(source);
			}

			public DrawerLayout.SavedState[] newArray(int size) {
				return new DrawerLayout.SavedState[size];
			}
		}


		static {
			CREATOR = new AnonymousClass_1();
		}

		public SavedState(Parcel in) {
			super(in);
			openDrawerGravity = 0;
			lockModeLeft = 0;
			lockModeRight = 0;
			openDrawerGravity = in.readInt();
		}

		public SavedState(Parcelable superState) {
			super(superState);
			openDrawerGravity = 0;
			lockModeLeft = 0;
			lockModeRight = 0;
		}

		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(openDrawerGravity);
		}
	}

	@IntDef({0, 1, 2})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface State {
	}

	class AccessibilityDelegate extends AccessibilityDelegateCompat {
		private final Rect mTmpRect;
		final /* synthetic */ DrawerLayout this$0;

		AccessibilityDelegate(DrawerLayout r2_DrawerLayout) {
			super();
			this$0 = r2_DrawerLayout;
			mTmpRect = new Rect();
		}

		private void addChildrenForAccessibility(AccessibilityNodeInfoCompat info, ViewGroup v) {
			int i = STATE_IDLE;
			while (i < v.getChildCount()) {
				View child = v.getChildAt(i);
				if (DrawerLayout.includeChildForAccessibilitiy(child)) {
					info.addChild(child);
				}
				i++;
			}
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
		}

		public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
			if (event.getEventType() == 32) {
				List<CharSequence> eventText = event.getText();
				View visibleDrawer = this$0.findVisibleDrawer();
				if (visibleDrawer != null) {
					CharSequence title = this$0.getDrawerTitle(this$0.getDrawerViewAbsoluteGravity(visibleDrawer));
					if (title != null) {
						eventText.add(title);
					}
				}
				return CHILDREN_DISALLOW_INTERCEPT;
			} else {
				return super.dispatchPopulateAccessibilityEvent(host, event);
			}
		}

		public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
			super.onInitializeAccessibilityEvent(host, event);
			event.setClassName(DrawerLayout.class.getName());
		}

		public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
			AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
			super.onInitializeAccessibilityNodeInfo(host, superNode);
			info.setClassName(DrawerLayout.class.getName());
			info.setSource(host);
			ViewParent parent = ViewCompat.getParentForAccessibility(host);
			if (parent instanceof View) {
				info.setParent((View) parent);
			}
			copyNodeInfoNoChildren(info, superNode);
			superNode.recycle();
			addChildrenForAccessibility(info, (ViewGroup) host);
		}

		public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
			if (DrawerLayout.includeChildForAccessibilitiy(child)) {
				return super.onRequestSendAccessibilityEvent(host, child, event);
			} else {
				return ALLOW_EDGE_LOCK;
			}
		}
	}

	final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
		final /* synthetic */ DrawerLayout this$0;

		ChildAccessibilityDelegate(DrawerLayout r1_DrawerLayout) {
			super();
			this$0 = r1_DrawerLayout;
		}

		public void onInitializeAccessibilityNodeInfo(View child, AccessibilityNodeInfoCompat info) {
			super.onInitializeAccessibilityNodeInfo(child, info);
			if (!DrawerLayout.includeChildForAccessibilitiy(child)) {
				info.setParent(null);
			}
		}
	}

	public static abstract class SimpleDrawerListener implements DrawerLayout.DrawerListener {
		public SimpleDrawerListener() {
			super();
		}

		public void onDrawerClosed(View drawerView) {
		}

		public void onDrawerOpened(View drawerView) {
		}

		public void onDrawerSlide(View drawerView, float slideOffset) {
		}

		public void onDrawerStateChanged(int newState) {
		}
	}

	private class ViewDragCallback extends Callback {
		private final int mAbsGravity;
		private ViewDragHelper mDragger;
		private final Runnable mPeekRunnable;
		final /* synthetic */ DrawerLayout this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ DrawerLayout.ViewDragCallback this$1;

			AnonymousClass_1(DrawerLayout.ViewDragCallback r1_DrawerLayout_ViewDragCallback) {
				super();
				this$1 = r1_DrawerLayout_ViewDragCallback;
			}

			public void run() {
				this$1.peekDrawer();
			}
		}


		public ViewDragCallback(DrawerLayout r2_DrawerLayout, int gravity) {
			super();
			this$0 = r2_DrawerLayout;
			mPeekRunnable = new AnonymousClass_1(this);
			mAbsGravity = gravity;
		}

		private void closeOtherDrawer() {
			int otherGrav = WearableExtender.SIZE_MEDIUM;
			if (mAbsGravity == 3) {
				otherGrav = WearableExtender.SIZE_FULL_SCREEN;
			}
			View toClose = this$0.findDrawerWithGravity(otherGrav);
			if (toClose != null) {
				this$0.closeDrawer(toClose);
			}
		}

		/* JADX WARNING: inconsistent code */
		/*
		private void peekDrawer() {
			r9_this = this;
			r8 = 3;
			r6 = 1;
			r5 = 0;
			r7 = r9.mDragger;
			r3 = r7.getEdgeSize();
			r7 = r9.mAbsGravity;
			if (r7 != r8) goto L_0x0058;
		L_0x000d:
			r1 = r6;
		L_0x000e:
			if (r1_leftEdge == 0) goto L_0x005a;
		L_0x0010:
			r7 = r9.this$0;
			r4 = r7.findDrawerWithGravity(r8);
			if (r4_toCapture == 0) goto L_0x001d;
		L_0x0018:
			r5 = r4_toCapture.getWidth();
			r5 = -r5;
		L_0x001d:
			r0 = r5 + r3_peekDistance;
		L_0x001f:
			if (r4_toCapture == 0) goto L_0x0057;
		L_0x0021:
			if (r1_leftEdge == 0) goto L_0x0029;
		L_0x0023:
			r5 = r4_toCapture.getLeft();
			if (r5 < r0_childLeft) goto L_0x0031;
		L_0x0029:
			if (r1_leftEdge != 0) goto L_0x0057;
		L_0x002b:
			r5 = r4_toCapture.getLeft();
			if (r5 <= r0_childLeft) goto L_0x0057;
		L_0x0031:
			r5 = r9.this$0;
			r5 = r5.getDrawerLockMode(r4_toCapture);
			if (r5 != 0) goto L_0x0057;
		L_0x0039:
			r2 = r4_toCapture.getLayoutParams();
			r2 = (android.support.v4.widget.DrawerLayout.LayoutParams) r2;
			r5 = r9.mDragger;
			r7 = r4_toCapture.getTop();
			r5.smoothSlideViewTo(r4_toCapture, r0_childLeft, r7);
			r2_lp.isPeeking = r6;
			r5 = r9.this$0;
			r5.invalidate();
			r9.closeOtherDrawer();
			r5 = r9.this$0;
			r5.cancelChildViewTouch();
		L_0x0057:
			return;
		L_0x0058:
			r1_leftEdge = r5;
			goto L_0x000e;
		L_0x005a:
			r5 = r9.this$0;
			r7 = 5;
			r4_toCapture = r5.findDrawerWithGravity(r7);
			r5 = r9.this$0;
			r5 = r5.getWidth();
			r0_childLeft = r5 - r3_peekDistance;
			goto L_0x001f;
		}
		*/
		private void peekDrawer() {
			boolean leftEdge;
			View toCapture;
			int childLeft;
			int r5i = STATE_IDLE;
			int peekDistance = mDragger.getEdgeSize();
			if (mAbsGravity == 3) {
				leftEdge = true;
			} else {
				leftEdge = false;
			}
			if (leftEdge) {
				toCapture = this$0.findDrawerWithGravity(WearableExtender.SIZE_MEDIUM);
				if (toCapture != null) {
					r5i = -toCapture.getWidth();
				}
				childLeft = r5i + peekDistance;
			} else {
				toCapture = this$0.findDrawerWithGravity(WearableExtender.SIZE_FULL_SCREEN);
				childLeft = this$0.getWidth() - peekDistance;
			}
			if (toCapture != null) {
				if (!leftEdge || toCapture.getLeft() >= childLeft) {
				}
				if (this$0.getDrawerLockMode(toCapture) == 0) {
					mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
					((DrawerLayout.LayoutParams) toCapture.getLayoutParams()).isPeeking = true;
					this$0.invalidate();
					closeOtherDrawer();
					this$0.cancelChildViewTouch();
				}
			}
		}

		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (this$0.checkDrawerViewAbsoluteGravity(child, WearableExtender.SIZE_MEDIUM)) {
				return Math.max(-child.getWidth(), Math.min(left, STATE_IDLE));
			} else {
				int width = this$0.getWidth();
				return Math.max(width - child.getWidth(), Math.min(left, width));
			}
		}

		public int clampViewPositionVertical(View child, int top, int dy) {
			return child.getTop();
		}

		public int getViewHorizontalDragRange(View child) {
			return child.getWidth();
		}

		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
			View toCapture;
			if ((edgeFlags & 1) == 1) {
				toCapture = this$0.findDrawerWithGravity(WearableExtender.SIZE_MEDIUM);
			} else {
				toCapture = this$0.findDrawerWithGravity(WearableExtender.SIZE_FULL_SCREEN);
			}
			if (toCapture == null || this$0.getDrawerLockMode(toCapture) != 0) {
			} else {
				mDragger.captureChildView(toCapture, pointerId);
			}
		}

		public boolean onEdgeLock(int edgeFlags) {
			return ALLOW_EDGE_LOCK;
		}

		public void onEdgeTouched(int edgeFlags, int pointerId) {
			this$0.postDelayed(mPeekRunnable, 160);
		}

		public void onViewCaptured(View capturedChild, int activePointerId) {
			((DrawerLayout.LayoutParams) capturedChild.getLayoutParams()).isPeeking = false;
			closeOtherDrawer();
		}

		public void onViewDragStateChanged(int state) {
			this$0.updateDrawerState(mAbsGravity, state, mDragger.getCapturedView());
		}

		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			float offset;
			int r3i;
			int childWidth = changedView.getWidth();
			if (this$0.checkDrawerViewAbsoluteGravity(changedView, WearableExtender.SIZE_MEDIUM)) {
				offset = ((float) (childWidth + left)) / ((float) childWidth);
			} else {
				offset = ((float) (this$0.getWidth() - left)) / ((float) childWidth);
			}
			this$0.setDrawerViewOffset(changedView, offset);
			if (offset == 0.0f) {
				r3i = TransportMediator.FLAG_KEY_MEDIA_PLAY;
			} else {
				r3i = STATE_IDLE;
			}
			changedView.setVisibility(r3i);
			this$0.invalidate();
		}

		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			int left;
			float offset = this$0.getDrawerViewOffset(releasedChild);
			int childWidth = releasedChild.getWidth();
			if (this$0.checkDrawerViewAbsoluteGravity(releasedChild, WearableExtender.SIZE_MEDIUM)) {
				if (xvel <= 0.0f) {
					if (xvel != 0.0f || offset <= 0.5f) {
						left = -childWidth;
					}
				}
				left = STATE_IDLE;
			} else {
				int width = this$0.getWidth();
				if (xvel >= 0.0f) {
					if (xvel != 0.0f || offset <= 0.5f) {
						left = width;
					} else {
						left = width - childWidth;
					}
				} else {
					left = width - childWidth;
				}
			}
			mDragger.settleCapturedViewAt(left, releasedChild.getTop());
			this$0.invalidate();
		}

		public void removeCallbacks() {
			this$0.removeCallbacks(mPeekRunnable);
		}

		public void setDragger(ViewDragHelper dragger) {
			mDragger = dragger;
		}

		public boolean tryCaptureView(View child, int pointerId) {
			if (!this$0.isDrawerView(child) || !this$0.checkDrawerViewAbsoluteGravity(child, mAbsGravity) || this$0.getDrawerLockMode(child) != 0) {
				return ALLOW_EDGE_LOCK;
			} else {
				return CHILDREN_DISALLOW_INTERCEPT;
			}
		}
	}


	static {
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16842931;
		LAYOUT_ATTRS = r0_int_A;
	}

	public DrawerLayout(Context context) {
		this(context, null);
	}

	public DrawerLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mChildAccessibilityDelegate = new ChildAccessibilityDelegate(this);
		mScrimColor = -1728053248;
		mScrimPaint = new Paint();
		mFirstLayout = true;
		float density = getResources().getDisplayMetrics().density;
		mMinDrawerMargin = (int) ((64.0f * density) + 0.5f);
		float minVel = 400.0f * density;
		mLeftCallback = new ViewDragCallback(this, 3);
		mRightCallback = new ViewDragCallback(this, 5);
		mLeftDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mLeftCallback);
		mLeftDragger.setEdgeTrackingEnabled(STATE_DRAGGING);
		mLeftDragger.setMinVelocity(minVel);
		mLeftCallback.setDragger(mLeftDragger);
		mRightDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mRightCallback);
		mRightDragger.setEdgeTrackingEnabled(STATE_SETTLING);
		mRightDragger.setMinVelocity(minVel);
		mRightCallback.setDragger(mRightDragger);
		setFocusableInTouchMode(CHILDREN_DISALLOW_INTERCEPT);
		ViewCompat.setImportantForAccessibility(this, STATE_DRAGGING);
		ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate(this));
		ViewGroupCompat.setMotionEventSplittingEnabled(this, ALLOW_EDGE_LOCK);
	}

	/* JADX WARNING: inconsistent code */
	/*
	private android.view.View findVisibleDrawer() {
		r4_this = this;
		r1 = r4.getChildCount();
		r2 = 0;
	L_0x0005:
		if (r2_i >= r1_childCount) goto L_0x001b;
	L_0x0007:
		r0 = r4.getChildAt(r2_i);
		r3 = r4.isDrawerView(r0_child);
		if (r3 == 0) goto L_0x0018;
	L_0x0011:
		r3 = r4.isDrawerVisible(r0_child);
		if (r3 == 0) goto L_0x0018;
	L_0x0017:
		return r0_child;
	L_0x0018:
		r2_i++;
		goto L_0x0005;
	L_0x001b:
		r0_child = 0;
		goto L_0x0017;
	}
	*/
	private View findVisibleDrawer() {
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (!isDrawerView(child) || !isDrawerVisible(child)) {
				i++;
			}
		}
		return null;
	}

	static String gravityToString(int gravity) {
		if ((gravity & 3) == 3) {
			return "LEFT";
		} else if ((gravity & 5) == 5) {
			return "RIGHT";
		} else {
			return Integer.toHexString(gravity);
		}
	}

	private static boolean hasOpaqueBackground(View v) {
		boolean r1z = ALLOW_EDGE_LOCK;
		Drawable bg = v.getBackground();
		if (bg == null || bg.getOpacity() != -1) {
			return r1z;
		} else {
			r1z = CHILDREN_DISALLOW_INTERCEPT;
			return r1z;
		}
	}

	private boolean hasPeekingDrawer() {
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			if (((LayoutParams) getChildAt(i).getLayoutParams()).isPeeking) {
				return CHILDREN_DISALLOW_INTERCEPT;
			} else {
				i++;
			}
		}
		return ALLOW_EDGE_LOCK;
	}

	private boolean hasVisibleDrawer() {
		if (findVisibleDrawer() != null) {
			return CHILDREN_DISALLOW_INTERCEPT;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	private static boolean includeChildForAccessibilitiy(View child) {
		if (ViewCompat.getImportantForAccessibility(child) == 4 || ViewCompat.getImportantForAccessibility(child) == 2) {
			return ALLOW_EDGE_LOCK;
		} else {
			return CHILDREN_DISALLOW_INTERCEPT;
		}
	}

	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		if (index <= 0) {
			if (index >= 0 || getChildCount() <= 0) {
				ViewCompat.setImportantForAccessibility(child, STATE_DRAGGING);
			} else {
				ViewCompat.setImportantForAccessibility(child, TransportMediator.FLAG_KEY_MEDIA_PLAY);
				ViewCompat.setAccessibilityDelegate(child, mChildAccessibilityDelegate);
			}
		} else {
			ViewCompat.setImportantForAccessibility(child, TransportMediator.FLAG_KEY_MEDIA_PLAY);
			ViewCompat.setAccessibilityDelegate(child, mChildAccessibilityDelegate);
		}
		super.addView(child, index, params);
	}

	void cancelChildViewTouch() {
		if (!mChildrenCanceledTouch) {
			long now = SystemClock.uptimeMillis();
			MotionEvent cancelEvent = MotionEvent.obtain(now, now, WearableExtender.SIZE_MEDIUM, AutoScrollHelper.RELATIVE_UNSPECIFIED, 0.0f, STATE_IDLE);
			int i = STATE_IDLE;
			while (i < getChildCount()) {
				getChildAt(i).dispatchTouchEvent(cancelEvent);
				i++;
			}
			cancelEvent.recycle();
			mChildrenCanceledTouch = true;
		}
	}

	boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
		if ((getDrawerViewAbsoluteGravity(drawerView) & checkFor) == checkFor) {
			return CHILDREN_DISALLOW_INTERCEPT;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		if (!(p instanceof LayoutParams) || !super.checkLayoutParams(p)) {
			return ALLOW_EDGE_LOCK;
		} else {
			return CHILDREN_DISALLOW_INTERCEPT;
		}
	}

	public void closeDrawer(int gravity) {
		View drawerView = findDrawerWithGravity(gravity);
		if (drawerView == null) {
			throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
		} else {
			closeDrawer(drawerView);
		}
	}

	public void closeDrawer(View drawerView) {
		if (!isDrawerView(drawerView)) {
			throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
		} else {
			if (mFirstLayout) {
				LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
				lp.onScreen = 0.0f;
				lp.knownOpen = false;
			} else if (checkDrawerViewAbsoluteGravity(drawerView, WearableExtender.SIZE_MEDIUM)) {
				mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
			} else {
				mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
			}
			invalidate();
		}
	}

	public void closeDrawers() {
		closeDrawers(ALLOW_EDGE_LOCK);
	}

	void closeDrawers(boolean peekingOnly) {
		boolean needsInvalidate = ALLOW_EDGE_LOCK;
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			if (isDrawerView(child)) {
				if (!peekingOnly || lp.isPeeking) {
					int childWidth = child.getWidth();
					if (checkDrawerViewAbsoluteGravity(child, WearableExtender.SIZE_MEDIUM)) {
						needsInvalidate |= mLeftDragger.smoothSlideViewTo(child, -childWidth, child.getTop());
					} else {
						needsInvalidate |= mRightDragger.smoothSlideViewTo(child, getWidth(), child.getTop());
					}
					lp.isPeeking = false;
				}
			}
			i++;
		}
		mLeftCallback.removeCallbacks();
		mRightCallback.removeCallbacks();
		if (needsInvalidate) {
			invalidate();
		}
	}

	public void computeScroll() {
		float scrimOpacity = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			scrimOpacity = Math.max(scrimOpacity, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
			i++;
		}
		mScrimOpacity = scrimOpacity;
		if ((mLeftDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT) | mRightDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT)) != 0) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	void dispatchOnDrawerClosed(View drawerView) {
		LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
		if (lp.knownOpen) {
			lp.knownOpen = false;
			if (mListener != null) {
				mListener.onDrawerClosed(drawerView);
			}
			View content = getChildAt(STATE_IDLE);
			if (content != null) {
				ViewCompat.setImportantForAccessibility(content, STATE_DRAGGING);
			}
			ViewCompat.setImportantForAccessibility(drawerView, TransportMediator.FLAG_KEY_MEDIA_PLAY);
			if (hasWindowFocus()) {
				View rootView = getRootView();
				if (rootView != null) {
					rootView.sendAccessibilityEvent(TransportMediator.FLAG_KEY_MEDIA_STOP);
				}
			}
		}
	}

	void dispatchOnDrawerOpened(View drawerView) {
		LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
		if (!lp.knownOpen) {
			lp.knownOpen = true;
			if (mListener != null) {
				mListener.onDrawerOpened(drawerView);
			}
			View content = getChildAt(STATE_IDLE);
			if (content != null) {
				ViewCompat.setImportantForAccessibility(content, TransportMediator.FLAG_KEY_MEDIA_PLAY);
			}
			ViewCompat.setImportantForAccessibility(drawerView, STATE_DRAGGING);
			sendAccessibilityEvent(TransportMediator.FLAG_KEY_MEDIA_STOP);
			drawerView.requestFocus();
		}
	}

	void dispatchOnDrawerSlide(View drawerView, float slideOffset) {
		if (mListener != null) {
			mListener.onDrawerSlide(drawerView, slideOffset);
		}
	}

	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		int height = getHeight();
		boolean drawingContent = isContentView(child);
		int clipLeft = STATE_IDLE;
		int clipRight = getWidth();
		int restoreCount = canvas.save();
		if (drawingContent) {
			int childCount = getChildCount();
			int i = STATE_IDLE;
			while (i < childCount) {
				View v = getChildAt(i);
				if (v == child || v.getVisibility() != 0 || !hasOpaqueBackground(v) || !isDrawerView(v) || v.getHeight() < height) {
					i++;
				} else if (checkDrawerViewAbsoluteGravity(v, WearableExtender.SIZE_MEDIUM)) {
					int vright = v.getRight();
					if (vright > clipLeft) {
						clipLeft = vright;
					}
					i++;
				} else {
					int vleft = v.getLeft();
					if (vleft < clipRight) {
						clipRight = vleft;
					}
					i++;
				}
			}
			canvas.clipRect(clipLeft, STATE_IDLE, clipRight, getHeight());
		}
		boolean result = super.drawChild(canvas, child, drawingTime);
		canvas.restoreToCount(restoreCount);
		if (mScrimOpacity <= 0.0f || !drawingContent) {
			if (mShadowLeft == null || !checkDrawerViewAbsoluteGravity(child, WearableExtender.SIZE_MEDIUM)) {
				if (mShadowRight != null) {
					if (checkDrawerViewAbsoluteGravity(child, WearableExtender.SIZE_FULL_SCREEN)) {
						int childLeft = child.getLeft();
						mShadowRight.setBounds(childLeft - mShadowRight.getIntrinsicWidth(), child.getTop(), childLeft, child.getBottom());
						mShadowRight.setAlpha((int) (255.0f * Math.max(AutoScrollHelper.RELATIVE_UNSPECIFIED, Math.min(((float) (getWidth() - childLeft)) / ((float) mRightDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY))));
						mShadowRight.draw(canvas);
						return result;
					} else {
						return result;
					}
				} else {
					return result;
				}
			} else {
				int childRight = child.getRight();
				mShadowLeft.setBounds(childRight, child.getTop(), childRight + mShadowLeft.getIntrinsicWidth(), child.getBottom());
				mShadowLeft.setAlpha((int) (255.0f * Math.max(AutoScrollHelper.RELATIVE_UNSPECIFIED, Math.min(((float) childRight) / ((float) mLeftDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY))));
				mShadowLeft.draw(canvas);
				return result;
			}
		} else {
			mScrimPaint.setColor((((int) (((float) ((mScrimColor & -16777216) >>> 24)) * mScrimOpacity)) << 24) | (mScrimColor & 16777215));
			canvas.drawRect((float) clipLeft, AutoScrollHelper.RELATIVE_UNSPECIFIED, (float) clipRight, (float) getHeight(), mScrimPaint);
			return result;
		}
	}

	View findDrawerWithGravity(int gravity) {
		int absHorizGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if ((getDrawerViewAbsoluteGravity(child) & 7) == absHorizGravity) {
				return child;
			} else {
				i++;
			}
		}
		return null;
	}

	View findOpenDrawer() {
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (((LayoutParams) child.getLayoutParams()).knownOpen) {
				return child;
			} else {
				i++;
			}
		}
		return null;
	}

	protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(-1, -1);
	}

	public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
		if (p instanceof LayoutParams) {
			return new LayoutParams((LayoutParams) p);
		} else if (p instanceof MarginLayoutParams) {
			return new LayoutParams((MarginLayoutParams) p);
		} else {
			return new LayoutParams(p);
		}
	}

	public int getDrawerLockMode(int edgeGravity) {
		int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
		if (absGravity == WearableExtender.SIZE_MEDIUM) {
			return mLockModeLeft;
		} else if (absGravity == 5) {
			return mLockModeRight;
		} else {
			return STATE_IDLE;
		}
	}

	public int getDrawerLockMode(View drawerView) {
		int absGravity = getDrawerViewAbsoluteGravity(drawerView);
		if (absGravity == 3) {
			return mLockModeLeft;
		} else if (absGravity == 5) {
			return mLockModeRight;
		} else {
			return STATE_IDLE;
		}
	}

	@Nullable
	public CharSequence getDrawerTitle(int edgeGravity) {
		int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
		if (absGravity == WearableExtender.SIZE_MEDIUM) {
			return mTitleLeft;
		} else if (absGravity == 5) {
			return mTitleRight;
		} else {
			return null;
		}
	}

	int getDrawerViewAbsoluteGravity(View drawerView) {
		return GravityCompat.getAbsoluteGravity(((LayoutParams) drawerView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
	}

	float getDrawerViewOffset(View drawerView) {
		return ((LayoutParams) drawerView.getLayoutParams()).onScreen;
	}

	boolean isContentView(View child) {
		if (((LayoutParams) child.getLayoutParams()).gravity == 0) {
			return CHILDREN_DISALLOW_INTERCEPT;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	public boolean isDrawerOpen(int drawerGravity) {
		View drawerView = findDrawerWithGravity(drawerGravity);
		if (drawerView != null) {
			return isDrawerOpen(drawerView);
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	public boolean isDrawerOpen(View drawer) {
		if (!isDrawerView(drawer)) {
			throw new IllegalArgumentException("View " + drawer + " is not a drawer");
		} else {
			return ((LayoutParams) drawer.getLayoutParams()).knownOpen;
		}
	}

	boolean isDrawerView(View child) {
		if ((GravityCompat.getAbsoluteGravity(((LayoutParams) child.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(child)) & 7) != 0) {
			return CHILDREN_DISALLOW_INTERCEPT;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	public boolean isDrawerVisible(int drawerGravity) {
		View drawerView = findDrawerWithGravity(drawerGravity);
		if (drawerView != null) {
			return isDrawerVisible(drawerView);
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	public boolean isDrawerVisible(View drawer) {
		if (!isDrawerView(drawer)) {
			throw new IllegalArgumentException("View " + drawer + " is not a drawer");
		} else if (((LayoutParams) drawer.getLayoutParams()).onScreen > 0.0f) {
			return CHILDREN_DISALLOW_INTERCEPT;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	void moveDrawerToOffset(View drawerView, float slideOffset) {
		int width = drawerView.getWidth();
		int dx = ((int) (((float) width) * slideOffset)) - ((int) (((float) width) * getDrawerViewOffset(drawerView)));
		if (checkDrawerViewAbsoluteGravity(drawerView, WearableExtender.SIZE_MEDIUM)) {
			drawerView.offsetLeftAndRight(dx);
			setDrawerViewOffset(drawerView, slideOffset);
		} else {
			dx = -dx;
			drawerView.offsetLeftAndRight(dx);
			setDrawerViewOffset(drawerView, slideOffset);
		}
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mFirstLayout = true;
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mFirstLayout = true;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean interceptForDrag = mLeftDragger.shouldInterceptTouchEvent(ev) | mRightDragger.shouldInterceptTouchEvent(ev);
		boolean interceptForTap = ALLOW_EDGE_LOCK;
		switch(MotionEventCompat.getActionMasked(ev)) {
		case STATE_IDLE:
			float x = ev.getX();
			float y = ev.getY();
			mInitialMotionX = x;
			mInitialMotionY = y;
			if (mScrimOpacity <= 0.0f || !isContentView(mLeftDragger.findTopChildUnder((int) x, (int) y))) {
				mDisallowInterceptRequested = false;
				mChildrenCanceledTouch = false;
			} else {
				interceptForTap = CHILDREN_DISALLOW_INTERCEPT;
				mDisallowInterceptRequested = false;
				mChildrenCanceledTouch = false;
			}
			break;
		case STATE_DRAGGING:
		case WearableExtender.SIZE_MEDIUM:
			closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
			mDisallowInterceptRequested = false;
			mChildrenCanceledTouch = false;
			break;
		case STATE_SETTLING:
			if (mLeftDragger.checkTouchSlop(WearableExtender.SIZE_MEDIUM)) {
				mLeftCallback.removeCallbacks();
				mRightCallback.removeCallbacks();
			}
			break;
		}
		if (interceptForDrag || interceptForTap || hasPeekingDrawer() || mChildrenCanceledTouch) {
			return true;
		} else {
			return ALLOW_EDGE_LOCK;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != 4 || !hasVisibleDrawer()) {
			return super.onKeyDown(keyCode, event);
		} else {
			KeyEventCompat.startTracking(event);
			return CHILDREN_DISALLOW_INTERCEPT;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean onKeyUp(int r3_keyCode, android.view.KeyEvent r4_event) {
		r2_this = this;
		r1 = 4;
		if (r3_keyCode != r1) goto L_0x0018;
	L_0x0003:
		r0 = r2.findVisibleDrawer();
		if (r0_visibleDrawer == 0) goto L_0x0012;
	L_0x0009:
		r1 = r2.getDrawerLockMode(r0_visibleDrawer);
		if (r1 != 0) goto L_0x0012;
	L_0x000f:
		r2.closeDrawers();
	L_0x0012:
		if (r0_visibleDrawer == 0) goto L_0x0016;
	L_0x0014:
		r1 = 1;
	L_0x0015:
		return r1;
	L_0x0016:
		r1 = 0;
		goto L_0x0015;
	L_0x0018:
		r1 = super.onKeyUp(r3_keyCode, r4_event);
		goto L_0x0015;
	}
	*/
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			View visibleDrawer = findVisibleDrawer();
			if (visibleDrawer == null || getDrawerLockMode(visibleDrawer) != 0) {
				return ALLOW_EDGE_LOCK;
			} else {
				closeDrawers();
				return ALLOW_EDGE_LOCK;
			}
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mInLayout = true;
		int width = r - l;
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (child.getVisibility() == 8) {
				i++;
			} else {
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (isContentView(child)) {
					child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(), lp.topMargin + child.getMeasuredHeight());
					i++;
				} else {
					int childLeft;
					float newOffset;
					boolean changeOffset;
					int newVisibility;
					int childWidth = child.getMeasuredWidth();
					int childHeight = child.getMeasuredHeight();
					if (checkDrawerViewAbsoluteGravity(child, 3)) {
						childLeft = (-childWidth) + ((int) (((float) childWidth) * lp.onScreen));
						newOffset = ((float) (childWidth + childLeft)) / ((float) childWidth);
					} else {
						childLeft = width - ((int) (((float) childWidth) * lp.onScreen));
						newOffset = ((float) (width - childLeft)) / ((float) childWidth);
					}
					if (newOffset != lp.onScreen) {
						changeOffset = CHILDREN_DISALLOW_INTERCEPT;
					} else {
						changeOffset = ALLOW_EDGE_LOCK;
					}
					int height;
					switch((lp.gravity & 112)) {
					case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
						height = b - t;
						int childTop = (height - childHeight) / 2;
						if (childTop < lp.topMargin) {
							childTop = lp.topMargin;
						} else if (childTop + childHeight > height - lp.bottomMargin) {
							childTop = (height - lp.bottomMargin) - childHeight;
						}
						child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
						if (changeOffset) {
							setDrawerViewOffset(child, newOffset);
						}
						if (lp.onScreen <= 0.0f) {
							newVisibility = STATE_IDLE;
						} else {
							newVisibility = TransportMediator.FLAG_KEY_MEDIA_PLAY;
						}
						if (child.getVisibility() == newVisibility) {
							child.setVisibility(newVisibility);
						}
						i++;
						break;
					case 80:
						height = b - t;
						child.layout(childLeft, (height - lp.bottomMargin) - child.getMeasuredHeight(), childLeft + childWidth, height - lp.bottomMargin);
						if (changeOffset) {
							if (lp.onScreen <= 0.0f) {
								newVisibility = TransportMediator.FLAG_KEY_MEDIA_PLAY;
							} else {
								newVisibility = STATE_IDLE;
							}
							if (child.getVisibility() == newVisibility) {
								i++;
							} else {
								child.setVisibility(newVisibility);
								i++;
							}
						} else {
							setDrawerViewOffset(child, newOffset);
							if (lp.onScreen <= 0.0f) {
								newVisibility = STATE_IDLE;
							} else {
								newVisibility = TransportMediator.FLAG_KEY_MEDIA_PLAY;
							}
							if (child.getVisibility() == newVisibility) {
								child.setVisibility(newVisibility);
							}
							i++;
						}
						break;
					}
					child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
					if (changeOffset) {
						setDrawerViewOffset(child, newOffset);
					}
					if (lp.onScreen <= 0.0f) {
						newVisibility = TransportMediator.FLAG_KEY_MEDIA_PLAY;
					} else {
						newVisibility = STATE_IDLE;
					}
					if (child.getVisibility() == newVisibility) {
						i++;
					} else {
						child.setVisibility(newVisibility);
						i++;
					}
				}
			}
		}
		mInLayout = false;
		mFirstLayout = false;
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int i;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthMode != 1073741824 || heightMode != 1073741824) {
			if (isInEditMode()) {
				if (widthMode == -2147483648) {
				} else if (widthMode == 0) {
					widthSize = 300;
				}
				if (heightMode == -2147483648) {
				} else if (heightMode == 0) {
					heightSize = 300;
				}
			} else {
				throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
			}
		} else {
			setMeasuredDimension(widthSize, heightSize);
			i = STATE_IDLE;
		}
		setMeasuredDimension(widthSize, heightSize);
		i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (child.getVisibility() == 8) {
				i++;
			} else {
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (isContentView(child)) {
					child.measure(MeasureSpec.makeMeasureSpec((widthSize - lp.leftMargin) - lp.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec((heightSize - lp.topMargin) - lp.bottomMargin, 1073741824));
					i++;
				} else if (isDrawerView(child)) {
					int childGravity = getDrawerViewAbsoluteGravity(child) & 7;
					if ((0 & childGravity) != 0) {
						throw new IllegalStateException("Child drawer has absolute gravity " + gravityToString(childGravity) + " but this " + TAG + " already has a " + "drawer view along that edge");
					} else {
						child.measure(getChildMeasureSpec(widthMeasureSpec, (mMinDrawerMargin + lp.leftMargin) + lp.rightMargin, lp.width), getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height));
						i++;
					}
				} else {
					throw new IllegalStateException("Child " + child + " at index " + i + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
				}
			}
		}
	}

	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		if (ss.openDrawerGravity != 0) {
			View toOpen = findDrawerWithGravity(ss.openDrawerGravity);
			if (toOpen != null) {
				openDrawer(toOpen);
			}
		}
		setDrawerLockMode(ss.lockModeLeft, (int)WearableExtender.SIZE_MEDIUM);
		setDrawerLockMode(ss.lockModeRight, (int)WearableExtender.SIZE_FULL_SCREEN);
	}

	protected Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		int i = STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (!isDrawerView(child)) {
				i++;
			} else {
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp.knownOpen) {
					ss.openDrawerGravity = lp.gravity;
				}
			}
		}
		ss.lockModeLeft = mLockModeLeft;
		ss.lockModeRight = mLockModeRight;
		return ss;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		mLeftDragger.processTouchEvent(ev);
		mRightDragger.processTouchEvent(ev);
		switch((ev.getAction() & 255)) {
		case STATE_IDLE:
			mInitialMotionX = ev.getX();
			mInitialMotionY = ev.getY();
			mDisallowInterceptRequested = false;
			mChildrenCanceledTouch = false;
			return CHILDREN_DISALLOW_INTERCEPT;
		case STATE_DRAGGING:
			float x = ev.getX();
			float y = ev.getY();
			boolean peekingOnly = CHILDREN_DISALLOW_INTERCEPT;
			View touchedView = mLeftDragger.findTopChildUnder((int) x, (int) y);
			if (touchedView == null || !isContentView(touchedView)) {
				closeDrawers(peekingOnly);
				mDisallowInterceptRequested = false;
				return CHILDREN_DISALLOW_INTERCEPT;
			} else {
				float dx = x - mInitialMotionX;
				float dy = y - mInitialMotionY;
				int slop = mLeftDragger.getTouchSlop();
				if ((dx * dx) + (dy * dy) < ((float) (slop * slop))) {
					View openDrawer = findOpenDrawer();
					if (openDrawer != null) {
						if (getDrawerLockMode(openDrawer) == 2) {
							peekingOnly = CHILDREN_DISALLOW_INTERCEPT;
						} else {
							peekingOnly = ALLOW_EDGE_LOCK;
						}
					}
				}
				closeDrawers(peekingOnly);
				mDisallowInterceptRequested = false;
				return CHILDREN_DISALLOW_INTERCEPT;
			}
		case WearableExtender.SIZE_MEDIUM:
			closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
			mDisallowInterceptRequested = false;
			mChildrenCanceledTouch = false;
			return CHILDREN_DISALLOW_INTERCEPT;
		}
		return CHILDREN_DISALLOW_INTERCEPT;
	}

	public void openDrawer(int gravity) {
		View drawerView = findDrawerWithGravity(gravity);
		if (drawerView == null) {
			throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
		} else {
			openDrawer(drawerView);
		}
	}

	public void openDrawer(View drawerView) {
		if (!isDrawerView(drawerView)) {
			throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
		} else {
			if (mFirstLayout) {
				LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
				lp.onScreen = 1.0f;
				lp.knownOpen = true;
			} else if (checkDrawerViewAbsoluteGravity(drawerView, WearableExtender.SIZE_MEDIUM)) {
				mLeftDragger.smoothSlideViewTo(drawerView, STATE_IDLE, drawerView.getTop());
			} else {
				mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
			}
			invalidate();
		}
	}

	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
		mDisallowInterceptRequested = disallowIntercept;
		if (disallowIntercept) {
			closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
		}
	}

	public void requestLayout() {
		if (!mInLayout) {
			super.requestLayout();
		}
	}

	public void setDrawerListener(DrawerListener listener) {
		mListener = listener;
	}

	public void setDrawerLockMode(int lockMode) {
		setDrawerLockMode(lockMode, (int)WearableExtender.SIZE_MEDIUM);
		setDrawerLockMode(lockMode, (int)WearableExtender.SIZE_FULL_SCREEN);
	}

	public void setDrawerLockMode(int lockMode, int edgeGravity) {
		int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
		if (absGravity == 3) {
			mLockModeLeft = lockMode;
		} else if (absGravity == 5) {
			mLockModeRight = lockMode;
		}
		if (lockMode != 0) {
			ViewDragHelper helper;
			if (absGravity == 3) {
				helper = mLeftDragger;
			} else {
				helper = mRightDragger;
			}
			helper.cancel();
		}
		switch(lockMode) {
		case STATE_DRAGGING:
			View toClose = findDrawerWithGravity(absGravity);
			if (toClose != null) {
				closeDrawer(toClose);
			}
		case STATE_SETTLING:
			View toOpen = findDrawerWithGravity(absGravity);
			if (toOpen != null) {
				openDrawer(toOpen);
			}
		}
	}

	public void setDrawerLockMode(int lockMode, View drawerView) {
		if (!isDrawerView(drawerView)) {
			throw new IllegalArgumentException("View " + drawerView + " is not a " + "drawer with appropriate layout_gravity");
		} else {
			setDrawerLockMode(lockMode, ((LayoutParams) drawerView.getLayoutParams()).gravity);
		}
	}

	public void setDrawerShadow(int resId, int gravity) {
		setDrawerShadow(getResources().getDrawable(resId), gravity);
	}

	public void setDrawerShadow(Drawable shadowDrawable, int gravity) {
		int absGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
		if ((absGravity & 3) == 3) {
			mShadowLeft = shadowDrawable;
			invalidate();
		}
		if ((absGravity & 5) == 5) {
			mShadowRight = shadowDrawable;
			invalidate();
		}
	}

	public void setDrawerTitle(int edgeGravity, CharSequence title) {
		int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
		if (absGravity == WearableExtender.SIZE_MEDIUM) {
			mTitleLeft = title;
		} else if (absGravity == 5) {
			mTitleRight = title;
		}
	}

	void setDrawerViewOffset(View drawerView, float slideOffset) {
		LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
		if (slideOffset == lp.onScreen) {
		} else {
			lp.onScreen = slideOffset;
			dispatchOnDrawerSlide(drawerView, slideOffset);
		}
	}

	public void setScrimColor(int color) {
		mScrimColor = color;
		invalidate();
	}

	void updateDrawerState(int forGravity, int activeState, View activeDrawer) {
		int state;
		int leftState = mLeftDragger.getViewDragState();
		int rightState = mRightDragger.getViewDragState();
		if (leftState == 1 || rightState == 1) {
			state = STATE_DRAGGING;
		} else if (leftState == 2 || rightState == 2) {
			state = STATE_SETTLING;
		} else {
			state = STATE_IDLE;
		}
		if (activeDrawer == null || activeState != 0) {
			if (state != mDrawerState) {
				mDrawerState = state;
				if (mListener != null) {
					mListener.onDrawerStateChanged(state);
				}
			}
		} else {
			LayoutParams lp = (LayoutParams) activeDrawer.getLayoutParams();
			if (lp.onScreen == 0.0f) {
				dispatchOnDrawerClosed(activeDrawer);
				if (state != mDrawerState) {
				} else {
					mDrawerState = state;
					if (mListener != null) {
					} else {
						mListener.onDrawerStateChanged(state);
					}
				}
			} else {
				if (lp.onScreen == 1.0f) {
					dispatchOnDrawerOpened(activeDrawer);
				}
				if (state != mDrawerState) {
					mDrawerState = state;
					if (mListener != null) {
						mListener.onDrawerStateChanged(state);
					}
				}
			}
		}
	}
}
