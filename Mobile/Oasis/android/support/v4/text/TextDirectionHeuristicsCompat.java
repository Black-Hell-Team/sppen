package android.support.v4.text;

import android.support.v4.media.TransportMediator;
import android.support.v4.widget.ViewDragHelper;
import java.nio.CharBuffer;
import java.util.Locale;

public class TextDirectionHeuristicsCompat {
	public static final TextDirectionHeuristicCompat ANYRTL_LTR;
	public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
	public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
	public static final TextDirectionHeuristicCompat LOCALE;
	public static final TextDirectionHeuristicCompat LTR;
	public static final TextDirectionHeuristicCompat RTL;
	private static final int STATE_FALSE = 1;
	private static final int STATE_TRUE = 0;
	private static final int STATE_UNKNOWN = 2;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private static interface TextDirectionAlgorithm {
		public int checkRtl(CharSequence r1_CharSequence, int r2i, int r3i);
	}

	private static class AnyStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
		public static final TextDirectionHeuristicsCompat.AnyStrong INSTANCE_LTR;
		public static final TextDirectionHeuristicsCompat.AnyStrong INSTANCE_RTL;
		private final boolean mLookForRtl;

		static {
			INSTANCE_RTL = new TextDirectionHeuristicsCompat.AnyStrong(true);
			INSTANCE_LTR = new TextDirectionHeuristicsCompat.AnyStrong(false);
		}

		private AnyStrong(boolean lookForRtl) {
			super();
			mLookForRtl = lookForRtl;
		}

		/* JADX WARNING: inconsistent code */
		/*
		public int checkRtl(java.lang.CharSequence r7_cs, int r8_start, int r9_count) {
			r6_this = this;
			r3 = 1;
			r4 = 0;
			r1 = 0;
			r2 = r8_start;
			r0 = r8_start + r9_count;
		L_0x0006:
			if (r2_i >= r0_e) goto L_0x0028;
		L_0x0008:
			r5 = r7_cs.charAt(r2_i);
			r5 = java.lang.Character.getDirectionality(r5);
			r5 = android.support.v4.text.TextDirectionHeuristicsCompat.isRtlText(r5);
			switch(r5) {
				case 0: goto L_0x001a;
				case 1: goto L_0x0022;
				default: goto L_0x0017;
			}
		L_0x0017:
			r2_i++;
			goto L_0x0006;
		L_0x001a:
			r5 = r6.mLookForRtl;
			if (r5 == 0) goto L_0x0020;
		L_0x001e:
			r3 = r4;
		L_0x001f:
			return r3;
		L_0x0020:
			r1_haveUnlookedFor = 1;
			goto L_0x0017;
		L_0x0022:
			r5 = r6.mLookForRtl;
			if (r5 == 0) goto L_0x001f;
		L_0x0026:
			r1_haveUnlookedFor = 1;
			goto L_0x0017;
		L_0x0028:
			if (r1_haveUnlookedFor == 0) goto L_0x0030;
		L_0x002a:
			r5 = r6.mLookForRtl;
			if (r5 != 0) goto L_0x001f;
		L_0x002e:
			r3 = r4;
			goto L_0x001f;
		L_0x0030:
			r3 = 2;
			goto L_0x001f;
		}
		*/
		public int checkRtl(CharSequence cs, int start, int count) {
			boolean haveUnlookedFor = false;
			int i = start;
			while (i < start + count) {
				switch(TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(cs.charAt(i)))) {
				case STATE_TRUE:
					if (mLookForRtl) {
						return 0;
					} else {
						haveUnlookedFor = true;
					}
					break;
				case STATE_FALSE:
					if (mLookForRtl) {
						haveUnlookedFor = true;
					}
					break;
				}
				i++;
			}
			if (haveUnlookedFor) {
				if (!mLookForRtl) {
					return 0;
				} else {
					return STATE_FALSE;
				}
			} else {
				return STATE_UNKNOWN;
			}
		}
	}

	private static class FirstStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
		public static final TextDirectionHeuristicsCompat.FirstStrong INSTANCE;

		static {
			INSTANCE = new TextDirectionHeuristicsCompat.FirstStrong();
		}

		private FirstStrong() {
			super();
		}

		public int checkRtl(CharSequence cs, int start, int count) {
			int result = STATE_UNKNOWN;
			int i = start;
			while (i < start + count && result == 2) {
				result = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(cs.charAt(i)));
				i++;
			}
			return result;
		}
	}

	private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
		private final TextDirectionHeuristicsCompat.TextDirectionAlgorithm mAlgorithm;

		public TextDirectionHeuristicImpl(TextDirectionHeuristicsCompat.TextDirectionAlgorithm algorithm) {
			super();
			mAlgorithm = algorithm;
		}

		private boolean doCheck(CharSequence cs, int start, int count) {
			switch(mAlgorithm.checkRtl(cs, start, count)) {
			case STATE_TRUE:
				return true;
			case STATE_FALSE:
				return false;
			}
			return defaultIsRtl();
		}

		protected abstract boolean defaultIsRtl();

		public boolean isRtl(CharSequence cs, int start, int count) {
			if (cs == null || start < 0 || count < 0 || cs.length() - count < start) {
				throw new IllegalArgumentException();
			} else if (mAlgorithm == null) {
				return defaultIsRtl();
			} else {
				return doCheck(cs, start, count);
			}
		}

		public boolean isRtl(char[] array, int start, int count) {
			return isRtl(CharBuffer.wrap(array), start, count);
		}
	}

	private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
		private final boolean mDefaultIsRtl;

		private TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.TextDirectionAlgorithm algorithm, boolean defaultIsRtl) {
			super(algorithm);
			mDefaultIsRtl = defaultIsRtl;
		}

		/* synthetic */ TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.TextDirectionAlgorithm x0, boolean x1, TextDirectionHeuristicsCompat.AnonymousClass_1 x2) {
			this(x0, x1);
		}

		protected boolean defaultIsRtl() {
			return mDefaultIsRtl;
		}
	}

	private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
		public static final TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale INSTANCE;

		static {
			INSTANCE = new TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale();
		}

		public TextDirectionHeuristicLocale() {
			super(null);
		}

		protected boolean defaultIsRtl() {
			if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
				return true;
			} else {
				return false;
			}
		}
	}


	static {
		LTR = new TextDirectionHeuristicInternal(null, false, null);
		RTL = new TextDirectionHeuristicInternal(null, true, null);
		FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false, null);
		FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true, null);
		ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false, null);
		LOCALE = TextDirectionHeuristicLocale.INSTANCE;
	}

	public TextDirectionHeuristicsCompat() {
		super();
	}

	private static int isRtlText(int directionality) {
		switch(directionality) {
		case STATE_TRUE:
			return STATE_FALSE;
		case STATE_FALSE:
		case STATE_UNKNOWN:
			return STATE_TRUE;
		}
		return STATE_UNKNOWN;
	}

	private static int isRtlTextOrFormat(int directionality) {
		switch(directionality) {
		case STATE_TRUE:
		case 14:
		case ViewDragHelper.EDGE_ALL:
			return STATE_FALSE;
		case STATE_FALSE:
		case STATE_UNKNOWN:
		case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
		case 17:
			return STATE_TRUE;
		}
		return STATE_UNKNOWN;
	}
}
