package kotlin.text;

import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* access modifiers changed from: package-private */
@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0010\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006*\u00020\u0002¨\u0006\u0007"}, mo6274d2 = {"elementAt", "", "", "index", "", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, mo6275k = 5, mo6276mv = {1, 1, 15}, mo6278xi = 1, mo6279xs = "kotlin/text/StringsKt")
/* renamed from: kotlin.text.StringsKt___StringsJvmKt */
public class _StringsJvm extends StringsKt__StringsKt {
    private static final char elementAt(CharSequence charSequence, int i) {
        return charSequence.charAt(i);
    }

    public static final SortedSet<Character> toSortedSet(CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$this$toSortedSet");
        return (SortedSet) StringsKt.toCollection(charSequence, new TreeSet());
    }
}
