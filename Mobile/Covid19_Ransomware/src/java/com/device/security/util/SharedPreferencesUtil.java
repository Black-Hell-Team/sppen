package com.device.security.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
    public static final String APP_PREF = "APP_LOCKER_PREFS";
    private static final String PREF_APP_HIDDEN = "PREF_APP_HIDDEN";
    private static final String PREF_AUTHORIZED_USER = "PREF_AUTHORIZED_USER";

    public static void setAppAsHidden(Context context, boolean z) {
        Editor edit = getSharedPreferences(context).edit();
        edit.putBoolean(PREF_APP_HIDDEN, z);
        edit.apply();
    }

    public static boolean isAppHidden(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_APP_HIDDEN, false);
    }

    public static void setAuthorizedUser(Context context, String str) {
        Editor edit = getSharedPreferences(context).edit();
        edit.putString(PREF_AUTHORIZED_USER, str);
        edit.apply();
    }

    public static String getAuthorizedUser(Context context) {
        return getSharedPreferences(context).getString(PREF_AUTHORIZED_USER, "0");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREF, 0);
    }
}
