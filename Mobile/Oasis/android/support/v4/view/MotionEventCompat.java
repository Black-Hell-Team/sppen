package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public class MotionEventCompat {
	public static final int ACTION_HOVER_ENTER = 9;
	public static final int ACTION_HOVER_EXIT = 10;
	public static final int ACTION_HOVER_MOVE = 7;
	public static final int ACTION_MASK = 255;
	public static final int ACTION_POINTER_DOWN = 5;
	public static final int ACTION_POINTER_INDEX_MASK = 65280;
	public static final int ACTION_POINTER_INDEX_SHIFT = 8;
	public static final int ACTION_POINTER_UP = 6;
	public static final int ACTION_SCROLL = 8;
	static final MotionEventVersionImpl IMPL;

	static interface MotionEventVersionImpl {
		public int findPointerIndex(MotionEvent r1_MotionEvent, int r2i);

		public int getPointerCount(MotionEvent r1_MotionEvent);

		public int getPointerId(MotionEvent r1_MotionEvent, int r2i);

		public float getX(MotionEvent r1_MotionEvent, int r2i);

		public float getY(MotionEvent r1_MotionEvent, int r2i);
	}

	static class BaseMotionEventVersionImpl implements MotionEventCompat.MotionEventVersionImpl {
		BaseMotionEventVersionImpl() {
			super();
		}

		public int findPointerIndex(MotionEvent event, int pointerId) {
			if (pointerId == 0) {
				return 0;
			} else {
				return -1;
			}
		}

		public int getPointerCount(MotionEvent event) {
			return 1;
		}

		public int getPointerId(MotionEvent event, int pointerIndex) {
			if (pointerIndex == 0) {
				return 0;
			} else {
				throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
			}
		}

		public float getX(MotionEvent event, int pointerIndex) {
			if (pointerIndex == 0) {
				return event.getX();
			} else {
				throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
			}
		}

		public float getY(MotionEvent event, int pointerIndex) {
			if (pointerIndex == 0) {
				return event.getY();
			} else {
				throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
			}
		}
	}

	static class EclairMotionEventVersionImpl implements MotionEventCompat.MotionEventVersionImpl {
		EclairMotionEventVersionImpl() {
			super();
		}

		public int findPointerIndex(MotionEvent event, int pointerId) {
			return MotionEventCompatEclair.findPointerIndex(event, pointerId);
		}

		public int getPointerCount(MotionEvent event) {
			return MotionEventCompatEclair.getPointerCount(event);
		}

		public int getPointerId(MotionEvent event, int pointerIndex) {
			return MotionEventCompatEclair.getPointerId(event, pointerIndex);
		}

		public float getX(MotionEvent event, int pointerIndex) {
			return MotionEventCompatEclair.getX(event, pointerIndex);
		}

		public float getY(MotionEvent event, int pointerIndex) {
			return MotionEventCompatEclair.getY(event, pointerIndex);
		}
	}


	static {
		if (VERSION.SDK_INT >= 5) {
			IMPL = new EclairMotionEventVersionImpl();
		} else {
			IMPL = new BaseMotionEventVersionImpl();
		}
	}

	public MotionEventCompat() {
		super();
	}

	public static int findPointerIndex(MotionEvent event, int pointerId) {
		return IMPL.findPointerIndex(event, pointerId);
	}

	public static int getActionIndex(MotionEvent event) {
		return (event.getAction() & 65280) >> 8;
	}

	public static int getActionMasked(MotionEvent event) {
		return event.getAction() & 255;
	}

	public static int getPointerCount(MotionEvent event) {
		return IMPL.getPointerCount(event);
	}

	public static int getPointerId(MotionEvent event, int pointerIndex) {
		return IMPL.getPointerId(event, pointerIndex);
	}

	public static float getX(MotionEvent event, int pointerIndex) {
		return IMPL.getX(event, pointerIndex);
	}

	public static float getY(MotionEvent event, int pointerIndex) {
		return IMPL.getY(event, pointerIndex);
	}
}
