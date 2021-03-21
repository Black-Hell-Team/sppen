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

class LoggerPrinter implements Printer {
    private static final int JSON_INDENT = 2;
    private final ThreadLocal<String> localTag = new ThreadLocal();
    private final List<LogAdapter> logAdapters = new ArrayList();

    LoggerPrinter() {
    }

    public Printer t(String str) {
        if (str != null) {
            this.localTag.set(str);
        }
        return this;
    }

    public void d(String str, Object... objArr) {
        log(3, null, str, objArr);
    }

    public void d(Object obj) {
        log(3, null, Utils.toString(obj), new Object[0]);
    }

    public void e(String str, Object... objArr) {
        e(null, str, objArr);
    }

    public void e(Throwable th, String str, Object... objArr) {
        log(6, th, str, objArr);
    }

    public void w(String str, Object... objArr) {
        log(5, null, str, objArr);
    }

    public void i(String str, Object... objArr) {
        log(4, null, str, objArr);
    }

    public void v(String str, Object... objArr) {
        log(2, null, str, objArr);
    }

    public void wtf(String str, Object... objArr) {
        log(7, null, str, objArr);
    }

    public void json(String str) {
        String str2 = "Invalid Json";
        if (Utils.isEmpty(str)) {
            d("Empty/Null json content");
            return;
        }
        try {
            str = str.trim();
            if (str.startsWith("{")) {
                d(new JSONObject(str).toString(2));
            } else if (str.startsWith("[")) {
                d(new JSONArray(str).toString(2));
            } else {
                e(str2, new Object[0]);
            }
        } catch (JSONException unused) {
            e(str2, new Object[0]);
        }
    }

    public void xml(String str) {
        if (Utils.isEmpty(str)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            StreamSource streamSource = new StreamSource(new StringReader(str));
            StreamResult streamResult = new StreamResult(new StringWriter());
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            newTransformer.transform(streamSource, streamResult);
            d(streamResult.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException unused) {
            e("Invalid xml", new Object[0]);
        }
    }

    public synchronized void log(int i, String str, String str2, Throwable th) {
        if (!(th == null || str2 == null)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(" : ");
            stringBuilder.append(Utils.getStackTraceString(th));
            str2 = stringBuilder.toString();
        }
        if (th != null && r5 == null) {
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

    public void clearLogAdapters() {
        this.logAdapters.clear();
    }

    public void addAdapter(LogAdapter logAdapter) {
        this.logAdapters.add(Utils.checkNotNull(logAdapter));
    }

    private synchronized void log(int i, Throwable th, String str, Object... objArr) {
        Utils.checkNotNull(str);
        log(i, getTag(), createMessage(str, objArr), th);
    }

    private String getTag() {
        String str = (String) this.localTag.get();
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
