package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import java.util.List;

class AccessibilityManagerCompatIcs {
	static class AnonymousClass_1 implements AccessibilityStateChangeListener {
		final /* synthetic */ AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge val$bridge;

		AnonymousClass_1(AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge r1_AccessibilityManagerCompatIcs_AccessibilityStateChangeListenerBridge) {
			super();
			val$bridge = r1_AccessibilityManagerCompatIcs_AccessibilityStateChangeListenerBridge;
		}

		public void onAccessibilityStateChanged(boolean enabled) {
			val$bridge.onAccessibilityStateChanged(enabled);
		}
	}

	static interface AccessibilityStateChangeListenerBridge {
		public void onAccessibilityStateChanged(boolean r1z);
	}


	AccessibilityManagerCompatIcs() {
		super();
	}

	public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, Object listener) {
		return manager.addAccessibilityStateChangeListener((AccessibilityStateChangeListener) listener);
	}

	public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
		return manager.getEnabledAccessibilityServiceList(feedbackTypeFlags);
	}

	public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
		return manager.getInstalledAccessibilityServiceList();
	}

	public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
		return manager.isTouchExplorationEnabled();
	}

	public static Object newAccessibilityStateChangeListener(AccessibilityStateChangeListenerBridge bridge) {
		return new AnonymousClass_1(bridge);
	}

	public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, Object listener) {
		return manager.removeAccessibilityStateChangeListener((AccessibilityStateChangeListener) listener);
	}
}
