package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\b\u001a\u001f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\n\u001a\"\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b0\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003¨\u0006\t"}, mo6274d2 = {"forEach", "", "T", "", "operation", "Lkotlin/Function1;", "iterator", "withIndex", "Lkotlin/collections/IndexedValue;", "kotlin-stdlib"}, mo6275k = 5, mo6276mv = {1, 1, 15}, mo6278xi = 1, mo6279xs = "kotlin/collections/CollectionsKt")
/* renamed from: kotlin.collections.CollectionsKt__IteratorsKt */
class Iterators extends IteratorsJVM {
    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.util.Iterator<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> Iterator<T> iterator(Iterator<? extends T> it) {
        Intrinsics.checkParameterIsNotNull(it, "$this$iterator");
        return it;
    }

    public static final <T> Iterator<IndexedValue<T>> withIndex(Iterator<? extends T> it) {
        Intrinsics.checkParameterIsNotNull(it, "$this$withIndex");
        return new IndexingIterator(it);
    }

    public static final <T> void forEach(Iterator<? extends T> it, Function1<? super T, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(it, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function1, "operation");
        while (it.hasNext()) {
            function1.invoke((Object) it.next());
        }
    }
}
