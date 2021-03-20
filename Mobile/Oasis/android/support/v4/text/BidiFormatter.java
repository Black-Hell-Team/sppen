package android.support.v4.text;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.ViewDragHelper;
import java.util.Locale;

public final class BidiFormatter {
	private static final int DEFAULT_FLAGS = 2;
	private static final BidiFormatter DEFAULT_LTR_INSTANCE;
	private static final BidiFormatter DEFAULT_RTL_INSTANCE;
	private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = null;
	private static final int DIR_LTR = -1;
	private static final int DIR_RTL = 1;
	private static final int DIR_UNKNOWN = 0;
	private static final String EMPTY_STRING = "";
	private static final int FLAG_STEREO_RESET = 2;
	private static final char LRE = '\u202a';
	private static final char LRM = '\u200e';
	private static final String LRM_STRING;
	private static final char PDF = '\u202c';
	private static final char RLE = '\u202b';
	private static final char RLM = '\u200f';
	private static final String RLM_STRING;
	private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
	private final int mFlags;
	private final boolean mIsRtlContext;

	static /* synthetic */ class AnonymousClass_1 {
	}

	public static final class Builder {
		private int mFlags;
		private boolean mIsRtlContext;
		private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

		public Builder() {
			super();
			initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
		}

		public Builder(Locale locale) {
			super();
			initialize(BidiFormatter.isRtlLocale(locale));
		}

		public Builder(boolean rtlContext) {
			super();
			initialize(rtlContext);
		}

		private static BidiFormatter getDefaultInstanceFromContext(boolean isRtlContext) {
			if (isRtlContext) {
				return DEFAULT_RTL_INSTANCE;
			} else {
				return DEFAULT_LTR_INSTANCE;
			}
		}

		private void initialize(boolean isRtlContext) {
			mIsRtlContext = isRtlContext;
			mTextDirectionHeuristicCompat = DEFAULT_TEXT_DIRECTION_HEURISTIC;
			mFlags = 2;
		}

		public BidiFormatter build() {
			if (mFlags != 2 || mTextDirectionHeuristicCompat != DEFAULT_TEXT_DIRECTION_HEURISTIC) {
				return new BidiFormatter(mIsRtlContext, mFlags, mTextDirectionHeuristicCompat, null);
			} else {
				return getDefaultInstanceFromContext(mIsRtlContext);
			}
		}

		public BidiFormatter.Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat heuristic) {
			mTextDirectionHeuristicCompat = heuristic;
			return this;
		}

		public BidiFormatter.Builder stereoReset(boolean stereoReset) {
			if (stereoReset) {
				mFlags |= 2;
				return this;
			} else {
				mFlags &= -3;
				return this;
			}
		}
	}

	private static class DirectionalityEstimator {
		private static final byte[] DIR_TYPE_CACHE;
		private static final int DIR_TYPE_CACHE_SIZE = 1792;
		private int charIndex;
		private final boolean isHtml;
		private char lastChar;
		private final int length;
		private final String text;

		static {
			DIR_TYPE_CACHE = new byte[1792];
			int i = DIR_UNKNOWN;
			while (i < 1792) {
				DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
				i++;
			}
		}

		DirectionalityEstimator(String text, boolean isHtml) {
			super();
			this.text = text;
			this.isHtml = isHtml;
			length = text.length();
		}

		private static byte getCachedDirectionality(char c) {
			if (c < '\u0700') {
				return DIR_TYPE_CACHE[c];
			} else {
				return Character.getDirectionality(c);
			}
		}

		private byte skipEntityBackward() {
			int initialCharIndex = charIndex;
			while (charIndex > 0) {
				int r2i = charIndex - 1;
				charIndex = r2i;
				lastChar = text.charAt(r2i);
				if (lastChar == '&') {
					return (byte) 12;
				} else if (lastChar == ';') {
					charIndex = initialCharIndex;
					lastChar = ';';
					return (byte) 13;
				}
			}
			charIndex = initialCharIndex;
			lastChar = ';';
			return (byte) 13;
		}

		/* JADX WARNING: inconsistent code */
		/*
		private byte skipEntityForward() {
			r3_this = this;
		L_0x0000:
			r0 = r3.charIndex;
			r1 = r3.length;
			if (r0 >= r1) goto L_0x0018;
		L_0x0006:
			r0 = r3.text;
			r1 = r3.charIndex;
			r2 = r1 + 1;
			r3.charIndex = r2;
			r0 = r0.charAt(r1);
			r3.lastChar = r0;
			r1 = 59;
			if (r0 != r1) goto L_0x0000;
		L_0x0018:
			r0 = 12;
			return r0;
		}
		*/
		private byte skipEntityForward() {
			throw new UnsupportedOperationException("Method not decompiled: byte android.support.v4.text.BidiFormatter.DirectionalityEstimator.skipEntityForward()");
			// jadx: method processing error
/*
			Error: java.lang.StackOverflowError: Deep code hierarchy
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:57)
	at jadx.core.dex.visitors.DepthTraverser.visit(DepthTraverser.java:27)
	at jadx.core.dex.visitors.DepthTraverser.visit(DepthTraverser.java:15)
	at jadx.core.dex.visitors.DepthTraverser.visit(DepthTraverser.java:13)
	at jadx.core.ProcessClass.run(ProcessClass.java:29)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
	at java.lang.Thread.run(Thread.java:764)
*/
			/*
			private byte skipEntityForward() {
				r3_this = this;
			L_0x0000:
				r0 = r3.charIndex;
				r1 = r3.length;
				if (r0 >= r1) goto L_0x0018;
			L_0x0006:
				r0 = r3.text;
				r1 = r3.charIndex;
				r2 = r1 + 1;
				r3.charIndex = r2;
				r0 = r0.charAt(r1);
				r3.lastChar = r0;
				r1 = 59;
				if (r0 != r1) goto L_0x0000;
			L_0x0018:
				r0 = 12;
				return r0;
			}
			*/
		}

		private byte skipTagBackward() {
			int initialCharIndex = charIndex;
			while (charIndex > 0) {
				int r3i = charIndex - 1;
				charIndex = r3i;
				lastChar = text.charAt(r3i);
				if (lastChar == '<') {
					return (byte) 12;
				} else if (lastChar == '>') {
					charIndex = initialCharIndex;
					lastChar = '>';
					return (byte) 13;
				} else if (lastChar == '\"' || lastChar == '\'') {
					char quote = lastChar;
					while (charIndex > 0) {
						r3i = charIndex - 1;
						charIndex = r3i;
						char r2c = text.charAt(r3i);
						lastChar = r2c;
						if (r2c != quote) {
						}
					}
				}
			}
			charIndex = initialCharIndex;
			lastChar = '>';
			return (byte) 13;
		}

		private byte skipTagForward() {
			int initialCharIndex = charIndex;
			while (charIndex < length) {
				int r3i = charIndex;
				charIndex = r3i + 1;
				lastChar = text.charAt(r3i);
				if (lastChar == '>') {
					return (byte) 12;
				} else if (lastChar == '\"' || lastChar == '\'') {
					char quote = lastChar;
					while (charIndex < length) {
						r3i = charIndex;
						charIndex = r3i + 1;
						char r2c = text.charAt(r3i);
						lastChar = r2c;
						if (r2c != quote) {
						}
					}
				}
			}
			charIndex = initialCharIndex;
			lastChar = '<';
			return (byte) 13;
		}

		byte dirTypeBackward() {
			lastChar = text.charAt(charIndex - 1);
			if (Character.isLowSurrogate(lastChar)) {
				int codePoint = Character.codePointBefore(text, charIndex);
				charIndex -= Character.charCount(codePoint);
				return Character.getDirectionality(codePoint);
			} else {
				charIndex--;
				byte dirType = getCachedDirectionality(lastChar);
				if (isHtml) {
					if (lastChar == '>') {
						return skipTagBackward();
					} else if (lastChar == ';') {
						return skipEntityBackward();
					} else {
						return dirType;
					}
				} else {
					return dirType;
				}
			}
		}

		byte dirTypeForward() {
			lastChar = text.charAt(charIndex);
			if (Character.isHighSurrogate(lastChar)) {
				int codePoint = Character.codePointAt(text, charIndex);
				charIndex += Character.charCount(codePoint);
				return Character.getDirectionality(codePoint);
			} else {
				charIndex++;
				byte dirType = getCachedDirectionality(lastChar);
				if (isHtml) {
					if (lastChar == '<') {
						return skipTagForward();
					} else if (lastChar == '&') {
						return skipEntityForward();
					} else {
						return dirType;
					}
				} else {
					return dirType;
				}
			}
		}

		/* JADX WARNING: inconsistent code */
		/*
		int getEntryDir() {
			r8_this = this;
			r4 = 1;
			r3 = -1;
			r5 = 0;
			r8.charIndex = r5;
			r0 = 0;
			r1 = 0;
			r2 = 0;
		L_0x0008:
			r6 = r8.charIndex;
			r7 = r8.length;
			if (r6 >= r7) goto L_0x0031;
		L_0x000e:
			if (r2_firstNonEmptyEmbeddingLevel != 0) goto L_0x0031;
		L_0x0010:
			r6 = r8.dirTypeForward();
			switch(r6) {
				case 0: goto L_0x0025;
				case 1: goto L_0x002b;
				case 2: goto L_0x002b;
				case 3: goto L_0x0017;
				case 4: goto L_0x0017;
				case 5: goto L_0x0017;
				case 6: goto L_0x0017;
				case 7: goto L_0x0017;
				case 8: goto L_0x0017;
				case 9: goto L_0x0008;
				case 10: goto L_0x0017;
				case 11: goto L_0x0017;
				case 12: goto L_0x0017;
				case 13: goto L_0x0017;
				case 14: goto L_0x0019;
				case 15: goto L_0x0019;
				case 16: goto L_0x001d;
				case 17: goto L_0x001d;
				case 18: goto L_0x0021;
				default: goto L_0x0017;
			}
		L_0x0017:
			r2_firstNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x0019:
			r0_embeddingLevel++;
			r1_embeddingLevelDir = -1;
			goto L_0x0008;
		L_0x001d:
			r0_embeddingLevel++;
			r1_embeddingLevelDir = 1;
			goto L_0x0008;
		L_0x0021:
			r0_embeddingLevel++;
			r1_embeddingLevelDir = 0;
			goto L_0x0008;
		L_0x0025:
			if (r0_embeddingLevel != 0) goto L_0x0029;
		L_0x0027:
			r1_embeddingLevelDir = r3;
		L_0x0028:
			return r1_embeddingLevelDir;
		L_0x0029:
			r2_firstNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x002b:
			if (r0_embeddingLevel != 0) goto L_0x002f;
		L_0x002d:
			r1_embeddingLevelDir = r4;
			goto L_0x0028;
		L_0x002f:
			r2_firstNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x0031:
			if (r2_firstNonEmptyEmbeddingLevel != 0) goto L_0x0035;
		L_0x0033:
			r1_embeddingLevelDir = r5;
			goto L_0x0028;
		L_0x0035:
			if (r1_embeddingLevelDir != 0) goto L_0x0028;
		L_0x0037:
			r6 = r8.charIndex;
			if (r6 <= 0) goto L_0x0054;
		L_0x003b:
			r6 = r8.dirTypeBackward();
			switch(r6) {
				case 14: goto L_0x0043;
				case 15: goto L_0x0043;
				case 16: goto L_0x004a;
				case 17: goto L_0x004a;
				case 18: goto L_0x0051;
				default: goto L_0x0042;
			}
		L_0x0042:
			goto L_0x0037;
		L_0x0043:
			if (r2_firstNonEmptyEmbeddingLevel != r0_embeddingLevel) goto L_0x0047;
		L_0x0045:
			r1_embeddingLevelDir = r3;
			goto L_0x0028;
		L_0x0047:
			r0_embeddingLevel++;
			goto L_0x0037;
		L_0x004a:
			if (r2_firstNonEmptyEmbeddingLevel != r0_embeddingLevel) goto L_0x004e;
		L_0x004c:
			r1_embeddingLevelDir = r4;
			goto L_0x0028;
		L_0x004e:
			r0_embeddingLevel++;
			goto L_0x0037;
		L_0x0051:
			r0_embeddingLevel++;
			goto L_0x0037;
		L_0x0054:
			r1_embeddingLevelDir = r5;
			goto L_0x0028;
		}
		*/
		int getEntryDir() {
			charIndex = 0;
			int embeddingLevel = DIR_UNKNOWN;
			int embeddingLevelDir = DIR_UNKNOWN;
			int firstNonEmptyEmbeddingLevel = DIR_UNKNOWN;
			while (charIndex < length && firstNonEmptyEmbeddingLevel == 0) {
				switch(dirTypeForward()) {
				case DIR_UNKNOWN:
					if (embeddingLevel == 0) {
						return -1;
					} else {
						firstNonEmptyEmbeddingLevel = embeddingLevel;
					}
					break;
				case DIR_RTL:
				case FLAG_STEREO_RESET:
					if (embeddingLevel == 0) {
						return 1;
					} else {
						firstNonEmptyEmbeddingLevel = embeddingLevel;
					}
					break;
				case WindowCompat.FEATURE_ACTION_BAR_OVERLAY:
					break;
				case (byte) 14:
				case ViewDragHelper.EDGE_ALL:
					embeddingLevel++;
					embeddingLevelDir = DIR_LTR;
					break;
				case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
				case (byte) 17:
					embeddingLevel++;
					embeddingLevelDir = DIR_RTL;
					break;
				case (byte) 18:
					embeddingLevel--;
					embeddingLevelDir = DIR_UNKNOWN;
					break;
				default:
					firstNonEmptyEmbeddingLevel = embeddingLevel;
					break;
				}
			}
			if (embeddingLevelDir == 0) {
				while (charIndex > 0) {
					switch(dirTypeBackward()) {
					case (byte) 14:
					case ViewDragHelper.EDGE_ALL:
						if (firstNonEmptyEmbeddingLevel == embeddingLevel) {
							return -1;
						} else {
							embeddingLevel--;
						}
						break;
					case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
					case (byte) 17:
						if (firstNonEmptyEmbeddingLevel == embeddingLevel) {
							return 1;
						} else {
							embeddingLevel--;
						}
						break;
					case (byte) 18:
						embeddingLevel++;
						break;
					}
				}
				return 0;
			} else {
				return embeddingLevelDir;
			}
		}

		/* JADX WARNING: inconsistent code */
		/*
		int getExitDir() {
			r5_this = this;
			r3 = 1;
			r2 = -1;
			r4 = r5.length;
			r5.charIndex = r4;
			r0 = 0;
			r1 = 0;
		L_0x0008:
			r4 = r5.charIndex;
			if (r4 <= 0) goto L_0x0035;
		L_0x000c:
			r4 = r5.dirTypeBackward();
			switch(r4) {
				case 0: goto L_0x0017;
				case 1: goto L_0x0023;
				case 2: goto L_0x0023;
				case 3: goto L_0x0013;
				case 4: goto L_0x0013;
				case 5: goto L_0x0013;
				case 6: goto L_0x0013;
				case 7: goto L_0x0013;
				case 8: goto L_0x0013;
				case 9: goto L_0x0008;
				case 10: goto L_0x0013;
				case 11: goto L_0x0013;
				case 12: goto L_0x0013;
				case 13: goto L_0x0013;
				case 14: goto L_0x001e;
				case 15: goto L_0x001e;
				case 16: goto L_0x002b;
				case 17: goto L_0x002b;
				case 18: goto L_0x0032;
				default: goto L_0x0013;
			}
		L_0x0013:
			if (r1_lastNonEmptyEmbeddingLevel != 0) goto L_0x0008;
		L_0x0015:
			r1_lastNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x0017:
			if (r0_embeddingLevel != 0) goto L_0x001a;
		L_0x0019:
			return r2;
		L_0x001a:
			if (r1_lastNonEmptyEmbeddingLevel != 0) goto L_0x0008;
		L_0x001c:
			r1_lastNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x001e:
			if (r1_lastNonEmptyEmbeddingLevel == r0_embeddingLevel) goto L_0x0019;
		L_0x0020:
			r0_embeddingLevel++;
			goto L_0x0008;
		L_0x0023:
			if (r0_embeddingLevel != 0) goto L_0x0027;
		L_0x0025:
			r2 = r3;
			goto L_0x0019;
		L_0x0027:
			if (r1_lastNonEmptyEmbeddingLevel != 0) goto L_0x0008;
		L_0x0029:
			r1_lastNonEmptyEmbeddingLevel = r0_embeddingLevel;
			goto L_0x0008;
		L_0x002b:
			if (r1_lastNonEmptyEmbeddingLevel != r0_embeddingLevel) goto L_0x002f;
		L_0x002d:
			r2 = r3;
			goto L_0x0019;
		L_0x002f:
			r0_embeddingLevel++;
			goto L_0x0008;
		L_0x0032:
			r0_embeddingLevel++;
			goto L_0x0008;
		L_0x0035:
			r2 = 0;
			goto L_0x0019;
		}
		*/
		int getExitDir() {
			charIndex = length;
			int embeddingLevel = DIR_UNKNOWN;
			int lastNonEmptyEmbeddingLevel = DIR_UNKNOWN;
			while (charIndex > 0) {
				switch(dirTypeBackward()) {
				case DIR_UNKNOWN:
					if (embeddingLevel == 0) {
						return DIR_LTR;
					} else if (lastNonEmptyEmbeddingLevel == 0) {
						lastNonEmptyEmbeddingLevel = embeddingLevel;
					}
					break;
				case DIR_RTL:
				case FLAG_STEREO_RESET:
					if (embeddingLevel == 0) {
						return 1;
					} else if (lastNonEmptyEmbeddingLevel == 0) {
						lastNonEmptyEmbeddingLevel = embeddingLevel;
					}
					break;
				case WindowCompat.FEATURE_ACTION_BAR_OVERLAY:
					break;
				case (byte) 14:
				case ViewDragHelper.EDGE_ALL:
					if (lastNonEmptyEmbeddingLevel != embeddingLevel) {
						embeddingLevel--;
					}
					break;
				case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
				case (byte) 17:
					if (lastNonEmptyEmbeddingLevel == embeddingLevel) {
						return 1;
					} else {
						embeddingLevel--;
					}
					break;
				case (byte) 18:
					embeddingLevel++;
					break;
				default:
					if (lastNonEmptyEmbeddingLevel == 0) {
						lastNonEmptyEmbeddingLevel = embeddingLevel;
					}
					break;
				}
			}
			return DIR_UNKNOWN;
		}
	}


	static {
		DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
		LRM_STRING = Character.toString(LRM);
		RLM_STRING = Character.toString(RLM);
		DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
		DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
	}

	private BidiFormatter(boolean isRtlContext, int flags, TextDirectionHeuristicCompat heuristic) {
		super();
		mIsRtlContext = isRtlContext;
		mFlags = flags;
		mDefaultTextDirectionHeuristicCompat = heuristic;
	}

	/* synthetic */ BidiFormatter(boolean x0, int x1, TextDirectionHeuristicCompat x2, AnonymousClass_1 x3) {
		this(x0, x1, x2);
	}

	private static int getEntryDir(String str) {
		return new DirectionalityEstimator(str, false).getEntryDir();
	}

	private static int getExitDir(String str) {
		return new DirectionalityEstimator(str, false).getExitDir();
	}

	public static BidiFormatter getInstance() {
		return new Builder().build();
	}

	public static BidiFormatter getInstance(Locale locale) {
		return new Builder(locale).build();
	}

	public static BidiFormatter getInstance(boolean rtlContext) {
		return new Builder(rtlContext).build();
	}

	private static boolean isRtlLocale(Locale locale) {
		if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1) {
			return true;
		} else {
			return false;
		}
	}

	private String markAfter(String str, TextDirectionHeuristicCompat heuristic) {
		boolean isRtl = heuristic.isRtl((CharSequence)str, (int)DIR_UNKNOWN, str.length());
		if (!mIsRtlContext) {
			if (isRtl || getExitDir(str) == 1) {
				return LRM_STRING;
			}
		}
		if (mIsRtlContext) {
			if (!isRtl || getExitDir(str) == -1) {
				return RLM_STRING;
			}
		}
		return EMPTY_STRING;
	}

	private String markBefore(String str, TextDirectionHeuristicCompat heuristic) {
		boolean isRtl = heuristic.isRtl((CharSequence)str, (int)DIR_UNKNOWN, str.length());
		if (!mIsRtlContext) {
			if (isRtl || getEntryDir(str) == 1) {
				return LRM_STRING;
			}
		}
		if (mIsRtlContext) {
			if (!isRtl || getEntryDir(str) == -1) {
				return RLM_STRING;
			}
		}
		return EMPTY_STRING;
	}

	public boolean getStereoReset() {
		if ((mFlags & 2) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRtl(String str) {
		return mDefaultTextDirectionHeuristicCompat.isRtl((CharSequence)str, (int)DIR_UNKNOWN, str.length());
	}

	public boolean isRtlContext() {
		return mIsRtlContext;
	}

	public String unicodeWrap(String str) {
		return unicodeWrap(str, mDefaultTextDirectionHeuristicCompat, true);
	}

	public String unicodeWrap(String str, TextDirectionHeuristicCompat heuristic) {
		return unicodeWrap(str, heuristic, true);
	}

	public String unicodeWrap(String str, TextDirectionHeuristicCompat heuristic, boolean isolate) {
		boolean isRtl = heuristic.isRtl((CharSequence)str, (int)DIR_UNKNOWN, str.length());
		StringBuilder result = new StringBuilder();
		char r2c;
		TextDirectionHeuristicCompat r2_TextDirectionHeuristicCompat;
		if (!getStereoReset() || !isolate) {
			if (isRtl == mIsRtlContext) {
				if (!isRtl) {
					r2c = RLE;
				} else {
					r2c = LRE;
				}
				result.append(r2c);
				result.append(str);
				result.append(PDF);
			} else {
				result.append(str);
			}
			if (!isolate) {
				if (!isRtl) {
					r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
				} else {
					r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
				}
				result.append(markAfter(str, r2_TextDirectionHeuristicCompat));
			}
			return result.toString();
		} else {
			if (isRtl) {
				r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
			} else {
				r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
			}
			result.append(markBefore(str, r2_TextDirectionHeuristicCompat));
			if (isRtl == mIsRtlContext) {
				result.append(str);
			} else {
				if (!isRtl) {
					r2c = LRE;
				} else {
					r2c = RLE;
				}
				result.append(r2c);
				result.append(str);
				result.append(PDF);
			}
			if (!isolate) {
				return result.toString();
			} else {
				if (!isRtl) {
					r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
				} else {
					r2_TextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
				}
				result.append(markAfter(str, r2_TextDirectionHeuristicCompat));
				return result.toString();
			}
		}
	}

	public String unicodeWrap(String str, boolean isolate) {
		return unicodeWrap(str, mDefaultTextDirectionHeuristicCompat, isolate);
	}
}
