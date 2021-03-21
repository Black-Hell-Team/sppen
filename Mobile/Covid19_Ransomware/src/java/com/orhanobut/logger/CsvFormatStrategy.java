package com.orhanobut.logger;

import android.os.Environment;
import android.os.HandlerThread;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvFormatStrategy implements FormatStrategy {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";
    private final Date date;
    private final SimpleDateFormat dateFormat;
    private final LogStrategy logStrategy;
    private final String tag;

    public static final class Builder {
        private static final int MAX_BYTES = 512000;
        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag;

        private Builder() {
            this.tag = "PRETTY_LOGGER";
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder dateFormat(SimpleDateFormat simpleDateFormat) {
            this.dateFormat = simpleDateFormat;
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

        public CsvFormatStrategy build() {
            if (this.date == null) {
                this.date = new Date();
            }
            if (this.dateFormat == null) {
                this.dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
            }
            if (this.logStrategy == null) {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(absolutePath);
                stringBuilder.append(File.separatorChar);
                stringBuilder.append("logger");
                absolutePath = stringBuilder.toString();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("AndroidFileLogger.");
                stringBuilder2.append(absolutePath);
                HandlerThread handlerThread = new HandlerThread(stringBuilder2.toString());
                handlerThread.start();
                this.logStrategy = new DiskLogStrategy(new WriteHandler(handlerThread.getLooper(), absolutePath, MAX_BYTES));
            }
            return new CsvFormatStrategy(this);
        }
    }

    private CsvFormatStrategy(Builder builder) {
        Utils.checkNotNull(builder);
        this.date = builder.date;
        this.dateFormat = builder.dateFormat;
        this.logStrategy = builder.logStrategy;
        this.tag = builder.tag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void log(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        str = formatTag(str);
        this.date.setTime(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Long.toString(this.date.getTime()));
        String str3 = SEPARATOR;
        stringBuilder.append(str3);
        stringBuilder.append(this.dateFormat.format(this.date));
        stringBuilder.append(str3);
        stringBuilder.append(Utils.logLevel(i));
        stringBuilder.append(str3);
        stringBuilder.append(str);
        if (str2.contains(NEW_LINE)) {
            str2 = str2.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        stringBuilder.append(str3);
        stringBuilder.append(str2);
        stringBuilder.append(NEW_LINE);
        this.logStrategy.log(i, str, stringBuilder.toString());
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
