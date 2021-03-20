package hack.hackit.pankaj.keyboardlisten;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class startingDialog extends Activity {
    private static boolean calledIM_Picker = false;
    private static String color = "Red";
    private static Context context;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_dialog);
        initialiseParameter();
    }

    private void initialiseParameter() {
        Button openApp = (Button) findViewById(R.id.openApp);
        TextView setKB = (TextView) findViewById(R.id.setKB);
        final ImageButton button = (ImageButton) findViewById(R.id.imageButton);
        RefreshStatus(false);
        if (color.equals("Green")) {
            button.setBackgroundResource(R.drawable.green);
        } else if (color.equals("Red")) {
            button.setBackgroundResource(R.drawable.red);
        } else if (color.equals("Yellow")) {
            button.setBackgroundResource(R.drawable.yellow);
        }
        openApp.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startingDialog.this.startActivity(new Intent(startingDialog.this, WelcomeScreen.class));
                startingDialog.this.finish();
            }
        });
        setKB.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startingDialog.this.openKeyboardChooser();
                startingDialog.this.RefreshStatus(false);
            }
        });
        button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    String pre_Color = startingDialog.color;
                    if (pre_Color.equals("Green")) {
                        button.setBackgroundResource(R.drawable.green_pressed);
                    } else if (pre_Color.equals("Red")) {
                        button.setBackgroundResource(R.drawable.red_pressed);
                    } else if (pre_Color.equals("Yellow")) {
                        button.setBackgroundResource(R.drawable.yellow_pressed);
                    }
                } else if (motionEvent.getAction() == 1) {
                    startingDialog.this.changeHackingStatus();
                    startingDialog.this.RefreshStatus(true);
                    if (startingDialog.color.equals("Green")) {
                        button.setBackgroundResource(R.drawable.green);
                    } else if (startingDialog.color.equals("Red")) {
                        button.setBackgroundResource(R.drawable.red);
                    } else if (startingDialog.color.equals("Yellow")) {
                        button.setBackgroundResource(R.drawable.yellow);
                    }
                }
                return false;
            }
        });
        SpannableString content = new SpannableString("Click here to set up 'Hack Keyboard'");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        setKB.setText(content);
    }

    private void openKeyboardChooser() {
        ((InputMethodManager) getApplicationContext().getSystemService("input_method")).showInputMethodPicker();
        calledIM_Picker = true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && calledIM_Picker) {
            calledIM_Picker = false;
            finish();
            startActivity(getIntent());
        }
    }

    private void changeHackingStatus() {
        String status = getHackingStatus();
        Editor editor = getSharedPreferences("HackMode", 0).edit();
        if (status.equals("Active")) {
            editor.putString("HackStatus", "Inactive");
        } else {
            editor.putString("HackStatus", "Active");
        }
        editor.commit();
    }

    private String getKeyBoardStatus() {
        String status = "Inactive";
        if (Secure.getString(getContentResolver(), "default_input_method").equals("hack.hackit.pankaj.keyboardlisten/.HackingKeyBoard")) {
            return "Active";
        }
        return status;
    }

    private void setImageButton(String color) {
        ImageButton button = (ImageButton) findViewById(R.id.imageButton);
        if (!(color.equals("Red") || color.equals("Yellow") || !color.equals("Red"))) {
        }
    }

    /* Access modifiers changed, original: protected */
    public String getHackingStatus() {
        return getSharedPreferences("HackMode", 0).getString("HackStatus", "Inactive");
    }

    private void RefreshStatus(boolean fromButtonClick) {
        String KBstatus = getKeyBoardStatus();
        String HackingStatus = getHackingStatus();
        TextView modeLabel = (TextView) findViewById(R.id.modeLabel);
        TextView setKB = (TextView) findViewById(R.id.setKB);
        String imageButtonStatus = "Red";
        modeLabel.setText("Hacking mode is Deactivated.");
        if (!KBstatus.equals("Active")) {
            setKB.setText("Click here to set up 'Hack Keyboard'");
            if (HackingStatus.equals("Active") && fromButtonClick) {
                Toast.makeText(this, "Hacking Mode can not be Activated.Set 'Hack Keyboard' to default", 1).show();
            }
        } else if (HackingStatus.equals("Active")) {
            modeLabel.setText("Hacking mode is Activated.");
            imageButtonStatus = "Green";
        }
        color = imageButtonStatus;
    }
}
