package kotlin.system;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0017\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\b\u001a\u0017\u0010\u0005\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\b¨\u0006\u0006"}, mo6274d2 = {"measureNanoTime", "", "block", "Lkotlin/Function0;", "", "measureTimeMillis", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.system.TimingKt */
public final class Timing {
    public static final long measureTimeMillis(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        long currentTimeMillis = System.currentTimeMillis();
        function0.invoke();
        return System.currentTimeMillis() - currentTimeMillis;
    }

    public static final long measureNanoTime(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        long nanoTime = System.nanoTime();
        function0.invoke();
        return System.nanoTime() - nanoTime;
    }
}
