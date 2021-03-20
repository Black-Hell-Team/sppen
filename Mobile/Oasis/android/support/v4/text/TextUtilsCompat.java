package android.support.v4.text;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import java.util.Locale;

public class TextUtilsCompat {
	private static String ARAB_SCRIPT_SUBTAG;
	private static String HEBR_SCRIPT_SUBTAG;
	public static final Locale ROOT;

	static {
		ROOT = new Locale("", "");
		ARAB_SCRIPT_SUBTAG = "Arab";
		HEBR_SCRIPT_SUBTAG = "Hebr";
	}

	public TextUtilsCompat() {
		super();
	}

	private static int getLayoutDirectionFromFirstChar(Locale locale) {
		switch(Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
		case CursorAdapter.FLAG_AUTO_REQUERY:
		case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
			return 1;
		}
		return 0;
	}

	public static int getLayoutDirectionFromLocale(@Nullable Locale locale) {
		if (locale == null || locale.equals(ROOT)) {
			return 0;
		} else {
			String scriptSubtag = ICUCompat.getScript(ICUCompat.addLikelySubtags(locale.toString()));
			if (scriptSubtag == null) {
				return getLayoutDirectionFromFirstChar(locale);
			} else if (scriptSubtag.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || scriptSubtag.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@NonNull
	public static String htmlEncode(@NonNull String s) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < s.length()) {
			char c = s.charAt(i);
			switch(c) {
			case '\"':
				sb.append("&quot;");
				i++;
				break;
			case '&':
				sb.append("&amp;");
				i++;
				break;
			case '\'':
				sb.append("&#39;");
				i++;
				break;
			case '<':
				sb.append("&lt;");
				i++;
				break;
			case '>':
				sb.append("&gt;");
				i++;
				break;
			}
			sb.append(c);
			i++;
		}
		return sb.toString();
	}
}
