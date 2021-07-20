package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\n\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, mo6274d2 = {"<anonymous>", "", "T", "invoke"}, mo6275k = 3, mo6276mv = {1, 1, 15})
/* compiled from: _Collections.kt */
final class CollectionsKt___CollectionsKt$withIndex$1 extends Lambda implements Function0<Iterator<? extends T>> {
    final /* synthetic */ Iterable $this_withIndex;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    CollectionsKt___CollectionsKt$withIndex$1(Iterable iterable) {
        super(0);
        this.$this_withIndex = iterable;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Iterator<T> invoke() {
        return this.$this_withIndex.iterator();
    }
}
