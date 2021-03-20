package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnTouchListener;

public class ListPopupWindowCompat {
	static final ListPopupWindowImpl IMPL;

	static interface ListPopupWindowImpl {
		public OnTouchListener createDragToOpenListener(Object r1_Object, View r2_View);
	}

	static class BaseListPopupWindowImpl implements ListPopupWindowCompat.ListPopupWindowImpl {
		BaseListPopupWindowImpl() {
			super();
		}

		public OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
			return null;
		}
	}

	static class KitKatListPopupWindowImpl extends ListPopupWindowCompat.BaseListPopupWindowImpl {
		KitKatListPopupWindowImpl() {
			super();
		}

		public OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
			return ListPopupWindowCompatKitKat.createDragToOpenListener(listPopupWindow, src);
		}
	}


	static {
		if (VERSION.SDK_INT >= 19) {
			IMPL = new KitKatListPopupWindowImpl();
		} else {
			IMPL = new BaseListPopupWindowImpl();
		}
	}

	private ListPopupWindowCompat() {
		super();
	}

	public static OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
		return IMPL.createDragToOpenListener(listPopupWindow, src);
	}
}
