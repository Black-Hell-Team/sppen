package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class SwipeRefreshLayout extends ViewGroup {
	private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
	private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
	private static final int INVALID_POINTER = -1;
	private static final int[] LAYOUT_ATTRS;
	private static final String LOG_TAG;
	private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6f;
	private static final float PROGRESS_BAR_HEIGHT = 4.0f;
	private static final int REFRESH_TRIGGER_DISTANCE = 120;
	private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300;
	private final AccelerateInterpolator mAccelerateInterpolator;
	private int mActivePointerId;
	private final Animation mAnimateToStartPosition;
	private final Runnable mCancel;
	private float mCurrPercentage;
	private int mCurrentTargetOffsetTop;
	private final DecelerateInterpolator mDecelerateInterpolator;
	private float mDistanceToTriggerSync;
	private int mFrom;
	private float mFromPercentage;
	private float mInitialMotionY;
	private boolean mIsBeingDragged;
	private float mLastMotionY;
	private OnRefreshListener mListener;
	private int mMediumAnimationDuration;
	private int mOriginalOffsetTop;
	private SwipeProgressBar mProgressBar;
	private int mProgressBarHeight;
	private boolean mRefreshing;
	private final Runnable mReturnToStartPosition;
	private final AnimationListener mReturnToStartPositionListener;
	private boolean mReturningToStart;
	private final AnimationListener mShrinkAnimationListener;
	private Animation mShrinkTrigger;
	private View mTarget;
	private int mTouchSlop;

	class AnonymousClass_1 extends Animation {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_1(SwipeRefreshLayout r1_SwipeRefreshLayout) {
			super();
			this$0 = r1_SwipeRefreshLayout;
		}

		public void applyTransformation(float interpolatedTime, Transformation t) {
			int targetTop = 0;
			if (this$0.mFrom != this$0.mOriginalOffsetTop) {
				targetTop = this$0.mFrom + ((int) (((float) (this$0.mOriginalOffsetTop - this$0.mFrom)) * interpolatedTime));
			}
			int offset = targetTop - this$0.mTarget.getTop();
			int currentTop = this$0.mTarget.getTop();
			if (offset + currentTop < 0) {
				offset = 0 - currentTop;
			}
			this$0.setTargetOffsetTopAndBottom(offset);
		}
	}

	class AnonymousClass_2 extends Animation {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_2(SwipeRefreshLayout r1_SwipeRefreshLayout) {
			super();
			this$0 = r1_SwipeRefreshLayout;
		}

		public void applyTransformation(float interpolatedTime, Transformation t) {
			this$0.mProgressBar.setTriggerPercentage(this$0.mFromPercentage + ((0.0f - this$0.mFromPercentage) * interpolatedTime));
		}
	}

	class AnonymousClass_5 implements Runnable {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_5(SwipeRefreshLayout r1_SwipeRefreshLayout) {
			super();
			this$0 = r1_SwipeRefreshLayout;
		}

		public void run() {
			this$0.mReturningToStart = true;
			this$0.animateOffsetToStartPosition(this$0.mCurrentTargetOffsetTop + this$0.getPaddingTop(), this$0.mReturnToStartPositionListener);
		}
	}

	class AnonymousClass_6 implements Runnable {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_6(SwipeRefreshLayout r1_SwipeRefreshLayout) {
			super();
			this$0 = r1_SwipeRefreshLayout;
		}

		public void run() {
			this$0.mReturningToStart = true;
			if (this$0.mProgressBar != null) {
				this$0.mFromPercentage = this$0.mCurrPercentage;
				this$0.mShrinkTrigger.setDuration((long) this$0.mMediumAnimationDuration);
				this$0.mShrinkTrigger.setAnimationListener(this$0.mShrinkAnimationListener);
				this$0.mShrinkTrigger.reset();
				this$0.mShrinkTrigger.setInterpolator(this$0.mDecelerateInterpolator);
				this$0.startAnimation(this$0.mShrinkTrigger);
			}
			this$0.animateOffsetToStartPosition(this$0.mCurrentTargetOffsetTop + this$0.getPaddingTop(), this$0.mReturnToStartPositionListener);
		}
	}

	private class BaseAnimationListener implements AnimationListener {
		final /* synthetic */ SwipeRefreshLayout this$0;

		private BaseAnimationListener(SwipeRefreshLayout r1_SwipeRefreshLayout) {
			super();
			this$0 = r1_SwipeRefreshLayout;
		}

		/* synthetic */ BaseAnimationListener(SwipeRefreshLayout x0, SwipeRefreshLayout.AnonymousClass_1 x1) {
			this(x0);
		}

		public void onAnimationEnd(Animation animation) {
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation animation) {
		}
	}

	public static interface OnRefreshListener {
		public void onRefresh();
	}

	class AnonymousClass_3 extends SwipeRefreshLayout.BaseAnimationListener {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_3(SwipeRefreshLayout r2_SwipeRefreshLayout) {
			super(r2_SwipeRefreshLayout, null);
			this$0 = r2_SwipeRefreshLayout;
		}

		public void onAnimationEnd(Animation animation) {
			this$0.mCurrentTargetOffsetTop = 0;
		}
	}

	class AnonymousClass_4 extends SwipeRefreshLayout.BaseAnimationListener {
		final /* synthetic */ SwipeRefreshLayout this$0;

		AnonymousClass_4(SwipeRefreshLayout r2_SwipeRefreshLayout) {
			super(r2_SwipeRefreshLayout, null);
			this$0 = r2_SwipeRefreshLayout;
		}

		public void onAnimationEnd(Animation animation) {
			this$0.mCurrPercentage = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}
	}


	static {
		LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16842766;
		LAYOUT_ATTRS = r0_int_A;
	}

	public SwipeRefreshLayout(Context context) {
		this(context, null);
	}

	public SwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRefreshing = false;
		mDistanceToTriggerSync = -1.0f;
		mFromPercentage = 0.0f;
		mCurrPercentage = 0.0f;
		mActivePointerId = -1;
		mAnimateToStartPosition = new AnonymousClass_1(this);
		mShrinkTrigger = new AnonymousClass_2(this);
		mReturnToStartPositionListener = new AnonymousClass_3(this);
		mShrinkAnimationListener = new AnonymousClass_4(this);
		mReturnToStartPosition = new AnonymousClass_5(this);
		mCancel = new AnonymousClass_6(this);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMediumAnimationDuration = getResources().getInteger(17694721);
		setWillNotDraw(false);
		mProgressBar = new SwipeProgressBar(this);
		mProgressBarHeight = (int) (getResources().getDisplayMetrics().density * 4.0f);
		mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
		mAccelerateInterpolator = new AccelerateInterpolator(1.5f);
		TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
		setEnabled(a.getBoolean(0, true));
		a.recycle();
	}

	private void animateOffsetToStartPosition(int from, AnimationListener listener) {
		mFrom = from;
		mAnimateToStartPosition.reset();
		mAnimateToStartPosition.setDuration((long) mMediumAnimationDuration);
		mAnimateToStartPosition.setAnimationListener(listener);
		mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
		mTarget.startAnimation(mAnimateToStartPosition);
	}

	private void ensureTarget() {
		if (mTarget == null) {
			if (getChildCount() <= 1 || isInEditMode()) {
				mTarget = getChildAt(0);
				mOriginalOffsetTop = mTarget.getTop() + getPaddingTop();
			} else {
				throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
			}
		}
		if (mDistanceToTriggerSync != -1.0f || getParent() == null || ((View) getParent()).getHeight() <= 0) {
		} else {
			mDistanceToTriggerSync = (float) ((int) Math.min(((float) ((View) getParent()).getHeight()) * 0.6f, 120.0f * getResources().getDisplayMetrics().density));
		}
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		int pointerIndex = MotionEventCompat.getActionIndex(ev);
		if (MotionEventCompat.getPointerId(ev, pointerIndex) == mActivePointerId) {
			int newPointerIndex;
			if (pointerIndex == 0) {
				newPointerIndex = 1;
			} else {
				newPointerIndex = 0;
			}
			mLastMotionY = MotionEventCompat.getY(ev, newPointerIndex);
			mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
		}
	}

	private void setTargetOffsetTopAndBottom(int offset) {
		mTarget.offsetTopAndBottom(offset);
		mCurrentTargetOffsetTop = mTarget.getTop();
	}

	private void setTriggerPercentage(float percent) {
		if (percent == 0.0f) {
			mCurrPercentage = 0.0f;
		} else {
			mCurrPercentage = percent;
			mProgressBar.setTriggerPercentage(percent);
		}
	}

	private void startRefresh() {
		removeCallbacks(mCancel);
		mReturnToStartPosition.run();
		setRefreshing(true);
		mListener.onRefresh();
	}

	private void updateContentOffsetTop(int targetTop) {
		int currentTop = mTarget.getTop();
		if (((float) targetTop) > mDistanceToTriggerSync) {
			targetTop = (int) mDistanceToTriggerSync;
		} else if (targetTop < 0) {
			targetTop = 0;
		}
		setTargetOffsetTopAndBottom(targetTop - currentTop);
	}

	private void updatePositionTimeout() {
		removeCallbacks(mCancel);
		postDelayed(mCancel, RETURN_TO_ORIGINAL_POSITION_TIMEOUT);
	}

	public boolean canChildScrollUp() {
		if (VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				AbsListView absListView = (AbsListView) mTarget;
				if (absListView.getChildCount() > 0) {
					if (absListView.getFirstVisiblePosition() <= 0) {
						if (absListView.getChildAt(0).getTop() < absListView.getPaddingTop()) {
							return true;
						}
					} else {
						return true;
					}
				}
				return false;
			} else if (mTarget.getScrollY() <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return ViewCompat.canScrollVertically(mTarget, INVALID_POINTER);
		}
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);
		mProgressBar.draw(canvas);
	}

	public boolean isRefreshing() {
		return mRefreshing;
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		removeCallbacks(mCancel);
		removeCallbacks(mReturnToStartPosition);
	}

	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeCallbacks(mReturnToStartPosition);
		removeCallbacks(mCancel);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		ensureTarget();
		int action = MotionEventCompat.getActionMasked(ev);
		float r5f;
		int pointerIndex;
		float y;
		if (!mReturningToStart || action != 0) {
			if (!isEnabled()) {
				if (mReturningToStart) {
					if (!canChildScrollUp()) {
						return false;
					} else {
						switch(action) {
						case WearableExtender.SIZE_DEFAULT:
							r5f = ev.getY();
							mInitialMotionY = r5f;
							mLastMotionY = r5f;
							mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
							mIsBeingDragged = false;
							mCurrPercentage = 0.0f;
							break;
						case CursorAdapter.FLAG_AUTO_REQUERY:
						case WearableExtender.SIZE_MEDIUM:
							mIsBeingDragged = false;
							mCurrPercentage = 0.0f;
							mActivePointerId = -1;
							break;
						case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
							if (mActivePointerId != -1) {
								Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
								return false;
							} else {
								pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
								if (pointerIndex >= 0) {
									Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
									return false;
								} else {
									y = MotionEventCompat.getY(ev, pointerIndex);
									if (y - mInitialMotionY <= ((float) mTouchSlop)) {
										mLastMotionY = y;
										mIsBeingDragged = true;
									}
								}
							}
							break;
						case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
							onSecondaryPointerUp(ev);
							break;
						}
						return mIsBeingDragged;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			mReturningToStart = false;
			if (!isEnabled()) {
				return false;
			} else if (mReturningToStart) {
				return false;
			} else if (!canChildScrollUp()) {
				switch(action) {
				case WearableExtender.SIZE_DEFAULT:
					r5f = ev.getY();
					mInitialMotionY = r5f;
					mLastMotionY = r5f;
					mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
					mIsBeingDragged = false;
					mCurrPercentage = 0.0f;
					break;
				case CursorAdapter.FLAG_AUTO_REQUERY:
				case WearableExtender.SIZE_MEDIUM:
					mIsBeingDragged = false;
					mCurrPercentage = 0.0f;
					mActivePointerId = -1;
					break;
				case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
					if (mActivePointerId != -1) {
						pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
						if (pointerIndex >= 0) {
							y = MotionEventCompat.getY(ev, pointerIndex);
							if (y - mInitialMotionY <= ((float) mTouchSlop)) {
								return mIsBeingDragged;
							} else {
								mLastMotionY = y;
								mIsBeingDragged = true;
							}
						} else {
							Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
							return false;
						}
					} else {
						Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
						return false;
					}
				case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
					onSecondaryPointerUp(ev);
					break;
				}
				return mIsBeingDragged;
			} else {
				return false;
			}
		}
	}

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		mProgressBar.setBounds(0, 0, width, mProgressBarHeight);
		if (getChildCount() == 0) {
		} else {
			int childLeft = getPaddingLeft();
			int childTop = mCurrentTargetOffsetTop + getPaddingTop();
			getChildAt(0).layout(childLeft, childTop, childLeft + ((width - getPaddingLeft()) - getPaddingRight()), childTop + ((height - getPaddingTop()) - getPaddingBottom()));
		}
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getChildCount() <= 1 || isInEditMode()) {
			if (getChildCount() > 0) {
				getChildAt(0).measure(MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
			}
		} else {
			throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		float r7f;
		int pointerIndex;
		float y;
		float yDiff;
		int index;
		if (!mReturningToStart || action != 0) {
			if (!isEnabled()) {
				if (mReturningToStart) {
					if (!canChildScrollUp()) {
						return false;
					} else {
						switch(action) {
						case WearableExtender.SIZE_DEFAULT:
							r7f = ev.getY();
							mInitialMotionY = r7f;
							mLastMotionY = r7f;
							mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
							mIsBeingDragged = false;
							mCurrPercentage = 0.0f;
							break;
						case CursorAdapter.FLAG_AUTO_REQUERY:
						case WearableExtender.SIZE_MEDIUM:
							mIsBeingDragged = false;
							mCurrPercentage = 0.0f;
							mActivePointerId = -1;
							return false;
						case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
							pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
							if (pointerIndex >= 0) {
								Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
								return false;
							} else {
								y = MotionEventCompat.getY(ev, pointerIndex);
								yDiff = y - mInitialMotionY;
								if (mIsBeingDragged || yDiff <= ((float) mTouchSlop)) {
									if (!mIsBeingDragged) {
										if (yDiff <= mDistanceToTriggerSync) {
											startRefresh();
										} else {
											setTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
											updateContentOffsetTop((int) yDiff);
											if (mLastMotionY <= y || mTarget.getTop() != getPaddingTop()) {
												updatePositionTimeout();
											} else {
												removeCallbacks(mCancel);
											}
										}
										mLastMotionY = y;
									}
								} else {
									mIsBeingDragged = true;
									if (!mIsBeingDragged) {
										return true;
									} else {
										if (yDiff <= mDistanceToTriggerSync) {
											setTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
											updateContentOffsetTop((int) yDiff);
											if (mLastMotionY <= y || mTarget.getTop() != getPaddingTop()) {
												updatePositionTimeout();
											} else {
												removeCallbacks(mCancel);
											}
										} else {
											startRefresh();
										}
										mLastMotionY = y;
									}
								}
							}
							break;
						case WearableExtender.SIZE_FULL_SCREEN:
							index = MotionEventCompat.getActionIndex(ev);
							mLastMotionY = MotionEventCompat.getY(ev, index);
							mActivePointerId = MotionEventCompat.getPointerId(ev, index);
							break;
						case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
							onSecondaryPointerUp(ev);
							break;
						}
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			mReturningToStart = false;
			if (!isEnabled()) {
				return false;
			} else if (mReturningToStart) {
				return false;
			} else if (!canChildScrollUp()) {
				switch(action) {
				case WearableExtender.SIZE_DEFAULT:
					r7f = ev.getY();
					mInitialMotionY = r7f;
					mLastMotionY = r7f;
					mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
					mIsBeingDragged = false;
					mCurrPercentage = 0.0f;
					break;
				case CursorAdapter.FLAG_AUTO_REQUERY:
				case WearableExtender.SIZE_MEDIUM:
					mIsBeingDragged = false;
					mCurrPercentage = 0.0f;
					mActivePointerId = -1;
					return false;
				case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
					pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
					if (pointerIndex >= 0) {
						y = MotionEventCompat.getY(ev, pointerIndex);
						yDiff = y - mInitialMotionY;
						if (mIsBeingDragged || yDiff <= ((float) mTouchSlop)) {
							if (!mIsBeingDragged) {
								if (yDiff <= mDistanceToTriggerSync) {
									startRefresh();
								} else {
									setTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
									updateContentOffsetTop((int) yDiff);
									if (mLastMotionY <= y || mTarget.getTop() != getPaddingTop()) {
										updatePositionTimeout();
									} else {
										removeCallbacks(mCancel);
									}
								}
								mLastMotionY = y;
							}
						} else {
							mIsBeingDragged = true;
							if (!mIsBeingDragged) {
								return true;
							} else {
								if (yDiff <= mDistanceToTriggerSync) {
									setTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
									updateContentOffsetTop((int) yDiff);
									if (mLastMotionY <= y || mTarget.getTop() != getPaddingTop()) {
										updatePositionTimeout();
									} else {
										removeCallbacks(mCancel);
									}
								} else {
									startRefresh();
								}
								mLastMotionY = y;
							}
						}
					} else {
						Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
						return false;
					}
				case WearableExtender.SIZE_FULL_SCREEN:
					index = MotionEventCompat.getActionIndex(ev);
					mLastMotionY = MotionEventCompat.getY(ev, index);
					mActivePointerId = MotionEventCompat.getPointerId(ev, index);
					break;
				case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
					onSecondaryPointerUp(ev);
					break;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	public void requestDisallowInterceptTouchEvent(boolean b) {
	}

	@Deprecated
	public void setColorScheme(int colorRes1, int colorRes2, int colorRes3, int colorRes4) {
		setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
	}

	public void setColorSchemeColors(int color1, int color2, int color3, int color4) {
		ensureTarget();
		mProgressBar.setColorScheme(color1, color2, color3, color4);
	}

	public void setColorSchemeResources(int colorRes1, int colorRes2, int colorRes3, int colorRes4) {
		Resources res = getResources();
		setColorSchemeColors(res.getColor(colorRes1), res.getColor(colorRes2), res.getColor(colorRes3), res.getColor(colorRes4));
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public void setRefreshing(boolean refreshing) {
		if (mRefreshing != refreshing) {
			ensureTarget();
			mCurrPercentage = 0.0f;
			mRefreshing = refreshing;
			if (mRefreshing) {
				mProgressBar.start();
			} else {
				mProgressBar.stop();
			}
		}
	}
}
