package hack.hackit.pankaj.keyboardlisten;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.TextView;

public class ShowTextDialog extends Activity {
    private static Context context;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        String appName = getIntent().getExtras().getString("appName");
        String typedText = getIntent().getExtras().getString("typedText");
        String dateTime = getIntent().getExtras().getString("dateTime");
        setTitle(appName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_typedtext);
        ((EditText) findViewById(R.id.dialog_editText)).setText(typedText);
        ((TextView) findViewById(R.id.dialog_appname)).setText(appName);
        TextView tv2 = (TextView) findViewById(R.id.dialog_datetime);
        SpannableString content = new SpannableString("Opened At : " + dateTime);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv2.setText(content);
        context = this;
    }

    public void makeEditable() {
        ((EditText) findViewById(R.id.dialog_editText)).setEnabled(true);
    }
}
