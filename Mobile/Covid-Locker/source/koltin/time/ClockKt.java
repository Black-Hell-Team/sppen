package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\n\u001a\u001d\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\nø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, mo6274d2 = {"compareTo", "", "Lkotlin/time/ClockMark;", "other", "minus", "Lkotlin/time/Duration;", "(Lkotlin/time/ClockMark;Lkotlin/time/ClockMark;)D", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* compiled from: Clock.kt */
public final class ClockKt {
    @Deprecated(level = DeprecationLevel.ERROR, message = "Subtracting one ClockMark from another is not a well defined operation because these clock marks could have been obtained from the different clocks.")
    private static final double minus(ClockMark clockMark, ClockMark clockMark2) {
        Intrinsics.checkParameterIsNotNull(clockMark, "$this$minus");
        throw new Error("Operation is disallowed.");
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Comparing one ClockMark to another is not a well defined operation because these clock marks could have been obtained from the different clocks.")
    private static final int compareTo(ClockMark clockMark, ClockMark clockMark2) {
        Intrinsics.checkParameterIsNotNull(clockMark, "$this$compareTo");
        throw new Error("Operation is disallowed.");
    }
}
