package kotlin;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\bø\u0001\u0000¢\u0006\u0002\u0010\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0004H\bø\u0001\u0000¢\u0006\u0002\u0010\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0006H\bø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\bH\bø\u0001\u0000¢\u0006\u0002\u0010\t\u0002\u0004\n\u0002\b\u0019¨\u0006\n"}, mo6274d2 = {"toUByte", "Lkotlin/UByte;", "", "(B)B", "", "(I)B", "", "(J)B", "", "(S)B", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* compiled from: UByte.kt */
public final class UByteKt {
    private static final byte toUByte(byte b) {
        return UByte.m82constructorimpl(b);
    }

    private static final byte toUByte(short s) {
        return UByte.m82constructorimpl((byte) s);
    }

    private static final byte toUByte(int i) {
        return UByte.m82constructorimpl((byte) i);
    }

    private static final byte toUByte(long j) {
        return UByte.m82constructorimpl((byte) ((int) j));
    }
}
