package hack.hackit.pankaj.keyboardlisten;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.Settings.Secure;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Settings extends PreferenceActivity {
    private static boolean calledIM_Picker = false;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Preference kbPref = findPreference("defaultKeyboard");
        CheckBoxPreference hsPref = (CheckBoxPreference) findPreference("Hacking Status");
        ListPreference limitPref = (ListPreference) findPreference("Limit");
        if (getHackingStatus().equals("Active") && getKeyBoardStatus().equals("Active")) {
            hsPref.setChecked(true);
        } else {
            hsPref.setChecked(false);
        }
        limitPref.setValue(Integer.toString(getDatabaseLimit()));
        if (getKeyBoardStatus().equals("Active")) {
            kbPref.setSummary("'Hack Keyboard' is Active");
        } else {
            kbPref.setSummary("Please activate 'Hack Keyboard'");
        }
        kbPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Settings.this.openKeyboardChooser();
                Settings.this.RefreshStatus(false);
                return true;
            }
        });
        hsPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (Settings.this.getKeyBoardStatus().equals("Active")) {
                    Settings.this.changeHackingStatus();
                    return true;
                }
                Toast.makeText(HKApplication.getAppContext(), "Please select 'Hack Keyboard' as default Keyboard", 0).show();
                return false;
            }
        });
        limitPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object o) {
                Settings.this.setDatabaseLimit(Integer.parseInt(o.toString()));
                return true;
            }
        });
    }

    private int getDatabaseLimit() {
        return getSharedPreferences("HackMode", 0).getInt("DB_Limit", 50);
    }

    private void setDatabaseLimit(int limit) {
        String status = getHackingStatus();
        Editor editor = getSharedPreferences("HackMode", 0).edit();
        editor.putInt("DB_Limit", limit);
        editor.commit();
    }

    private void openKeyboardChooser() {
        ((InputMethodManager) getApplicationContext().getSystemService("input_method")).showInputMethodPicker();
        calledIM_Picker = true;
    }

    /* Access modifiers changed, original: protected */
    public String getHackingStatus() {
        return getSharedPreferences("HackMode", 0).getString("HackStatus", "Inactive");
    }

    private String getKeyBoardStatus() {
        String status = "Inactive";
        if (Secure.getString(getContentResolver(), "default_input_method").equals("hack.hackit.pankaj.keyboardlisten/.HackingKeyBoard")) {
            return "Active";
        }
        return status;
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

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && calledIM_Picker) {
            calledIM_Picker = false;
            finish();
            startActivity(getIntent());
        }
    }

    private void RefreshStatus(boolean fromButtonClick) {
        String KBstatus = getKeyBoardStatus();
        String HackingStatus = getHackingStatus();
        CheckBoxPreference hsPref = (CheckBoxPreference) findPreference("Hacking Status");
        Preference kbPref = findPreference("defaultKeyboard");
        if (KBstatus.equals("Active")) {
            kbPref.setSummary("'Hack Keyboard' is Active");
            if (HackingStatus.equals("Active")) {
                hsPref.setChecked(true);
                return;
            }
            return;
        }
        kbPref.setSummary("Please activate 'Hack Keyboard'");
        hsPref.setChecked(false);
        if (HackingStatus.equals("Active") && fromButtonClick) {
            Toast.makeText(this, "Hacking Mode can not be Activated.. while 'Hack Keyboard' is in Deactivation mode.", 1).show();
        }
    }
}
