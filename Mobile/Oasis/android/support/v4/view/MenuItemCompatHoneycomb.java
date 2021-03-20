package android.support.v4.view;

import android.view.MenuItem;
import android.view.View;

class MenuItemCompatHoneycomb {
	MenuItemCompatHoneycomb() {
		super();
	}

	public static View getActionView(MenuItem item) {
		return item.getActionView();
	}

	public static MenuItem setActionView(MenuItem item, int resId) {
		return item.setActionView(resId);
	}

	public static MenuItem setActionView(MenuItem item, View view) {
		return item.setActionView(view);
	}

	public static void setShowAsAction(MenuItem item, int actionEnum) {
		item.setShowAsAction(actionEnum);
	}
}
