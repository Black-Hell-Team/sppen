package kotlin.collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* access modifiers changed from: package-private */
@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u00000\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0005\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a\u001f\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\f\u001a\u0015\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u0002H\b\u001a+\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u0015\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\b\u001a+\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a!\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\b¨\u0006\u0013"}, mo6274d2 = {"emptySet", "", "T", "hashSetOf", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/HashSet;", "linkedSetOf", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "([Ljava/lang/Object;)Ljava/util/LinkedHashSet;", "mutableSetOf", "", "([Ljava/lang/Object;)Ljava/util/Set;", "setOf", "optimizeReadOnlySet", "orEmpty", "kotlin-stdlib"}, mo6275k = 5, mo6276mv = {1, 1, 15}, mo6278xi = 1, mo6279xs = "kotlin/collections/SetsKt")
/* renamed from: kotlin.collections.SetsKt__SetsKt */
public class Sets extends SetsJVM {
    public static final <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }

    public static final <T> Set<T> setOf(T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return tArr.length > 0 ? ArraysKt.toSet(tArr) : SetsKt.emptySet();
    }

    private static final <T> Set<T> setOf() {
        return SetsKt.emptySet();
    }

    private static final <T> Set<T> mutableSetOf() {
        return new LinkedHashSet();
    }

    public static final <T> Set<T> mutableSetOf(T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (Set) ArraysKt.toCollection(tArr, new LinkedHashSet(MapsKt.mapCapacity(tArr.length)));
    }

    private static final <T> HashSet<T> hashSetOf() {
        return new HashSet<>();
    }

    public static final <T> HashSet<T> hashSetOf(T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (HashSet) ArraysKt.toCollection(tArr, new HashSet(MapsKt.mapCapacity(tArr.length)));
    }

    private static final <T> LinkedHashSet<T> linkedSetOf() {
        return new LinkedHashSet<>();
    }

    public static final <T> LinkedHashSet<T> linkedSetOf(T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (LinkedHashSet) ArraysKt.toCollection(tArr, new LinkedHashSet(MapsKt.mapCapacity(tArr.length)));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: java.util.Set<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> Set<T> orEmpty(Set<? extends T> set) {
        return set != 0 ? set : SetsKt.emptySet();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.util.Set<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Set<T> optimizeReadOnlySet(Set<? extends T> set) {
        Intrinsics.checkParameterIsNotNull(set, "$this$optimizeReadOnlySet");
        int size = set.size();
        if (size == 0) {
            return SetsKt.emptySet();
        }
        if (size != 1) {
            return set;
        }
        return SetsKt.setOf(set.iterator().next());
    }
}
