package butterknife;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ButterKnife {
    static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap();
    private static final String TAG = "ButterKnife";
    private static boolean debug = false;

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public static void setDebug(boolean z) {
        debug = z;
    }

    public static Unbinder bind(Activity activity) {
        return bind(activity, activity.getWindow().getDecorView());
    }

    public static Unbinder bind(View view) {
        return bind(view, view);
    }

    public static Unbinder bind(Dialog dialog) {
        return bind(dialog, dialog.getWindow().getDecorView());
    }

    public static Unbinder bind(Object obj, Activity activity) {
        return bind(obj, activity.getWindow().getDecorView());
    }

    public static Unbinder bind(Object obj, Dialog dialog) {
        return bind(obj, dialog.getWindow().getDecorView());
    }

    public static Unbinder bind(Object obj, View view) {
        Class<?> cls = obj.getClass();
        if (debug) {
            Log.d(TAG, "Looking up binding for " + cls.getName());
        }
        Constructor<? extends Unbinder> findBindingConstructorForClass = findBindingConstructorForClass(cls);
        if (findBindingConstructorForClass == null) {
            return Unbinder.EMPTY;
        }
        try {
            return (Unbinder) findBindingConstructorForClass.newInstance(obj, view);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + findBindingConstructorForClass, e);
        } catch (InstantiationException e2) {
            throw new RuntimeException("Unable to invoke " + findBindingConstructorForClass, e2);
        } catch (InvocationTargetException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unable to create binding instance.", cause);
            }
        }
    }

    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> cls) {
        Constructor<? extends Unbinder> constructor;
        Constructor<? extends Unbinder> constructor2 = BINDINGS.get(cls);
        if (constructor2 != null || BINDINGS.containsKey(cls)) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in binding map.");
            }
            return constructor2;
        }
        String name = cls.getName();
        if (!name.startsWith("android.") && !name.startsWith("java.") && !name.startsWith("androidx.")) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                constructor = classLoader.loadClass(name + "_ViewBinding").getConstructor(cls, View.class);
                if (debug) {
                    Log.d(TAG, "HIT: Loaded binding class and constructor.");
                }
            } catch (ClassNotFoundException unused) {
                if (debug) {
                    Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
                }
                constructor = findBindingConstructorForClass(cls.getSuperclass());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Unable to find binding constructor for " + name, e);
            }
            BINDINGS.put(cls, constructor);
            return constructor;
        } else if (!debug) {
            return null;
        } else {
            Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
    }
}
