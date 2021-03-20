package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompatIcs.SupportActionExpandProxy;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MenuItemCompat {
	static final MenuVersionImpl IMPL;
	public static final int SHOW_AS_ACTION_ALWAYS = 2;
	public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
	public static final int SHOW_AS_ACTION_IF_ROOM = 1;
	public static final int SHOW_AS_ACTION_NEVER = 0;
	public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
	private static final String TAG = "MenuItemCompat";

	static interface MenuVersionImpl {
		public boolean collapseActionView(MenuItem r1_MenuItem);

		public boolean expandActionView(MenuItem r1_MenuItem);

		public View getActionView(MenuItem r1_MenuItem);

		public boolean isActionViewExpanded(MenuItem r1_MenuItem);

		public MenuItem setActionView(MenuItem r1_MenuItem, int r2i);

		public MenuItem setActionView(MenuItem r1_MenuItem, View r2_View);

		public MenuItem setOnActionExpandListener(MenuItem r1_MenuItem, MenuItemCompat.OnActionExpandListener r2_MenuItemCompat_OnActionExpandListener);

		public void setShowAsAction(MenuItem r1_MenuItem, int r2i);
	}

	public static interface OnActionExpandListener {
		public boolean onMenuItemActionCollapse(MenuItem r1_MenuItem);

		public boolean onMenuItemActionExpand(MenuItem r1_MenuItem);
	}

	static class BaseMenuVersionImpl implements MenuItemCompat.MenuVersionImpl {
		BaseMenuVersionImpl() {
			super();
		}

		public boolean collapseActionView(MenuItem item) {
			return false;
		}

		public boolean expandActionView(MenuItem item) {
			return false;
		}

		public View getActionView(MenuItem item) {
			return null;
		}

		public boolean isActionViewExpanded(MenuItem item) {
			return false;
		}

		public MenuItem setActionView(MenuItem item, int resId) {
			return item;
		}

		public MenuItem setActionView(MenuItem item, View view) {
			return item;
		}

		public MenuItem setOnActionExpandListener(MenuItem item, MenuItemCompat.OnActionExpandListener listener) {
			return item;
		}

		public void setShowAsAction(MenuItem item, int actionEnum) {
		}
	}

	static class HoneycombMenuVersionImpl implements MenuItemCompat.MenuVersionImpl {
		HoneycombMenuVersionImpl() {
			super();
		}

		public boolean collapseActionView(MenuItem item) {
			return false;
		}

		public boolean expandActionView(MenuItem item) {
			return false;
		}

		public View getActionView(MenuItem item) {
			return MenuItemCompatHoneycomb.getActionView(item);
		}

		public boolean isActionViewExpanded(MenuItem item) {
			return false;
		}

		public MenuItem setActionView(MenuItem item, int resId) {
			return MenuItemCompatHoneycomb.setActionView(item, resId);
		}

		public MenuItem setActionView(MenuItem item, View view) {
			return MenuItemCompatHoneycomb.setActionView(item, view);
		}

		public MenuItem setOnActionExpandListener(MenuItem item, MenuItemCompat.OnActionExpandListener listener) {
			return item;
		}

		public void setShowAsAction(MenuItem item, int actionEnum) {
			MenuItemCompatHoneycomb.setShowAsAction(item, actionEnum);
		}
	}

	static class IcsMenuVersionImpl extends MenuItemCompat.HoneycombMenuVersionImpl {
		class AnonymousClass_1 implements SupportActionExpandProxy {
			final /* synthetic */ MenuItemCompat.IcsMenuVersionImpl this$0;
			final /* synthetic */ MenuItemCompat.OnActionExpandListener val$listener;

			AnonymousClass_1(MenuItemCompat.IcsMenuVersionImpl r1_MenuItemCompat_IcsMenuVersionImpl, MenuItemCompat.OnActionExpandListener r2_MenuItemCompat_OnActionExpandListener) {
				super();
				this$0 = r1_MenuItemCompat_IcsMenuVersionImpl;
				val$listener = r2_MenuItemCompat_OnActionExpandListener;
			}

			public boolean onMenuItemActionCollapse(MenuItem item) {
				return val$listener.onMenuItemActionCollapse(item);
			}

			public boolean onMenuItemActionExpand(MenuItem item) {
				return val$listener.onMenuItemActionExpand(item);
			}
		}


		IcsMenuVersionImpl() {
			super();
		}

		public boolean collapseActionView(MenuItem item) {
			return MenuItemCompatIcs.collapseActionView(item);
		}

		public boolean expandActionView(MenuItem item) {
			return MenuItemCompatIcs.expandActionView(item);
		}

		public boolean isActionViewExpanded(MenuItem item) {
			return MenuItemCompatIcs.isActionViewExpanded(item);
		}

		public MenuItem setOnActionExpandListener(MenuItem item, MenuItemCompat.OnActionExpandListener listener) {
			if (listener == null) {
				return MenuItemCompatIcs.setOnActionExpandListener(item, null);
			} else {
				return MenuItemCompatIcs.setOnActionExpandListener(item, new AnonymousClass_1(this, listener));
			}
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 14) {
			IMPL = new IcsMenuVersionImpl();
		} else if (version >= 11) {
			IMPL = new HoneycombMenuVersionImpl();
		} else {
			IMPL = new BaseMenuVersionImpl();
		}
	}

	public MenuItemCompat() {
		super();
	}

	public static boolean collapseActionView(MenuItem item) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).collapseActionView();
		} else {
			return IMPL.collapseActionView(item);
		}
	}

	public static boolean expandActionView(MenuItem item) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).expandActionView();
		} else {
			return IMPL.expandActionView(item);
		}
	}

	public static ActionProvider getActionProvider(MenuItem item) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).getSupportActionProvider();
		} else {
			Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
			return null;
		}
	}

	public static View getActionView(MenuItem item) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).getActionView();
		} else {
			return IMPL.getActionView(item);
		}
	}

	public static boolean isActionViewExpanded(MenuItem item) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).isActionViewExpanded();
		} else {
			return IMPL.isActionViewExpanded(item);
		}
	}

	public static MenuItem setActionProvider(MenuItem item, ActionProvider provider) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).setSupportActionProvider(provider);
		} else {
			Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
			return item;
		}
	}

	public static MenuItem setActionView(MenuItem item, int resId) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).setActionView(resId);
		} else {
			return IMPL.setActionView(item, resId);
		}
	}

	public static MenuItem setActionView(MenuItem item, View view) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).setActionView(view);
		} else {
			return IMPL.setActionView(item, view);
		}
	}

	public static MenuItem setOnActionExpandListener(MenuItem item, OnActionExpandListener listener) {
		if (item instanceof SupportMenuItem) {
			return ((SupportMenuItem) item).setSupportOnActionExpandListener(listener);
		} else {
			return IMPL.setOnActionExpandListener(item, listener);
		}
	}

	public static void setShowAsAction(MenuItem item, int actionEnum) {
		if (item instanceof SupportMenuItem) {
			((SupportMenuItem) item).setShowAsAction(actionEnum);
		} else {
			IMPL.setShowAsAction(item, actionEnum);
		}
	}
}
