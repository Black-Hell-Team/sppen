package kotlin.collections;

import kotlin.Metadata;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u0005\u001a\u00020\u0006H\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo6274d2 = {"kotlin/collections/RingBuffer$iterator$1", "Lkotlin/collections/AbstractIterator;", "count", "", "index", "computeNext", "", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.collections.RingBuffer$iterator$1 */
public final class SlidingWindow extends AbstractIterator<T> {
    private int count;
    private int index;
    final /* synthetic */ RingBuffer this$0;

    /* JADX WARN: Incorrect args count in method signature: ()V */
    SlidingWindow(RingBuffer ringBuffer) {
        this.this$0 = ringBuffer;
        this.count = ringBuffer.size();
        this.index = RingBuffer.access$getStartIndex$p(ringBuffer);
    }

    /* access modifiers changed from: protected */
    @Override // kotlin.collections.AbstractIterator
    public void computeNext() {
        if (this.count == 0) {
            done();
            return;
        }
        setNext(RingBuffer.access$getBuffer$p(this.this$0)[this.index]);
        this.index = (this.index + 1) % RingBuffer.access$getCapacity$p(this.this$0);
        this.count--;
    }
}
