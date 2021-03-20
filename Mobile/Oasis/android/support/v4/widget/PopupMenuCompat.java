package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;

public class PopupMenuCompat {
	static final PopupMenuImpl IMPL;

	static interface PopupMenuImpl {
		public OnTouchListener getDragToOpenListener(Object r1_Object);
	}

	static class BasePopupMenuImpl implements PopupMenuCompat.PopupMenuImpl {
		BasePopupMenuImpl() {
			super();
		}

		public OnTouchListener getDragToOpenListener(Object popupMenu) {
			return null;
		}
	}

	static class KitKatPopupMenuImpl extends PopupMenuCompat.BasePopupMenuImpl {
		KitKatPopupMenuImpl() {
			super();
		}

		public OnTouchListener getDragToOpenListener(Object popupMenu) {
			return PopupMenuCompatKitKat.getDragToOpenListener(popupMenu);
		}
	}


	static {
		if (VERSION.SDK_INT >= 19) {
			IMPL = new KitKatPopupMenuImpl();
		} else {
			IMPL = new BasePopupMenuImpl();
		}
	}

	private PopupMenuCompat() {
		super();
	}

	public static OnTouchListener getDragToOpenListener(Object popupMenu) {
		return IMPL.getDragToOpenListener(popupMenu);
	}
}
