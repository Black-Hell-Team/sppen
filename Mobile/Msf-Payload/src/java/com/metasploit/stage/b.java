package com.metasploit.stage;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public final class b {
    private static final long a = TimeUnit.SECONDS.toMillis(1);

    private static int a(byte[] bArr, int i) {
        int i2 = 0;
        int i3 = 0;
        while (i2 < 4) {
            i3 |= (bArr[i2 + i] & 255) << (i2 << 3);
            i2++;
        }
        return i3;
    }

    public static a a(byte[] bArr) {
        a aVar = new a();
        aVar.a = a(bArr, 0);
        aVar.b = a * ((long) a(bArr, 12));
        b(bArr, 16, 16);
        b(bArr, 32, 16);
        int i = 48;
        if ((aVar.a & 1) != 0) {
            aVar.c = a(bArr, 8000, 100);
        }
        while (bArr[i] != (byte) 0) {
            g gVar = new g();
            gVar.a = a(bArr, i, 512);
            i = (i + 512) + 4;
            gVar.b = a * ((long) a(bArr, i));
            i += 4;
            gVar.c = a * ((long) a(bArr, i));
            i += 4;
            if (gVar.a.startsWith("http")) {
                a(bArr, i, 128);
                i += 128;
                a(bArr, i, 64);
                i += 64;
                a(bArr, i, 64);
                i += 64;
                gVar.d = a(bArr, i, 256);
                i += 256;
                gVar.e = null;
                byte[] b = b(bArr, i, 20);
                int i2 = i + 20;
                for (byte b2 : b) {
                    if (b2 != (byte) 0) {
                        gVar.e = b;
                        break;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                int length = bArr.length;
                for (i = i2; i < length; i++) {
                    byte b3 = bArr[i];
                    if (b3 == (byte) 0) {
                        break;
                    }
                    stringBuilder.append((char) (b3 & 255));
                }
                String stringBuilder2 = stringBuilder.toString();
                gVar.f = stringBuilder2;
                i = stringBuilder2.length() + i2;
            }
            aVar.d.add(gVar);
        }
        return aVar;
    }

    private static String a(byte[] bArr, int i, int i2) {
        byte[] b = b(bArr, i, i2);
        try {
            return new String(b, "ISO-8859-1").trim();
        } catch (UnsupportedEncodingException e) {
            return new String(b).trim();
        }
    }

    private static byte[] b(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }
}
