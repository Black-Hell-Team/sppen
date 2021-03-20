package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ViewGroupCompat {
	static final ViewGroupCompatImpl IMPL;
	public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
	public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

	static interface ViewGroupCompatImpl {
		public int getLayoutMode(ViewGroup r1_ViewGroup);

		public boolean onRequestSendAccessibilityEvent(ViewGroup r1_ViewGroup, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public void setLayoutMode(ViewGroup r1_ViewGroup, int r2i);

		public void setMotionEventSplittingEnabled(ViewGroup r1_ViewGroup, boolean r2z);
	}

	static class ViewGroupCompatStubImpl implements ViewGroupCompat.ViewGroupCompatImpl {
		ViewGroupCompatStubImpl() {
			super();
		}

		public int getLayoutMode(ViewGroup group) {
			return LAYOUT_MODE_CLIP_BOUNDS;
		}

		public boolean onRequestSendAccessibilityEvent(ViewGroup group, View child, AccessibilityEvent event) {
			return true;
		}

		public void setLayoutMode(ViewGroup group, int mode) {
		}

		public void setMotionEventSplittingEnabled(ViewGroup group, boolean split) {
		}
	}

	static class ViewGroupCompatHCImpl extends ViewGroupCompat.ViewGroupCompatStubImpl {
		ViewGroupCompatHCImpl() {
			super();
		}

		public void setMotionEventSplittingEnabled(ViewGroup group, boolean split) {
			ViewGroupCompatHC.setMotionEventSplittingEnabled(group, split);
		}
	}

	static class ViewGroupCompatIcsImpl extends ViewGroupCompat.ViewGroupCompatHCImpl {
		ViewGroupCompatIcsImpl() {
			super();
		}

		public boolean onRequestSendAccessibilityEvent(ViewGroup group, View child, AccessibilityEvent event) {
			return ViewGroupCompatIcs.onRequestSendAccessibilityEvent(group, child, event);
		}
	}

	static class ViewGroupCompatJellybeanMR2Impl extends ViewGroupCompat.ViewGroupCompatIcsImpl {
		ViewGroupCompatJellybeanMR2Impl() {
			super();
		}

		public int getLayoutMode(ViewGroup group) {
			return ViewGroupCompatJellybeanMR2.getLayoutMode(group);
		}

		public void setLayoutMode(ViewGroup group, int mode) {
			ViewGroupCompatJellybeanMR2.setLayoutMode(group, mode);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 18) {
			IMPL = new ViewGroupCompatJellybeanMR2Impl();
		} else if (version >= 14) {
			IMPL = new ViewGroupCompatIcsImpl();
		} else if (version >= 11) {
			IMPL = new ViewGroupCompatHCImpl();
		} else {
			IMPL = new ViewGroupCompatStubImpl();
		}
	}

	private ViewGroupCompat() {
		super();
	}

	public static int getLayoutMode(ViewGroup group) {
		return IMPL.getLayoutMode(group);
	}

	public static boolean onRequestSendAccessibilityEvent(ViewGroup group, View child, AccessibilityEvent event) {
		return IMPL.onRequestSendAccessibilityEvent(group, child, event);
	}

	public static void setLayoutMode(ViewGroup group, int mode) {
		IMPL.setLayoutMode(group, mode);
	}

	public static void setMotionEventSplittingEnabled(ViewGroup group, boolean split) {
		IMPL.setMotionEventSplittingEnabled(group, split);
	}
}
