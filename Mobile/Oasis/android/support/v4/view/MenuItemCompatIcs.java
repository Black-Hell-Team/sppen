package android.support.v4.view;

import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;

class MenuItemCompatIcs {
	static class OnActionExpandListenerWrapper implements OnActionExpandListener {
		private MenuItemCompatIcs.SupportActionExpandProxy mWrapped;

		public OnActionExpandListenerWrapper(MenuItemCompatIcs.SupportActionExpandProxy wrapped) {
			super();
			mWrapped = wrapped;
		}

		public boolean onMenuItemActionCollapse(MenuItem item) {
			return mWrapped.onMenuItemActionCollapse(item);
		}

		public boolean onMenuItemActionExpand(MenuItem item) {
			return mWrapped.onMenuItemActionExpand(item);
		}
	}

	static interface SupportActionExpandProxy {
		public boolean onMenuItemActionCollapse(MenuItem r1_MenuItem);

		public boolean onMenuItemActionExpand(MenuItem r1_MenuItem);
	}


	MenuItemCompatIcs() {
		super();
	}

	public static boolean collapseActionView(MenuItem item) {
		return item.collapseActionView();
	}

	public static boolean expandActionView(MenuItem item) {
		return item.expandActionView();
	}

	public static boolean isActionViewExpanded(MenuItem item) {
		return item.isActionViewExpanded();
	}

	public static MenuItem setOnActionExpandListener(MenuItem item, SupportActionExpandProxy listener) {
		return item.setOnActionExpandListener(new OnActionExpandListenerWrapper(listener));
	}
}
