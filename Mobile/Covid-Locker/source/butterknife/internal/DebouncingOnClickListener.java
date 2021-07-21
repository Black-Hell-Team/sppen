package butterknife.internal;

import android.view.View;

public abstract class DebouncingOnClickListener implements View.OnClickListener {
    private static final Runnable ENABLE_AGAIN = $$Lambda$DebouncingOnClickListener$EDavjG1Da3G8JTdFPVGk_7OErB8.INSTANCE;
    static boolean enabled = true;

    public abstract void doClick(View view);

    public final void onClick(View view) {
        if (enabled) {
            enabled = false;
            view.post(ENABLE_AGAIN);
            doClick(view);
        }
    }
}
