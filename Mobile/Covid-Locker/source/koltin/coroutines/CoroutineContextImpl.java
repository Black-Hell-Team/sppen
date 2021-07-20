package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo6274d2 = {"<anonymous>", "", "acc", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke"}, mo6275k = 3, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.coroutines.CombinedContext$toString$1 */
final class CoroutineContextImpl extends Lambda implements Function2<String, CoroutineContext.Element, String> {
    public static final CoroutineContextImpl INSTANCE = new CoroutineContextImpl();

    CoroutineContextImpl() {
        super(2);
    }

    public final String invoke(String str, CoroutineContext.Element element) {
        Intrinsics.checkParameterIsNotNull(str, "acc");
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (str.length() == 0) {
            return element.toString();
        }
        return str + ", " + element;
    }
}
