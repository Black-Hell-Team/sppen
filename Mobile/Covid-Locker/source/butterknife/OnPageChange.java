package butterknife;

import butterknife.internal.ListenerClass;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@ListenerClass(callbacks = Callback.class, remover = "removeOnPageChangeListener", setter = "addOnPageChangeListener", targetType = "androidx.viewpager.widget.ViewPager", type = "androidx.viewpager.widget.ViewPager.OnPageChangeListener")
public @interface OnPageChange {

    public enum Callback {
        PAGE_SELECTED,
        PAGE_SCROLLED,
        PAGE_SCROLL_STATE_CHANGED
    }

    Callback callback() default Callback.PAGE_SELECTED;

    int[] value() default {-1};
}
