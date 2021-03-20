package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.CursorAdapter;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;

public class KeyEventCompat {
	static final KeyEventVersionImpl IMPL;

	static interface KeyEventVersionImpl {
		public boolean dispatch(KeyEvent r1_KeyEvent, Callback r2_Callback, Object r3_Object, Object r4_Object);

		public Object getKeyDispatcherState(View r1_View);

		public boolean isTracking(KeyEvent r1_KeyEvent);

		public boolean metaStateHasModifiers(int r1i, int r2i);

		public boolean metaStateHasNoModifiers(int r1i);

		public int normalizeMetaState(int r1i);

		public void startTracking(KeyEvent r1_KeyEvent);
	}

	static class BaseKeyEventVersionImpl implements KeyEventCompat.KeyEventVersionImpl {
		private static final int META_ALL_MASK = 247;
		private static final int META_MODIFIER_MASK = 247;

		BaseKeyEventVersionImpl() {
			super();
		}

		private static int metaStateFilterDirectionalModifiers(int metaState, int modifiers, int basic, int left, int right) {
			boolean wantBasic;
			boolean wantLeftOrRight = true;
			if ((modifiers & basic) != 0) {
				wantBasic = true;
			} else {
				wantBasic = false;
			}
			int directional = left | right;
			if ((modifiers & directional) != 0) {
				if (!wantBasic) {
					if (!wantLeftOrRight) {
						throw new IllegalArgumentException("bad arguments");
					} else {
						return metaState & (directional ^ -1);
					}
				} else if (!wantLeftOrRight) {
					return metaState & (basic ^ -1);
				} else {
					return metaState;
				}
			} else {
				wantLeftOrRight = false;
				if (!wantBasic) {
					if (!wantLeftOrRight) {
						return metaState;
					} else {
						return metaState & (basic ^ -1);
					}
				} else if (!wantLeftOrRight) {
					return metaState & (directional ^ -1);
				} else {
					throw new IllegalArgumentException("bad arguments");
				}
			}
		}

		public boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
			return event.dispatch(receiver);
		}

		public Object getKeyDispatcherState(View view) {
			return null;
		}

		public boolean isTracking(KeyEvent event) {
			return false;
		}

		public boolean metaStateHasModifiers(int metaState, int modifiers) {
			if (metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(metaState) & 247, modifiers, 1, TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD, TransportMediator.FLAG_KEY_MEDIA_NEXT), modifiers, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, TransportMediator.FLAG_KEY_MEDIA_PAUSE, TransportMediator.FLAG_KEY_MEDIA_STOP) == modifiers) {
				return true;
			} else {
				return false;
			}
		}

		public boolean metaStateHasNoModifiers(int metaState) {
			if ((normalizeMetaState(metaState) & 247) == 0) {
				return true;
			} else {
				return false;
			}
		}

		public int normalizeMetaState(int metaState) {
			if ((metaState & 192) != 0) {
				metaState |= 1;
			}
			if ((metaState & 48) != 0) {
				metaState |= 2;
			}
			return metaState & 247;
		}

		public void startTracking(KeyEvent event) {
		}
	}

	static class EclairKeyEventVersionImpl extends KeyEventCompat.BaseKeyEventVersionImpl {
		EclairKeyEventVersionImpl() {
			super();
		}

		public boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
			return KeyEventCompatEclair.dispatch(event, receiver, state, target);
		}

		public Object getKeyDispatcherState(View view) {
			return KeyEventCompatEclair.getKeyDispatcherState(view);
		}

		public boolean isTracking(KeyEvent event) {
			return KeyEventCompatEclair.isTracking(event);
		}

		public void startTracking(KeyEvent event) {
			KeyEventCompatEclair.startTracking(event);
		}
	}

	static class HoneycombKeyEventVersionImpl extends KeyEventCompat.EclairKeyEventVersionImpl {
		HoneycombKeyEventVersionImpl() {
			super();
		}

		public boolean metaStateHasModifiers(int metaState, int modifiers) {
			return KeyEventCompatHoneycomb.metaStateHasModifiers(metaState, modifiers);
		}

		public boolean metaStateHasNoModifiers(int metaState) {
			return KeyEventCompatHoneycomb.metaStateHasNoModifiers(metaState);
		}

		public int normalizeMetaState(int metaState) {
			return KeyEventCompatHoneycomb.normalizeMetaState(metaState);
		}
	}


	static {
		if (VERSION.SDK_INT >= 11) {
			IMPL = new HoneycombKeyEventVersionImpl();
		} else {
			IMPL = new BaseKeyEventVersionImpl();
		}
	}

	public KeyEventCompat() {
		super();
	}

	public static boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
		return IMPL.dispatch(event, receiver, state, target);
	}

	public static Object getKeyDispatcherState(View view) {
		return IMPL.getKeyDispatcherState(view);
	}

	public static boolean hasModifiers(KeyEvent event, int modifiers) {
		return IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
	}

	public static boolean hasNoModifiers(KeyEvent event) {
		return IMPL.metaStateHasNoModifiers(event.getMetaState());
	}

	public static boolean isTracking(KeyEvent event) {
		return IMPL.isTracking(event);
	}

	public static boolean metaStateHasModifiers(int metaState, int modifiers) {
		return IMPL.metaStateHasModifiers(metaState, modifiers);
	}

	public static boolean metaStateHasNoModifiers(int metaState) {
		return IMPL.metaStateHasNoModifiers(metaState);
	}

	public static int normalizeMetaState(int metaState) {
		return IMPL.normalizeMetaState(metaState);
	}

	public static void startTracking(KeyEvent event) {
		IMPL.startTracking(event);
	}
}
