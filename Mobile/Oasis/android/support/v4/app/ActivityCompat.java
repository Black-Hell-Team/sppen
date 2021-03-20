package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

public class ActivityCompat extends ContextCompat {
	public ActivityCompat() {
		super();
	}

	public static void finishAffinity(Activity activity) {
		if (VERSION.SDK_INT >= 16) {
			ActivityCompatJB.finishAffinity(activity);
		} else {
			activity.finish();
		}
	}

	public static boolean invalidateOptionsMenu(Activity activity) {
		if (VERSION.SDK_INT >= 11) {
			ActivityCompatHoneycomb.invalidateOptionsMenu(activity);
			return true;
		} else {
			return false;
		}
	}

	public static void startActivity(Activity activity, Intent intent, @Nullable Bundle options) {
		if (VERSION.SDK_INT >= 16) {
			ActivityCompatJB.startActivity(activity, intent, options);
		} else {
			activity.startActivity(intent);
		}
	}

	public static void startActivityForResult(Activity activity, Intent intent, int requestCode, @Nullable Bundle options) {
		if (VERSION.SDK_INT >= 16) {
			ActivityCompatJB.startActivityForResult(activity, intent, requestCode, options);
		} else {
			activity.startActivityForResult(intent, requestCode);
		}
	}
}
