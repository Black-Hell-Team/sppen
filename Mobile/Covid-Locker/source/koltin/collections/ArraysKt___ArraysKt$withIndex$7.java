package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.ArrayIterators;
import kotlin.jvm.internal.Lambda;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo6274d2 = {"<anonymous>", "Lkotlin/collections/DoubleIterator;", "invoke"}, mo6275k = 3, mo6276mv = {1, 1, 15})
/* compiled from: _Arrays.kt */
final class ArraysKt___ArraysKt$withIndex$7 extends Lambda implements Function0<DoubleIterator> {
    final /* synthetic */ double[] $this_withIndex;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ArraysKt___ArraysKt$withIndex$7(double[] dArr) {
        super(0);
        this.$this_withIndex = dArr;
    }

    @Override // kotlin.jvm.functions.Function0
    public final DoubleIterator invoke() {
        return ArrayIterators.iterator(this.$this_withIndex);
    }
}
