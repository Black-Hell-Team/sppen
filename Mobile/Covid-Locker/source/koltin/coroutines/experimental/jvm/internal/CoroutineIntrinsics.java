package kotlin.coroutines.experimental.jvm.internal;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a \u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001¨\u0006\u0007"}, mo6274d2 = {"interceptContinuationIfNeeded", "Lkotlin/coroutines/experimental/Continuation;", "T", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "continuation", "normalizeContinuation", "kotlin-stdlib-coroutines"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* compiled from: CoroutineIntrinsics.kt */
public final class CoroutineIntrinsics {
    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.coroutines.experimental.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Continuation<T> normalizeContinuation(Continuation<? super T> continuation) {
        Continuation<T> continuation2;
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        CoroutineImpl coroutineImpl = !(continuation instanceof CoroutineImpl) ? null : continuation;
        return (coroutineImpl == null || (continuation2 = (Continuation<T>) coroutineImpl.getFacade()) == null) ? continuation : continuation2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.coroutines.experimental.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Continuation<T> interceptContinuationIfNeeded(CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        Continuation<T> interceptContinuation;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) coroutineContext.get(ContinuationInterceptor.Key);
        return (continuationInterceptor == null || (interceptContinuation = continuationInterceptor.interceptContinuation(continuation)) == null) ? continuation : interceptContinuation;
    }
}
