package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
	private static final AccessibilityNodeProviderImpl IMPL;
	private final Object mProvider;

	static interface AccessibilityNodeProviderImpl {
		public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat r1_AccessibilityNodeProviderCompat);
	}

	static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl {
		AccessibilityNodeProviderStubImpl() {
			super();
		}

		public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
			return null;
		}
	}

	static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
		class AnonymousClass_1 implements AccessibilityNodeInfoBridge {
			final /* synthetic */ AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl this$0;
			final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

			AnonymousClass_1(AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl r1_AccessibilityNodeProviderCompat_AccessibilityNodeProviderJellyBeanImpl, AccessibilityNodeProviderCompat r2_AccessibilityNodeProviderCompat) {
				super();
				this$0 = r1_AccessibilityNodeProviderCompat_AccessibilityNodeProviderJellyBeanImpl;
				val$compat = r2_AccessibilityNodeProviderCompat;
			}

			public Object createAccessibilityNodeInfo(int virtualViewId) {
				AccessibilityNodeInfoCompat compatInfo = val$compat.createAccessibilityNodeInfo(virtualViewId);
				if (compatInfo == null) {
					return null;
				} else {
					return compatInfo.getInfo();
				}
			}

			public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
				List<AccessibilityNodeInfoCompat> compatInfos = val$compat.findAccessibilityNodeInfosByText(text, virtualViewId);
				List<Object> infos = new ArrayList();
				int i = 0;
				while (i < compatInfos.size()) {
					infos.add(((AccessibilityNodeInfoCompat) compatInfos.get(i)).getInfo());
					i++;
				}
				return infos;
			}

			public boolean performAction(int virtualViewId, int action, Bundle arguments) {
				return val$compat.performAction(virtualViewId, action, arguments);
			}
		}


		AccessibilityNodeProviderJellyBeanImpl() {
			super();
		}

		public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
			return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AnonymousClass_1(this, compat));
		}
	}

	static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
		class AnonymousClass_1 implements AccessibilityNodeInfoBridge {
			final /* synthetic */ AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl this$0;
			final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

			AnonymousClass_1(AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl r1_AccessibilityNodeProviderCompat_AccessibilityNodeProviderKitKatImpl, AccessibilityNodeProviderCompat r2_AccessibilityNodeProviderCompat) {
				super();
				this$0 = r1_AccessibilityNodeProviderCompat_AccessibilityNodeProviderKitKatImpl;
				val$compat = r2_AccessibilityNodeProviderCompat;
			}

			public Object createAccessibilityNodeInfo(int virtualViewId) {
				AccessibilityNodeInfoCompat compatInfo = val$compat.createAccessibilityNodeInfo(virtualViewId);
				if (compatInfo == null) {
					return null;
				} else {
					return compatInfo.getInfo();
				}
			}

			public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
				List<AccessibilityNodeInfoCompat> compatInfos = val$compat.findAccessibilityNodeInfosByText(text, virtualViewId);
				List<Object> infos = new ArrayList();
				int i = 0;
				while (i < compatInfos.size()) {
					infos.add(((AccessibilityNodeInfoCompat) compatInfos.get(i)).getInfo());
					i++;
				}
				return infos;
			}

			public Object findFocus(int focus) {
				AccessibilityNodeInfoCompat compatInfo = val$compat.findFocus(focus);
				if (compatInfo == null) {
					return null;
				} else {
					return compatInfo.getInfo();
				}
			}

			public boolean performAction(int virtualViewId, int action, Bundle arguments) {
				return val$compat.performAction(virtualViewId, action, arguments);
			}
		}


		AccessibilityNodeProviderKitKatImpl() {
			super();
		}

		public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
			return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AnonymousClass_1(this, compat));
		}
	}


	static {
		if (VERSION.SDK_INT >= 19) {
			IMPL = new AccessibilityNodeProviderKitKatImpl();
		} else if (VERSION.SDK_INT >= 16) {
			IMPL = new AccessibilityNodeProviderJellyBeanImpl();
		} else {
			IMPL = new AccessibilityNodeProviderStubImpl();
		}
	}

	public AccessibilityNodeProviderCompat() {
		super();
		mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
	}

	public AccessibilityNodeProviderCompat(Object provider) {
		super();
		mProvider = provider;
	}

	public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
		return null;
	}

	public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
		return null;
	}

	public AccessibilityNodeInfoCompat findFocus(int focus) {
		return null;
	}

	public Object getProvider() {
		return mProvider;
	}

	public boolean performAction(int virtualViewId, int action, Bundle arguments) {
		return false;
	}
}
