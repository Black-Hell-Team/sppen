package com.device.security.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.device.security.model.InstalledApp;
import com.device.security.model.InstalledApp.InstalledAppBuilder;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstalledAppUtil {
    public static List<InstalledApp> queryInstalledApps(Context context) {
        ArrayList arrayList = new ArrayList();
        try {
            PackageManager packageManager = context.getPackageManager();
            for (PackageInfo packageInfo : packageManager.getInstalledPackages(0)) {
                PackageInfo packageInfo2 = packageManager.getPackageInfo(packageInfo.packageName, 136);
                if (packageInfo2 != null) {
                    ApplicationInfo applicationInfo = packageInfo2.applicationInfo;
                    StringBuilder stringBuilder;
                    try {
                        String charSequence = packageManager.getApplicationLabel(applicationInfo).toString();
                        if (!charSequence.equals("Designius App")) {
                            boolean isSystemPackage = isSystemPackage(applicationInfo);
                            InstalledApp create = new InstalledAppBuilder().setPackageName(applicationInfo.packageName).setName(charSequence).setVersion(packageInfo.versionName).setInstallTime(Util.formatDate(String.valueOf(new File(applicationInfo.sourceDir).lastModified()))).setIsSystemApp(isSystemPackage).setIsBrowserApp(Util.isBrowserApp(context, applicationInfo.packageName)).create();
                            arrayList.add(create);
                            if (Util.isBrowserApp(context, create.getPackageName())) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("browser app = ");
                                stringBuilder.append(create);
                                Logger.d(stringBuilder.toString());
                            }
                        }
                    } catch (Exception e) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Error Inserting installed app: ");
                        stringBuilder.append(e.getMessage());
                        Logger.d(stringBuilder.toString());
                    }
                }
            }
        } catch (Exception e2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Error getting installed apps list: ");
            stringBuilder2.append(e2.getMessage());
            Logger.d(stringBuilder2.toString());
        }
        return arrayList;
    }

    private static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 1) != 0;
    }
}
