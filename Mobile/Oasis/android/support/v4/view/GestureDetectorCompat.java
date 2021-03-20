package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.widget.AutoScrollHelper;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.elite.DeviceManager;

public class GestureDetectorCompat {
	private final GestureDetectorCompatImpl mImpl;

	static interface GestureDetectorCompatImpl {
		public boolean isLongpressEnabled();

		public boolean onTouchEvent(MotionEvent r1_MotionEvent);

		public void setIsLongpressEnabled(boolean r1z);

		public void setOnDoubleTapListener(OnDoubleTapListener r1_OnDoubleTapListener);
	}

	static class GestureDetectorCompatImplBase implements GestureDetectorCompat.GestureDetectorCompatImpl {
		private static final int DOUBLE_TAP_TIMEOUT;
		private static final int LONGPRESS_TIMEOUT;
		private static final int LONG_PRESS = 2;
		private static final int SHOW_PRESS = 1;
		private static final int TAP = 3;
		private static final int TAP_TIMEOUT;
		private boolean mAlwaysInBiggerTapRegion;
		private boolean mAlwaysInTapRegion;
		private MotionEvent mCurrentDownEvent;
		private boolean mDeferConfirmSingleTap;
		private OnDoubleTapListener mDoubleTapListener;
		private int mDoubleTapSlopSquare;
		private float mDownFocusX;
		private float mDownFocusY;
		private final Handler mHandler;
		private boolean mInLongPress;
		private boolean mIsDoubleTapping;
		private boolean mIsLongpressEnabled;
		private float mLastFocusX;
		private float mLastFocusY;
		private final OnGestureListener mListener;
		private int mMaximumFlingVelocity;
		private int mMinimumFlingVelocity;
		private MotionEvent mPreviousUpEvent;
		private boolean mStillDown;
		private int mTouchSlopSquare;
		private VelocityTracker mVelocityTracker;

		private class GestureHandler extends Handler {
			final /* synthetic */ GestureDetectorCompat.GestureDetectorCompatImplBase this$0;

			GestureHandler(GestureDetectorCompat.GestureDetectorCompatImplBase r1_GestureDetectorCompat_GestureDetectorCompatImplBase) {
				super();
				this$0 = r1_GestureDetectorCompat_GestureDetectorCompatImplBase;
			}

			GestureHandler(GestureDetectorCompat.GestureDetectorCompatImplBase r2_GestureDetectorCompat_GestureDetectorCompatImplBase, Handler handler) {
				super(handler.getLooper());
				this$0 = r2_GestureDetectorCompat_GestureDetectorCompatImplBase;
			}

			public void handleMessage(Message msg) {
				switch(msg.what) {
				case SHOW_PRESS:
					this$0.mListener.onShowPress(this$0.mCurrentDownEvent);
				case LONG_PRESS:
					this$0.dispatchLongPress();
				case TAP:
					if (this$0.mDoubleTapListener != null) {
						if (!this$0.mStillDown) {
							this$0.mDoubleTapListener.onSingleTapConfirmed(this$0.mCurrentDownEvent);
						} else {
							this$0.mDeferConfirmSingleTap = true;
						}
					}
				}
				throw new RuntimeException("Unknown message " + msg);
			}
		}


		static {
			LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
			TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
			DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
		}

		public GestureDetectorCompatImplBase(Context context, OnGestureListener listener, Handler handler) {
			super();
			if (handler != null) {
				mHandler = new GestureHandler(this, handler);
			} else {
				mHandler = new GestureHandler(this);
			}
			mListener = listener;
			if (listener instanceof OnDoubleTapListener) {
				setOnDoubleTapListener((OnDoubleTapListener) listener);
			}
			init(context);
		}

		private void cancel() {
			mHandler.removeMessages(SHOW_PRESS);
			mHandler.removeMessages(LONG_PRESS);
			mHandler.removeMessages(TAP);
			mVelocityTracker.recycle();
			mVelocityTracker = null;
			mIsDoubleTapping = false;
			mStillDown = false;
			mAlwaysInTapRegion = false;
			mAlwaysInBiggerTapRegion = false;
			mDeferConfirmSingleTap = false;
			if (mInLongPress) {
				mInLongPress = false;
			}
		}

		private void cancelTaps() {
			mHandler.removeMessages(SHOW_PRESS);
			mHandler.removeMessages(LONG_PRESS);
			mHandler.removeMessages(TAP);
			mIsDoubleTapping = false;
			mAlwaysInTapRegion = false;
			mAlwaysInBiggerTapRegion = false;
			mDeferConfirmSingleTap = false;
			if (mInLongPress) {
				mInLongPress = false;
			}
		}

		private void dispatchLongPress() {
			mHandler.removeMessages(TAP);
			mDeferConfirmSingleTap = false;
			mInLongPress = true;
			mListener.onLongPress(mCurrentDownEvent);
		}

		private void init(Context context) {
			if (context == null) {
				throw new IllegalArgumentException("Context must not be null");
			} else if (mListener == null) {
				throw new IllegalArgumentException("OnGestureListener must not be null");
			} else {
				mIsLongpressEnabled = true;
				ViewConfiguration configuration = ViewConfiguration.get(context);
				int touchSlop = configuration.getScaledTouchSlop();
				int doubleTapSlop = configuration.getScaledDoubleTapSlop();
				mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
				mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
				mTouchSlopSquare = touchSlop * touchSlop;
				mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
			}
		}

		private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
			if (!mAlwaysInBiggerTapRegion) {
				return false;
			} else if (secondDown.getEventTime() - firstUp.getEventTime() <= ((long) DOUBLE_TAP_TIMEOUT)) {
				int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
				int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
				if ((deltaX * deltaX) + (deltaY * deltaY) < mDoubleTapSlopSquare) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		public boolean isLongpressEnabled() {
			return mIsLongpressEnabled;
		}

		public boolean onTouchEvent(MotionEvent ev) {
			boolean pointerUp;
			int skipIndex;
			int div;
			int action = ev.getAction();
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(ev);
			if ((action & 255) == 6) {
				pointerUp = true;
			} else {
				pointerUp = false;
			}
			if (pointerUp) {
				skipIndex = MotionEventCompat.getActionIndex(ev);
			} else {
				skipIndex = -1;
			}
			float sumX = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			float sumY = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			int count = MotionEventCompat.getPointerCount(ev);
			int i = LONGPRESS_TIMEOUT;
			while (i < count) {
				if (skipIndex == i) {
					i++;
				} else {
					sumX += MotionEventCompat.getX(ev, i);
					sumY += MotionEventCompat.getY(ev, i);
					i++;
				}
			}
			if (pointerUp) {
				div = count - 1;
			} else {
				div = count;
			}
			float focusX = sumX / ((float) div);
			float focusY = sumY / ((float) div);
			boolean handled = false;
			switch((action & 255)) {
			case LONGPRESS_TIMEOUT:
				if (mDoubleTapListener != null) {
					boolean hadTapMessage = mHandler.hasMessages(TAP);
					if (hadTapMessage) {
						mHandler.removeMessages(TAP);
					}
					if (mCurrentDownEvent == null || mPreviousUpEvent == null || !hadTapMessage || !isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, ev)) {
						mHandler.sendEmptyMessageDelayed(TAP, (long) DOUBLE_TAP_TIMEOUT);
					} else {
						mIsDoubleTapping = true;
						handled = (handled | mDoubleTapListener.onDoubleTap(mCurrentDownEvent)) | mDoubleTapListener.onDoubleTapEvent(ev);
					}
				}
				mLastFocusX = focusX;
				mDownFocusX = focusX;
				mLastFocusY = focusY;
				mDownFocusY = focusY;
				if (mCurrentDownEvent != null) {
					mCurrentDownEvent.recycle();
				}
				mCurrentDownEvent = MotionEvent.obtain(ev);
				mAlwaysInTapRegion = true;
				mAlwaysInBiggerTapRegion = true;
				mStillDown = true;
				mInLongPress = false;
				mDeferConfirmSingleTap = false;
				if (mIsLongpressEnabled) {
					mHandler.removeMessages(LONG_PRESS);
					mHandler.sendEmptyMessageAtTime(LONG_PRESS, (mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT)) + ((long) LONGPRESS_TIMEOUT));
				}
				mHandler.sendEmptyMessageAtTime(SHOW_PRESS, mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT));
				return handled | mListener.onDown(ev);
			case SHOW_PRESS:
				mStillDown = false;
				MotionEvent currentUpEvent = MotionEvent.obtain(ev);
				if (mIsDoubleTapping) {
					handled |= mDoubleTapListener.onDoubleTapEvent(ev);
				} else if (mInLongPress) {
					mHandler.removeMessages(TAP);
					mInLongPress = false;
				} else if (mAlwaysInTapRegion) {
					handled = mListener.onSingleTapUp(ev);
					if (mDeferConfirmSingleTap) {
						if (mDoubleTapListener != null) {
							mDoubleTapListener.onSingleTapConfirmed(ev);
						}
					}
				} else {
					VelocityTracker velocityTracker = mVelocityTracker;
					int pointerId = MotionEventCompat.getPointerId(ev, 0);
					velocityTracker.computeCurrentVelocity(1000, (float) mMaximumFlingVelocity);
					float velocityY = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);
					float velocityX = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
					if (Math.abs(velocityY) > ((float) mMinimumFlingVelocity) || Math.abs(velocityX) > ((float) mMinimumFlingVelocity)) {
						handled = mListener.onFling(mCurrentDownEvent, ev, velocityX, velocityY);
					}
				}
				if (mPreviousUpEvent != null) {
					mPreviousUpEvent.recycle();
				}
				mPreviousUpEvent = currentUpEvent;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				mIsDoubleTapping = false;
				mDeferConfirmSingleTap = false;
				mHandler.removeMessages(SHOW_PRESS);
				mHandler.removeMessages(LONG_PRESS);
				return handled;
			case LONG_PRESS:
				if (!mInLongPress) {
					float scrollX = mLastFocusX - focusX;
					float scrollY = mLastFocusY - focusY;
					if (mIsDoubleTapping) {
						return handled | mDoubleTapListener.onDoubleTapEvent(ev);
					} else if (mAlwaysInTapRegion) {
						int deltaX = (int) (focusX - mDownFocusX);
						int deltaY = (int) (focusY - mDownFocusY);
						int distance = (deltaX * deltaX) + (deltaY * deltaY);
						if (distance > mTouchSlopSquare) {
							handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
							mLastFocusX = focusX;
							mLastFocusY = focusY;
							mAlwaysInTapRegion = false;
							mHandler.removeMessages(TAP);
							mHandler.removeMessages(SHOW_PRESS);
							mHandler.removeMessages(LONG_PRESS);
						}
						if (distance > mTouchSlopSquare) {
							mAlwaysInBiggerTapRegion = false;
							return handled;
						} else {
							return handled;
						}
					} else if (Math.abs(scrollX) >= 1.0f || Math.abs(scrollY) >= 1.0f) {
						mLastFocusX = focusX;
						mLastFocusY = focusY;
						return mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
					} else {
						return false;
					}
				} else {
					return false;
				}
			case TAP:
				cancel();
				return false;
			case WearableExtender.SIZE_FULL_SCREEN:
				mLastFocusX = focusX;
				mDownFocusX = focusX;
				mLastFocusY = focusY;
				mDownFocusY = focusY;
				cancelTaps();
				return false;
			case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
				mLastFocusX = focusX;
				mDownFocusX = focusX;
				mLastFocusY = focusY;
				mDownFocusY = focusY;
				mVelocityTracker.computeCurrentVelocity(DeviceManager.REQUEST_CODE_ENABLE_ADMIN, (float) mMaximumFlingVelocity);
				int upIndex = MotionEventCompat.getActionIndex(ev);
				int id1 = MotionEventCompat.getPointerId(ev, upIndex);
				float x1 = VelocityTrackerCompat.getXVelocity(mVelocityTracker, id1);
				float y1 = VelocityTrackerCompat.getYVelocity(mVelocityTracker, id1);
				i = LONGPRESS_TIMEOUT;
				while (i < count) {
					if (i == upIndex) {
						i++;
					} else {
						int id2 = MotionEventCompat.getPointerId(ev, i);
						if ((x1 * VelocityTrackerCompat.getXVelocity(mVelocityTracker, id2)) + (y1 * VelocityTrackerCompat.getYVelocity(mVelocityTracker, id2)) < 0.0f) {
							mVelocityTracker.clear();
							return false;
						}
						i++;
					}
				}
				return false;
			}
			return false;
		}

		public void setIsLongpressEnabled(boolean isLongpressEnabled) {
			mIsLongpressEnabled = isLongpressEnabled;
		}

		public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
			mDoubleTapListener = onDoubleTapListener;
		}
	}

	static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompat.GestureDetectorCompatImpl {
		private final GestureDetector mDetector;

		public GestureDetectorCompatImplJellybeanMr2(Context context, OnGestureListener listener, Handler handler) {
			super();
			mDetector = new GestureDetector(context, listener, handler);
		}

		public boolean isLongpressEnabled() {
			return mDetector.isLongpressEnabled();
		}

		public boolean onTouchEvent(MotionEvent ev) {
			return mDetector.onTouchEvent(ev);
		}

		public void setIsLongpressEnabled(boolean enabled) {
			mDetector.setIsLongpressEnabled(enabled);
		}

		public void setOnDoubleTapListener(OnDoubleTapListener listener) {
			mDetector.setOnDoubleTapListener(listener);
		}
	}


	public GestureDetectorCompat(Context context, OnGestureListener listener) {
		this(context, listener, null);
	}

	public GestureDetectorCompat(Context context, OnGestureListener listener, Handler handler) {
		super();
		if (VERSION.SDK_INT > 17) {
			mImpl = new GestureDetectorCompatImplJellybeanMr2(context, listener, handler);
		} else {
			mImpl = new GestureDetectorCompatImplBase(context, listener, handler);
		}
	}

	public boolean isLongpressEnabled() {
		return mImpl.isLongpressEnabled();
	}

	public boolean onTouchEvent(MotionEvent event) {
		return mImpl.onTouchEvent(event);
	}

	public void setIsLongpressEnabled(boolean enabled) {
		mImpl.setIsLongpressEnabled(enabled);
	}

	public void setOnDoubleTapListener(OnDoubleTapListener listener) {
		mImpl.setOnDoubleTapListener(listener);
	}
}
