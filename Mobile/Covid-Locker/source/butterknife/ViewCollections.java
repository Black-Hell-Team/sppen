package butterknife;

import android.util.Property;
import android.view.View;
import java.util.List;

public final class ViewCollections {
    @SafeVarargs
    public static <T extends View> void run(List<T> list, Action<? super T>... actionArr) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (Action<? super T> action : actionArr) {
                action.apply(list.get(i), i);
            }
        }
    }

    @SafeVarargs
    public static <T extends View> void run(T[] tArr, Action<? super T>... actionArr) {
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            for (Action<? super T> action : actionArr) {
                action.apply(tArr[i], i);
            }
        }
    }

    public static <T extends View> void run(List<T> list, Action<? super T> action) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            action.apply(list.get(i), i);
        }
    }

    public static <T extends View> void run(T[] tArr, Action<? super T> action) {
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            action.apply(tArr[i], i);
        }
    }

    @SafeVarargs
    public static <T extends View> void run(T t, Action<? super T>... actionArr) {
        for (Action<? super T> action : actionArr) {
            action.apply(t, 0);
        }
    }

    public static <T extends View> void run(T t, Action<? super T> action) {
        action.apply(t, 0);
    }

    public static <T extends View, V> void set(List<T> list, Setter<? super T, V> setter, V v) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            setter.set(list.get(i), v, i);
        }
    }

    public static <T extends View, V> void set(T[] tArr, Setter<? super T, V> setter, V v) {
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            setter.set(tArr[i], v, i);
        }
    }

    public static <T extends View, V> void set(T t, Setter<? super T, V> setter, V v) {
        setter.set(t, v, 0);
    }

    public static <T extends View, V> void set(List<T> list, Property<? super T, V> property, V v) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            property.set(list.get(i), v);
        }
    }

    public static <T extends View, V> void set(T[] tArr, Property<? super T, V> property, V v) {
        for (T t : tArr) {
            property.set(t, v);
        }
    }

    public static <T extends View, V> void set(T t, Property<? super T, V> property, V v) {
        property.set(t, v);
    }

    private ViewCollections() {
    }
}
