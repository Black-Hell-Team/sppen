package com.orhanobut.logger;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* access modifiers changed from: package-private */
public class LoggerPrinter implements Printer {
    private static final int JSON_INDENT = 2;
    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final List<LogAdapter> logAdapters = new ArrayList();

    LoggerPrinter() {
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: t */
    public Printer mo6244t(String str) {
        if (str != null) {
            this.localTag.set(str);
        }
        return this;
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: d */
    public void mo6238d(String str, Object... objArr) {
        log(3, (Throwable) null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: d */
    public void mo6237d(Object obj) {
        log(3, (Throwable) null, Utils.toString(obj), new Object[0]);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: e */
    public void mo6239e(String str, Object... objArr) {
        mo6240e(null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: e */
    public void mo6240e(Throwable th, String str, Object... objArr) {
        log(6, th, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: w */
    public void mo6246w(String str, Object... objArr) {
        log(5, (Throwable) null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: i */
    public void mo6241i(String str, Object... objArr) {
        log(4, (Throwable) null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    /* renamed from: v */
    public void mo6245v(String str, Object... objArr) {
        log(2, (Throwable) null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    public void wtf(String str, Object... objArr) {
        log(7, (Throwable) null, str, objArr);
    }

    @Override // com.orhanobut.logger.Printer
    public void json(String str) {
        if (Utils.isEmpty(str)) {
            mo6237d("Empty/Null json content");
            return;
        }
        try {
            String trim = str.trim();
            if (trim.startsWith("{")) {
                mo6237d(new JSONObject(trim).toString(2));
            } else if (trim.startsWith("[")) {
                mo6237d(new JSONArray(trim).toString(2));
            } else {
                mo6239e("Invalid Json", new Object[0]);
            }
        } catch (JSONException unused) {
            mo6239e("Invalid Json", new Object[0]);
        }
    }

    @Override // com.orhanobut.logger.Printer
    public void xml(String str) {
        if (Utils.isEmpty(str)) {
            mo6237d("Empty/Null xml content");
            return;
        }
        try {
            StreamSource streamSource = new StreamSource(new StringReader(str));
            StreamResult streamResult = new StreamResult(new StringWriter());
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            newTransformer.transform(streamSource, streamResult);
            mo6237d(streamResult.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException unused) {
            mo6239e("Invalid xml", new Object[0]);
        }
    }

    @Override // com.orhanobut.logger.Printer
    public synchronized void log(int i, String str, String str2, Throwable th) {
        if (!(th == null || str2 == null)) {
            str2 = str2 + " : " + Utils.getStackTraceString(th);
        }
        if (th != null && str2 == null) {
            str2 = Utils.getStackTraceString(th);
        }
        if (Utils.isEmpty(str2)) {
            str2 = "Empty/NULL log message";
        }
        for (LogAdapter logAdapter : this.logAdapters) {
            if (logAdapter.isLoggable(i, str)) {
                logAdapter.log(i, str, str2);
            }
        }
    }

    @Override // com.orhanobut.logger.Printer
    public void clearLogAdapters() {
        this.logAdapters.clear();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: java.util.List<com.orhanobut.logger.LogAdapter> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.orhanobut.logger.Printer
    public void addAdapter(LogAdapter logAdapter) {
        this.logAdapters.add(Utils.checkNotNull(logAdapter));
    }

    private synchronized void log(int i, Throwable th, String str, Object... objArr) {
        Utils.checkNotNull(str);
        log(i, getTag(), createMessage(str, objArr), th);
    }

    private String getTag() {
        String str = this.localTag.get();
        if (str == null) {
            return null;
        }
        this.localTag.remove();
        return str;
    }

    private String createMessage(String str, Object... objArr) {
        return (objArr == null || objArr.length == 0) ? str : String.format(str, objArr);
    }
}
