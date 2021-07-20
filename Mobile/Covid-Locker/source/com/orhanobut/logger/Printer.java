package com.orhanobut.logger;

public interface Printer {
    void addAdapter(LogAdapter logAdapter);

    void clearLogAdapters();

    /* renamed from: d */
    void mo6237d(Object obj);

    /* renamed from: d */
    void mo6238d(String str, Object... objArr);

    /* renamed from: e */
    void mo6239e(String str, Object... objArr);

    /* renamed from: e */
    void mo6240e(Throwable th, String str, Object... objArr);

    /* renamed from: i */
    void mo6241i(String str, Object... objArr);

    void json(String str);

    void log(int i, String str, String str2, Throwable th);

    /* renamed from: t */
    Printer mo6244t(String str);

    /* renamed from: v */
    void mo6245v(String str, Object... objArr);

    /* renamed from: w */
    void mo6246w(String str, Object... objArr);

    void wtf(String str, Object... objArr);

    void xml(String str);
}
