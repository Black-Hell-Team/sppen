package android.support.v4.view;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge;
import android.support.v4.view.AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityDelegateCompat {
	private static final Object DEFAULT_DELEGATE;
	private static final AccessibilityDelegateImpl IMPL;
	final Object mBridge;

	static interface AccessibilityDelegateImpl {
		public boolean dispatchPopulateAccessibilityEvent(Object r1_Object, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object r1_Object, View r2_View);

		public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat r1_AccessibilityDelegateCompat);

		public Object newAccessiblityDelegateDefaultImpl();

		public void onInitializeAccessibilityEvent(Object r1_Object, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public void onInitializeAccessibilityNodeInfo(Object r1_Object, View r2_View, AccessibilityNodeInfoCompat r3_AccessibilityNodeInfoCompat);

		public void onPopulateAccessibilityEvent(Object r1_Object, View r2_View, AccessibilityEvent r3_AccessibilityEvent);

		public boolean onRequestSendAccessibilityEvent(Object r1_Object, ViewGroup r2_ViewGroup, View r3_View, AccessibilityEvent r4_AccessibilityEvent);

		public boolean performAccessibilityAction(Object r1_Object, View r2_View, int r3i, Bundle r4_Bundle);

		public void sendAccessibilityEvent(Object r1_Object, View r2_View, int r3i);

		public void sendAccessibilityEventUnchecked(Object r1_Object, View r2_View, AccessibilityEvent r3_AccessibilityEvent);
	}

	static class AccessibilityDelegateStubImpl implements AccessibilityDelegateCompat.AccessibilityDelegateImpl {
		AccessibilityDelegateStubImpl() {
			super();
		}

		public boolean dispatchPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
			return false;
		}

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object delegate, View host) {
			return null;
		}

		public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat listener) {
			return null;
		}

		public Object newAccessiblityDelegateDefaultImpl() {
			return null;
		}

		public void onInitializeAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
		}

		public void onInitializeAccessibilityNodeInfo(Object delegate, View host, AccessibilityNodeInfoCompat info) {
		}

		public void onPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
		}

		public boolean onRequestSendAccessibilityEvent(Object delegate, ViewGroup host, View child, AccessibilityEvent event) {
			return true;
		}

		public boolean performAccessibilityAction(Object delegate, View host, int action, Bundle args) {
			return false;
		}

		public void sendAccessibilityEvent(Object delegate, View host, int eventType) {
		}

		public void sendAccessibilityEventUnchecked(Object delegate, View host, AccessibilityEvent event) {
		}
	}

	static class AccessibilityDelegateIcsImpl extends AccessibilityDelegateCompat.AccessibilityDelegateStubImpl {
		class AnonymousClass_1 implements AccessibilityDelegateBridge {
			final /* synthetic */ AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl this$0;
			final /* synthetic */ AccessibilityDelegateCompat val$compat;

			AnonymousClass_1(AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl r1_AccessibilityDelegateCompat_AccessibilityDelegateIcsImpl, AccessibilityDelegateCompat r2_AccessibilityDelegateCompat) {
				super();
				this$0 = r1_AccessibilityDelegateCompat_AccessibilityDelegateIcsImpl;
				val$compat = r2_AccessibilityDelegateCompat;
			}

			public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
				return val$compat.dispatchPopulateAccessibilityEvent(host, event);
			}

			public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
				val$compat.onInitializeAccessibilityEvent(host, event);
			}

			public void onInitializeAccessibilityNodeInfo(View host, Object info) {
				val$compat.onInitializeAccessibilityNodeInfo(host, new AccessibilityNodeInfoCompat(info));
			}

			public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
				val$compat.onPopulateAccessibilityEvent(host, event);
			}

			public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
				return val$compat.onRequestSendAccessibilityEvent(host, child, event);
			}

			public void sendAccessibilityEvent(View host, int eventType) {
				val$compat.sendAccessibilityEvent(host, eventType);
			}

			public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
				val$compat.sendAccessibilityEventUnchecked(host, event);
			}
		}


		AccessibilityDelegateIcsImpl() {
			super();
		}

		public boolean dispatchPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
			return AccessibilityDelegateCompatIcs.dispatchPopulateAccessibilityEvent(delegate, host, event);
		}

		public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat compat) {
			return AccessibilityDelegateCompatIcs.newAccessibilityDelegateBridge(new AnonymousClass_1(this, compat));
		}

		public Object newAccessiblityDelegateDefaultImpl() {
			return AccessibilityDelegateCompatIcs.newAccessibilityDelegateDefaultImpl();
		}

		public void onInitializeAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
			AccessibilityDelegateCompatIcs.onInitializeAccessibilityEvent(delegate, host, event);
		}

		public void onInitializeAccessibilityNodeInfo(Object delegate, View host, AccessibilityNodeInfoCompat info) {
			AccessibilityDelegateCompatIcs.onInitializeAccessibilityNodeInfo(delegate, host, info.getInfo());
		}

		public void onPopulateAccessibilityEvent(Object delegate, View host, AccessibilityEvent event) {
			AccessibilityDelegateCompatIcs.onPopulateAccessibilityEvent(delegate, host, event);
		}

		public boolean onRequestSendAccessibilityEvent(Object delegate, ViewGroup host, View child, AccessibilityEvent event) {
			return AccessibilityDelegateCompatIcs.onRequestSendAccessibilityEvent(delegate, host, child, event);
		}

		public void sendAccessibilityEvent(Object delegate, View host, int eventType) {
			AccessibilityDelegateCompatIcs.sendAccessibilityEvent(delegate, host, eventType);
		}

		public void sendAccessibilityEventUnchecked(Object delegate, View host, AccessibilityEvent event) {
			AccessibilityDelegateCompatIcs.sendAccessibilityEventUnchecked(delegate, host, event);
		}
	}

	static class AccessibilityDelegateJellyBeanImpl extends AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl {
		class AnonymousClass_1 implements AccessibilityDelegateBridgeJellyBean {
			final /* synthetic */ AccessibilityDelegateCompat.AccessibilityDelegateJellyBeanImpl this$0;
			final /* synthetic */ AccessibilityDelegateCompat val$compat;

			AnonymousClass_1(AccessibilityDelegateCompat.AccessibilityDelegateJellyBeanImpl r1_AccessibilityDelegateCompat_AccessibilityDelegateJellyBeanImpl, AccessibilityDelegateCompat r2_AccessibilityDelegateCompat) {
				super();
				this$0 = r1_AccessibilityDelegateCompat_AccessibilityDelegateJellyBeanImpl;
				val$compat = r2_AccessibilityDelegateCompat;
			}

			public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
				return val$compat.dispatchPopulateAccessibilityEvent(host, event);
			}

			public Object getAccessibilityNodeProvider(View host) {
				AccessibilityNodeProviderCompat provider = val$compat.getAccessibilityNodeProvider(host);
				if (provider != null) {
					return provider.getProvider();
				} else {
					return null;
				}
			}

			public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
				val$compat.onInitializeAccessibilityEvent(host, event);
			}

			public void onInitializeAccessibilityNodeInfo(View host, Object info) {
				val$compat.onInitializeAccessibilityNodeInfo(host, new AccessibilityNodeInfoCompat(info));
			}

			public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
				val$compat.onPopulateAccessibilityEvent(host, event);
			}

			public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
				return val$compat.onRequestSendAccessibilityEvent(host, child, event);
			}

			public boolean performAccessibilityAction(View host, int action, Bundle args) {
				return val$compat.performAccessibilityAction(host, action, args);
			}

			public void sendAccessibilityEvent(View host, int eventType) {
				val$compat.sendAccessibilityEvent(host, eventType);
			}

			public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
				val$compat.sendAccessibilityEventUnchecked(host, event);
			}
		}


		AccessibilityDelegateJellyBeanImpl() {
			super();
		}

		public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object delegate, View host) {
			Object provider = AccessibilityDelegateCompatJellyBean.getAccessibilityNodeProvider(delegate, host);
			if (provider != null) {
				return new AccessibilityNodeProviderCompat(provider);
			} else {
				return null;
			}
		}

		public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat compat) {
			return AccessibilityDelegateCompatJellyBean.newAccessibilityDelegateBridge(new AnonymousClass_1(this, compat));
		}

		public boolean performAccessibilityAction(Object delegate, View host, int action, Bundle args) {
			return AccessibilityDelegateCompatJellyBean.performAccessibilityAction(delegate, host, action, args);
		}
	}


	static {
		if (VERSION.SDK_INT >= 16) {
			IMPL = new AccessibilityDelegateJellyBeanImpl();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new AccessibilityDelegateIcsImpl();
		} else {
			IMPL = new AccessibilityDelegateStubImpl();
		}
		DEFAULT_DELEGATE = IMPL.newAccessiblityDelegateDefaultImpl();
	}

	public AccessibilityDelegateCompat() {
		super();
		mBridge = IMPL.newAccessiblityDelegateBridge(this);
	}

	public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
		return IMPL.dispatchPopulateAccessibilityEvent(DEFAULT_DELEGATE, host, event);
	}

	public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
		return IMPL.getAccessibilityNodeProvider(DEFAULT_DELEGATE, host);
	}

	Object getBridge() {
		return mBridge;
	}

	public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
		IMPL.onInitializeAccessibilityEvent(DEFAULT_DELEGATE, host, event);
	}

	public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
		IMPL.onInitializeAccessibilityNodeInfo(DEFAULT_DELEGATE, host, info);
	}

	public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
		IMPL.onPopulateAccessibilityEvent(DEFAULT_DELEGATE, host, event);
	}

	public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
		return IMPL.onRequestSendAccessibilityEvent(DEFAULT_DELEGATE, host, child, event);
	}

	public boolean performAccessibilityAction(View host, int action, Bundle args) {
		return IMPL.performAccessibilityAction(DEFAULT_DELEGATE, host, action, args);
	}

	public void sendAccessibilityEvent(View host, int eventType) {
		IMPL.sendAccessibilityEvent(DEFAULT_DELEGATE, host, eventType);
	}

	public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
		IMPL.sendAccessibilityEventUnchecked(DEFAULT_DELEGATE, host, event);
	}
}
