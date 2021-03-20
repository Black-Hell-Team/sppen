package oauth.signpost.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oauth.signpost.http.HttpRequest;

public class HttpURLConnectionRequestAdapter implements HttpRequest {
    protected HttpURLConnection connection;

    public InputStream getMessagePayload() throws IOException {
        return null;
    }

    public void setRequestUrl(String str) {
    }

    public HttpURLConnectionRequestAdapter(HttpURLConnection httpURLConnection) {
        this.connection = httpURLConnection;
    }

    public String getMethod() {
        return this.connection.getRequestMethod();
    }

    public String getRequestUrl() {
        return this.connection.getURL().toExternalForm();
    }

    public void setHeader(String str, String str2) {
        this.connection.setRequestProperty(str, str2);
    }

    public String getHeader(String str) {
        return this.connection.getRequestProperty(str);
    }

    public Map<String, String> getAllHeaders() {
        Map requestProperties = this.connection.getRequestProperties();
        HashMap hashMap = new HashMap(requestProperties.size());
        for (String str : requestProperties.keySet()) {
            List list = (List) requestProperties.get(str);
            if (!list.isEmpty()) {
                hashMap.put(str, list.get(0));
            }
        }
        return hashMap;
    }

    public String getContentType() {
        return this.connection.getRequestProperty("Content-Type");
    }

    public HttpURLConnection unwrap() {
        return this.connection;
    }
}
