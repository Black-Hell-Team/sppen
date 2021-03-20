package com.google.gdata.util.common.base;

public class PercentEscaper extends UnicodeEscaper {
    public static final String SAFECHARS_URLENCODER = "-_.*";
    public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
    public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
    private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private static final char[] URI_ESCAPED_SPACE = new char[]{'+'};
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    public PercentEscaper(String str, boolean z) {
        if (str.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        } else if (z && str.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        } else if (str.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        } else {
            this.plusForSpace = z;
            this.safeOctets = createSafeOctets(str);
        }
    }

    private static boolean[] createSafeOctets(String str) {
        char[] toCharArray = str.toCharArray();
        int i = 0;
        int i2 = 122;
        for (char max : toCharArray) {
            i2 = Math.max(max, i2);
        }
        boolean[] zArr = new boolean[(i2 + 1)];
        for (i2 = 48; i2 <= 57; i2++) {
            zArr[i2] = true;
        }
        for (i2 = 65; i2 <= 90; i2++) {
            zArr[i2] = true;
        }
        for (i2 = 97; i2 <= 122; i2++) {
            zArr[i2] = true;
        }
        int length = toCharArray.length;
        while (i < length) {
            zArr[toCharArray[i]] = true;
            i++;
        }
        return zArr;
    }

    /* Access modifiers changed, original: protected */
    public int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            boolean[] zArr = this.safeOctets;
            if (charAt >= zArr.length || !zArr[charAt]) {
                break;
            }
            i++;
        }
        return i;
    }

    public String escape(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            boolean[] zArr = this.safeOctets;
            if (charAt >= zArr.length || !zArr[charAt]) {
                return escapeSlow(str, i);
            }
        }
        return str;
    }

    /* Access modifiers changed, original: protected */
    public char[] escape(int i) {
        boolean[] zArr = this.safeOctets;
        if (i < zArr.length && zArr[i]) {
            return null;
        }
        if (i == 32 && this.plusForSpace) {
            return URI_ESCAPED_SPACE;
        }
        char[] cArr;
        char[] cArr2;
        if (i <= 127) {
            cArr = new char[3];
            cArr2 = UPPER_HEX_DIGITS;
            cArr[2] = cArr2[i & 15];
            cArr[1] = cArr2[i >>> 4];
            return cArr;
        } else if (i <= 2047) {
            cArr = new char[6];
            cArr2 = UPPER_HEX_DIGITS;
            cArr[5] = cArr2[i & 15];
            i >>>= 4;
            cArr[4] = cArr2[(i & 3) | 8];
            i >>>= 2;
            cArr[2] = cArr2[i & 15];
            cArr[1] = cArr2[(i >>> 4) | 12];
            return cArr;
        } else if (i <= 65535) {
            cArr = new char[9];
            cArr[0] = '%';
            cArr[1] = 'E';
            cArr[3] = '%';
            cArr[6] = '%';
            char[] cArr3 = UPPER_HEX_DIGITS;
            cArr[8] = cArr3[i & 15];
            i >>>= 4;
            cArr[7] = cArr3[(i & 3) | 8];
            i >>>= 2;
            cArr[5] = cArr3[i & 15];
            i >>>= 4;
            cArr[4] = cArr3[(i & 3) | 8];
            cArr[2] = cArr3[i >>> 2];
            return cArr;
        } else if (i <= 1114111) {
            cArr = new char[12];
            cArr[0] = '%';
            cArr[1] = 'F';
            cArr[3] = '%';
            cArr[6] = '%';
            cArr[9] = '%';
            cArr2 = UPPER_HEX_DIGITS;
            cArr[11] = cArr2[i & 15];
            i >>>= 4;
            cArr[10] = cArr2[(i & 3) | 8];
            i >>>= 2;
            cArr[8] = cArr2[i & 15];
            i >>>= 4;
            cArr[7] = cArr2[(i & 3) | 8];
            i >>>= 2;
            cArr[5] = cArr2[i & 15];
            i >>>= 4;
            cArr[4] = cArr2[(i & 3) | 8];
            cArr[2] = cArr2[(i >>> 2) & 7];
            return cArr;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid unicode character value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
