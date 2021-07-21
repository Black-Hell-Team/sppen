package kotlin.internal;

import kotlin.Metadata;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedUtils;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, mo6274d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.internal.UProgressionUtilKt */
public final class UProgressionUtil {
    /* renamed from: differenceModulo-WZ9TVnA  reason: not valid java name */
    private static final int m923differenceModuloWZ9TVnA(int i, int i2, int i3) {
        int r1 = UnsignedUtils.m376uintRemainderJ1ME1BU(i, i3);
        int r2 = UnsignedUtils.m376uintRemainderJ1ME1BU(i2, i3);
        int uintCompare = UnsignedUtils.uintCompare(r1, r2);
        int r12 = UInt.m149constructorimpl(r1 - r2);
        return uintCompare >= 0 ? r12 : UInt.m149constructorimpl(r12 + i3);
    }

    /* renamed from: differenceModulo-sambcqE  reason: not valid java name */
    private static final long m924differenceModulosambcqE(long j, long j2, long j3) {
        long r1 = UnsignedUtils.m378ulongRemaindereb3DHEI(j, j3);
        long r3 = UnsignedUtils.m378ulongRemaindereb3DHEI(j2, j3);
        int ulongCompare = UnsignedUtils.ulongCompare(r1, r3);
        long r12 = ULong.m218constructorimpl(r1 - r3);
        return ulongCompare >= 0 ? r12 : ULong.m218constructorimpl(r12 + j3);
    }

    /* renamed from: getProgressionLastElement-Nkh28Cs  reason: not valid java name */
    public static final int m926getProgressionLastElementNkh28Cs(int i, int i2, int i3) {
        if (i3 > 0) {
            if (UnsignedUtils.uintCompare(i, i2) >= 0) {
                return i2;
            }
            return UInt.m149constructorimpl(i2 - m923differenceModuloWZ9TVnA(i2, i, UInt.m149constructorimpl(i3)));
        } else if (i3 < 0) {
            return UnsignedUtils.uintCompare(i, i2) <= 0 ? i2 : UInt.m149constructorimpl(i2 + m923differenceModuloWZ9TVnA(i, i2, UInt.m149constructorimpl(-i3)));
        } else {
            throw new IllegalArgumentException("Step is zero.");
        }
    }

    /* renamed from: getProgressionLastElement-7ftBX0g  reason: not valid java name */
    public static final long m925getProgressionLastElement7ftBX0g(long j, long j2, long j3) {
        if (j3 > 0) {
            if (UnsignedUtils.ulongCompare(j, j2) >= 0) {
                return j2;
            }
            return ULong.m218constructorimpl(j2 - m924differenceModulosambcqE(j2, j, ULong.m218constructorimpl(j3)));
        } else if (j3 < 0) {
            return UnsignedUtils.ulongCompare(j, j2) <= 0 ? j2 : ULong.m218constructorimpl(j2 + m924differenceModulosambcqE(j, j2, ULong.m218constructorimpl(-j3)));
        } else {
            throw new IllegalArgumentException("Step is zero.");
        }
    }
}
