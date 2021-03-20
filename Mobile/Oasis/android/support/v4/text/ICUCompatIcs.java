package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ICUCompatIcs {
	private static final String TAG = "ICUCompatIcs";
	private static Method sAddLikelySubtagsMethod;
	private static Method sGetScriptMethod;

	static {
		Class<?> clazz;
		try {
			clazz = Class.forName("libcore.icu.ICU");
			if (clazz != null) {
				Class[] r3_Class_A = new Class[1];
				r3_Class_A[0] = String.class;
				sGetScriptMethod = clazz.getMethod("getScript", r3_Class_A);
				r3_Class_A = new Class[1];
				r3_Class_A[0] = String.class;
				sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", r3_Class_A);
			}
		} catch (Exception e) {
			Log.w(TAG, e);
		}
	}

	ICUCompatIcs() {
		super();
	}

	public static String addLikelySubtags(String locale) {
		try {
			if (sAddLikelySubtagsMethod != null) {
				Object[] args = new Object[1];
				args[0] = locale;
				return (String) sAddLikelySubtagsMethod.invoke(null, args);
			}
		} catch (IllegalAccessException e) {
			Log.w(TAG, e);
		} catch (InvocationTargetException e_2) {
			Log.w(TAG, e_2);
		}
		return locale;
	}

	public static String getScript(String locale) {
		try {
			if (sGetScriptMethod != null) {
				Object[] args = new Object[1];
				args[0] = locale;
				return (String) sGetScriptMethod.invoke(null, args);
			}
		} catch (IllegalAccessException e) {
			Log.w(TAG, e);
		} catch (InvocationTargetException e_2) {
			Log.w(TAG, e_2);
		}
		return null;
	}
}
