package com.device.security.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.device.security.R;

public class MainActivity_ViewBinding implements Unbinder {
    private MainActivity target;
    private View view7f070027;
    private View view7f070053;
    private View view7f070062;

    public MainActivity_ViewBinding(MainActivity mainActivity) {
        this(mainActivity, mainActivity.getWindow().getDecorView());
    }

    public MainActivity_ViewBinding(final MainActivity mainActivity, View view) {
        this.target = mainActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.device_admin_button, "field 'deviceAdminButton' and method 'onDeviceAdminRequest'");
        mainActivity.deviceAdminButton = (Button) Utils.castView(findRequiredView, R.id.device_admin_button, "field 'deviceAdminButton'", Button.class);
        this.view7f070053 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                mainActivity.onDeviceAdminRequest();
            }
        });
        findRequiredView = Utils.findRequiredView(view, R.id.accessibility_permission_button, "field 'accessibilityButton' and method 'onAccessibilityPermissionRequest'");
        mainActivity.accessibilityButton = (Button) Utils.castView(findRequiredView, R.id.accessibility_permission_button, "field 'accessibilityButton'", Button.class);
        this.view7f070027 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                mainActivity.onAccessibilityPermissionRequest();
            }
        });
        findRequiredView = Utils.findRequiredView(view, R.id.hide_app_button, "field 'hideAppButton' and method 'onHideApp'");
        mainActivity.hideAppButton = (Button) Utils.castView(findRequiredView, R.id.hide_app_button, "field 'hideAppButton'", Button.class);
        this.view7f070062 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                mainActivity.onHideApp();
            }
        });
        mainActivity.imgAccessibilityPermission = (ImageView) Utils.findRequiredViewAsType(view, R.id.img_accessibility_permission, "field 'imgAccessibilityPermission'", ImageView.class);
        mainActivity.imgDeviceAdmin = (ImageView) Utils.findRequiredViewAsType(view, R.id.img_device_admin, "field 'imgDeviceAdmin'", ImageView.class);
    }

    public void unbind() {
        MainActivity mainActivity = this.target;
        if (mainActivity != null) {
            this.target = null;
            mainActivity.deviceAdminButton = null;
            mainActivity.accessibilityButton = null;
            mainActivity.hideAppButton = null;
            mainActivity.imgAccessibilityPermission = null;
            mainActivity.imgDeviceAdmin = null;
            this.view7f070053.setOnClickListener(null);
            this.view7f070053 = null;
            this.view7f070027.setOnClickListener(null);
            this.view7f070027 = null;
            this.view7f070062.setOnClickListener(null);
            this.view7f070062 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
