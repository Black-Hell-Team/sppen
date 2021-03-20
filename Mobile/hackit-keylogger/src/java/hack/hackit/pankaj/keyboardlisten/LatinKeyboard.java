package hack.hackit.pankaj.keyboardlisten;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.Keyboard.Row;

public class LatinKeyboard extends Keyboard {
    private Key mEnterKey;

    static class LatinKey extends Key {
        public LatinKey(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }

        public boolean isInside(int x, int y) {
            if (this.codes[0] == -3) {
                y -= 10;
            }
            return super.isInside(x, y);
        }
    }

    public LatinKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public LatinKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    /* Access modifiers changed, original: protected */
    public Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = new LatinKey(res, parent, x, y, parser);
        if (key.codes[0] == 10) {
            this.mEnterKey = key;
        }
        return key;
    }

    /* Access modifiers changed, original: 0000 */
    public void setImeOptions(Resources res, int options) {
        if (this.mEnterKey != null) {
            switch (1073742079 & options) {
                case 2:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = res.getText(R.string.label_go_key);
                    return;
                case 3:
                    this.mEnterKey.icon = res.getDrawable(R.drawable.enter_key);
                    this.mEnterKey.label = null;
                    return;
                case 4:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = res.getText(R.string.label_send_key);
                    return;
                case 5:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = res.getText(R.string.label_next_key);
                    return;
                default:
                    this.mEnterKey.icon = res.getDrawable(R.drawable.enter_key);
                    this.mEnterKey.label = null;
                    return;
            }
        }
    }
}
