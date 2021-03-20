package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewConfiguration;

public class ViewConfigurationCompat {
	static final ViewConfigurationVersionImpl IMPL;

	static interface ViewConfigurationVersionImpl {
		public int getScaledPagingTouchSlop(ViewConfiguration r1_ViewConfiguration);
	}

	static class BaseViewConfigurationVersionImpl implements ViewConfigurationCompat.ViewConfigurationVersionImpl {
		BaseViewConfigurationVersionImpl() {
			super();
		}

		public int getScaledPagingTouchSlop(ViewConfiguration config) {
			return config.getScaledTouchSlop();
		}
	}

	static class FroyoViewConfigurationVersionImpl implements ViewConfigurationCompat.ViewConfigurationVersionImpl {
		FroyoViewConfigurationVersionImpl() {
			super();
		}

		public int getScaledPagingTouchSlop(ViewConfiguration config) {
			return ViewConfigurationCompatFroyo.getScaledPagingTouchSlop(config);
		}
	}


	static {
		if (VERSION.SDK_INT >= 11) {
			IMPL = new FroyoViewConfigurationVersionImpl();
		} else {
			IMPL = new BaseViewConfigurationVersionImpl();
		}
	}

	public ViewConfigurationCompat() {
		super();
	}

	public static int getScaledPagingTouchSlop(ViewConfiguration config) {
		return IMPL.getScaledPagingTouchSlop(config);
	}
}
