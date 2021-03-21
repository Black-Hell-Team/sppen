package com.orhanobut.logger;

public class PrettyFormatStrategy implements FormatStrategy {
    private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final int CHUNK_SIZE = 4000;
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final char HORIZONTAL_LINE = '│';
    private static final String MIDDLE_BORDER = "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final char MIDDLE_CORNER = '├';
    private static final int MIN_STACK_OFFSET = 5;
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final char TOP_LEFT_CORNER = '┌';
    private final LogStrategy logStrategy;
    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    private final String tag;

    public static class Builder {
        LogStrategy logStrategy;
        int methodCount;
        int methodOffset;
        boolean showThreadInfo;
        String tag;

        private Builder() {
            this.methodCount = 2;
            this.methodOffset = 0;
            this.showThreadInfo = true;
            this.tag = "PRETTY_LOGGER";
        }

        public Builder methodCount(int i) {
            this.methodCount = i;
            return this;
        }

        public Builder methodOffset(int i) {
            this.methodOffset = i;
            return this;
        }

        public Builder showThreadInfo(boolean z) {
            this.showThreadInfo = z;
            return this;
        }

        public Builder logStrategy(LogStrategy logStrategy) {
            this.logStrategy = logStrategy;
            return this;
        }

        public Builder tag(String str) {
            this.tag = str;
            return this;
        }

        public PrettyFormatStrategy build() {
            if (this.logStrategy == null) {
                this.logStrategy = new LogcatLogStrategy();
            }
            return new PrettyFormatStrategy(this);
        }
    }

    private PrettyFormatStrategy(Builder builder) {
        Utils.checkNotNull(builder);
        this.methodCount = builder.methodCount;
        this.methodOffset = builder.methodOffset;
        this.showThreadInfo = builder.showThreadInfo;
        this.logStrategy = builder.logStrategy;
        this.tag = builder.tag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void log(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        str = formatTag(str);
        logTopBorder(i, str);
        logHeaderContent(i, str, this.methodCount);
        byte[] bytes = str2.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            if (this.methodCount > 0) {
                logDivider(i, str);
            }
            logContent(i, str, str2);
            logBottomBorder(i, str);
            return;
        }
        if (this.methodCount > 0) {
            logDivider(i, str);
        }
        for (int i2 = 0; i2 < length; i2 += CHUNK_SIZE) {
            logContent(i, str, new String(bytes, i2, Math.min(length - i2, CHUNK_SIZE)));
        }
        logBottomBorder(i, str);
    }

    private void logTopBorder(int i, String str) {
        logChunk(i, str, TOP_BORDER);
    }

    private void logHeaderContent(int i, String str, int i2) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (this.showThreadInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("│ Thread: ");
            stringBuilder.append(Thread.currentThread().getName());
            logChunk(i, str, stringBuilder.toString());
            logDivider(i, str);
        }
        int stackOffset = getStackOffset(stackTrace) + this.methodOffset;
        if (i2 + stackOffset > stackTrace.length) {
            i2 = (stackTrace.length - stackOffset) - 1;
        }
        String str2 = "";
        for (i2 = 
/*
Method generation error in method: com.orhanobut.logger.PrettyFormatStrategy.logHeaderContent(int, java.lang.String, int):void, dex: 
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r9_4 'i2' int) = (r9_0 'i2' int), (r9_3 'i2' int) binds: {(r9_0 'i2' int)=B:4:0x0035, (r9_3 'i2' int)=B:5:0x0037} in method: com.orhanobut.logger.PrettyFormatStrategy.logHeaderContent(int, java.lang.String, int):void, dex: 
	at jadx.core.a.f.a(InsnGen.java:228)
	at jadx.core.a.i.a(RegionGen.java:185)
	at jadx.core.a.i.a(RegionGen.java:63)
	at jadx.core.a.i.a(RegionGen.java:89)
	at jadx.core.a.i.a(RegionGen.java:55)
	at jadx.core.a.g.b(MethodGen.java:183)
	at jadx.core.a.b.a(ClassGen.java:321)
	at jadx.core.a.b.d(ClassGen.java:259)
	at jadx.core.a.b.c(ClassGen.java:221)
	at jadx.core.a.b.a(ClassGen.java:111)
	at jadx.core.a.b.b(ClassGen.java:77)
	at jadx.core.a.c.a(CodeGen.java:10)
	at jadx.core.b.a(ProcessClass.java:38)
	at jadx.a.d.a(JadxDecompiler.java:292)
	at jadx.a.e.a(JavaClass.java:62)
	at jadx.a.d.a(JadxDecompiler.java:200)
	at jadx.a.d.lambda$rWZLWBP0-oQoClOyCzw_TOCTGIQ(Unknown Source:0)
	at jadx.a.-$$Lambda$d$rWZLWBP0-oQoClOyCzw_TOCTGIQ.run(Unknown Source:6)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.a.f.a(InsnGen.java:539)
	at jadx.core.a.f.a(InsnGen.java:511)
	at jadx.core.a.f.a(InsnGen.java:222)
	... 17 more

*/

    private void logBottomBorder(int i, String str) {
        logChunk(i, str, BOTTOM_BORDER);
    }

    private void logDivider(int i, String str) {
        logChunk(i, str, MIDDLE_BORDER);
    }

    private void logContent(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        for (String str3 : str2.split(System.getProperty("line.separator"))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("│ ");
            stringBuilder.append(str3);
            logChunk(i, str, stringBuilder.toString());
        }
    }

    private void logChunk(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        this.logStrategy.log(i, str, str2);
    }

    private String getSimpleClassName(String str) {
        Utils.checkNotNull(str);
        return str.substring(str.lastIndexOf(".") + 1);
    }

    private int getStackOffset(StackTraceElement[] stackTraceElementArr) {
        Utils.checkNotNull(stackTraceElementArr);
        for (int i = 5; i < stackTraceElementArr.length; i++) {
            String className = stackTraceElementArr[i].getClassName();
            if (!className.equals(LoggerPrinter.class.getName()) && !className.equals(Logger.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }

    private String formatTag(String str) {
        if (Utils.isEmpty(str) || Utils.equals(this.tag, str)) {
            return this.tag;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.tag);
        stringBuilder.append("-");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}
