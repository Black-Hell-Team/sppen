package android.support.v4.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

class AccessibilityDelegateCompatJellyBean {
	static class AnonymousClass_1 extends AccessibilityDelegate {
		final /* synthetic */ AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean val$bridge;

		AnonymousClass_1(AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean r1_AccessibilityDelegateCompatJellyBean_AccessibilityDelegateBridgeJellyBean) {
			super();
			val$bridge = r1_AccessibilityDelegateCompatJellyBean_AccessibilityDelegateBridgeJellyBean;
		}

		public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
			return val$bridge.dispatchPopulateAccessibilityEvent(host, event);
		}

		public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
			return (AccessibilityNodeProvider) val$bridge.getAccessibilityNodeProvider(host);
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

		public boolean performAccessibilityAction(View host, int action, Bundle args) {
			return val$bridge.performAccessibilityAction(host, action, args);
		}

		public void sendAccessibilityEvent(View host, int eventType) {
			val$bridge.sendAccessibilityEvent(host, eventType);
		}

		public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
			val$bridge.sendAccessibilityEventUnchecked(host, event);
		}
	}

	public static interface AccessibilityDelegateBridgeJellyBean {
		public boolean dispatchPopulateAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public Object getAccessibilityNodeProvider(View r1_View);

		public void onInitializeAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public void onInitializeAccessibilityNodeInfo(View r1_View, Object r2_Object);

		public void onPopulateAccessibilityEvent(View r1_View, AccessibilityEvent r2_AccessibilityEvent);

		public boolean onRequestSendAccessibilityEvent(ViewGroup r1_ViewGroup, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public boolean performAccessibilityAction(View r1_View, int r2i, Bundle r3_Bundle);

		public void sendAccessibilityEvent(View r1_View, int r2i);

		public void sendAccessibilityEventUnchecked(View r1_View, AccessibilityEvent r2_AccessibilityEvent);
	}


	AccessibilityDelegateCompatJellyBean() {
		super();
	}

	public static Object getAccessibilityNodeProvider(Object delegate, View host) {
		return ((AccessibilityDelegate) delegate).getAccessibilityNodeProvider(host);
	}

	public static Object newAccessibilityDelegateBridge(AccessibilityDelegateBridgeJellyBean bridge) {
		return new AnonymousClass_1(bridge);
	}

	public static boolean performAccessibilityAction(Object delegate, View host, int action, Bundle args) {
		return ((AccessibilityDelegate) delegate).performAccessibilityAction(host, action, args);
	}
}
