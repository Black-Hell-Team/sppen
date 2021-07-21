package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ListenerClass(method = {@ListenerMethod(defaultReturn = "false", name = "onLongClick", parameters = {"android.view.View"}, returnType = "boolean")}, setter = "setOnLongClickListener", targetType = "android.view.View", type = "android.view.View.OnLongClickListener")
public @interface OnLongClick {
    int[] value() default {-1};
}
