package kotlin.p004io;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0017\n\u0000\n\u0002\u0010(\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0006\u001a\u00020\u0004H\u0002J\t\u0010\u0007\u001a\u00020\u0002H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0002X\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, mo6274d2 = {"kotlin/io/LinesSequence$iterator$1", "", "", "done", "", "nextValue", "hasNext", "next", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.io.LinesSequence$iterator$1 */
public final class ReadWrite implements Iterator<String>, KMappedMarker {
    private boolean done;
    private String nextValue;
    final /* synthetic */ LinesSequence this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX WARN: Incorrect args count in method signature: ()V */
    ReadWrite(LinesSequence linesSequence) {
        this.this$0 = linesSequence;
    }

    public boolean hasNext() {
        if (this.nextValue == null && !this.done) {
            String readLine = LinesSequence.access$getReader$p(this.this$0).readLine();
            this.nextValue = readLine;
            if (readLine == null) {
                this.done = true;
            }
        }
        if (this.nextValue != null) {
            return true;
        }
        return false;
    }

    @Override // java.util.Iterator
    public String next() {
        if (hasNext()) {
            String str = this.nextValue;
            this.nextValue = null;
            if (str == null) {
                Intrinsics.throwNpe();
            }
            return str;
        }
        throw new NoSuchElementException();
    }
}
