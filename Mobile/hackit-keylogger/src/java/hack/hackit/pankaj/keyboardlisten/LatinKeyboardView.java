package hack.hackit.pankaj.keyboardlisten;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

public class LatinKeyboardView extends KeyboardView {
    static final int KEYCODE_OPTIONS = -100;

    public LatinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LatinKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* Access modifiers changed, original: protected */
    public boolean onLongPress(Key key) {
        if (key.codes[0] != -3) {
            return super.onLongPress(key);
        }
        getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(16.0f);
        paint.setFakeBoldText(true);
        paint.setColor(getResources().getColor(R.color.keyboard_text));
        int c = 0;
        for (Key key : getKeyboard().getKeys()) {
            if (key.codes[0] == 32) {
                Drawable dr = getResources().getDrawable(R.drawable.space_key);
                int x = key.x;
                int y = key.y;
                int xw = key.width;
                int yw = key.height;
                dr.setBounds(x, (yw / 3) + y, xw + x, ((yw * 2) / 3) + y);
                dr.draw(canvas);
            }
            c++;
        }
        paint.reset();
    }
}
