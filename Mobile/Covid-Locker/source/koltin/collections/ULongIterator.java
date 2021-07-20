package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0005\b'\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\f\u0010\u0004\u001a\u00020\u0002H\u0002ø\u0001\u0000J\u0010\u0010\u0005\u001a\u00020\u0002H&ø\u0001\u0000¢\u0006\u0002\u0010\u0006ø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, mo6274d2 = {"Lkotlin/collections/ULongIterator;", "", "Lkotlin/ULong;", "()V", "next", "nextULong", "()J", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* compiled from: UIterators.kt */
public abstract class ULongIterator implements Iterator<ULong>, KMappedMarker {
    public abstract long nextULong();

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public final ULong next() {
        return ULong.m212boximpl(nextULong());
    }
}
