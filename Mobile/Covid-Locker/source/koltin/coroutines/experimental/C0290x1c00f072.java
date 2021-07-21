package kotlin.coroutines.experimental;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0002¨\u0006\u0004¸\u0006\u0000"}, mo6274d2 = {"kotlin/sequences/SequencesKt__SequencesKt$Sequence$1", "Lkotlin/sequences/Sequence;", "iterator", "", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.coroutines.experimental.SequenceBuilderKt__SequenceBuilderKt$buildSequence$$inlined$Sequence$1 */
/* compiled from: Sequences.kt */
public final class C0290x1c00f072 implements Sequence<T> {
    final /* synthetic */ Function2 $builderAction$inlined;

    public C0290x1c00f072(Function2 function2) {
        this.$builderAction$inlined = function2;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return SequenceBuilderKt.buildIterator(this.$builderAction$inlined);
    }
}
