package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollerCompat {
	static final int CHASE_FRAME_TIME = 16;
	private static final String TAG = "ScrollerCompat";
	ScrollerCompatImpl mImpl;
	Object mScroller;

	static interface ScrollerCompatImpl {
		public void abortAnimation(Object r1_Object);

		public boolean computeScrollOffset(Object r1_Object);

		public Object createScroller(Context r1_Context, Interpolator r2_Interpolator);

		public void fling(Object r1_Object, int r2i, int r3i, int r4i, int r5i, int r6i, int r7i, int r8i, int r9i);

		public void fling(Object r1_Object, int r2i, int r3i, int r4i, int r5i, int r6i, int r7i, int r8i, int r9i, int r10i, int r11i);

		public float getCurrVelocity(Object r1_Object);

		public int getCurrX(Object r1_Object);

		public int getCurrY(Object r1_Object);

		public int getFinalX(Object r1_Object);

		public int getFinalY(Object r1_Object);

		public boolean isFinished(Object r1_Object);

		public boolean isOverScrolled(Object r1_Object);

		public void notifyHorizontalEdgeReached(Object r1_Object, int r2i, int r3i, int r4i);

		public void notifyVerticalEdgeReached(Object r1_Object, int r2i, int r3i, int r4i);

		public void startScroll(Object r1_Object, int r2i, int r3i, int r4i, int r5i);

		public void startScroll(Object r1_Object, int r2i, int r3i, int r4i, int r5i, int r6i);
	}

	static class ScrollerCompatImplBase implements ScrollerCompat.ScrollerCompatImpl {
		ScrollerCompatImplBase() {
			super();
		}

		public void abortAnimation(Object scroller) {
			((Scroller) scroller).abortAnimation();
		}

		public boolean computeScrollOffset(Object scroller) {
			return ((Scroller) scroller).computeScrollOffset();
		}

		public Object createScroller(Context context, Interpolator interpolator) {
			if (interpolator != null) {
				return new Scroller(context, interpolator);
			} else {
				return new Scroller(context);
			}
		}

		public void fling(Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY) {
			((Scroller) scroller).fling(startX, startY, velX, velY, minX, maxX, minY, maxY);
		}

		public void fling(Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
			((Scroller) scroller).fling(startX, startY, velX, velY, minX, maxX, minY, maxY);
		}

		public float getCurrVelocity(Object scroller) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public int getCurrX(Object scroller) {
			return ((Scroller) scroller).getCurrX();
		}

		public int getCurrY(Object scroller) {
			return ((Scroller) scroller).getCurrY();
		}

		public int getFinalX(Object scroller) {
			return ((Scroller) scroller).getFinalX();
		}

		public int getFinalY(Object scroller) {
			return ((Scroller) scroller).getFinalY();
		}

		public boolean isFinished(Object scroller) {
			return ((Scroller) scroller).isFinished();
		}

		public boolean isOverScrolled(Object scroller) {
			return false;
		}

		public void notifyHorizontalEdgeReached(Object scroller, int startX, int finalX, int overX) {
		}

		public void notifyVerticalEdgeReached(Object scroller, int startY, int finalY, int overY) {
		}

		public void startScroll(Object scroller, int startX, int startY, int dx, int dy) {
			((Scroller) scroller).startScroll(startX, startY, dx, dy);
		}

		public void startScroll(Object scroller, int startX, int startY, int dx, int dy, int duration) {
			((Scroller) scroller).startScroll(startX, startY, dx, dy, duration);
		}
	}

	static class ScrollerCompatImplGingerbread implements ScrollerCompat.ScrollerCompatImpl {
		ScrollerCompatImplGingerbread() {
			super();
		}

		public void abortAnimation(Object scroller) {
			ScrollerCompatGingerbread.abortAnimation(scroller);
		}

		public boolean computeScrollOffset(Object scroller) {
			return ScrollerCompatGingerbread.computeScrollOffset(scroller);
		}

		public Object createScroller(Context context, Interpolator interpolator) {
			return ScrollerCompatGingerbread.createScroller(context, interpolator);
		}

		public void fling(Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY) {
			ScrollerCompatGingerbread.fling(scroller, startX, startY, velX, velY, minX, maxX, minY, maxY);
		}

		public void fling(Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
			ScrollerCompatGingerbread.fling(scroller, startX, startY, velX, velY, minX, maxX, minY, maxY, overX, overY);
		}

		public float getCurrVelocity(Object scroller) {
			return AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}

		public int getCurrX(Object scroller) {
			return ScrollerCompatGingerbread.getCurrX(scroller);
		}

		public int getCurrY(Object scroller) {
			return ScrollerCompatGingerbread.getCurrY(scroller);
		}

		public int getFinalX(Object scroller) {
			return ScrollerCompatGingerbread.getFinalX(scroller);
		}

		public int getFinalY(Object scroller) {
			return ScrollerCompatGingerbread.getFinalY(scroller);
		}

		public boolean isFinished(Object scroller) {
			return ScrollerCompatGingerbread.isFinished(scroller);
		}

		public boolean isOverScrolled(Object scroller) {
			return ScrollerCompatGingerbread.isOverScrolled(scroller);
		}

		public void notifyHorizontalEdgeReached(Object scroller, int startX, int finalX, int overX) {
			ScrollerCompatGingerbread.notifyHorizontalEdgeReached(scroller, startX, finalX, overX);
		}

		public void notifyVerticalEdgeReached(Object scroller, int startY, int finalY, int overY) {
			ScrollerCompatGingerbread.notifyVerticalEdgeReached(scroller, startY, finalY, overY);
		}

		public void startScroll(Object scroller, int startX, int startY, int dx, int dy) {
			ScrollerCompatGingerbread.startScroll(scroller, startX, startY, dx, dy);
		}

		public void startScroll(Object scroller, int startX, int startY, int dx, int dy, int duration) {
			ScrollerCompatGingerbread.startScroll(scroller, startX, startY, dx, dy, duration);
		}
	}

	static class ScrollerCompatImplIcs extends ScrollerCompat.ScrollerCompatImplGingerbread {
		ScrollerCompatImplIcs() {
			super();
		}

		public float getCurrVelocity(Object scroller) {
			return ScrollerCompatIcs.getCurrVelocity(scroller);
		}
	}


	private ScrollerCompat(int apiVersion, Context context, Interpolator interpolator) {
		super();
		if (apiVersion >= 14) {
			mImpl = new ScrollerCompatImplIcs();
		} else if (apiVersion >= 9) {
			mImpl = new ScrollerCompatImplGingerbread();
		} else {
			mImpl = new ScrollerCompatImplBase();
		}
		mScroller = mImpl.createScroller(context, interpolator);
	}

	ScrollerCompat(Context context, Interpolator interpolator) {
		this(VERSION.SDK_INT, context, interpolator);
	}

	public static ScrollerCompat create(Context context) {
		return create(context, null);
	}

	public static ScrollerCompat create(Context context, Interpolator interpolator) {
		return new ScrollerCompat(context, interpolator);
	}

	public void abortAnimation() {
		mImpl.abortAnimation(mScroller);
	}

	public boolean computeScrollOffset() {
		return mImpl.computeScrollOffset(mScroller);
	}

	public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
		mImpl.fling(mScroller, startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
	}

	public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
		mImpl.fling(mScroller, startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
	}

	public float getCurrVelocity() {
		return mImpl.getCurrVelocity(mScroller);
	}

	public int getCurrX() {
		return mImpl.getCurrX(mScroller);
	}

	public int getCurrY() {
		return mImpl.getCurrY(mScroller);
	}

	public int getFinalX() {
		return mImpl.getFinalX(mScroller);
	}

	public int getFinalY() {
		return mImpl.getFinalY(mScroller);
	}

	public boolean isFinished() {
		return mImpl.isFinished(mScroller);
	}

	public boolean isOverScrolled() {
		return mImpl.isOverScrolled(mScroller);
	}

	public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
		mImpl.notifyHorizontalEdgeReached(mScroller, startX, finalX, overX);
	}

	public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
		mImpl.notifyVerticalEdgeReached(mScroller, startY, finalY, overY);
	}

	public void startScroll(int startX, int startY, int dx, int dy) {
		mImpl.startScroll(mScroller, startX, startY, dx, dy);
	}

	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		mImpl.startScroll(mScroller, startX, startY, dx, dy, duration);
	}
}
