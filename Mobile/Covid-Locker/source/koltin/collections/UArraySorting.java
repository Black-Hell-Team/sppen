package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedUtils;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\bH\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000bH\u0001ø\u0001\u0000¢\u0006\u0004\b \u0010!\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\"\u0010#\u0002\u0004\n\u0002\b\u0019¨\u0006$"}, mo6274d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "sortArray-GBYM_sE", "([B)V", "sortArray--ajY-9A", "([I)V", "sortArray-QwZRm1k", "([J)V", "sortArray-rL5Bavg", "([S)V", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.collections.UArraySortingKt */
public final class UArraySorting {
    /* renamed from: partition-4UcCI2c  reason: not valid java name */
    private static final int m384partition4UcCI2c(byte[] bArr, int i, int i2) {
        int i3;
        byte b = UByteArray.m131getimpl(bArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int i4 = UByteArray.m131getimpl(bArr, i) & UByte.MAX_VALUE;
                i3 = b & UByte.MAX_VALUE;
                if (Intrinsics.compare(i4, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UByteArray.m131getimpl(bArr, i2) & UByte.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte b2 = UByteArray.m131getimpl(bArr, i);
                UByteArray.m136setVurrAj0(bArr, i, UByteArray.m131getimpl(bArr, i2));
                UByteArray.m136setVurrAj0(bArr, i2, b2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-4UcCI2c  reason: not valid java name */
    private static final void m388quickSort4UcCI2c(byte[] bArr, int i, int i2) {
        int r0 = m384partition4UcCI2c(bArr, i, i2);
        int i3 = r0 - 1;
        if (i < i3) {
            m388quickSort4UcCI2c(bArr, i, i3);
        }
        if (r0 < i2) {
            m388quickSort4UcCI2c(bArr, r0, i2);
        }
    }

    /* renamed from: partition-Aa5vz7o  reason: not valid java name */
    private static final int m385partitionAa5vz7o(short[] sArr, int i, int i2) {
        int i3;
        short s = UShortArray.m364getimpl(sArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int i4 = UShortArray.m364getimpl(sArr, i) & UShort.MAX_VALUE;
                i3 = s & UShort.MAX_VALUE;
                if (Intrinsics.compare(i4, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UShortArray.m364getimpl(sArr, i2) & UShort.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                short s2 = UShortArray.m364getimpl(sArr, i);
                UShortArray.m369set01HTLdE(sArr, i, UShortArray.m364getimpl(sArr, i2));
                UShortArray.m369set01HTLdE(sArr, i2, s2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-Aa5vz7o  reason: not valid java name */
    private static final void m389quickSortAa5vz7o(short[] sArr, int i, int i2) {
        int r0 = m385partitionAa5vz7o(sArr, i, i2);
        int i3 = r0 - 1;
        if (i < i3) {
            m389quickSortAa5vz7o(sArr, i, i3);
        }
        if (r0 < i2) {
            m389quickSortAa5vz7o(sArr, r0, i2);
        }
    }

    /* renamed from: partition-oBK06Vg  reason: not valid java name */
    private static final int m386partitionoBK06Vg(int[] iArr, int i, int i2) {
        int i3 = UIntArray.m200getimpl(iArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedUtils.uintCompare(UIntArray.m200getimpl(iArr, i), i3) < 0) {
                i++;
            }
            while (UnsignedUtils.uintCompare(UIntArray.m200getimpl(iArr, i2), i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                int i4 = UIntArray.m200getimpl(iArr, i);
                UIntArray.m205setVXSXFK8(iArr, i, UIntArray.m200getimpl(iArr, i2));
                UIntArray.m205setVXSXFK8(iArr, i2, i4);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-oBK06Vg  reason: not valid java name */
    private static final void m390quickSortoBK06Vg(int[] iArr, int i, int i2) {
        int r0 = m386partitionoBK06Vg(iArr, i, i2);
        int i3 = r0 - 1;
        if (i < i3) {
            m390quickSortoBK06Vg(iArr, i, i3);
        }
        if (r0 < i2) {
            m390quickSortoBK06Vg(iArr, r0, i2);
        }
    }

    /* renamed from: partition--nroSd4  reason: not valid java name */
    private static final int m383partitionnroSd4(long[] jArr, int i, int i2) {
        long j = ULongArray.m269getimpl(jArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedUtils.ulongCompare(ULongArray.m269getimpl(jArr, i), j) < 0) {
                i++;
            }
            while (UnsignedUtils.ulongCompare(ULongArray.m269getimpl(jArr, i2), j) > 0) {
                i2--;
            }
            if (i <= i2) {
                long j2 = ULongArray.m269getimpl(jArr, i);
                ULongArray.m274setk8EXiF4(jArr, i, ULongArray.m269getimpl(jArr, i2));
                ULongArray.m274setk8EXiF4(jArr, i2, j2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort--nroSd4  reason: not valid java name */
    private static final void m387quickSortnroSd4(long[] jArr, int i, int i2) {
        int r0 = m383partitionnroSd4(jArr, i, i2);
        int i3 = r0 - 1;
        if (i < i3) {
            m387quickSortnroSd4(jArr, i, i3);
        }
        if (r0 < i2) {
            m387quickSortnroSd4(jArr, r0, i2);
        }
    }

    /* renamed from: sortArray-GBYM_sE  reason: not valid java name */
    public static final void m392sortArrayGBYM_sE(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "array");
        m388quickSort4UcCI2c(bArr, 0, UByteArray.m132getSizeimpl(bArr) - 1);
    }

    /* renamed from: sortArray-rL5Bavg  reason: not valid java name */
    public static final void m394sortArrayrL5Bavg(short[] sArr) {
        Intrinsics.checkParameterIsNotNull(sArr, "array");
        m389quickSortAa5vz7o(sArr, 0, UShortArray.m365getSizeimpl(sArr) - 1);
    }

    /* renamed from: sortArray--ajY-9A  reason: not valid java name */
    public static final void m391sortArrayajY9A(int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "array");
        m390quickSortoBK06Vg(iArr, 0, UIntArray.m201getSizeimpl(iArr) - 1);
    }

    /* renamed from: sortArray-QwZRm1k  reason: not valid java name */
    public static final void m393sortArrayQwZRm1k(long[] jArr) {
        Intrinsics.checkParameterIsNotNull(jArr, "array");
        m387quickSortnroSd4(jArr, 0, ULongArray.m270getSizeimpl(jArr) - 1);
    }
}
