package se.akerfeldt.okhttp.signpost;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import oauth.signpost.http.HttpRequest;
import okhttp3.Request;
import okio.Buffer;

public class OkHttpRequestAdapter implements HttpRequest {
    private Request request;

    public OkHttpRequestAdapter(Request request) {
        this.request = request;
    }

    public Map<String, String> getAllHeaders() {
        HashMap hashMap = new HashMap();
        for (String str : this.request.headers().names()) {
            hashMap.put(str, this.request.header(str));
        }
        return hashMap;
    }

    public String getContentType() {
        if (this.request.body() == null || this.request.body().contentType() == null) {
            return null;
        }
        return this.request.body().contentType().toString();
    }

    public String getHeader(String str) {
        return this.request.header(str);
    }

    public InputStream getMessagePayload() throws IOException {
        if (this.request.body() == null) {
            return null;
        }
        Buffer buffer = new Buffer();
        this.request.body().writeTo(buffer);
        return buffer.inputStream();
    }

    public String getMethod() {
        return this.request.method();
    }

    public String getRequestUrl() {
        return this.request.url().toString();
    }

    public void setHeader(String str, String str2) {
        this.request = this.request.newBuilder().header(str, str2).build();
    }

    public void setRequestUrl(String str) {
        this.request = this.request.newBuilder().url(str).build();
    }

    public Object unwrap() {
        return this.request;
    }
}
