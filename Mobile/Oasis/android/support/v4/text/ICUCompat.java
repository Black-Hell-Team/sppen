package android.support.v4.text;

import android.os.Build.VERSION;

public class ICUCompat {
	private static final ICUCompatImpl IMPL;

	static interface ICUCompatImpl {
		public String addLikelySubtags(String r1_String);

		public String getScript(String r1_String);
	}

	static class ICUCompatImplBase implements ICUCompat.ICUCompatImpl {
		ICUCompatImplBase() {
			super();
		}

		public String addLikelySubtags(String locale) {
			return locale;
		}

		public String getScript(String locale) {
			return null;
		}
	}

	static class ICUCompatImplIcs implements ICUCompat.ICUCompatImpl {
		ICUCompatImplIcs() {
			super();
		}

		public String addLikelySubtags(String locale) {
			return ICUCompatIcs.addLikelySubtags(locale);
		}

		public String getScript(String locale) {
			return ICUCompatIcs.getScript(locale);
		}
	}


	static {
		if (VERSION.SDK_INT >= 14) {
			IMPL = new ICUCompatImplIcs();
		} else {
			IMPL = new ICUCompatImplBase();
		}
	}

	public ICUCompat() {
		super();
	}

	public static String addLikelySubtags(String locale) {
		return IMPL.addLikelySubtags(locale);
	}

	public static String getScript(String locale) {
		return IMPL.getScript(locale);
	}
}
