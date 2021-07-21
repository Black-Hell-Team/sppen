package kotlin.random;

import kotlin.Metadata;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedUtils;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongRange;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0001ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007ø\u0001\u0000¢\u0006\u0002\u0010(\u0002\u0004\n\u0002\b\u0019¨\u0006)"}, mo6274d2 = {"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.random.URandomKt */
public final class URandom {
    public static final int nextUInt(Random random) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUInt");
        return UInt.m149constructorimpl(random.nextInt());
    }

    /* renamed from: nextUInt-qCasIEU  reason: not valid java name */
    public static final int m937nextUIntqCasIEU(Random random, int i) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUInt");
        return m936nextUInta8DCA5k(random, 0, i);
    }

    /* renamed from: nextUInt-a8DCA5k  reason: not valid java name */
    public static final int m936nextUInta8DCA5k(Random random, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUInt");
        m931checkUIntRangeBoundsJ1ME1BU(i, i2);
        return UInt.m149constructorimpl(random.nextInt(i ^ Integer.MIN_VALUE, i2 ^ Integer.MIN_VALUE) ^ Integer.MIN_VALUE);
    }

    public static final int nextUInt(Random random, UIntRange uIntRange) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUInt");
        Intrinsics.checkParameterIsNotNull(uIntRange, "range");
        if (uIntRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + uIntRange);
        } else if (UnsignedUtils.uintCompare(uIntRange.getLast(), -1) < 0) {
            return m936nextUInta8DCA5k(random, uIntRange.getFirst(), UInt.m149constructorimpl(uIntRange.getLast() + 1));
        } else {
            if (UnsignedUtils.uintCompare(uIntRange.getFirst(), 0) > 0) {
                return UInt.m149constructorimpl(m936nextUInta8DCA5k(random, UInt.m149constructorimpl(uIntRange.getFirst() - 1), uIntRange.getLast()) + 1);
            }
            return nextUInt(random);
        }
    }

    public static final long nextULong(Random random) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextULong");
        return ULong.m218constructorimpl(random.nextLong());
    }

    /* renamed from: nextULong-V1Xi4fY  reason: not valid java name */
    public static final long m938nextULongV1Xi4fY(Random random, long j) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextULong");
        return m939nextULongjmpaWc(random, 0, j);
    }

    /* renamed from: nextULong-jmpaW-c  reason: not valid java name */
    public static final long m939nextULongjmpaWc(Random random, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextULong");
        m932checkULongRangeBoundseb3DHEI(j, j2);
        return ULong.m218constructorimpl(random.nextLong(j ^ Long.MIN_VALUE, j2 ^ Long.MIN_VALUE) ^ Long.MIN_VALUE);
    }

    public static final long nextULong(Random random, ULongRange uLongRange) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextULong");
        Intrinsics.checkParameterIsNotNull(uLongRange, "range");
        if (uLongRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + uLongRange);
        } else if (UnsignedUtils.ulongCompare(uLongRange.getLast(), -1) < 0) {
            return m939nextULongjmpaWc(random, uLongRange.getFirst(), ULong.m218constructorimpl(uLongRange.getLast() + ULong.m218constructorimpl(4294967295L & ((long) 1))));
        } else {
            if (UnsignedUtils.ulongCompare(uLongRange.getFirst(), 0) <= 0) {
                return nextULong(random);
            }
            long j = 4294967295L & ((long) 1);
            return ULong.m218constructorimpl(m939nextULongjmpaWc(random, ULong.m218constructorimpl(uLongRange.getFirst() - ULong.m218constructorimpl(j)), uLongRange.getLast()) + ULong.m218constructorimpl(j));
        }
    }

    /* renamed from: nextUBytes-EVgfTAA  reason: not valid java name */
    public static final byte[] m933nextUBytesEVgfTAA(Random random, byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(bArr, "array");
        random.nextBytes(bArr);
        return bArr;
    }

    public static final byte[] nextUBytes(Random random, int i) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUBytes");
        return UByteArray.m126constructorimpl(random.nextBytes(i));
    }

    /* renamed from: nextUBytes-Wvrt4B4$default  reason: not valid java name */
    public static /* synthetic */ byte[] m935nextUBytesWvrt4B4$default(Random random, byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UByteArray.m132getSizeimpl(bArr);
        }
        return m934nextUBytesWvrt4B4(random, bArr, i, i2);
    }

    /* renamed from: nextUBytes-Wvrt4B4  reason: not valid java name */
    public static final byte[] m934nextUBytesWvrt4B4(Random random, byte[] bArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(bArr, "array");
        random.nextBytes(bArr, i, i2);
        return bArr;
    }

    /* renamed from: checkUIntRangeBounds-J1ME1BU  reason: not valid java name */
    public static final void m931checkUIntRangeBoundsJ1ME1BU(int i, int i2) {
        if (!(UnsignedUtils.uintCompare(i2, i) > 0)) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(UInt.m143boximpl(i), UInt.m143boximpl(i2)).toString());
        }
    }

    /* renamed from: checkULongRangeBounds-eb3DHEI  reason: not valid java name */
    public static final void m932checkULongRangeBoundseb3DHEI(long j, long j2) {
        if (!(UnsignedUtils.ulongCompare(j2, j) > 0)) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(ULong.m212boximpl(j), ULong.m212boximpl(j2)).toString());
        }
    }
}
