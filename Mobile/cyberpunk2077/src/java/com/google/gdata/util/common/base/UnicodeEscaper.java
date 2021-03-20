package com.google.gdata.util.common.base;

import java.io.IOException;

public abstract class UnicodeEscaper implements Escaper {
    private static final int DEST_PAD = 32;
    private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() {
        /* Access modifiers changed, original: protected */
        public char[] initialValue() {
            return new char[1024];
        }
    };

    public abstract char[] escape(int i);

    /* Access modifiers changed, original: protected */
    public int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            int codePointAt = codePointAt(charSequence, i, i2);
            if (codePointAt < 0 || escape(codePointAt) != null) {
                break;
            }
            i += Character.isSupplementaryCodePoint(codePointAt) ? 2 : 1;
        }
        return i;
    }

    public String escape(String str) {
        int length = str.length();
        int nextEscapeIndex = nextEscapeIndex(str, 0, length);
        return nextEscapeIndex == length ? str : escapeSlow(str, nextEscapeIndex);
    }

    /* Access modifiers changed, original: protected|final */
    public final String escapeSlow(String str, int i) {
        int length = str.length();
        char[] cArr = (char[]) DEST_TL.get();
        int i2 = 0;
        int i3 = i2;
        while (i < length) {
            int codePointAt = codePointAt(str, i, length);
            if (codePointAt >= 0) {
                char[] escape = escape(codePointAt);
                if (escape != null) {
                    int i4 = i - i2;
                    int i5 = i3 + i4;
                    int length2 = escape.length + i5;
                    if (cArr.length < length2) {
                        cArr = growBuffer(cArr, i3, (length2 + (length - i)) + DEST_PAD);
                    }
                    if (i4 > 0) {
                        str.getChars(i2, i, cArr, i3);
                        i3 = i5;
                    }
                    if (escape.length > 0) {
                        System.arraycopy(escape, 0, cArr, i3, escape.length);
                        i3 += escape.length;
                    }
                }
                i2 = (Character.isSupplementaryCodePoint(codePointAt) ? 2 : 1) + i;
                i = nextEscapeIndex(str, i2, length);
            } else {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
        }
        i = length - i2;
        if (i > 0) {
            i += i3;
            if (cArr.length < i) {
                cArr = growBuffer(cArr, i3, i);
            }
            str.getChars(i2, length, cArr, i3);
            i3 = i;
        }
        return new String(cArr, 0, i3);
    }

    public Appendable escape(final Appendable appendable) {
        Preconditions.checkNotNull(appendable);
        return new Appendable() {
            char[] decodedChars = new char[2];
            int pendingHighSurrogate = -1;

            public Appendable append(CharSequence charSequence) throws IOException {
                return append(charSequence, 0, charSequence.length());
            }

            public Appendable append(CharSequence charSequence, int i, int i2) throws IOException {
                if (i < i2) {
                    int i3;
                    if (this.pendingHighSurrogate != -1) {
                        i3 = i + 1;
                        char charAt = charSequence.charAt(i);
                        if (Character.isLowSurrogate(charAt)) {
                            char[] escape = UnicodeEscaper.this.escape(Character.toCodePoint((char) this.pendingHighSurrogate, charAt));
                            if (escape != null) {
                                outputChars(escape, escape.length);
                                i = i3;
                            } else {
                                appendable.append((char) this.pendingHighSurrogate);
                            }
                            this.pendingHighSurrogate = -1;
                            int i4 = i3;
                            i3 = i;
                            i = i4;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Expected low surrogate character but got ");
                            stringBuilder.append(charAt);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    i3 = i;
                    while (true) {
                        i = UnicodeEscaper.this.nextEscapeIndex(charSequence, i, i2);
                        if (i > i3) {
                            appendable.append(charSequence, i3, i);
                        }
                        if (i == i2) {
                            break;
                        }
                        i3 = UnicodeEscaper.codePointAt(charSequence, i, i2);
                        if (i3 < 0) {
                            this.pendingHighSurrogate = -i3;
                            break;
                        }
                        char[] escape2 = UnicodeEscaper.this.escape(i3);
                        if (escape2 != null) {
                            outputChars(escape2, escape2.length);
                        } else {
                            outputChars(this.decodedChars, Character.toChars(i3, this.decodedChars, 0));
                        }
                        i3 = (Character.isSupplementaryCodePoint(i3) ? 2 : 1) + i;
                        i = i3;
                    }
                }
                return this;
            }

            public Appendable append(char c) throws IOException {
                String str = "' with value ";
                char[] escape;
                StringBuilder stringBuilder;
                if (this.pendingHighSurrogate != -1) {
                    if (Character.isLowSurrogate(c)) {
                        escape = UnicodeEscaper.this.escape(Character.toCodePoint((char) this.pendingHighSurrogate, c));
                        if (escape != null) {
                            outputChars(escape, escape.length);
                        } else {
                            appendable.append((char) this.pendingHighSurrogate);
                            appendable.append(c);
                        }
                        this.pendingHighSurrogate = -1;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected low surrogate character but got '");
                        stringBuilder.append(c);
                        stringBuilder.append(str);
                        stringBuilder.append(c);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                } else if (Character.isHighSurrogate(c)) {
                    this.pendingHighSurrogate = c;
                } else if (Character.isLowSurrogate(c)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected low surrogate character '");
                    stringBuilder.append(c);
                    stringBuilder.append(str);
                    stringBuilder.append(c);
                    throw new IllegalArgumentException(stringBuilder.toString());
                } else {
                    escape = UnicodeEscaper.this.escape((int) c);
                    if (escape != null) {
                        outputChars(escape, escape.length);
                    } else {
                        appendable.append(c);
                    }
                }
                return this;
            }

            private void outputChars(char[] cArr, int i) throws IOException {
                for (int i2 = 0; i2 < i; i2++) {
                    appendable.append(cArr[i2]);
                }
            }
        };
    }

    protected static final int codePointAt(CharSequence charSequence, int i, int i2) {
        if (i < i2) {
            int i3 = i + 1;
            char charAt = charSequence.charAt(i);
            if (charAt < 55296 || charAt > 57343) {
                return charAt;
            }
            String str = " at index ";
            String str2 = "' with value ";
            StringBuilder stringBuilder;
            if (charAt > 56319) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected low surrogate character '");
                stringBuilder.append(charAt);
                stringBuilder.append(str2);
                stringBuilder.append(charAt);
                stringBuilder.append(str);
                stringBuilder.append(i3 - 1);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (i3 == i2) {
                return -charAt;
            } else {
                char charAt2 = charSequence.charAt(i3);
                if (Character.isLowSurrogate(charAt2)) {
                    return Character.toCodePoint(charAt, charAt2);
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Expected low surrogate but got char '");
                stringBuilder.append(charAt2);
                stringBuilder.append(str2);
                stringBuilder.append(charAt2);
                stringBuilder.append(str);
                stringBuilder.append(i3);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static final char[] growBuffer(char[] cArr, int i, int i2) {
        char[] cArr2 = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, cArr2, 0, i);
        }
        return cArr2;
    }
}
