package com.device.security.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils.SimpleStringSplitter;
import androidx.core.content.ContextCompat;
import com.device.security.MyApplication;
import com.device.security.receiver.ActivateDeviceAdminReceiver;
import com.device.security.services.ForegroundAppService;
import com.orhanobut.logger.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static String getDefaultKeyboardPackage(Context context) {
        return Secure.getString(context.getContentResolver(), "default_input_method");
    }

    public static String getDefaultLauncherPackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
        return resolveActivity != null ? resolveActivity.activityInfo.packageName : "";
    }

    public static boolean shouldRestrictDeviceUsage() {
        String currentTime = getCurrentTime();
        if (currentTime.contains("PM") || currentTime.contains("pm")) {
            return isDateGreaterThanOther("11:59:59 PM", currentTime);
        }
        if (currentTime.contains("AM") || currentTime.contains("am")) {
            return isDateGreaterThanOther(currentTime, "01:00:00 AM");
        }
        return false;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm:ss a").format(new Date(Long.valueOf(System.currentTimeMillis()).longValue()));
    }

    public static boolean isDateGreaterThanOther(String str, String str2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        try {
            return simpleDateFormat.parse(str).after(simpleDateFormat.parse(str2));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void startService(Context context) {
        ContextCompat.startForegroundService(context, new Intent(context, ForegroundAppService.class));
    }

    public static void stopService(Context context) {
        context.stopService(new Intent(context, ForegroundAppService.class));
    }

    public static boolean isBrowserApp(Context context, String str) {
        ResolveInfo resolveActivity;
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
        intent.setPackage(str);
        try {
            resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Not a Browser: ");
            stringBuilder.append(e.getMessage());
            Logger.d(stringBuilder.toString());
            resolveActivity = null;
        }
        if (resolveActivity != null) {
            return true;
        }
        return false;
    }

    public static List<String> getAllBrowsersList(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://www.google.com"));
        ArrayList arrayList = new ArrayList();
        try {
            List queryIntentActivities;
            if (VERSION.SDK_INT >= 23) {
                queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 131072);
            } else {
                queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            }
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                if (resolveInfo != null) {
                    arrayList.add(resolveInfo.activityInfo.packageName);
                }
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error Getting Browsers List: ");
            stringBuilder.append(e.getMessage());
            Logger.d(stringBuilder.toString());
        }
        return arrayList;
    }

    private static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 1) != 0;
    }

    public static boolean isServiceRunning(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            try {
                for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                    if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return false;
    }

    public static boolean isEnabledAsDeviceAdministrator() {
        return ((DevicePolicyManager) MyApplication.getContext().getSystemService("device_policy")).isAdminActive(new ComponentName(MyApplication.getContext(), ActivateDeviceAdminReceiver.class));
    }

    public static boolean isAccessibilityEnabled(Context context) {
        try {
            SimpleStringSplitter simpleStringSplitter = new SimpleStringSplitter(':');
            String string = Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
            if (string != null && string.length() > 0) {
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    if (simpleStringSplitter.next().contains(context.getPackageName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public static void hideAppIcon(Context context, String str, String str2) {
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(str, str2), 2, 1);
    }

    public static String getAppNameFromPackage(String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }

    public static String formatDate(String str) {
        if (str == null) {
            return null;
        }
        long longValue = Long.valueOf(str).longValue();
        if (str.length() < 13) {
            longValue *= 1000;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(longValue));
    }
}
