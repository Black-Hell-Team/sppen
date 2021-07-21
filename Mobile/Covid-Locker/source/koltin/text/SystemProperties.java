package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo6274d2 = {"Lkotlin/text/SystemProperties;", "", "()V", "LINE_SEPARATOR", "", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
/* compiled from: StringBuilderJVM.kt */
final class SystemProperties {
    public static final SystemProperties INSTANCE = new SystemProperties();
    public static final String LINE_SEPARATOR;

    static {
        String property = System.getProperty("line.separator");
        if (property == null) {
            Intrinsics.throwNpe();
        }
        LINE_SEPARATOR = property;
    }

    private SystemProperties() {
    }
}
