package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.WindowCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
	private static final String DEFAULT_CLASS_NAME;
	public static final int INVALID_ID = -2147483648;
	private int mFocusedVirtualViewId;
	private int mHoveredVirtualViewId;
	private final AccessibilityManager mManager;
	private ExploreByTouchNodeProvider mNodeProvider;
	private final int[] mTempGlobalRect;
	private final Rect mTempParentRect;
	private final Rect mTempScreenRect;
	private final Rect mTempVisibleRect;
	private final View mView;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private class ExploreByTouchNodeProvider extends AccessibilityNodeProviderCompat {
		final /* synthetic */ ExploreByTouchHelper this$0;

		private ExploreByTouchNodeProvider(ExploreByTouchHelper r1_ExploreByTouchHelper) {
			super();
			this$0 = r1_ExploreByTouchHelper;
		}

		/* synthetic */ ExploreByTouchNodeProvider(ExploreByTouchHelper x0, ExploreByTouchHelper.AnonymousClass_1 x1) {
			this(x0);
		}

		public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
			return this$0.createNode(virtualViewId);
		}

		public boolean performAction(int virtualViewId, int action, Bundle arguments) {
			return this$0.performAction(virtualViewId, action, arguments);
		}
	}


	static {
		DEFAULT_CLASS_NAME = View.class.getName();
	}

	public ExploreByTouchHelper(View forView) {
		super();
		mTempScreenRect = new Rect();
		mTempParentRect = new Rect();
		mTempVisibleRect = new Rect();
		mTempGlobalRect = new int[2];
		mFocusedVirtualViewId = -2147483648;
		mHoveredVirtualViewId = -2147483648;
		if (forView == null) {
			throw new IllegalArgumentException("View may not be null");
		} else {
			mView = forView;
			mManager = (AccessibilityManager) forView.getContext().getSystemService("accessibility");
		}
	}

	private boolean clearAccessibilityFocus(int virtualViewId) {
		if (isAccessibilityFocused(virtualViewId)) {
			mFocusedVirtualViewId = -2147483648;
			mView.invalidate();
			sendEventForVirtualView(virtualViewId, AccessibilityNodeInfoCompat.ACTION_CUT);
			return true;
		} else {
			return false;
		}
	}

	private AccessibilityEvent createEvent(int virtualViewId, int eventType) {
		switch(virtualViewId) {
		case WearableExtender.UNSET_ACTION_INDEX:
			return createEventForHost(eventType);
		}
		return createEventForChild(virtualViewId, eventType);
	}

	private AccessibilityEvent createEventForChild(int virtualViewId, int eventType) {
		AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
		event.setEnabled(true);
		event.setClassName(DEFAULT_CLASS_NAME);
		onPopulateEventForVirtualView(virtualViewId, event);
		if (!event.getText().isEmpty() || event.getContentDescription() != null) {
			event.setPackageName(mView.getContext().getPackageName());
			AccessibilityEventCompat.asRecord(event).setSource(mView, virtualViewId);
			return event;
		} else {
			throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
		}
	}

	private AccessibilityEvent createEventForHost(int eventType) {
		AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
		ViewCompat.onInitializeAccessibilityEvent(mView, event);
		return event;
	}

	private AccessibilityNodeInfoCompat createNode(int virtualViewId) {
		switch(virtualViewId) {
		case WearableExtender.UNSET_ACTION_INDEX:
			return createNodeForHost();
		}
		return createNodeForChild(virtualViewId);
	}

	private AccessibilityNodeInfoCompat createNodeForChild(int virtualViewId) {
		AccessibilityNodeInfoCompat node = AccessibilityNodeInfoCompat.obtain();
		node.setEnabled(true);
		node.setClassName(DEFAULT_CLASS_NAME);
		onPopulateNodeForVirtualView(virtualViewId, node);
		if (node.getText() != null || node.getContentDescription() != null) {
			node.getBoundsInParent(mTempParentRect);
			if (mTempParentRect.isEmpty()) {
				throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
			} else {
				int actions = node.getActions();
				if ((actions & 64) != 0) {
					throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
				} else if ((actions & 128) != 0) {
					throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
				} else {
					node.setPackageName(mView.getContext().getPackageName());
					node.setSource(mView, virtualViewId);
					node.setParent(mView);
					if (mFocusedVirtualViewId == virtualViewId) {
						node.setAccessibilityFocused(true);
						node.addAction(TransportMediator.FLAG_KEY_MEDIA_NEXT);
					} else {
						node.setAccessibilityFocused(false);
						node.addAction(TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD);
					}
					if (intersectVisibleToUser(mTempParentRect)) {
						node.setVisibleToUser(true);
						node.setBoundsInParent(mTempParentRect);
					}
					mView.getLocationOnScreen(mTempGlobalRect);
					mTempScreenRect.set(mTempParentRect);
					mTempScreenRect.offset(mTempGlobalRect[0], mTempGlobalRect[1]);
					node.setBoundsInScreen(mTempScreenRect);
					return node;
				}
			}
		} else {
			throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
		}
	}

	private AccessibilityNodeInfoCompat createNodeForHost() {
		AccessibilityNodeInfoCompat node = AccessibilityNodeInfoCompat.obtain(mView);
		ViewCompat.onInitializeAccessibilityNodeInfo(mView, node);
		LinkedList<Integer> virtualViewIds = new LinkedList();
		getVisibleVirtualViews(virtualViewIds);
		Iterator i$ = virtualViewIds.iterator();
		while (i$.hasNext()) {
			node.addChild(mView, ((Integer) i$.next()).intValue());
		}
		return node;
	}

	/* JADX WARNING: inconsistent code */
	/*
	private boolean intersectVisibleToUser(android.graphics.Rect r6_localRect) {
		r5_this = this;
		r2 = 0;
		if (r6_localRect == 0) goto L_0x0009;
	L_0x0003:
		r3 = r6_localRect.isEmpty();
		if (r3 == 0) goto L_0x000a;
	L_0x0009:
		return r2;
	L_0x000a:
		r3 = r5.mView;
		r3 = r3.getWindowVisibility();
		if (r3 != 0) goto L_0x0009;
	L_0x0012:
		r3 = r5.mView;
		r1 = r3.getParent();
	L_0x0018:
		r3 = r1_viewParent instanceof android.view.View;
		if (r3 == 0) goto L_0x0033;
	L_0x001c:
		r0 = r1_viewParent;
		r0 = (android.view.View) r0;
		r3 = android.support.v4.view.ViewCompat.getAlpha(r0_view);
		r4 = 0;
		r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
		if (r3 <= 0) goto L_0x0009;
	L_0x0028:
		r3 = r0_view.getVisibility();
		if (r3 != 0) goto L_0x0009;
	L_0x002e:
		r1_viewParent = r0_view.getParent();
		goto L_0x0018;
	L_0x0033:
		if (r1_viewParent == 0) goto L_0x0009;
	L_0x0035:
		r3 = r5.mView;
		r4 = r5.mTempVisibleRect;
		r3 = r3.getLocalVisibleRect(r4);
		if (r3 == 0) goto L_0x0009;
	L_0x003f:
		r2 = r5.mTempVisibleRect;
		r2 = r6.intersect(r2);
		goto L_0x0009;
	}
	*/
	private boolean intersectVisibleToUser(Rect localRect) {
		if (localRect != null) {
			if (localRect.isEmpty()) {
				return false;
			} else if (mView.getWindowVisibility() == 0) {
				ViewParent viewParent = mView.getParent();
				while (viewParent instanceof View) {
					View view = (View) viewParent;
					if (ViewCompat.getAlpha(view) > 0.0f) {
						if (view.getVisibility() == 0) {
							viewParent = view.getParent();
						}
					}
				}
				if (viewParent != null) {
					if (mView.getLocalVisibleRect(mTempVisibleRect)) {
						return localRect.intersect(mTempVisibleRect);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isAccessibilityFocused(int virtualViewId) {
		if (mFocusedVirtualViewId == virtualViewId) {
			return true;
		} else {
			return false;
		}
	}

	private boolean manageFocusForChild(int virtualViewId, int action, Bundle arguments) {
		switch(action) {
		case TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD:
			return requestAccessibilityFocus(virtualViewId);
		case TransportMediator.FLAG_KEY_MEDIA_NEXT:
			return clearAccessibilityFocus(virtualViewId);
		}
		return false;
	}

	private boolean performAction(int virtualViewId, int action, Bundle arguments) {
		switch(virtualViewId) {
		case WearableExtender.UNSET_ACTION_INDEX:
			return performActionForHost(action, arguments);
		}
		return performActionForChild(virtualViewId, action, arguments);
	}

	private boolean performActionForChild(int virtualViewId, int action, Bundle arguments) {
		switch(action) {
		case TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD:
		case TransportMediator.FLAG_KEY_MEDIA_NEXT:
			return manageFocusForChild(virtualViewId, action, arguments);
		}
		return onPerformActionForVirtualView(virtualViewId, action, arguments);
	}

	private boolean performActionForHost(int action, Bundle arguments) {
		return ViewCompat.performAccessibilityAction(mView, action, arguments);
	}

	private boolean requestAccessibilityFocus(int virtualViewId) {
		if (mManager.isEnabled()) {
			if (!AccessibilityManagerCompat.isTouchExplorationEnabled(mManager)) {
				return false;
			} else if (!isAccessibilityFocused(virtualViewId)) {
				mFocusedVirtualViewId = virtualViewId;
				mView.invalidate();
				sendEventForVirtualView(virtualViewId, AccessibilityNodeInfoCompat.ACTION_PASTE);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void updateHoveredVirtualView(int virtualViewId) {
		if (mHoveredVirtualViewId == virtualViewId) {
		} else {
			mHoveredVirtualViewId = virtualViewId;
			sendEventForVirtualView(virtualViewId, TransportMediator.FLAG_KEY_MEDIA_NEXT);
			sendEventForVirtualView(mHoveredVirtualViewId, AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
		}
	}

	public boolean dispatchHoverEvent(MotionEvent event) {
		boolean r1z = true;
		if (mManager.isEnabled()) {
			if (!AccessibilityManagerCompat.isTouchExplorationEnabled(mManager)) {
				return false;
			} else {
				switch(event.getAction()) {
				case MotionEventCompat.ACTION_HOVER_MOVE:
				case WindowCompat.FEATURE_ACTION_BAR_OVERLAY:
					int virtualViewId = getVirtualViewAt(event.getX(), event.getY());
					updateHoveredVirtualView(virtualViewId);
					if (virtualViewId != -2147483648) {
						return r1z;
					} else {
						r1z = false;
						return r1z;
					}
				case WindowCompat.FEATURE_ACTION_MODE_OVERLAY:
					if (mFocusedVirtualViewId != -2147483648) {
						updateHoveredVirtualView(INVALID_ID);
						return true;
					} else {
						return false;
					}
				}
				return false;
			}
		} else {
			return false;
		}
	}

	public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
		if (mNodeProvider == null) {
			mNodeProvider = new ExploreByTouchNodeProvider(this, null);
		}
		return mNodeProvider;
	}

	public int getFocusedVirtualView() {
		return mFocusedVirtualViewId;
	}

	protected abstract int getVirtualViewAt(float r1f, float r2f);

	protected abstract void getVisibleVirtualViews(List<Integer> r1_List_Integer);

	public void invalidateRoot() {
		invalidateVirtualView(-1);
	}

	public void invalidateVirtualView(int virtualViewId) {
		sendEventForVirtualView(virtualViewId, AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
	}

	protected abstract boolean onPerformActionForVirtualView(int r1i, int r2i, Bundle r3_Bundle);

	protected abstract void onPopulateEventForVirtualView(int r1i, AccessibilityEvent r2_AccessibilityEvent);

	protected abstract void onPopulateNodeForVirtualView(int r1i, AccessibilityNodeInfoCompat r2_AccessibilityNodeInfoCompat);

	public boolean sendEventForVirtualView(int virtualViewId, int eventType) {
		if (virtualViewId != -2147483648) {
			if (!mManager.isEnabled()) {
				return false;
			} else {
				ViewParent parent = mView.getParent();
				if (parent != null) {
					return ViewParentCompat.requestSendAccessibilityEvent(parent, mView, createEvent(virtualViewId, eventType));
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
