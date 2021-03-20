package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.VelocityTracker;

public class VelocityTrackerCompat {
	static final VelocityTrackerVersionImpl IMPL;

	static interface VelocityTrackerVersionImpl {
		public float getXVelocity(VelocityTracker r1_VelocityTracker, int r2i);

		public float getYVelocity(VelocityTracker r1_VelocityTracker, int r2i);
	}

	static class BaseVelocityTrackerVersionImpl implements VelocityTrackerCompat.VelocityTrackerVersionImpl {
		BaseVelocityTrackerVersionImpl() {
			super();
		}

		public float getXVelocity(VelocityTracker tracker, int pointerId) {
			return tracker.getXVelocity();
		}

		public float getYVelocity(VelocityTracker tracker, int pointerId) {
			return tracker.getYVelocity();
		}
	}

	static class HoneycombVelocityTrackerVersionImpl implements VelocityTrackerCompat.VelocityTrackerVersionImpl {
		HoneycombVelocityTrackerVersionImpl() {
			super();
		}

		public float getXVelocity(VelocityTracker tracker, int pointerId) {
			return VelocityTrackerCompatHoneycomb.getXVelocity(tracker, pointerId);
		}

		public float getYVelocity(VelocityTracker tracker, int pointerId) {
			return VelocityTrackerCompatHoneycomb.getYVelocity(tracker, pointerId);
		}
	}


	static {
		if (VERSION.SDK_INT >= 11) {
			IMPL = new HoneycombVelocityTrackerVersionImpl();
		} else {
			IMPL = new BaseVelocityTrackerVersionImpl();
		}
	}

	public VelocityTrackerCompat() {
		super();
	}

	public static float getXVelocity(VelocityTracker tracker, int pointerId) {
		return IMPL.getXVelocity(tracker, pointerId);
	}

	public static float getYVelocity(VelocityTracker tracker, int pointerId) {
		return IMPL.getYVelocity(tracker, pointerId);
	}
}
