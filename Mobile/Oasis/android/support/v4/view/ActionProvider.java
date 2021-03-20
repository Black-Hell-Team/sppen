package android.support.v4.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public abstract class ActionProvider {
	private static final String TAG = "ActionProvider(support)";
	private final Context mContext;
	private SubUiVisibilityListener mSubUiVisibilityListener;
	private VisibilityListener mVisibilityListener;

	public static interface SubUiVisibilityListener {
		public void onSubUiVisibilityChanged(boolean r1z);
	}

	public static interface VisibilityListener {
		public void onActionProviderVisibilityChanged(boolean r1z);
	}


	public ActionProvider(Context context) {
		super();
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	public boolean hasSubMenu() {
		return false;
	}

	public boolean isVisible() {
		return true;
	}

	public abstract View onCreateActionView();

	public View onCreateActionView(MenuItem forItem) {
		return onCreateActionView();
	}

	public boolean onPerformDefaultAction() {
		return false;
	}

	public void onPrepareSubMenu(SubMenu subMenu) {
	}

	public boolean overridesItemVisibility() {
		return false;
	}

	public void refreshVisibility() {
		if (mVisibilityListener == null || !overridesItemVisibility()) {
		} else {
			mVisibilityListener.onActionProviderVisibilityChanged(isVisible());
		}
	}

	public void setSubUiVisibilityListener(SubUiVisibilityListener listener) {
		mSubUiVisibilityListener = listener;
	}

	public void setVisibilityListener(VisibilityListener listener) {
		if (mVisibilityListener == null || listener == null) {
			mVisibilityListener = listener;
		} else {
			Log.w(TAG, "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + getClass().getSimpleName() + " instance while it is still in use somewhere else?");
			mVisibilityListener = listener;
		}
	}

	public void subUiVisibilityChanged(boolean isVisible) {
		if (mSubUiVisibilityListener != null) {
			mSubUiVisibilityListener.onSubUiVisibilityChanged(isVisible);
		}
	}
}
