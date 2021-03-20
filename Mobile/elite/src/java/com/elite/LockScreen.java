package com.elite;

import android.app.Activity;
import android.os.Bundle;

public class LockScreen extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.lock_screen);
    }
}
