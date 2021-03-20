package android.support.v4.widget;

import android.content.Context;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.elite.DeviceManager;
import java.util.Arrays;

public class ViewDragHelper {
	private static final int BASE_SETTLE_DURATION = 256;
	public static final int DIRECTION_ALL = 3;
	public static final int DIRECTION_HORIZONTAL = 1;
	public static final int DIRECTION_VERTICAL = 2;
	public static final int EDGE_ALL = 15;
	public static final int EDGE_BOTTOM = 8;
	public static final int EDGE_LEFT = 1;
	public static final int EDGE_RIGHT = 2;
	private static final int EDGE_SIZE = 20;
	public static final int EDGE_TOP = 4;
	public static final int INVALID_POINTER = -1;
	private static final int MAX_SETTLE_DURATION = 600;
	public static final int STATE_DRAGGING = 1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_SETTLING = 2;
	private static final String TAG = "ViewDragHelper";
	private static final Interpolator sInterpolator;
	private int mActivePointerId;
	private final Callback mCallback;
	private View mCapturedView;
	private int mDragState;
	private int[] mEdgeDragsInProgress;
	private int[] mEdgeDragsLocked;
	private int mEdgeSize;
	private int[] mInitialEdgesTouched;
	private float[] mInitialMotionX;
	private float[] mInitialMotionY;
	private float[] mLastMotionX;
	private float[] mLastMotionY;
	private float mMaxVelocity;
	private float mMinVelocity;
	private final ViewGroup mParentView;
	private int mPointersDown;
	private boolean mReleaseInProgress;
	private ScrollerCompat mScroller;
	private final Runnable mSetIdleRunnable;
	private int mTouchSlop;
	private int mTrackingEdges;
	private VelocityTracker mVelocityTracker;

	static class AnonymousClass_1 implements Interpolator {
		AnonymousClass_1() {
			super();
		}

		public float getInterpolation(float t) {
			t -= 1.0f;
			return ((((t * t) * t) * t) * t) + 1.0f;
		}
	}

	class AnonymousClass_2 implements Runnable {
		final /* synthetic */ ViewDragHelper this$0;

		AnonymousClass_2(ViewDragHelper r1_ViewDragHelper) {
			super();
			this$0 = r1_ViewDragHelper;
		}

		public void run() {
			this$0.setDragState(STATE_IDLE);
		}
	}

	public static abstract class Callback {
		public Callback() {
			super();
		}

		public int clampViewPositionHorizontal(View child, int left, int dx) {
			return STATE_IDLE;
		}

		public int clampViewPositionVertical(View child, int top, int dy) {
			return STATE_IDLE;
		}

		public int getOrderedChildIndex(int index) {
			return index;
		}

		public int getViewHorizontalDragRange(View child) {
			return STATE_IDLE;
		}

		public int getViewVerticalDragRange(View child) {
			return STATE_IDLE;
		}

		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
		}

		public boolean onEdgeLock(int edgeFlags) {
			return false;
		}

		public void onEdgeTouched(int edgeFlags, int pointerId) {
		}

		public void onViewCaptured(View capturedChild, int activePointerId) {
		}

		public void onViewDragStateChanged(int state) {
		}

		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
		}

		public void onViewReleased(View releasedChild, float xvel, float yvel) {
		}

		public abstract boolean tryCaptureView(View r1_View, int r2i);
	}


	static {
		sInterpolator = new AnonymousClass_1();
	}

	private ViewDragHelper(Context context, ViewGroup forParent, Callback cb) {
		super();
		mActivePointerId = -1;
		mSetIdleRunnable = new AnonymousClass_2(this);
		if (forParent == null) {
			throw new IllegalArgumentException("Parent view may not be null");
		} else if (cb == null) {
			throw new IllegalArgumentException("Callback may not be null");
		} else {
			mParentView = forParent;
			mCallback = cb;
			ViewConfiguration vc = ViewConfiguration.get(context);
			mEdgeSize = (int) ((20.0f * context.getResources().getDisplayMetrics().density) + 0.5f);
			mTouchSlop = vc.getScaledTouchSlop();
			mMaxVelocity = (float) vc.getScaledMaximumFlingVelocity();
			mMinVelocity = (float) vc.getScaledMinimumFlingVelocity();
			mScroller = ScrollerCompat.create(context, sInterpolator);
		}
	}

	private boolean checkNewEdgeDrag(float delta, float odelta, int pointerId, int edge) {
		float absDelta = Math.abs(delta);
		float absODelta = Math.abs(odelta);
		if ((mInitialEdgesTouched[pointerId] & edge) == edge) {
			if ((mTrackingEdges & edge) != 0) {
				if ((mEdgeDragsLocked[pointerId] & edge) != edge) {
					if ((mEdgeDragsInProgress[pointerId] & edge) != edge) {
						if (absDelta > ((float) mTouchSlop) || absODelta > ((float) mTouchSlop)) {
							if (absDelta >= 0.5f * absODelta || !mCallback.onEdgeLock(edge)) {
								if ((mEdgeDragsInProgress[pointerId] & edge) == 0) {
									if (absDelta > ((float) mTouchSlop)) {
										return true;
									} else {
										return false;
									}
								} else {
									return false;
								}
							} else {
								int[] r3_int_A = mEdgeDragsLocked;
								r3_int_A[pointerId] = r3_int_A[pointerId] | edge;
								return false;
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
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

	/* JADX WARNING: inconsistent code */
	/*
	private boolean checkTouchSlop(android.view.View r8_child, float r9_dx, float r10_dy) {
		r7_this = this;
		r2 = 1;
		r3 = 0;
		if (r8_child != 0) goto L_0x0006;
	L_0x0004:
		r2 = r3;
	L_0x0005:
		return r2;
	L_0x0006:
		r4 = r7.mCallback;
		r4 = r4.getViewHorizontalDragRange(r8_child);
		if (r4 <= 0) goto L_0x002d;
	L_0x000e:
		r0 = r2;
	L_0x000f:
		r4 = r7.mCallback;
		r4 = r4.getViewVerticalDragRange(r8_child);
		if (r4 <= 0) goto L_0x002f;
	L_0x0017:
		r1 = r2;
	L_0x0018:
		if (r0_checkHorizontal == 0) goto L_0x0031;
	L_0x001a:
		if (r1_checkVertical == 0) goto L_0x0031;
	L_0x001c:
		r4 = r9_dx * r9_dx;
		r5 = r10_dy * r10_dy;
		r4 += r5;
		r5 = r7.mTouchSlop;
		r6 = r7.mTouchSlop;
		r5 *= r6;
		r5 = (float) r5;
		r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
		if (r4 > 0) goto L_0x0005;
	L_0x002b:
		r2 = r3;
		goto L_0x0005;
	L_0x002d:
		r0_checkHorizontal = r3;
		goto L_0x000f;
	L_0x002f:
		r1_checkVertical = r3;
		goto L_0x0018;
	L_0x0031:
		if (r0_checkHorizontal == 0) goto L_0x0040;
	L_0x0033:
		r4 = java.lang.Math.abs(r9_dx);
		r5 = r7.mTouchSlop;
		r5 = (float) r5;
		r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
		if (r4 > 0) goto L_0x0005;
	L_0x003e:
		r2 = r3;
		goto L_0x0005;
	L_0x0040:
		if (r1_checkVertical == 0) goto L_0x004f;
	L_0x0042:
		r4 = java.lang.Math.abs(r10_dy);
		r5 = r7.mTouchSlop;
		r5 = (float) r5;
		r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
		if (r4 > 0) goto L_0x0005;
	L_0x004d:
		r2 = r3;
		goto L_0x0005;
	L_0x004f:
		r2 = r3;
		goto L_0x0005;
	}
	*/
	private boolean checkTouchSlop(View child, float dx, float dy) {
		if (child == null) {
			return false;
		} else {
			boolean checkHorizontal;
			boolean checkVertical;
			if (mCallback.getViewHorizontalDragRange(child) > 0) {
				checkHorizontal = true;
			} else {
				checkHorizontal = false;
			}
			if (mCallback.getViewVerticalDragRange(child) > 0) {
				checkVertical = true;
			} else {
				checkVertical = false;
			}
			if (!checkHorizontal || !checkVertical) {
				if (checkVertical) {
					if (Math.abs(dy) <= ((float) mTouchSlop)) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else if ((dx * dx) + (dy * dy) <= ((float) (mTouchSlop * mTouchSlop))) {
				return false;
			} else {
				return true;
			}
		}
	}

	private float clampMag(float value, float absMin, float absMax) {
		float r1f = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		float absValue = Math.abs(value);
		if (absValue < absMin) {
			return 0.0f;
		} else if (absValue > absMax) {
			if (value <= r1f) {
				return -absMax;
			} else {
				return absMax;
			}
		} else {
			return value;
		}
	}

	private int clampMag(int value, int absMin, int absMax) {
		int absValue = Math.abs(value);
		if (absValue < absMin) {
			return STATE_IDLE;
		} else if (absValue > absMax) {
			if (value <= 0) {
				return -absMax;
			} else {
				return absMax;
			}
		} else {
			return value;
		}
	}

	private void clearMotionHistory() {
		if (mInitialMotionX == null) {
		} else {
			Arrays.fill(mInitialMotionX, AutoScrollHelper.RELATIVE_UNSPECIFIED);
			Arrays.fill(mInitialMotionY, AutoScrollHelper.RELATIVE_UNSPECIFIED);
			Arrays.fill(mLastMotionX, AutoScrollHelper.RELATIVE_UNSPECIFIED);
			Arrays.fill(mLastMotionY, AutoScrollHelper.RELATIVE_UNSPECIFIED);
			Arrays.fill(mInitialEdgesTouched, STATE_IDLE);
			Arrays.fill(mEdgeDragsInProgress, STATE_IDLE);
			Arrays.fill(mEdgeDragsLocked, STATE_IDLE);
			mPointersDown = 0;
		}
	}

	private void clearMotionHistory(int pointerId) {
		float r1f = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		if (mInitialMotionX == null) {
		} else {
			mInitialMotionX[pointerId] = r1f;
			mInitialMotionY[pointerId] = r1f;
			mLastMotionX[pointerId] = r1f;
			mLastMotionY[pointerId] = r1f;
			mInitialEdgesTouched[pointerId] = 0;
			mEdgeDragsInProgress[pointerId] = 0;
			mEdgeDragsLocked[pointerId] = 0;
			mPointersDown &= (1 << pointerId) ^ -1;
		}
	}

	private int computeAxisDuration(int delta, int velocity, int motionRange) {
		if (delta == 0) {
			return STATE_IDLE;
		} else {
			int duration;
			int width = mParentView.getWidth();
			int halfWidth = width / 2;
			float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, ((float) Math.abs(delta)) / ((float) width))));
			velocity = Math.abs(velocity);
			if (velocity > 0) {
				duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
			} else {
				duration = (int) (((((float) Math.abs(delta)) / ((float) motionRange)) + 1.0f) * 256.0f);
			}
			return Math.min(duration, MAX_SETTLE_DURATION);
		}
	}

	private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
		float xweight;
		float yweight;
		xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
		yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
		int absDx = Math.abs(dx);
		int absDy = Math.abs(dy);
		int absXVel = Math.abs(xvel);
		int absYVel = Math.abs(yvel);
		int addedVel = absXVel + absYVel;
		int addedDistance = absDx + absDy;
		if (xvel != 0) {
			xweight = ((float) absXVel) / ((float) addedVel);
		} else {
			xweight = ((float) absDx) / ((float) addedDistance);
		}
		if (yvel != 0) {
			yweight = ((float) absYVel) / ((float) addedVel);
		} else {
			yweight = ((float) absDy) / ((float) addedDistance);
		}
		return (int) ((((float) computeAxisDuration(dx, xvel, mCallback.getViewHorizontalDragRange(child))) * xweight) + (((float) computeAxisDuration(dy, yvel, mCallback.getViewVerticalDragRange(child))) * yweight));
	}

	public static ViewDragHelper create(ViewGroup forParent, float sensitivity, Callback cb) {
		ViewDragHelper helper = create(forParent, cb);
		helper.mTouchSlop = (int) (((float) helper.mTouchSlop) * (1.0f / sensitivity));
		return helper;
	}

	public static ViewDragHelper create(ViewGroup forParent, Callback cb) {
		return new ViewDragHelper(forParent.getContext(), forParent, cb);
	}

	private void dispatchViewReleased(float xvel, float yvel) {
		mReleaseInProgress = true;
		mCallback.onViewReleased(mCapturedView, xvel, yvel);
		mReleaseInProgress = false;
		if (mDragState == 1) {
			setDragState(STATE_IDLE);
		}
	}

	private float distanceInfluenceForSnapDuration(float f) {
		return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
	}

	private void dragTo(int left, int top, int dx, int dy) {
		int clampedX = left;
		int clampedY = top;
		int oldLeft = mCapturedView.getLeft();
		int oldTop = mCapturedView.getTop();
		if (dx != 0) {
			clampedX = mCallback.clampViewPositionHorizontal(mCapturedView, left, dx);
			mCapturedView.offsetLeftAndRight(clampedX - oldLeft);
		}
		if (dy != 0) {
			clampedY = mCallback.clampViewPositionVertical(mCapturedView, top, dy);
			mCapturedView.offsetTopAndBottom(clampedY - oldTop);
		}
		if (dx != 0 || dy != 0) {
			mCallback.onViewPositionChanged(mCapturedView, clampedX, clampedY, clampedX - oldLeft, clampedY - oldTop);
		}
	}

	private void ensureMotionHistorySizeForId(int pointerId) {
		if (mInitialMotionX == null || mInitialMotionX.length <= pointerId) {
			Object imx = new Object[(pointerId + 1)];
			Object imy = new Object[(pointerId + 1)];
			Object lmx = new Object[(pointerId + 1)];
			Object lmy = new Object[(pointerId + 1)];
			Object iit = new Object[(pointerId + 1)];
			Object edip = new Object[(pointerId + 1)];
			Object edl = new Object[(pointerId + 1)];
			if (mInitialMotionX != null) {
				System.arraycopy(mInitialMotionX, STATE_IDLE, imx, STATE_IDLE, mInitialMotionX.length);
				System.arraycopy(mInitialMotionY, STATE_IDLE, imy, STATE_IDLE, mInitialMotionY.length);
				System.arraycopy(mLastMotionX, STATE_IDLE, lmx, STATE_IDLE, mLastMotionX.length);
				System.arraycopy(mLastMotionY, STATE_IDLE, lmy, STATE_IDLE, mLastMotionY.length);
				System.arraycopy(mInitialEdgesTouched, STATE_IDLE, iit, STATE_IDLE, mInitialEdgesTouched.length);
				System.arraycopy(mEdgeDragsInProgress, STATE_IDLE, edip, STATE_IDLE, mEdgeDragsInProgress.length);
				System.arraycopy(mEdgeDragsLocked, STATE_IDLE, edl, STATE_IDLE, mEdgeDragsLocked.length);
			}
			mInitialMotionX = imx;
			mInitialMotionY = imy;
			mLastMotionX = lmx;
			mLastMotionY = lmy;
			mInitialEdgesTouched = iit;
			mEdgeDragsInProgress = edip;
			mEdgeDragsLocked = edl;
		}
	}

	private boolean forceSettleCapturedViewAt(int finalLeft, int finalTop, int xvel, int yvel) {
		int startLeft = mCapturedView.getLeft();
		int startTop = mCapturedView.getTop();
		int dx = finalLeft - startLeft;
		int dy = finalTop - startTop;
		if (dx != 0 || dy != 0) {
			mScroller.startScroll(startLeft, startTop, dx, dy, computeSettleDuration(mCapturedView, dx, dy, xvel, yvel));
			setDragState(STATE_SETTLING);
			return true;
		} else {
			mScroller.abortAnimation();
			setDragState(STATE_IDLE);
			return false;
		}
	}

	private int getEdgesTouched(int x, int y) {
		int result = STATE_IDLE;
		if (x < mParentView.getLeft() + mEdgeSize) {
			result |= 1;
		}
		if (y < mParentView.getTop() + mEdgeSize) {
			result |= 4;
		}
		if (x > mParentView.getRight() - mEdgeSize) {
			result |= 2;
		}
		if (y > mParentView.getBottom() - mEdgeSize) {
			result |= 8;
		}
		return result;
	}

	private void releaseViewForPointerUp() {
		mVelocityTracker.computeCurrentVelocity(DeviceManager.REQUEST_CODE_ENABLE_ADMIN, mMaxVelocity);
		dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), mMinVelocity, mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId), mMinVelocity, mMaxVelocity));
	}

	private void reportNewEdgeDrags(float dx, float dy, int pointerId) {
		int dragsStarted = STATE_IDLE;
		if (checkNewEdgeDrag(dx, dy, pointerId, STATE_DRAGGING)) {
			dragsStarted |= 1;
		}
		if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_TOP)) {
			dragsStarted |= 4;
		}
		if (checkNewEdgeDrag(dx, dy, pointerId, STATE_SETTLING)) {
			dragsStarted |= 2;
		}
		if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_BOTTOM)) {
			dragsStarted |= 8;
		}
		if (dragsStarted != 0) {
			int[] r1_int_A = mEdgeDragsInProgress;
			r1_int_A[pointerId] = r1_int_A[pointerId] | dragsStarted;
			mCallback.onEdgeDragStarted(dragsStarted, pointerId);
		}
	}

	private void saveInitialMotion(float x, float y, int pointerId) {
		ensureMotionHistorySizeForId(pointerId);
		mLastMotionX[pointerId] = x;
		mInitialMotionX[pointerId] = x;
		mLastMotionY[pointerId] = y;
		mInitialMotionY[pointerId] = y;
		mInitialEdgesTouched[pointerId] = getEdgesTouched((int) x, (int) y);
		mPointersDown |= 1 << pointerId;
	}

	private void saveLastMotion(MotionEvent ev) {
		int i = STATE_IDLE;
		while (i < MotionEventCompat.getPointerCount(ev)) {
			int pointerId = MotionEventCompat.getPointerId(ev, i);
			mLastMotionX[pointerId] = MotionEventCompat.getX(ev, i);
			mLastMotionY[pointerId] = MotionEventCompat.getY(ev, i);
			i++;
		}
	}

	public void abort() {
		cancel();
		if (mDragState == 2) {
			mScroller.abortAnimation();
			int newX = mScroller.getCurrX();
			int newY = mScroller.getCurrY();
			mCallback.onViewPositionChanged(mCapturedView, newX, newY, newX - mScroller.getCurrX(), newY - mScroller.getCurrY());
		}
		setDragState(STATE_IDLE);
	}

	/* JADX WARNING: inconsistent code */
	/*
	protected boolean canScroll(android.view.View r14_v, boolean r15_checkV, int r16_dx, int r17_dy, int r18_x, int r19_y) {
		r13_this = this;
		r1 = r14_v instanceof android.view.ViewGroup;
		if (r1 == 0) goto L_0x005c;
	L_0x0004:
		r9 = r14_v;
		r9 = (android.view.ViewGroup) r9;
		r11 = r14_v.getScrollX();
		r12 = r14_v.getScrollY();
		r8 = r9_group.getChildCount();
		r10 = r8_count + -1;
	L_0x0015:
		if (r10_i < 0) goto L_0x005c;
	L_0x0017:
		r2 = r9_group.getChildAt(r10_i);
		r1 = r18_x + r11_scrollX;
		r3 = r2_child.getLeft();
		if (r1 < r3) goto L_0x0059;
	L_0x0023:
		r1 = r18_x + r11_scrollX;
		r3 = r2_child.getRight();
		if (r1 >= r3) goto L_0x0059;
	L_0x002b:
		r1 = r19_y + r12_scrollY;
		r3 = r2_child.getTop();
		if (r1 < r3) goto L_0x0059;
	L_0x0033:
		r1 = r19_y + r12_scrollY;
		r3 = r2_child.getBottom();
		if (r1 >= r3) goto L_0x0059;
	L_0x003b:
		r3 = 1;
		r1 = r18_x + r11_scrollX;
		r4 = r2_child.getLeft();
		r6 = r1 - r4;
		r1 = r19_y + r12_scrollY;
		r4 = r2_child.getTop();
		r7 = r1 - r4;
		r1 = r13;
		r4 = r16_dx;
		r5 = r17_dy;
		r1 = r1.canScroll(r2_child, r3, r4, r5, r6, r7);
		if (r1 == 0) goto L_0x0059;
	L_0x0057:
		r1 = 1;
	L_0x0058:
		return r1;
	L_0x0059:
		r10_i++;
		goto L_0x0015;
	L_0x005c:
		if (r15_checkV == 0) goto L_0x0072;
	L_0x005e:
		r0 = r16;
		r1 = -r0;
		r1 = android.support.v4.view.ViewCompat.canScrollHorizontally(r14, r1);
		if (r1 != 0) goto L_0x0070;
	L_0x0067:
		r0 = r17;
		r1 = -r0;
		r1 = android.support.v4.view.ViewCompat.canScrollVertically(r14, r1);
		if (r1 == 0) goto L_0x0072;
	L_0x0070:
		r1 = 1;
		goto L_0x0058;
	L_0x0072:
		r1 = 0;
		goto L_0x0058;
	}
	*/
	protected boolean canScroll(View v, boolean checkV, int dx, int dy, int x, int y) {
		if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) v;
			int scrollX = v.getScrollX();
			int scrollY = v.getScrollY();
			int i = group.getChildCount() - 1;
			while (i >= 0) {
				View child = group.getChildAt(i);
				if (x + scrollX < child.getLeft() || x + scrollX >= child.getRight() || y + scrollY < child.getTop() || y + scrollY >= child.getBottom() || !canScroll(child, true, dx, dy, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
					i--;
				}
			}
		}
		if (checkV) {
			if (ViewCompat.canScrollHorizontally(v, -dx) || ViewCompat.canScrollVertically(v, -dy)) {
				return true;
			}
		}
		return false;
	}

	public void cancel() {
		mActivePointerId = -1;
		clearMotionHistory();
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	public void captureChildView(View childView, int activePointerId) {
		if (childView.getParent() != mParentView) {
			throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + mParentView + ")");
		} else {
			mCapturedView = childView;
			mActivePointerId = activePointerId;
			mCallback.onViewCaptured(childView, activePointerId);
			setDragState(STATE_DRAGGING);
		}
	}

	public boolean checkTouchSlop(int directions) {
		int i = STATE_IDLE;
		while (i < mInitialMotionX.length) {
			if (checkTouchSlop(directions, i)) {
				return true;
			} else {
				i++;
			}
		}
		return false;
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean checkTouchSlop(int r10_directions, int r11_pointerId) {
		r9_this = this;
		r4 = 1;
		r5 = 0;
		r6 = r9.isPointerDown(r11_pointerId);
		if (r6 != 0) goto L_0x000a;
	L_0x0008:
		r4 = r5;
	L_0x0009:
		return r4;
	L_0x000a:
		r6 = r10_directions & 1;
		if (r6 != r4) goto L_0x003e;
	L_0x000e:
		r0 = r4;
	L_0x000f:
		r6 = r10_directions & 2;
		r7 = 2;
		if (r6 != r7) goto L_0x0040;
	L_0x0014:
		r1 = r4;
	L_0x0015:
		r6 = r9.mLastMotionX;
		r6 = r6[r11_pointerId];
		r7 = r9.mInitialMotionX;
		r7 = r7[r11_pointerId];
		r2 = r6 - r7;
		r6 = r9.mLastMotionY;
		r6 = r6[r11_pointerId];
		r7 = r9.mInitialMotionY;
		r7 = r7[r11_pointerId];
		r3 = r6 - r7;
		if (r0_checkHorizontal == 0) goto L_0x0042;
	L_0x002b:
		if (r1_checkVertical == 0) goto L_0x0042;
	L_0x002d:
		r6 = r2_dx * r2_dx;
		r7 = r3_dy * r3_dy;
		r6 += r7;
		r7 = r9.mTouchSlop;
		r8 = r9.mTouchSlop;
		r7 *= r8;
		r7 = (float) r7;
		r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
		if (r6 > 0) goto L_0x0009;
	L_0x003c:
		r4 = r5;
		goto L_0x0009;
	L_0x003e:
		r0_checkHorizontal = r5;
		goto L_0x000f;
	L_0x0040:
		r1_checkVertical = r5;
		goto L_0x0015;
	L_0x0042:
		if (r0_checkHorizontal == 0) goto L_0x0051;
	L_0x0044:
		r6 = java.lang.Math.abs(r2_dx);
		r7 = r9.mTouchSlop;
		r7 = (float) r7;
		r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
		if (r6 > 0) goto L_0x0009;
	L_0x004f:
		r4 = r5;
		goto L_0x0009;
	L_0x0051:
		if (r1_checkVertical == 0) goto L_0x0060;
	L_0x0053:
		r6 = java.lang.Math.abs(r3_dy);
		r7 = r9.mTouchSlop;
		r7 = (float) r7;
		r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
		if (r6 > 0) goto L_0x0009;
	L_0x005e:
		r4 = r5;
		goto L_0x0009;
	L_0x0060:
		r4 = r5;
		goto L_0x0009;
	}
	*/
	public boolean checkTouchSlop(int directions, int pointerId) {
		if (!isPointerDown(pointerId)) {
			return false;
		} else {
			boolean checkHorizontal;
			boolean checkVertical;
			if ((directions & 1) == 1) {
				checkHorizontal = true;
			} else {
				checkHorizontal = false;
			}
			if ((directions & 2) == 2) {
				checkVertical = true;
			} else {
				checkVertical = false;
			}
			float dx = mLastMotionX[pointerId] - mInitialMotionX[pointerId];
			float dy = mLastMotionY[pointerId] - mInitialMotionY[pointerId];
			if (!checkHorizontal || !checkVertical) {
				if (checkVertical) {
					if (Math.abs(dy) <= ((float) mTouchSlop)) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else if ((dx * dx) + (dy * dy) <= ((float) (mTouchSlop * mTouchSlop))) {
				return false;
			} else {
				return true;
			}
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean continueSettling(boolean r10_deferCallbacks) {
		r9_this = this;
		r8 = 2;
		r7 = 0;
		r0 = r9.mDragState;
		if (r0 != r8) goto L_0x0069;
	L_0x0006:
		r0 = r9.mScroller;
		r6 = r0.computeScrollOffset();
		r0 = r9.mScroller;
		r2 = r0.getCurrX();
		r0 = r9.mScroller;
		r3 = r0.getCurrY();
		r0 = r9.mCapturedView;
		r0 = r0.getLeft();
		r4 = r2_x - r0;
		r0 = r9.mCapturedView;
		r0 = r0.getTop();
		r5 = r3_y - r0;
		if (r4_dx == 0) goto L_0x002f;
	L_0x002a:
		r0 = r9.mCapturedView;
		r0.offsetLeftAndRight(r4_dx);
	L_0x002f:
		if (r5_dy == 0) goto L_0x0036;
	L_0x0031:
		r0 = r9.mCapturedView;
		r0.offsetTopAndBottom(r5_dy);
	L_0x0036:
		if (r4_dx != 0) goto L_0x003a;
	L_0x0038:
		if (r5_dy == 0) goto L_0x0041;
	L_0x003a:
		r0 = r9.mCallback;
		r1 = r9.mCapturedView;
		r0.onViewPositionChanged(r1, r2_x, r3_y, r4_dx, r5_dy);
	L_0x0041:
		if (r6_keepGoing == 0) goto L_0x005e;
	L_0x0043:
		r0 = r9.mScroller;
		r0 = r0.getFinalX();
		if (r2_x != r0) goto L_0x005e;
	L_0x004b:
		r0 = r9.mScroller;
		r0 = r0.getFinalY();
		if (r3_y != r0) goto L_0x005e;
	L_0x0053:
		r0 = r9.mScroller;
		r0.abortAnimation();
		r0 = r9.mScroller;
		r6_keepGoing = r0.isFinished();
	L_0x005e:
		if (r6_keepGoing != 0) goto L_0x0069;
	L_0x0060:
		if (r10_deferCallbacks == 0) goto L_0x006f;
	L_0x0062:
		r0 = r9.mParentView;
		r1 = r9.mSetIdleRunnable;
		r0.post(r1);
	L_0x0069:
		r0 = r9.mDragState;
		if (r0 != r8) goto L_0x0073;
	L_0x006d:
		r0 = 1;
	L_0x006e:
		return r0;
	L_0x006f:
		r9.setDragState(r7);
		goto L_0x0069;
	L_0x0073:
		r0 = r7;
		goto L_0x006e;
	}
	*/
	public boolean continueSettling(boolean deferCallbacks) {
		if (mDragState == 2) {
			boolean keepGoing = mScroller.computeScrollOffset();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			int dx = x - mCapturedView.getLeft();
			int dy = y - mCapturedView.getTop();
			if (dx != 0) {
				mCapturedView.offsetLeftAndRight(dx);
			}
			if (dy != 0) {
				mCapturedView.offsetTopAndBottom(dy);
			}
			if (dx != 0 || dy != 0) {
				mCallback.onViewPositionChanged(mCapturedView, x, y, dx, dy);
			} else if (!keepGoing || x != mScroller.getFinalX() || y != mScroller.getFinalY()) {
			} else {
				mScroller.abortAnimation();
				keepGoing = mScroller.isFinished();
			}
			if (!keepGoing || x != mScroller.getFinalX() || y != mScroller.getFinalY()) {
			} else {
				mScroller.abortAnimation();
				keepGoing = mScroller.isFinished();
			}
		}
		if (mDragState == 2) {
			return true;
		} else {
			return false;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public android.view.View findTopChildUnder(int r6_x, int r7_y) {
		r5_this = this;
		r3 = r5.mParentView;
		r1 = r3.getChildCount();
		r2 = r1_childCount + -1;
	L_0x0008:
		if (r2_i < 0) goto L_0x0032;
	L_0x000a:
		r3 = r5.mParentView;
		r4 = r5.mCallback;
		r4 = r4.getOrderedChildIndex(r2_i);
		r0 = r3.getChildAt(r4);
		r3 = r0_child.getLeft();
		if (r6_x < r3) goto L_0x002f;
	L_0x001c:
		r3 = r0_child.getRight();
		if (r6_x >= r3) goto L_0x002f;
	L_0x0022:
		r3 = r0_child.getTop();
		if (r7_y < r3) goto L_0x002f;
	L_0x0028:
		r3 = r0_child.getBottom();
		if (r7_y >= r3) goto L_0x002f;
	L_0x002e:
		return r0_child;
	L_0x002f:
		r2_i++;
		goto L_0x0008;
	L_0x0032:
		r0_child = 0;
		goto L_0x002e;
	}
	*/
	public View findTopChildUnder(int x, int y) {
		int i = mParentView.getChildCount() - 1;
		while (i >= 0) {
			View child = mParentView.getChildAt(mCallback.getOrderedChildIndex(i));
			if (x < child.getLeft() || x >= child.getRight() || y < child.getTop() || y >= child.getBottom()) {
				i--;
			}
		}
		return null;
	}

	public void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop) {
		if (!mReleaseInProgress) {
			throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
		} else {
			mScroller.fling(mCapturedView.getLeft(), mCapturedView.getTop(), (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId), minLeft, maxLeft, minTop, maxTop);
			setDragState(STATE_SETTLING);
		}
	}

	public int getActivePointerId() {
		return mActivePointerId;
	}

	public View getCapturedView() {
		return mCapturedView;
	}

	public int getEdgeSize() {
		return mEdgeSize;
	}

	public float getMinVelocity() {
		return mMinVelocity;
	}

	public int getTouchSlop() {
		return mTouchSlop;
	}

	public int getViewDragState() {
		return mDragState;
	}

	public boolean isCapturedViewUnder(int x, int y) {
		return isViewUnder(mCapturedView, x, y);
	}

	public boolean isEdgeTouched(int edges) {
		int i = STATE_IDLE;
		while (i < mInitialEdgesTouched.length) {
			if (isEdgeTouched(edges, i)) {
				return true;
			} else {
				i++;
			}
		}
		return false;
	}

	public boolean isEdgeTouched(int edges, int pointerId) {
		if (!isPointerDown(pointerId) || (mInitialEdgesTouched[pointerId] & edges) == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isPointerDown(int pointerId) {
		if ((mPointersDown & (1 << pointerId)) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isViewUnder(View view, int x, int y) {
		if (view == null) {
			return false;
		} else if (x >= view.getLeft()) {
			if (x < view.getRight()) {
				if (y >= view.getTop()) {
					if (y < view.getBottom()) {
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
		} else {
			return false;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public void processTouchEvent(android.view.MotionEvent r22_ev) {
		r21_this = this;
		r3 = android.support.v4.view.MotionEventCompat.getActionMasked(r22_ev);
		r4 = android.support.v4.view.MotionEventCompat.getActionIndex(r22_ev);
		if (r3_action != 0) goto L_0x000d;
	L_0x000a:
		r21.cancel();
	L_0x000d:
		r0 = r21;
		r0 = r0.mVelocityTracker;
		r19 = r0;
		if (r19 != 0) goto L_0x001f;
	L_0x0015:
		r19 = android.view.VelocityTracker.obtain();
		r0 = r19;
		r1 = r21;
		r1.mVelocityTracker = r0;
	L_0x001f:
		r0 = r21;
		r0 = r0.mVelocityTracker;
		r19 = r0;
		r0 = r19;
		r1 = r22_ev;
		r0.addMovement(r1);
		switch(r3_action) {
			case 0: goto L_0x0030;
			case 1: goto L_0x0287;
			case 2: goto L_0x011a;
			case 3: goto L_0x029d;
			case 4: goto L_0x002f;
			case 5: goto L_0x008e;
			case 6: goto L_0x01fe;
			default: goto L_0x002f;
		}
	L_0x002f:
		return;
	L_0x0030:
		r17 = r22_ev.getX();
		r18 = r22_ev.getY();
		r19 = 0;
		r0 = r22_ev;
		r1 = r19;
		r15 = android.support.v4.view.MotionEventCompat.getPointerId(r0, r1);
		r0 = r17_x;
		r0 = (int) r0;
		r19 = r0;
		r0 = r18_y;
		r0 = (int) r0;
		r20 = r0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r16 = r0.findTopChildUnder(r1, r2);
		r0 = r21;
		r1 = r17_x;
		r2 = r18_y;
		r0.saveInitialMotion(r1, r2, r15_pointerId);
		r0 = r21;
		r1 = r16_toCapture;
		r0.tryCaptureViewForDrag(r1, r15_pointerId);
		r0 = r21;
		r0 = r0.mInitialEdgesTouched;
		r19 = r0;
		r7 = r19[r15_pointerId];
		r0 = r21;
		r0 = r0.mTrackingEdges;
		r19 = r0;
		r19 &= r7_edgesTouched;
		if (r19 == 0) goto L_0x002f;
	L_0x0078:
		r0 = r21;
		r0 = r0.mCallback;
		r19 = r0;
		r0 = r21;
		r0 = r0.mTrackingEdges;
		r20 = r0;
		r20 &= r7_edgesTouched;
		r0 = r19;
		r1 = r20;
		r0.onEdgeTouched(r1, r15_pointerId);
		goto L_0x002f;
	L_0x008e:
		r0 = r22_ev;
		r15_pointerId = android.support.v4.view.MotionEventCompat.getPointerId(r0, r4_actionIndex);
		r0 = r22_ev;
		r17_x = android.support.v4.view.MotionEventCompat.getX(r0, r4_actionIndex);
		r0 = r22_ev;
		r18_y = android.support.v4.view.MotionEventCompat.getY(r0, r4_actionIndex);
		r0 = r21;
		r1 = r17_x;
		r2 = r18_y;
		r0.saveInitialMotion(r1, r2, r15_pointerId);
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		if (r19 != 0) goto L_0x00f5;
	L_0x00b1:
		r0 = r17_x;
		r0 = (int) r0;
		r19 = r0;
		r0 = r18_y;
		r0 = (int) r0;
		r20 = r0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r16_toCapture = r0.findTopChildUnder(r1, r2);
		r0 = r21;
		r1 = r16_toCapture;
		r0.tryCaptureViewForDrag(r1, r15_pointerId);
		r0 = r21;
		r0 = r0.mInitialEdgesTouched;
		r19 = r0;
		r7_edgesTouched = r19[r15_pointerId];
		r0 = r21;
		r0 = r0.mTrackingEdges;
		r19 = r0;
		r19 &= r7_edgesTouched;
		if (r19 == 0) goto L_0x002f;
	L_0x00de:
		r0 = r21;
		r0 = r0.mCallback;
		r19 = r0;
		r0 = r21;
		r0 = r0.mTrackingEdges;
		r20 = r0;
		r20 &= r7_edgesTouched;
		r0 = r19;
		r1 = r20;
		r0.onEdgeTouched(r1, r15_pointerId);
		goto L_0x002f;
	L_0x00f5:
		r0 = r17_x;
		r0 = (int) r0;
		r19 = r0;
		r0 = r18_y;
		r0 = (int) r0;
		r20 = r0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r19 = r0.isCapturedViewUnder(r1, r2);
		if (r19 == 0) goto L_0x002f;
	L_0x010b:
		r0 = r21;
		r0 = r0.mCapturedView;
		r19 = r0;
		r0 = r21;
		r1 = r19;
		r0.tryCaptureViewForDrag(r1, r15_pointerId);
		goto L_0x002f;
	L_0x011a:
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		r20 = 1;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x018e;
	L_0x0128:
		r0 = r21;
		r0 = r0.mActivePointerId;
		r19 = r0;
		r0 = r22_ev;
		r1 = r19;
		r12 = android.support.v4.view.MotionEventCompat.findPointerIndex(r0, r1);
		r0 = r22_ev;
		r17_x = android.support.v4.view.MotionEventCompat.getX(r0, r12_index);
		r0 = r22_ev;
		r18_y = android.support.v4.view.MotionEventCompat.getY(r0, r12_index);
		r0 = r21;
		r0 = r0.mLastMotionX;
		r19 = r0;
		r0 = r21;
		r0 = r0.mActivePointerId;
		r20 = r0;
		r19 = r19[r20];
		r19 = r17_x - r19;
		r0 = r19;
		r10 = (int) r0;
		r0 = r21;
		r0 = r0.mLastMotionY;
		r19 = r0;
		r0 = r21;
		r0 = r0.mActivePointerId;
		r20 = r0;
		r19 = r19[r20];
		r19 = r18_y - r19;
		r0 = r19;
		r11 = (int) r0;
		r0 = r21;
		r0 = r0.mCapturedView;
		r19 = r0;
		r19 = r19.getLeft();
		r19 += r10_idx;
		r0 = r21;
		r0 = r0.mCapturedView;
		r20 = r0;
		r20 = r20.getTop();
		r20 += r11_idy;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r0.dragTo(r1, r2, r10_idx, r11_idy);
		r21.saveLastMotion(r22_ev);
		goto L_0x002f;
	L_0x018e:
		r14 = android.support.v4.view.MotionEventCompat.getPointerCount(r22_ev);
		r8 = 0;
	L_0x0193:
		if (r8_i >= r14_pointerCount) goto L_0x01ce;
	L_0x0195:
		r0 = r22_ev;
		r15_pointerId = android.support.v4.view.MotionEventCompat.getPointerId(r0, r8_i);
		r0 = r22_ev;
		r17_x = android.support.v4.view.MotionEventCompat.getX(r0, r8_i);
		r0 = r22_ev;
		r18_y = android.support.v4.view.MotionEventCompat.getY(r0, r8_i);
		r0 = r21;
		r0 = r0.mInitialMotionX;
		r19 = r0;
		r19 = r19[r15_pointerId];
		r5 = r17_x - r19;
		r0 = r21;
		r0 = r0.mInitialMotionY;
		r19 = r0;
		r19 = r19[r15_pointerId];
		r6 = r18_y - r19;
		r0 = r21;
		r0.reportNewEdgeDrags(r5_dx, r6_dy, r15_pointerId);
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		r20 = 1;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x01d3;
	L_0x01ce:
		r21.saveLastMotion(r22_ev);
		goto L_0x002f;
	L_0x01d3:
		r0 = r17_x;
		r0 = (int) r0;
		r19 = r0;
		r0 = r18_y;
		r0 = (int) r0;
		r20 = r0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r16_toCapture = r0.findTopChildUnder(r1, r2);
		r0 = r21;
		r1 = r16_toCapture;
		r19 = r0.checkTouchSlop(r1, r5_dx, r6_dy);
		if (r19 == 0) goto L_0x01fb;
	L_0x01f1:
		r0 = r21;
		r1 = r16_toCapture;
		r19 = r0.tryCaptureViewForDrag(r1, r15_pointerId);
		if (r19 != 0) goto L_0x01ce;
	L_0x01fb:
		r8_i++;
		goto L_0x0193;
	L_0x01fe:
		r0 = r22_ev;
		r15_pointerId = android.support.v4.view.MotionEventCompat.getPointerId(r0, r4_actionIndex);
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		r20 = 1;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x0280;
	L_0x0212:
		r0 = r21;
		r0 = r0.mActivePointerId;
		r19 = r0;
		r0 = r19;
		if (r15_pointerId != r0) goto L_0x0280;
	L_0x021c:
		r13 = -1;
		r14_pointerCount = android.support.v4.view.MotionEventCompat.getPointerCount(r22_ev);
		r8_i = 0;
	L_0x0222:
		if (r8_i >= r14_pointerCount) goto L_0x0277;
	L_0x0224:
		r0 = r22_ev;
		r9 = android.support.v4.view.MotionEventCompat.getPointerId(r0, r8_i);
		r0 = r21;
		r0 = r0.mActivePointerId;
		r19 = r0;
		r0 = r19;
		if (r9_id != r0) goto L_0x0237;
	L_0x0234:
		r8_i++;
		goto L_0x0222;
	L_0x0237:
		r0 = r22_ev;
		r17_x = android.support.v4.view.MotionEventCompat.getX(r0, r8_i);
		r0 = r22_ev;
		r18_y = android.support.v4.view.MotionEventCompat.getY(r0, r8_i);
		r0 = r17_x;
		r0 = (int) r0;
		r19 = r0;
		r0 = r18_y;
		r0 = (int) r0;
		r20 = r0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r19 = r0.findTopChildUnder(r1, r2);
		r0 = r21;
		r0 = r0.mCapturedView;
		r20 = r0;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x0234;
	L_0x0263:
		r0 = r21;
		r0 = r0.mCapturedView;
		r19 = r0;
		r0 = r21;
		r1 = r19;
		r19 = r0.tryCaptureViewForDrag(r1, r9_id);
		if (r19 == 0) goto L_0x0234;
	L_0x0273:
		r0 = r21;
		r13_newActivePointer = r0.mActivePointerId;
	L_0x0277:
		r19 = -1;
		r0 = r19;
		if (r13_newActivePointer != r0) goto L_0x0280;
	L_0x027d:
		r21.releaseViewForPointerUp();
	L_0x0280:
		r0 = r21;
		r0.clearMotionHistory(r15_pointerId);
		goto L_0x002f;
	L_0x0287:
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		r20 = 1;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x0298;
	L_0x0295:
		r21.releaseViewForPointerUp();
	L_0x0298:
		r21.cancel();
		goto L_0x002f;
	L_0x029d:
		r0 = r21;
		r0 = r0.mDragState;
		r19 = r0;
		r20 = 1;
		r0 = r19;
		r1 = r20;
		if (r0 != r1) goto L_0x02b8;
	L_0x02ab:
		r19 = 0;
		r20 = 0;
		r0 = r21;
		r1 = r19;
		r2 = r20;
		r0.dispatchViewReleased(r1, r2);
	L_0x02b8:
		r21.cancel();
		goto L_0x002f;
	}
	*/
	public void processTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		int actionIndex = MotionEventCompat.getActionIndex(ev);
		if (action == 0) {
			cancel();
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		float x;
		float y;
		int pointerId;
		int edgesTouched;
		int i;
		switch(action) {
		case STATE_IDLE:
			x = ev.getX();
			y = ev.getY();
			pointerId = MotionEventCompat.getPointerId(ev, 0);
			saveInitialMotion(x, y, pointerId);
			tryCaptureViewForDrag(findTopChildUnder((int) x, (int) y), pointerId);
			edgesTouched = mInitialEdgesTouched[pointerId];
			if ((mTrackingEdges & edgesTouched) != 0) {
				mCallback.onEdgeTouched(mTrackingEdges & edgesTouched, pointerId);
			}
		case STATE_DRAGGING:
			if (mDragState == 1) {
				releaseViewForPointerUp();
			}
			cancel();
		case STATE_SETTLING:
			if (mDragState == 1) {
				int index = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				int idx = (int) (MotionEventCompat.getX(ev, index) - mLastMotionX[mActivePointerId]);
				int idy = (int) (MotionEventCompat.getY(ev, index) - mLastMotionY[mActivePointerId]);
				dragTo(mCapturedView.getLeft() + idx, mCapturedView.getTop() + idy, idx, idy);
				saveLastMotion(ev);
			} else {
				i = STATE_IDLE;
				while (i < MotionEventCompat.getPointerCount(ev)) {
					pointerId = MotionEventCompat.getPointerId(ev, i);
					x = MotionEventCompat.getX(ev, i);
					y = MotionEventCompat.getY(ev, i);
					float dx = x - mInitialMotionX[pointerId];
					float dy = y - mInitialMotionY[pointerId];
					reportNewEdgeDrags(dx, dy, pointerId);
					if (mDragState == 1) {
						saveLastMotion(ev);
					} else {
						View toCapture = findTopChildUnder((int) x, (int) y);
						if (!checkTouchSlop(toCapture, dx, dy) || !tryCaptureViewForDrag(toCapture, pointerId)) {
							i++;
						}
					}
				}
				saveLastMotion(ev);
			}
		case DIRECTION_ALL:
			if (mDragState == 1) {
				dispatchViewReleased(0.0f, 0.0f);
			}
			cancel();
		case WearableExtender.SIZE_FULL_SCREEN:
			pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
			x = MotionEventCompat.getX(ev, actionIndex);
			y = MotionEventCompat.getY(ev, actionIndex);
			saveInitialMotion(x, y, pointerId);
			if (mDragState == 0) {
				tryCaptureViewForDrag(findTopChildUnder((int) x, (int) y), pointerId);
				edgesTouched = mInitialEdgesTouched[pointerId];
				if ((mTrackingEdges & edgesTouched) != 0) {
					mCallback.onEdgeTouched(mTrackingEdges & edgesTouched, pointerId);
				}
			} else if (isCapturedViewUnder((int) x, (int) y)) {
				tryCaptureViewForDrag(mCapturedView, pointerId);
			}
		case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
			pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
			if (mDragState != 1 || pointerId != mActivePointerId) {
				clearMotionHistory(pointerId);
			} else {
				int newActivePointer = INVALID_POINTER;
				i = STATE_IDLE;
				while (i < MotionEventCompat.getPointerCount(ev)) {
					int id = MotionEventCompat.getPointerId(ev, i);
					if (id != mActivePointerId && findTopChildUnder((int) MotionEventCompat.getX(ev, i), (int) MotionEventCompat.getY(ev, i)) == mCapturedView && tryCaptureViewForDrag(mCapturedView, id)) {
						newActivePointer = mActivePointerId;
					}
				}
				if (newActivePointer == -1) {
					releaseViewForPointerUp();
				}
				clearMotionHistory(pointerId);
			}
		}
	}

	void setDragState(int state) {
		if (mDragState != state) {
			mDragState = state;
			mCallback.onViewDragStateChanged(state);
			if (state == 0) {
				mCapturedView = null;
			}
		}
	}

	public void setEdgeTrackingEnabled(int edgeFlags) {
		mTrackingEdges = edgeFlags;
	}

	public void setMinVelocity(float minVel) {
		mMinVelocity = minVel;
	}

	public boolean settleCapturedViewAt(int finalLeft, int finalTop) {
		if (!mReleaseInProgress) {
			throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
		} else {
			return forceSettleCapturedViewAt(finalLeft, finalTop, (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId));
		}
	}

	public boolean shouldInterceptTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		int actionIndex = MotionEventCompat.getActionIndex(ev);
		if (action == 0) {
			cancel();
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		float x;
		float y;
		int pointerId;
		View toCapture;
		int edgesTouched;
		switch(action) {
		case STATE_IDLE:
			x = ev.getX();
			y = ev.getY();
			pointerId = MotionEventCompat.getPointerId(ev, STATE_IDLE);
			saveInitialMotion(x, y, pointerId);
			toCapture = findTopChildUnder((int) x, (int) y);
			if (toCapture != mCapturedView || mDragState != 2) {
				edgesTouched = mInitialEdgesTouched[pointerId];
				if ((mTrackingEdges & edgesTouched) == 0) {
					mCallback.onEdgeTouched(mTrackingEdges & edgesTouched, pointerId);
				}
			} else {
				tryCaptureViewForDrag(toCapture, pointerId);
				edgesTouched = mInitialEdgesTouched[pointerId];
				if ((mTrackingEdges & edgesTouched) == 0) {
				} else {
					mCallback.onEdgeTouched(mTrackingEdges & edgesTouched, pointerId);
				}
			}
			break;
		case STATE_DRAGGING:
		case DIRECTION_ALL:
			cancel();
			break;
		case STATE_SETTLING:
			int i = STATE_IDLE;
			while (i < MotionEventCompat.getPointerCount(ev)) {
				pointerId = MotionEventCompat.getPointerId(ev, i);
				x = MotionEventCompat.getX(ev, i);
				y = MotionEventCompat.getY(ev, i);
				float dx = x - mInitialMotionX[pointerId];
				float dy = y - mInitialMotionY[pointerId];
				reportNewEdgeDrags(dx, dy, pointerId);
				if (mDragState == 1) {
					saveLastMotion(ev);
				} else {
					toCapture = findTopChildUnder((int) x, (int) y);
					if (toCapture == null || !checkTouchSlop(toCapture, dx, dy) || !tryCaptureViewForDrag(toCapture, pointerId)) {
						i++;
					}
				}
			}
			saveLastMotion(ev);
			break;
		case WearableExtender.SIZE_FULL_SCREEN:
			pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
			x = MotionEventCompat.getX(ev, actionIndex);
			y = MotionEventCompat.getY(ev, actionIndex);
			saveInitialMotion(x, y, pointerId);
			if (mDragState == 0) {
				edgesTouched = mInitialEdgesTouched[pointerId];
				if ((mTrackingEdges & edgesTouched) != 0) {
					mCallback.onEdgeTouched(mTrackingEdges & edgesTouched, pointerId);
				}
			} else if (mDragState == 2) {
				toCapture = findTopChildUnder((int) x, (int) y);
				if (toCapture == mCapturedView) {
					tryCaptureViewForDrag(toCapture, pointerId);
				}
			}
			break;
		case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
			clearMotionHistory(MotionEventCompat.getPointerId(ev, actionIndex));
			break;
		}
		if (mDragState == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean smoothSlideViewTo(View child, int finalLeft, int finalTop) {
		mCapturedView = child;
		mActivePointerId = -1;
		return forceSettleCapturedViewAt(finalLeft, finalTop, STATE_IDLE, STATE_IDLE);
	}

	/* JADX WARNING: inconsistent code */
	/*
	boolean tryCaptureViewForDrag(android.view.View r3_toCapture, int r4_pointerId) {
		r2_this = this;
		r0 = 1;
		r1 = r2.mCapturedView;
		if (r3_toCapture != r1) goto L_0x000a;
	L_0x0005:
		r1 = r2.mActivePointerId;
		if (r1 != r4_pointerId) goto L_0x000a;
	L_0x0009:
		return r0;
	L_0x000a:
		if (r3_toCapture == 0) goto L_0x001a;
	L_0x000c:
		r1 = r2.mCallback;
		r1 = r1.tryCaptureView(r3_toCapture, r4_pointerId);
		if (r1 == 0) goto L_0x001a;
	L_0x0014:
		r2.mActivePointerId = r4_pointerId;
		r2.captureChildView(r3_toCapture, r4_pointerId);
		goto L_0x0009;
	L_0x001a:
		r0 = 0;
		goto L_0x0009;
	}
	*/
	boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
		if (toCapture != mCapturedView || mActivePointerId != pointerId) {
			return false;
		} else {
			return true;
		}
	}
}
