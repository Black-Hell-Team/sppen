package android.support.v4.content;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build.VERSION;

public class IntentCompat {
	public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
	public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
	public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
	public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
	public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
	public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
	public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
	private static final IntentCompatImpl IMPL;

	static interface IntentCompatImpl {
		public Intent makeMainActivity(ComponentName r1_ComponentName);

		public Intent makeMainSelectorActivity(String r1_String, String r2_String);

		public Intent makeRestartActivityTask(ComponentName r1_ComponentName);
	}

	static class IntentCompatImplBase implements IntentCompat.IntentCompatImpl {
		IntentCompatImplBase() {
			super();
		}

		public Intent makeMainActivity(ComponentName componentName) {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.setComponent(componentName);
			intent.addCategory("android.intent.category.LAUNCHER");
			return intent;
		}

		public Intent makeMainSelectorActivity(String selectorAction, String selectorCategory) {
			Intent intent = new Intent(selectorAction);
			intent.addCategory(selectorCategory);
			return intent;
		}

		public Intent makeRestartActivityTask(ComponentName mainActivity) {
			Intent intent = makeMainActivity(mainActivity);
			intent.addFlags(268468224);
			return intent;
		}
	}

	static class IntentCompatImplHC extends IntentCompat.IntentCompatImplBase {
		IntentCompatImplHC() {
			super();
		}

		public Intent makeMainActivity(ComponentName componentName) {
			return IntentCompatHoneycomb.makeMainActivity(componentName);
		}

		public Intent makeRestartActivityTask(ComponentName componentName) {
			return IntentCompatHoneycomb.makeRestartActivityTask(componentName);
		}
	}

	static class IntentCompatImplIcsMr1 extends IntentCompat.IntentCompatImplHC {
		IntentCompatImplIcsMr1() {
			super();
		}

		public Intent makeMainSelectorActivity(String selectorAction, String selectorCategory) {
			return IntentCompatIcsMr1.makeMainSelectorActivity(selectorAction, selectorCategory);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 15) {
			IMPL = new IntentCompatImplIcsMr1();
		} else if (version >= 11) {
			IMPL = new IntentCompatImplHC();
		} else {
			IMPL = new IntentCompatImplBase();
		}
	}

	private IntentCompat() {
		super();
	}

	public static Intent makeMainActivity(ComponentName mainActivity) {
		return IMPL.makeMainActivity(mainActivity);
	}

	public static Intent makeMainSelectorActivity(String selectorAction, String selectorCategory) {
		return IMPL.makeMainSelectorActivity(selectorAction, selectorCategory);
	}

	public static Intent makeRestartActivityTask(ComponentName mainActivity) {
		return IMPL.makeRestartActivityTask(mainActivity);
	}
}
