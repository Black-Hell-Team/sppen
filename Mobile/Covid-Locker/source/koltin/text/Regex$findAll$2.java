package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\b\u0003"}, mo6274d2 = {"<anonymous>", "Lkotlin/text/MatchResult;", "p1", "invoke"}, mo6275k = 3, mo6276mv = {1, 1, 15})
/* compiled from: Regex.kt */
final /* synthetic */ class Regex$findAll$2 extends FunctionReference implements Function1<MatchResult, MatchResult> {
    public static final Regex$findAll$2 INSTANCE = new Regex$findAll$2();

    Regex$findAll$2() {
        super(1);
    }

    @Override // kotlin.jvm.internal.CallableReference, kotlin.reflect.KCallable
    public final String getName() {
        return "next";
    }

    @Override // kotlin.jvm.internal.CallableReference
    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(MatchResult.class);
    }

    @Override // kotlin.jvm.internal.CallableReference
    public final String getSignature() {
        return "next()Lkotlin/text/MatchResult;";
    }

    public final MatchResult invoke(MatchResult matchResult) {
        Intrinsics.checkParameterIsNotNull(matchResult, "p1");
        return matchResult.next();
    }
}
