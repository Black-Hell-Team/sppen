package android.support.v4.view;

import android.view.KeyEvent;

class KeyEventCompatHoneycomb {
	KeyEventCompatHoneycomb() {
		super();
	}

	public static boolean metaStateHasModifiers(int metaState, int modifiers) {
		return KeyEvent.metaStateHasModifiers(metaState, modifiers);
	}

	public static boolean metaStateHasNoModifiers(int metaState) {
		return KeyEvent.metaStateHasNoModifiers(metaState);
	}

	public static int normalizeMetaState(int metaState) {
		return KeyEvent.normalizeMetaState(metaState);
	}
}
