package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

public class AccessibilityManagerCompat {
	private static final AccessibilityManagerVersionImpl IMPL;

	static interface AccessibilityManagerVersionImpl {
		public boolean addAccessibilityStateChangeListener(AccessibilityManager r1_AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat r2_AccessibilityManagerCompat_AccessibilityStateChangeListenerCompat);

		public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager r1_AccessibilityManager, int r2i);

		public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager r1_AccessibilityManager);

		public boolean isTouchExplorationEnabled(AccessibilityManager r1_AccessibilityManager);

		public Object newAccessiblityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat r1_AccessibilityManagerCompat_AccessibilityStateChangeListenerCompat);

		public boolean removeAccessibilityStateChangeListener(AccessibilityManager r1_AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat r2_AccessibilityManagerCompat_AccessibilityStateChangeListenerCompat);
	}

	public static abstract class AccessibilityStateChangeListenerCompat {
		final Object mListener;

		public AccessibilityStateChangeListenerCompat() {
			super();
			mListener = IMPL.newAccessiblityStateChangeListener(this);
		}

		public abstract void onAccessibilityStateChanged(boolean r1z);
	}

	static class AccessibilityManagerStubImpl implements AccessibilityManagerCompat.AccessibilityManagerVersionImpl {
		AccessibilityManagerStubImpl() {
			super();
		}

		public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return false;
		}

		public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
			return Collections.emptyList();
		}

		public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
			return Collections.emptyList();
		}

		public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
			return false;
		}

		public Object newAccessiblityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return null;
		}

		public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return false;
		}
	}

	static class AccessibilityManagerIcsImpl extends AccessibilityManagerCompat.AccessibilityManagerStubImpl {
		class AnonymousClass_1 implements AccessibilityStateChangeListenerBridge {
			final /* synthetic */ AccessibilityManagerCompat.AccessibilityManagerIcsImpl this$0;
			final /* synthetic */ AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat val$listener;

			AnonymousClass_1(AccessibilityManagerCompat.AccessibilityManagerIcsImpl r1_AccessibilityManagerCompat_AccessibilityManagerIcsImpl, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat r2_AccessibilityManagerCompat_AccessibilityStateChangeListenerCompat) {
				super();
				this$0 = r1_AccessibilityManagerCompat_AccessibilityManagerIcsImpl;
				val$listener = r2_AccessibilityManagerCompat_AccessibilityStateChangeListenerCompat;
			}

			public void onAccessibilityStateChanged(boolean enabled) {
				val$listener.onAccessibilityStateChanged(enabled);
			}
		}


		AccessibilityManagerIcsImpl() {
			super();
		}

		public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(manager, listener.mListener);
		}

		public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
			return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
		}

		public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
			return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(manager);
		}

		public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
			return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(manager);
		}

		public Object newAccessiblityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return AccessibilityManagerCompatIcs.newAccessibilityStateChangeListener(new AnonymousClass_1(this, listener));
		}

		public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
			return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(manager, listener.mListener);
		}
	}


	static {
		if (VERSION.SDK_INT >= 14) {
			IMPL = new AccessibilityManagerIcsImpl();
		} else {
			IMPL = new AccessibilityManagerStubImpl();
		}
	}

	public AccessibilityManagerCompat() {
		super();
	}

	public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerCompat listener) {
		return IMPL.addAccessibilityStateChangeListener(manager, listener);
	}

	public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
		return IMPL.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
	}

	public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
		return IMPL.getInstalledAccessibilityServiceList(manager);
	}

	public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
		return IMPL.isTouchExplorationEnabled(manager);
	}

	public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerCompat listener) {
		return IMPL.removeAccessibilityStateChangeListener(manager, listener);
	}
}
