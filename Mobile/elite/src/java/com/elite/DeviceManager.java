package com.elite;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

public class DeviceManager {
    public static final int REQUEST_CODE_ENABLE_ADMIN = 1000;
    public static boolean isActive = false;

    public void activateDeviceAdmin(Activity activity, int resultCode) {
        try {
            ComponentName comp = new ComponentName(activity, AdminReciever.class);
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", comp);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", Html.fromHtml(activity.getResources().getString(R.string.device_admin_manager_message)));
            intent.setFlags(1073741824);
            activity.startActivityForResult(intent, resultCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deactivateDeviceAdmin(Context context) {
        try {
            ((DevicePolicyManager) context.getSystemService("device_policy")).removeActiveAdmin(new ComponentName(context, AdminReciever.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDeviceAdminActive(Context context) {
        boolean flag = false;
        try {
            flag = ((DevicePolicyManager) context.getSystemService("device_policy")).isAdminActive(new AdminReciever().getWho(context));
            isActive = flag;
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }
    }
}
