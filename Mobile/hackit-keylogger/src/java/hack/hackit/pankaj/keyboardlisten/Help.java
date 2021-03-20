package hack.hackit.pankaj.keyboardlisten;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Help extends Activity {
    int currentIndex = 0;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpscreen);
        this.currentIndex = 0;
        ((Button) findViewById(R.id.prev)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Help.this.onPrev();
            }
        });
        ((Button) findViewById(R.id.next)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Help.this.onNext();
            }
        });
    }

    private void onNext() {
        ImageView iv = (ImageView) findViewById(R.id.ImageView);
        if (this.currentIndex + 1 < 5) {
            this.currentIndex++;
            switch (this.currentIndex) {
                case 0:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_0));
                    return;
                case 1:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_1));
                    return;
                case 2:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_2));
                    return;
                case 3:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_3));
                    return;
                case 4:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_4));
                    return;
                default:
                    return;
            }
        }
    }

    private void onPrev() {
        ImageView iv = (ImageView) findViewById(R.id.ImageView);
        if (this.currentIndex - 1 >= 0) {
            this.currentIndex--;
            switch (this.currentIndex) {
                case 0:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_0));
                    return;
                case 1:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_1));
                    return;
                case 2:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_2));
                    return;
                case 3:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_3));
                    return;
                case 4:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.untitled_4));
                    return;
                default:
                    return;
            }
        }
    }
}
