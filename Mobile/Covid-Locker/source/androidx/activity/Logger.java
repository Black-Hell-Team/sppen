package com.orhanobut.logger;

public final class Logger {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static Printer printer = new LoggerPrinter();

    private Logger() {
    }

    public static void printer(Printer printer2) {
        printer = (Printer) Utils.checkNotNull(printer2);
    }

    public static void addLogAdapter(LogAdapter logAdapter) {
        printer.addAdapter((LogAdapter) Utils.checkNotNull(logAdapter));
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    /* renamed from: t */
    public static Printer m20t(String str) {
        return printer.mo6244t(str);
    }

    public static void log(int i, String str, String str2, Throwable th) {
        printer.log(i, str, str2, th);
    }

    /* renamed from: d */
    public static void m16d(String str, Object... objArr) {
        printer.mo6238d(str, objArr);
    }

    /* renamed from: d */
    public static void m15d(Object obj) {
        printer.mo6237d(obj);
    }

    /* renamed from: e */
    public static void m17e(String str, Object... objArr) {
        printer.mo6240e(null, str, objArr);
    }

    /* renamed from: e */
    public static void m18e(Throwable th, String str, Object... objArr) {
        printer.mo6240e(th, str, objArr);
    }

    /* renamed from: i */
    public static void m19i(String str, Object... objArr) {
        printer.mo6241i(str, objArr);
    }

    /* renamed from: v */
    public static void m21v(String str, Object... objArr) {
        printer.mo6245v(str, objArr);
    }

    /* renamed from: w */
    public static void m22w(String str, Object... objArr) {
        printer.mo6246w(str, objArr);
    }

    public static void wtf(String str, Object... objArr) {
        printer.wtf(str, objArr);
    }

    public static void json(String str) {
        printer.json(str);
    }

    public static void xml(String str) {
        printer.xml(str);
    }
}
