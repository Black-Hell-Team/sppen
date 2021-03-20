package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public abstract class AutoScrollHelper implements OnTouchListener {
	private static final int DEFAULT_ACTIVATION_DELAY;
	private static final int DEFAULT_EDGE_TYPE = 1;
	private static final float DEFAULT_MAXIMUM_EDGE = 3.4028235E38f;
	private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
	private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
	private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
	private static final int DEFAULT_RAMP_UP_DURATION = 500;
	private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
	private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
	public static final int EDGE_TYPE_INSIDE = 0;
	public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
	public static final int EDGE_TYPE_OUTSIDE = 2;
	private static final int HORIZONTAL = 0;
	public static final float NO_MAX = 3.4028235E38f;
	public static final float NO_MIN = 0.0f;
	public static final float RELATIVE_UNSPECIFIED = 0.0f;
	private static final int VERTICAL = 1;
	private int mActivationDelay;
	private boolean mAlreadyDelayed;
	private boolean mAnimating;
	private final Interpolator mEdgeInterpolator;
	private int mEdgeType;
	private boolean mEnabled;
	private boolean mExclusive;
	private float[] mMaximumEdges;
	private float[] mMaximumVelocity;
	private float[] mMinimumVelocity;
	private boolean mNeedsCancel;
	private boolean mNeedsReset;
	private float[] mRelativeEdges;
	private float[] mRelativeVelocity;
	private Runnable mRunnable;
	private final ClampedScroller mScroller;
	private final View mTarget;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private static class ClampedScroller {
		private long mDeltaTime;
		private int mDeltaX;
		private int mDeltaY;
		private int mEffectiveRampDown;
		private int mRampDownDuration;
		private int mRampUpDuration;
		private long mStartTime;
		private long mStopTime;
		private float mStopValue;
		private float mTargetVelocityX;
		private float mTargetVelocityY;

		public ClampedScroller() {
			super();
			mStartTime = -9223372036854775808L;
			mStopTime = -1;
			mDeltaTime = 0;
			mDeltaX = 0;
			mDeltaY = 0;
		}

		private float getValueAt(long currentTime) {
			float r4f = RELATIVE_UNSPECIFIED;
			if (currentTime < mStartTime) {
				return RELATIVE_UNSPECIFIED;
			} else if (mStopTime < 0 || currentTime < mStopTime) {
				return AutoScrollHelper.constrain(((float) (currentTime - mStartTime)) / ((float) mRampUpDuration), r4f, (float)DEFAULT_RELATIVE_VELOCITY) * 0.5f;
			} else {
				return (AutoScrollHelper.constrain(((float) (currentTime - mStopTime)) / ((float) mEffectiveRampDown), r4f, (float)DEFAULT_RELATIVE_VELOCITY) * mStopValue) + (1.0f - mStopValue);
			}
		}

		private float interpolateValue(float value) {
			return ((-4.0f * value) * value) + (4.0f * value);
		}

		public void computeScrollDelta() {
			if (mDeltaTime == 0) {
				throw new RuntimeException("Cannot compute scroll delta before calling start()");
			} else {
				long currentTime = AnimationUtils.currentAnimationTimeMillis();
				float scale = interpolateValue(getValueAt(currentTime));
				long elapsedSinceDelta = currentTime - mDeltaTime;
				mDeltaTime = currentTime;
				mDeltaX = (int) ((((float) elapsedSinceDelta) * scale) * mTargetVelocityX);
				mDeltaY = (int) ((((float) elapsedSinceDelta) * scale) * mTargetVelocityY);
			}
		}

		public int getDeltaX() {
			return mDeltaX;
		}

		public int getDeltaY() {
			return mDeltaY;
		}

		public int getHorizontalDirection() {
			return (int) (mTargetVelocityX / Math.abs(mTargetVelocityX));
		}

		public int getVerticalDirection() {
			return (int) (mTargetVelocityY / Math.abs(mTargetVelocityY));
		}

		public boolean isFinished() {
			if (mStopTime <= 0 || AnimationUtils.currentAnimationTimeMillis() <= mStopTime + ((long) mEffectiveRampDown)) {
				return false;
			} else {
				return true;
			}
		}

		public void requestStop() {
			long currentTime = AnimationUtils.currentAnimationTimeMillis();
			mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentTime - mStartTime), (int)HORIZONTAL, mRampDownDuration);
			mStopValue = getValueAt(currentTime);
			mStopTime = currentTime;
		}

		public void setRampDownDuration(int durationMillis) {
			mRampDownDuration = durationMillis;
		}

		public void setRampUpDuration(int durationMillis) {
			mRampUpDuration = durationMillis;
		}

		public void setTargetVelocity(float x, float y) {
			mTargetVelocityX = x;
			mTargetVelocityY = y;
		}

		public void start() {
			mStartTime = AnimationUtils.currentAnimationTimeMillis();
			mStopTime = -1;
			mDeltaTime = mStartTime;
			mStopValue = 0.5f;
			mDeltaX = 0;
			mDeltaY = 0;
		}
	}

	private class ScrollAnimationRunnable implements Runnable {
		final /* synthetic */ AutoScrollHelper this$0;

		private ScrollAnimationRunnable(AutoScrollHelper r1_AutoScrollHelper) {
			super();
			this$0 = r1_AutoScrollHelper;
		}

		/* synthetic */ ScrollAnimationRunnable(AutoScrollHelper x0, AutoScrollHelper.AnonymousClass_1 x1) {
			this(x0);
		}

		public void run() {
			if (!this$0.mAnimating) {
			} else {
				if (this$0.mNeedsReset) {
					this$0.mNeedsReset = false;
					this$0.mScroller.start();
				}
				AutoScrollHelper.ClampedScroller scroller = this$0.mScroller;
				if (scroller.isFinished() || !this$0.shouldAnimate()) {
					this$0.mAnimating = false;
				} else {
					if (this$0.mNeedsCancel) {
						this$0.mNeedsCancel = false;
						this$0.cancelTargetTouch();
					}
					scroller.computeScrollDelta();
					this$0.scrollTargetBy(scroller.getDeltaX(), scroller.getDeltaY());
					ViewCompat.postOnAnimation(this$0.mTarget, this);
				}
			}
		}
	}


	static {
		DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
	}

	public AutoScrollHelper(View target) {
		super();
		mScroller = new ClampedScroller();
		mEdgeInterpolator = new AccelerateInterpolator();
		mRelativeEdges = new float[]{0.0f, 0.0f};
		mMaximumEdges = new float[]{3.4028235E38f, 3.4028235E38f};
		mRelativeVelocity = new float[]{0.0f, 0.0f};
		mMinimumVelocity = new float[]{0.0f, 0.0f};
		mMaximumVelocity = new float[]{3.4028235E38f, 3.4028235E38f};
		mTarget = target;
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int maxVelocity = (int) ((1575.0f * metrics.density) + 0.5f);
		int minVelocity = (int) ((315.0f * metrics.density) + 0.5f);
		setMaximumVelocity((float) maxVelocity, (float) maxVelocity);
		setMinimumVelocity((float) minVelocity, (float) minVelocity);
		setEdgeType(VERTICAL);
		setMaximumEdges(NO_MAX, NO_MAX);
		setRelativeEdges(DEFAULT_RELATIVE_EDGE, DEFAULT_RELATIVE_EDGE);
		setRelativeVelocity(DEFAULT_RELATIVE_VELOCITY, DEFAULT_RELATIVE_VELOCITY);
		setActivationDelay(DEFAULT_ACTIVATION_DELAY);
		setRampUpDuration(DEFAULT_RAMP_UP_DURATION);
		setRampDownDuration(DEFAULT_RAMP_UP_DURATION);
	}

	private void cancelTargetTouch() {
		long eventTime = SystemClock.uptimeMillis();
		MotionEvent cancel = MotionEvent.obtain(eventTime, eventTime, WearableExtender.SIZE_MEDIUM, RELATIVE_UNSPECIFIED, 0.0f, HORIZONTAL);
		mTarget.onTouchEvent(cancel);
		cancel.recycle();
	}

	private float computeTargetVelocity(int direction, float coordinate, float srcSize, float dstSize) {
		float r7f = RELATIVE_UNSPECIFIED;
		float value = getEdgeValue(mRelativeEdges[direction], srcSize, mMaximumEdges[direction], coordinate);
		if (value == 0.0f) {
			return RELATIVE_UNSPECIFIED;
		} else {
			float minimumVelocity = mMinimumVelocity[direction];
			float maximumVelocity = mMaximumVelocity[direction];
			float targetVelocity = mRelativeVelocity[direction] * dstSize;
			if (value > r7f) {
				return constrain(value * targetVelocity, minimumVelocity, maximumVelocity);
			} else {
				return -constrain((-value) * targetVelocity, minimumVelocity, maximumVelocity);
			}
		}
	}

	private static float constrain(float value, float min, float max) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		} else {
			return value;
		}
	}

	private static int constrain(int value, int min, int max) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		} else {
			return value;
		}
	}

	private float constrainEdgeValue(float current, float leading) {
		if (leading == 0.0f) {
			return RELATIVE_UNSPECIFIED;
		} else {
			switch(mEdgeType) {
			case HORIZONTAL:
			case VERTICAL:
				if (current < leading) {
					if (current >= 0.0f) {
						return 1.0f - (current / leading);
					} else if (mAnimating) {
						if (mEdgeType == 1) {
							return 1.0f;
						} else {
							return RELATIVE_UNSPECIFIED;
						}
					} else {
						return RELATIVE_UNSPECIFIED;
					}
				} else {
					return RELATIVE_UNSPECIFIED;
				}
			case EDGE_TYPE_OUTSIDE:
				if (current < 0.0f) {
					return current / (-leading);
				} else {
					return RELATIVE_UNSPECIFIED;
				}
			}
			return RELATIVE_UNSPECIFIED;
		}
	}

	private float getEdgeValue(float relativeValue, float size, float maxValue, float current) {
		float interpolated;
		float edgeSize = constrain(relativeValue * size, (float)RELATIVE_UNSPECIFIED, maxValue);
		float value = constrainEdgeValue(size - current, edgeSize) - constrainEdgeValue(current, edgeSize);
		if (value < 0.0f) {
			interpolated = -mEdgeInterpolator.getInterpolation(-value);
		} else if (value > 0.0f) {
			interpolated = mEdgeInterpolator.getInterpolation(value);
		} else {
			return RELATIVE_UNSPECIFIED;
		}
		return constrain(interpolated, -1.0f, (float)DEFAULT_RELATIVE_VELOCITY);
	}

	private void requestStop() {
		if (mNeedsReset) {
			mAnimating = false;
		} else {
			mScroller.requestStop();
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	private boolean shouldAnimate() {
		r4_this = this;
		r1 = r4.mScroller;
		r2 = r1_scroller.getVerticalDirection();
		r0_horizontalDirection = r1_scroller.getHorizontalDirection();
		if (r2_verticalDirection == 0) goto L_0x0012;
	L_0x000c:
		r3 = r4.canTargetScrollVertically(r2);
		if (r3 != 0) goto L_0x001a;
	L_0x0012:
		if (r0 == 0) goto L_0x001c;
	L_0x0014:
		r3 = r4.canTargetScrollHorizontally(r0);
		if (r3 == 0) goto L_0x001c;
	L_0x001a:
		r3 = 1;
	L_0x001b:
		return r3;
	L_0x001c:
		r3 = 0;
		goto L_0x001b;
	}
	*/
	private boolean shouldAnimate() {
		ClampedScroller scroller = mScroller;
		int verticalDirection = scroller.getVerticalDirection();
		int horizontalDirection = scroller.getHorizontalDirection();
		if (verticalDirection == 0 || !canTargetScrollVertically(verticalDirection)) {
			return false;
		} else {
			return true;
		}
		return true;
	}

	private void startAnimating() {
		if (mRunnable == null) {
			mRunnable = new ScrollAnimationRunnable(this, null);
		}
		mAnimating = true;
		mNeedsReset = true;
		if (mAlreadyDelayed || mActivationDelay <= 0) {
			mRunnable.run();
		} else {
			ViewCompat.postOnAnimationDelayed(mTarget, mRunnable, (long) mActivationDelay);
		}
		mAlreadyDelayed = true;
	}

	public abstract boolean canTargetScrollHorizontally(int r1i);

	public abstract boolean canTargetScrollVertically(int r1i);

	public boolean isEnabled() {
		return mEnabled;
	}

	public boolean isExclusive() {
		return mExclusive;
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean onTouch(android.view.View r9_v, android.view.MotionEvent r10_event) {
		r8_this = this;
		r3 = 1;
		r4 = 0;
		r5 = r8.mEnabled;
		if (r5 != 0) goto L_0x0007;
	L_0x0006:
		return r4;
	L_0x0007:
		r0 = android.support.v4.view.MotionEventCompat.getActionMasked(r10_event);
		switch(r0_action) {
			case 0: goto L_0x0018;
			case 1: goto L_0x0057;
			case 2: goto L_0x001c;
			case 3: goto L_0x0057;
			default: goto L_0x000e;
		}
	L_0x000e:
		r5 = r8.mExclusive;
		if (r5 == 0) goto L_0x005b;
	L_0x0012:
		r5 = r8.mAnimating;
		if (r5 == 0) goto L_0x005b;
	L_0x0016:
		r4 = r3;
		goto L_0x0006;
	L_0x0018:
		r8.mNeedsCancel = r3;
		r8.mAlreadyDelayed = r4;
	L_0x001c:
		r5 = r10_event.getX();
		r6 = r9_v.getWidth();
		r6 = (float) r6;
		r7 = r8.mTarget;
		r7 = r7.getWidth();
		r7 = (float) r7;
		r1 = r8.computeTargetVelocity(r4, r5, r6, r7);
		r5 = r10_event.getY();
		r6 = r9_v.getHeight();
		r6 = (float) r6;
		r7 = r8.mTarget;
		r7 = r7.getHeight();
		r7 = (float) r7;
		r2 = r8.computeTargetVelocity(r3, r5, r6, r7);
		r5 = r8.mScroller;
		r5.setTargetVelocity(r1_xTargetVelocity, r2_yTargetVelocity);
		r5 = r8.mAnimating;
		if (r5 != 0) goto L_0x000e;
	L_0x004d:
		r5 = r8.shouldAnimate();
		if (r5 == 0) goto L_0x000e;
	L_0x0053:
		r8.startAnimating();
		goto L_0x000e;
	L_0x0057:
		r8.requestStop();
		goto L_0x000e;
	L_0x005b:
		r3 = r4;
		goto L_0x0016;
	}
	*/
	public boolean onTouch(View v, MotionEvent event) {
		boolean r3z = true;
		if (!mEnabled) {
			return false;
		} else {
			switch(MotionEventCompat.getActionMasked(event)) {
			case HORIZONTAL:
				mNeedsCancel = true;
				mAlreadyDelayed = false;
				break;
			case VERTICAL:
			case WearableExtender.SIZE_MEDIUM:
				requestStop();
				break;
			case EDGE_TYPE_OUTSIDE:
				break;
			}
		}
	}

	public abstract void scrollTargetBy(int r1i, int r2i);

	public AutoScrollHelper setActivationDelay(int delayMillis) {
		mActivationDelay = delayMillis;
		return this;
	}

	public AutoScrollHelper setEdgeType(int type) {
		mEdgeType = type;
		return this;
	}

	public AutoScrollHelper setEnabled(boolean enabled) {
		if (!mEnabled || enabled) {
			mEnabled = enabled;
			return this;
		} else {
			requestStop();
			mEnabled = enabled;
			return this;
		}
	}

	public AutoScrollHelper setExclusive(boolean exclusive) {
		mExclusive = exclusive;
		return this;
	}

	public AutoScrollHelper setMaximumEdges(float horizontalMax, float verticalMax) {
		mMaximumEdges[0] = horizontalMax;
		mMaximumEdges[1] = verticalMax;
		return this;
	}

	public AutoScrollHelper setMaximumVelocity(float horizontalMax, float verticalMax) {
		mMaximumVelocity[0] = horizontalMax / 1000.0f;
		mMaximumVelocity[1] = verticalMax / 1000.0f;
		return this;
	}

	public AutoScrollHelper setMinimumVelocity(float horizontalMin, float verticalMin) {
		mMinimumVelocity[0] = horizontalMin / 1000.0f;
		mMinimumVelocity[1] = verticalMin / 1000.0f;
		return this;
	}

	public AutoScrollHelper setRampDownDuration(int durationMillis) {
		mScroller.setRampDownDuration(durationMillis);
		return this;
	}

	public AutoScrollHelper setRampUpDuration(int durationMillis) {
		mScroller.setRampUpDuration(durationMillis);
		return this;
	}

	public AutoScrollHelper setRelativeEdges(float horizontal, float vertical) {
		mRelativeEdges[0] = horizontal;
		mRelativeEdges[1] = vertical;
		return this;
	}

	public AutoScrollHelper setRelativeVelocity(float horizontal, float vertical) {
		mRelativeVelocity[0] = horizontal / 1000.0f;
		mRelativeVelocity[1] = vertical / 1000.0f;
		return this;
	}
}
