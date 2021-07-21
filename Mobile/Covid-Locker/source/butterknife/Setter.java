package butterknife;

import android.view.View;

public interface Setter<T extends View, V> {
    void set(T t, V v, int i);
}
