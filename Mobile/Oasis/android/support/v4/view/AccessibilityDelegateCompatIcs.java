package android.support.v4.view;

import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityDelegateCompatIcs {
	static class AnonymousClass_1 extends AccessibilityDelegate {
		final /* synthetic */ AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge val$bridge;

		AnonymousClass_1(AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge r1_AccessibilityDelegateCompatIcs_AccessibilityDelegateBridge) {
			super();
			val$bridge = r1_AccessibilityDelegateCompatIcs_AccessibilityDelegateBridge;
		}

		public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
			return val$bridge.dispatchPopulateAccessibilityEvent(host, event);
		}

		public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
			val$bridge.onInitializeAccessibilityEvent(host, event);
		}

		public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
			val$bridge.onInitializeAccessibilityNodeInfo(host, info);
		}

		public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
			val$bridge.onPopulateAccessibilityEvent(host, event);
		}

		public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
			return val$bridge.onRequestSendAccessibilityEvent(host, child, event);
		}

		public void sendAccessibilityEvent(View host, int eventType) {
			val$bridge.sendAccessibilityEvent(host, eventType);
		}

		public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
			val$bridge.sendAccessibilityEventUnchecked(host, event);
		}
	}

	public static interface AccessibilityDelegateBridge {
		public boolean dispatchPopulateAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public void onInitializeAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public void onInitializeAccessibilityNodeInfo(View r1_View, Object r2_Object);

		public void onPopulateAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public boolean onRequestSendAccessibilityEvent(ViewGroup r1_ViewGroup, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public void sendAccessibilityEvent(View r1_View, int r2i);

		public void sendAccessibilityEventUnchecked(View r1_View, AccessibilityEvent r2_AccessibilityEvent);
	}


	AccessibilityDelegateCompatIcs() {
		super();
	}

	public static boolean dispatchPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
		return ((AccessibilityDelegate) delegate).dispatchPopulateAccessibilityEvent(host, event);
	}

	public static Object newAccessibilityDelegateBridge(AccessibilityDelegateBridge bridge) {
		return new AnonymousClass_1(bridge);
	}

	public static Object newAccessibilityDelegateDefaultImpl() {
		return new AccessibilityDelegate();
	}

	public static void onInitializeAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
		((AccessibilityDelegate) delegate).onInitializeAccessibilityEvent(host, event);
	}

	public static void onInitializeAccessibilityNodeInfo(Object delegate, View host, Object info) {
		((AccessibilityDelegate) delegate).onInitializeAccessibilityNodeInfo(host, (AccessibilityNodeInfo) info);
	}

	public static void onPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
		((AccessibilityDelegate) delegate).onPopulateAccessibilityEvent(host, event);
	}

	public static boolean onRequestSendAccessibilityEvent(Object delegate, ViewGroup host, View child, AccessibilityEvent event) {
		return ((AccessibilityDelegate) delegate).onRequestSendAccessibilityEvent(host, child, event);
	}

	public static void sendAccessibilityEvent(Object delegate, View host, int eventType) {
		((AccessibilityDelegate) delegate).sendAccessibilityEvent(host, eventType);
	}

	public static void sendAccessibilityEventUnchecked(Object delegate, View host, AccessibilityEvent event) {
		((AccessibilityDelegate) delegate).sendAccessibilityEventUnchecked(host, event);
	}
}
