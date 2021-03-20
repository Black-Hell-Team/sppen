package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v4.media.TransportMediator;
import android.util.Log;

public class NavUtils {
	private static final NavUtilsImpl IMPL;
	public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
	private static final String TAG = "NavUtils";

	static interface NavUtilsImpl {
		public Intent getParentActivityIntent(Activity r1_Activity);

		public String getParentActivityName(Context r1_Context, ActivityInfo r2_ActivityInfo);

		public void navigateUpTo(Activity r1_Activity, Intent r2_Intent);

		public boolean shouldUpRecreateTask(Activity r1_Activity, Intent r2_Intent);
	}

	static class NavUtilsImplBase implements NavUtils.NavUtilsImpl {
		NavUtilsImplBase() {
			super();
		}

		public Intent getParentActivityIntent(Activity activity) {
			String parentName = NavUtils.getParentActivityName(activity);
			if (parentName == null) {
				return null;
			} else {
				Intent parentIntent;
				ComponentName target = new ComponentName(activity, parentName);
				try {
					if (NavUtils.getParentActivityName(activity, target) == null) {
						parentIntent = IntentCompat.makeMainActivity(target);
					} else {
						parentIntent = new Intent().setComponent(target);
					}
					return parentIntent;
				} catch (NameNotFoundException e) {
					Log.e(TAG, "getParentActivityIntent: bad parentActivityName '" + parentName + "' in manifest");
					return null;
				}
			}
		}

		public String getParentActivityName(Context context, ActivityInfo info) {
			if (info.metaData == null) {
				return null;
			} else {
				String parentActivity = info.metaData.getString(PARENT_ACTIVITY);
				if (parentActivity == null) {
					return null;
				} else if (parentActivity.charAt(0) == '.') {
					return context.getPackageName() + parentActivity;
				} else {
					return parentActivity;
				}
			}
		}

		public void navigateUpTo(Activity activity, Intent upIntent) {
			upIntent.addFlags(67108864);
			activity.startActivity(upIntent);
			activity.finish();
		}

		public boolean shouldUpRecreateTask(Activity activity, Intent targetIntent) {
			String action = activity.getIntent().getAction();
			if (action == null || action.equals("android.intent.action.MAIN")) {
				return false;
			} else {
				return true;
			}
		}
	}

	static class NavUtilsImplJB extends NavUtils.NavUtilsImplBase {
		NavUtilsImplJB() {
			super();
		}

		public Intent getParentActivityIntent(Activity activity) {
			Intent result = NavUtilsJB.getParentActivityIntent(activity);
			if (result == null) {
				result = superGetParentActivityIntent(activity);
			}
			return result;
		}

		public String getParentActivityName(Context context, ActivityInfo info) {
			String result = NavUtilsJB.getParentActivityName(info);
			if (result == null) {
				result = super.getParentActivityName(context, info);
			}
			return result;
		}

		public void navigateUpTo(Activity activity, Intent upIntent) {
			NavUtilsJB.navigateUpTo(activity, upIntent);
		}

		public boolean shouldUpRecreateTask(Activity activity, Intent targetIntent) {
			return NavUtilsJB.shouldUpRecreateTask(activity, targetIntent);
		}

		Intent superGetParentActivityIntent(Activity activity) {
			return super.getParentActivityIntent(activity);
		}
	}


	static {
		if (VERSION.SDK_INT >= 16) {
			IMPL = new NavUtilsImplJB();
		} else {
			IMPL = new NavUtilsImplBase();
		}
	}

	private NavUtils() {
		super();
	}

	public static Intent getParentActivityIntent(Activity sourceActivity) {
		return IMPL.getParentActivityIntent(sourceActivity);
	}

	public static Intent getParentActivityIntent(Context context, ComponentName componentName) throws NameNotFoundException {
		String parentActivity = getParentActivityName(context, componentName);
		if (parentActivity == null) {
			return null;
		} else {
			Intent parentIntent;
			ComponentName target = new ComponentName(componentName.getPackageName(), parentActivity);
			if (getParentActivityName(context, target) == null) {
				parentIntent = IntentCompat.makeMainActivity(target);
			} else {
				parentIntent = new Intent().setComponent(target);
			}
			return parentIntent;
		}
	}

	public static Intent getParentActivityIntent(Context context, Class<?> sourceActivityClass) throws NameNotFoundException {
		String parentActivity = getParentActivityName(context, new ComponentName(context, sourceActivityClass));
		if (parentActivity == null) {
			return null;
		} else {
			Intent parentIntent;
			ComponentName target = new ComponentName(context, parentActivity);
			if (getParentActivityName(context, target) == null) {
				parentIntent = IntentCompat.makeMainActivity(target);
			} else {
				parentIntent = new Intent().setComponent(target);
			}
			return parentIntent;
		}
	}

	@Nullable
	public static String getParentActivityName(Activity sourceActivity) {
		try {
			return getParentActivityName(sourceActivity, sourceActivity.getComponentName());
		} catch (NameNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Nullable
	public static String getParentActivityName(Context context, ComponentName componentName) throws NameNotFoundException {
		return IMPL.getParentActivityName(context, context.getPackageManager().getActivityInfo(componentName, TransportMediator.FLAG_KEY_MEDIA_NEXT));
	}

	public static void navigateUpFromSameTask(Activity sourceActivity) {
		Intent upIntent = getParentActivityIntent(sourceActivity);
		if (upIntent == null) {
			throw new IllegalArgumentException("Activity " + sourceActivity.getClass().getSimpleName() + " does not have a parent activity name specified." + " (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> " + " element in your manifest?)");
		} else {
			navigateUpTo(sourceActivity, upIntent);
		}
	}

	public static void navigateUpTo(Activity sourceActivity, Intent upIntent) {
		IMPL.navigateUpTo(sourceActivity, upIntent);
	}

	public static boolean shouldUpRecreateTask(Activity sourceActivity, Intent targetIntent) {
		return IMPL.shouldUpRecreateTask(sourceActivity, targetIntent);
	}
}
