package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.ArrayIterators;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0002¨\u0006\u0004¸\u0006\u0000"}, mo6274d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* compiled from: Iterables.kt */
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$6 implements Iterable<Float>, KMappedMarker {
    final /* synthetic */ float[] $this_asIterable$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$6(float[] fArr) {
        this.$this_asIterable$inlined = fArr;
    }

    @Override // java.lang.Iterable
    public Iterator<Float> iterator() {
        return ArrayIterators.iterator(this.$this_asIterable$inlined);
    }
}
