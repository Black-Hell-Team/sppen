package oauth.signpost.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import oauth.signpost.http.HttpRequest;

public class UrlStringRequestAdapter implements HttpRequest {
    private String url;

    public String getContentType() {
        return null;
    }

    public String getHeader(String str) {
        return null;
    }

    public InputStream getMessagePayload() throws IOException {
        return null;
    }

    public String getMethod() {
        return "GET";
    }

    public void setHeader(String str, String str2) {
    }

    public UrlStringRequestAdapter(String str) {
        this.url = str;
    }

    public String getRequestUrl() {
        return this.url;
    }

    public void setRequestUrl(String str) {
        this.url = str;
    }

    public Map<String, String> getAllHeaders() {
        return Collections.emptyMap();
    }

    public Object unwrap() {
        return this.url;
    }
}
