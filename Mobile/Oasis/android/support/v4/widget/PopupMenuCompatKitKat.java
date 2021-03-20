package android.support.v4.widget;

import android.view.View.OnTouchListener;
import android.widget.PopupMenu;

class PopupMenuCompatKitKat {
	PopupMenuCompatKitKat() {
		super();
	}

	public static OnTouchListener getDragToOpenListener(Object popupMenu) {
		return ((PopupMenu) popupMenu).getDragToOpenListener();
	}
}
