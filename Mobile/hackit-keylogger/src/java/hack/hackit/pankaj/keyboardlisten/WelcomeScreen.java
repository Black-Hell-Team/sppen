package hack.hackit.pankaj.keyboardlisten;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WelcomeScreen extends Activity {
    private static int SPLASH_TIME_OUT = 1000;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                WelcomeScreen.this.startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
                Log.w("ttttttt", "tttttttt");
                WelcomeScreen.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }
}
