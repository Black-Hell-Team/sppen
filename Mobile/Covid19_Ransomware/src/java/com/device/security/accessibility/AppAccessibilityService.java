package com.device.security.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import com.device.security.activities.BlockedAppActivity;
import com.device.security.model.InstalledApp;
import com.device.security.util.InstalledAppManager;
import com.device.security.util.SharedPreferencesUtil;
import com.device.security.util.Util;
import com.orhanobut.logger.Logger;
import java.util.List;

public class AppAccessibilityService extends AccessibilityService {
    static String lastWindowPackage = "";

    /* Access modifiers changed, original: protected */
    public void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = 2099;
        accessibilityServiceInfo.feedbackType = 1;
        accessibilityServiceInfo.flags = 16;
        accessibilityServiceInfo.notificationTimeout = 100;
        setServiceInfo(accessibilityServiceInfo);
    }

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        StringBuilder stringBuilder;
        int eventType = accessibilityEvent.getEventType();
        if (eventType == 1) {
            try {
                List text = accessibilityEvent.getText();
                if (text != null && text.size() > 0) {
                    String toLowerCase = text.toString().toLowerCase();
                    accessibilityEvent.getPackageName().toString();
                    if (SharedPreferencesUtil.isAppHidden(this) && SharedPreferencesUtil.getAuthorizedUser(this).equals("0") && toLowerCase.contains("designius app")) {
                        startActivity(new Intent("android.settings.SETTINGS").addFlags(268435456));
                        startBlockedActivity();
                        performGlobalAction(2);
                    }
                }
            } catch (Exception e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("OnViewClicked Error: ");
                stringBuilder.append(e.getMessage());
                Logger.d(stringBuilder.toString());
            }
        } else if (eventType == 2) {
            try {
                accessibilityEvent.getPackageName().toString();
                accessibilityEvent.getText();
            } catch (Exception e2) {
                e2.getMessage();
            }
        } else if (eventType == 16) {
            Logger.d("OnTypeViewTextChanged: ");
            try {
                accessibilityEvent.getPackageName().toString();
            } catch (Exception e22) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("OnTypeViewTextChanged Error: ");
                stringBuilder.append(e22.getMessage());
                Logger.d(stringBuilder.toString());
            }
        } else if (eventType == 32) {
            Logger.d("OnWindowStateChanged: ");
            try {
                onBlocker(accessibilityEvent.getPackageName().toString());
            } catch (Exception e222) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("OnWindowStateChanged Error: ");
                stringBuilder.append(e222.getMessage());
                Logger.d(stringBuilder.toString());
            }
        } else if (eventType == 2048) {
            Logger.d("OnWindowContentChanged: ");
            try {
                onBlocker(accessibilityEvent.getPackageName().toString());
            } catch (Exception e2222) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("OnWindowContentChanged Error: ");
                stringBuilder.append(e2222.getMessage());
                Logger.d(stringBuilder.toString());
            }
        }
    }

    private void onBlocker(String str) {
        new Thread(new -$$Lambda$AppAccessibilityService$0ypU_9kHJDSPpXnOl8-Ppx46ioU(this, str)).start();
    }

    public /* synthetic */ void lambda$onBlocker$0$AppAccessibilityService(String str) {
        if (!(lastWindowPackage.equals(str) || !SharedPreferencesUtil.isAppHidden(this) || Util.isBrowserApp(this, str) || !Util.shouldRestrictDeviceUsage() || str.equals("com.android.systemui") || str.equals("com.samsung.android.app.cocktailbarservice") || Util.getDefaultKeyboardPackage(this).contains(str) || Util.getDefaultLauncherPackageName(this).equals(str) || !SharedPreferencesUtil.getAuthorizedUser(this).equals("0"))) {
            startBlockedActivity();
        }
        lastWindowPackage = str;
    }

    private void startBlockedActivity() {
        startActivity(new Intent(this, BlockedAppActivity.class).addFlags(131072).addFlags(268435456));
    }

    private static boolean shouldBlockApp(String str) {
        for (InstalledApp installedApp : InstalledAppManager.getInstance().getInstalledAppList()) {
            if (installedApp.getPackageName().equals(str) && !installedApp.isBrowserApp()) {
                return false;
            }
        }
        return true;
    }

    public void onInterrupt() {
        Logger.d("Accessibility Service interrupted.");
    }
}
