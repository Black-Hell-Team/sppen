package android.support.v4.hardware.display;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat {
	public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
	private static final WeakHashMap<Context, DisplayManagerCompat> sInstances;

	private static class JellybeanMr1Impl extends DisplayManagerCompat {
		private final Object mDisplayManagerObj;

		public JellybeanMr1Impl(Context context) {
			super();
			mDisplayManagerObj = DisplayManagerJellybeanMr1.getDisplayManager(context);
		}

		public Display getDisplay(int displayId) {
			return DisplayManagerJellybeanMr1.getDisplay(mDisplayManagerObj, displayId);
		}

		public Display[] getDisplays() {
			return DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj);
		}

		public Display[] getDisplays(String category) {
			return DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj, category);
		}
	}

	private static class LegacyImpl extends DisplayManagerCompat {
		private final WindowManager mWindowManager;

		public LegacyImpl(Context context) {
			super();
			mWindowManager = (WindowManager) context.getSystemService("window");
		}

		public Display getDisplay(int displayId) {
			Display display = mWindowManager.getDefaultDisplay();
			if (display.getDisplayId() == displayId) {
				return display;
			} else {
				return null;
			}
		}

		public Display[] getDisplays() {
			Display[] r0_Display_A = new Display[1];
			r0_Display_A[0] = mWindowManager.getDefaultDisplay();
			return r0_Display_A;
		}

		public Display[] getDisplays(String category) {
			if (category == null) {
				return getDisplays();
			} else {
				return new Display[0];
			}
		}
	}


	static {
		sInstances = new WeakHashMap();
	}

	DisplayManagerCompat() {
		super();
	}

	public static DisplayManagerCompat getInstance(Context context) {
		DisplayManagerCompat instance;
		synchronized(sInstances) {
			instance = (DisplayManagerCompat) sInstances.get(context);
			if (instance == null) {
				if (VERSION.SDK_INT >= 17) {
					instance = new JellybeanMr1Impl(context);
				} else {
					instance = new LegacyImpl(context);
				}
				sInstances.put(context, instance);
			}
		}
		return instance;
	}

	public abstract Display getDisplay(int r1i);

	public abstract Display[] getDisplays();

	public abstract Display[] getDisplays(String r1_String);
}
