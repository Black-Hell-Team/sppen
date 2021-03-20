package com.elite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class UninstallAdminDevice extends Activity {
    private final int UNINSTALL_PACKAGE_EXISTED = 5002;
    private final int UNINSTALL_PACKAGE_EXISTED_PWD = 5004;
    boolean isCallfromPasswordScreen;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_uninstall_admin_device);
        this.isCallfromPasswordScreen = getIntent().getBooleanExtra("isCallfromPasswordScreen", false);
        AdminSettingCheckPackageExisted(getPackageName());
    }

    public void AdminSettingCheckPackageExisted(String packageName) {
        try {
            if (!this.isCallfromPasswordScreen) {
                DeviceManager deviceManager = new DeviceManager();
                if (deviceManager.isDeviceAdminActive(getApplicationContext())) {
                    deviceManager.deactivateDeviceAdmin(getApplicationContext());
                }
            }
            Intent uninstallIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + packageName));
            if (this.isCallfromPasswordScreen) {
                startActivityForResult(uninstallIntent, 5004);
            } else {
                startActivityForResult(uninstallIntent, 5002);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DeviceManager.REQUEST_CODE_ENABLE_ADMIN /*1000*/:
                if (new DeviceManager().isDeviceAdminActive(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.toast_device_admin_setting_activated, 1).show();
                }
                checkAdminPermission();
                return;
            case 5002:
                checkAdminPermission();
                return;
            case 5004:
                finish();
                super.onActivityResult(requestCode, resultCode, data);
                return;
            default:
                return;
        }
    }

    public void checkAdminPermission() {
        if (new DeviceManager().isDeviceAdminActive(getApplicationContext())) {
            finish();
        } else {
            showAdminSetting();
        }
    }

    public void showAdminSetting() {
        try {
            new DeviceManager().activateDeviceAdmin(this, DeviceManager.REQUEST_CODE_ENABLE_ADMIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
