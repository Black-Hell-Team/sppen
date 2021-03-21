package com.device.security.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.device.security.util.Util;

public class StartServiceActivity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Util.startService(this);
        finish();
    }
}
