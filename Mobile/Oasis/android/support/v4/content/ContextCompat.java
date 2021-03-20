package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

public class ContextCompat {
	private static final String DIR_ANDROID = "Android";
	private static final String DIR_CACHE = "cache";
	private static final String DIR_DATA = "data";
	private static final String DIR_FILES = "files";
	private static final String DIR_OBB = "obb";

	public ContextCompat() {
		super();
	}

	private static File buildPath(File base, String ... segments) {
		String[] arr$ = segments;
		int i$ = 0;
		File cur = base;
		while (i$ < arr$.length) {
			File cur_2;
			String segment = arr$[i$];
			if (cur == null) {
				cur_2 = new File(segment);
			} else if (segment != null) {
				cur_2 = new File(cur, segment);
			} else {
				cur_2 = cur;
			}
			i$++;
			cur = cur_2;
		}
		return cur;
	}

	public static File[] getExternalCacheDirs(Context context) {
		int version = VERSION.SDK_INT;
		if (version >= 19) {
			return ContextCompatKitKat.getExternalCacheDirs(context);
		} else {
			File single;
			if (version >= 8) {
				single = ContextCompatFroyo.getExternalCacheDir(context);
			} else {
				String[] r3_String_A = new String[4];
				r3_String_A[0] = DIR_ANDROID;
				r3_String_A[1] = DIR_DATA;
				r3_String_A[2] = context.getPackageName();
				r3_String_A[3] = DIR_CACHE;
				single = buildPath(Environment.getExternalStorageDirectory(), r3_String_A);
			}
			File[] r2_File_A = new File[1];
			r2_File_A[0] = single;
			return r2_File_A;
		}
	}

	public static File[] getExternalFilesDirs(Context context, String type) {
		int version = VERSION.SDK_INT;
		if (version >= 19) {
			return ContextCompatKitKat.getExternalFilesDirs(context, type);
		} else {
			File single;
			if (version >= 8) {
				single = ContextCompatFroyo.getExternalFilesDir(context, type);
			} else {
				String[] r3_String_A = new String[5];
				r3_String_A[0] = DIR_ANDROID;
				r3_String_A[1] = DIR_DATA;
				r3_String_A[2] = context.getPackageName();
				r3_String_A[3] = DIR_FILES;
				r3_String_A[4] = type;
				single = buildPath(Environment.getExternalStorageDirectory(), r3_String_A);
			}
			File[] r2_File_A = new File[1];
			r2_File_A[0] = single;
			return r2_File_A;
		}
	}

	public static File[] getObbDirs(Context context) {
		int version = VERSION.SDK_INT;
		if (version >= 19) {
			return ContextCompatKitKat.getObbDirs(context);
		} else {
			File single;
			if (version >= 11) {
				single = ContextCompatHoneycomb.getObbDir(context);
			} else {
				String[] r3_String_A = new String[3];
				r3_String_A[0] = DIR_ANDROID;
				r3_String_A[1] = DIR_OBB;
				r3_String_A[2] = context.getPackageName();
				single = buildPath(Environment.getExternalStorageDirectory(), r3_String_A);
			}
			File[] r2_File_A = new File[1];
			r2_File_A[0] = single;
			return r2_File_A;
		}
	}

	public static boolean startActivities(Context context, Intent[] intents) {
		return startActivities(context, intents, null);
	}

	public static boolean startActivities(Context context, Intent[] intents, Bundle options) {
		int version = VERSION.SDK_INT;
		if (version >= 16) {
			ContextCompatJellybean.startActivities(context, intents, options);
			return true;
		} else if (version >= 11) {
			ContextCompatHoneycomb.startActivities(context, intents);
			return true;
		} else {
			return false;
		}
	}
}
