package kotlin.jvm.internal;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KTypeProjection;

/* access modifiers changed from: package-private */
@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo6274d2 = {"<anonymous>", "", "it", "Lkotlin/reflect/KTypeProjection;", "invoke"}, mo6275k = 3, mo6276mv = {1, 1, 15})
/* compiled from: TypeReference.kt */
public final class TypeReference$asString$args$1 extends Lambda implements Function1<KTypeProjection, String> {
    final /* synthetic */ TypeReference this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TypeReference$asString$args$1(TypeReference typeReference) {
        super(1);
        this.this$0 = typeReference;
    }

    public final String invoke(KTypeProjection kTypeProjection) {
        Intrinsics.checkParameterIsNotNull(kTypeProjection, "it");
        return this.this$0.asString(kTypeProjection);
    }
}
