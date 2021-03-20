package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

class AccessibilityNodeProviderCompatKitKat {
	static class AnonymousClass_1 extends AccessibilityNodeProvider {
		final /* synthetic */ AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge val$bridge;

		AnonymousClass_1(AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge r1_AccessibilityNodeProviderCompatKitKat_AccessibilityNodeInfoBridge) {
			super();
			val$bridge = r1_AccessibilityNodeProviderCompatKitKat_AccessibilityNodeInfoBridge;
		}

		public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
			return (AccessibilityNodeInfo) val$bridge.createAccessibilityNodeInfo(virtualViewId);
		}

		public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
			return val$bridge.findAccessibilityNodeInfosByText(text, virtualViewId);
		}

		public AccessibilityNodeInfo findFocus(int focus) {
			return (AccessibilityNodeInfo) val$bridge.findFocus(focus);
		}

		public boolean performAction(int virtualViewId, int action, Bundle arguments) {
			return val$bridge.performAction(virtualViewId, action, arguments);
		}
	}

	static interface AccessibilityNodeInfoBridge {
		public Object createAccessibilityNodeInfo(int r1i);

		public List<Object> findAccessibilityNodeInfosByText(String r1_String, int r2i);

		public Object findFocus(int r1i);

		public boolean performAction(int r1i, int r2i, Bundle r3_Bundle);
	}


	AccessibilityNodeProviderCompatKitKat() {
		super();
	}

	public static Object newAccessibilityNodeProviderBridge(AccessibilityNodeInfoBridge bridge) {
		return new AnonymousClass_1(bridge);
	}
}
