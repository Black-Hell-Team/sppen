package kotlin.system;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\b¨\u0006\u0004"}, mo6274d2 = {"exitProcess", "", NotificationCompat.CATEGORY_STATUS, "", "kotlin-stdlib"}, mo6275k = 2, mo6276mv = {1, 1, 15})
/* renamed from: kotlin.system.ProcessKt */
public final class Process {
    private static final Void exitProcess(int i) {
        System.exit(i);
        throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
    }
}
