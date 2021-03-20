package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class ViewParentCompat {
	static final ViewParentCompatImpl IMPL;

	static interface ViewParentCompatImpl {
		public boolean requestSendAccessibilityEvent(ViewParent r1_ViewParent, View r2_View, AccessibilityEvent r3_AccessibilityEvent);
	}

	static class ViewParentCompatStubImpl implements ViewParentCompat.ViewParentCompatImpl {
		ViewParentCompatStubImpl() {
			super();
		}

		public boolean requestSendAccessibilityEvent(ViewParent parent, View child, AccessibilityEvent event) {
			if (child == null) {
				return false;
			} else {
				((AccessibilityManager) child.getContext().getSystemService("accessibility")).sendAccessibilityEvent(event);
				return true;
			}
		}
	}

	static class ViewParentCompatICSImpl extends ViewParentCompat.ViewParentCompatStubImpl {
		ViewParentCompatICSImpl() {
			super();
		}

		public boolean requestSendAccessibilityEvent(ViewParent parent, View child, AccessibilityEvent event) {
			return ViewParentCompatICS.requestSendAccessibilityEvent(parent, child, event);
		}
	}


	static {
		if (VERSION.SDK_INT >= 14) {
			IMPL = new ViewParentCompatICSImpl();
		} else {
			IMPL = new ViewParentCompatStubImpl();
		}
	}

	private ViewParentCompat() {
		super();
	}

	public static boolean requestSendAccessibilityEvent(ViewParent parent, View child, AccessibilityEvent event) {
		return IMPL.requestSendAccessibilityEvent(parent, child, event);
	}
}
