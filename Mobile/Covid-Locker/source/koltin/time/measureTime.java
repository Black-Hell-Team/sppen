package kotlin.time;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\bø\u0001\u0000\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u001a0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\b\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\bø\u0001\u0000\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u001a4\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\b\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"}, mo6274d2 = {"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)D", "measureTimedValue", "Lkotlin/time/TimedValue;", "T", "Lkotlin/time/Clock;", "(Lkotlin/time/Clock;Lkotlin/jvm/functions/Function0;)D", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.time.MeasureTimeKt */
public final class measureTime {
    public static final double measureTime(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        ClockMark markNow = MonoClock.INSTANCE.markNow();
        function0.invoke();
        return markNow.elapsedNow();
    }

    public static final double measureTime(Clock clock, Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(clock, "$this$measureTime");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        ClockMark markNow = clock.markNow();
        function0.invoke();
        return markNow.elapsedNow();
    }

    public static final <T> TimedValue<T> measureTimedValue(Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        return new TimedValue<>(function0.invoke(), MonoClock.INSTANCE.markNow().elapsedNow(), null);
    }

    public static final <T> TimedValue<T> measureTimedValue(Clock clock, Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(clock, "$this$measureTimedValue");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        return new TimedValue<>(function0.invoke(), clock.markNow().elapsedNow(), null);
    }
}
