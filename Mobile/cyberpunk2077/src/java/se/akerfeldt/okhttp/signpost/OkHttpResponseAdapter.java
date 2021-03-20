package se.akerfeldt.okhttp.signpost;

import java.io.IOException;
import java.io.InputStream;
import oauth.signpost.http.HttpResponse;
import okhttp3.Response;

public class OkHttpResponseAdapter implements HttpResponse {
    private Response response;

    public OkHttpResponseAdapter(Response response) {
        this.response = response;
    }

    public int getStatusCode() throws IOException {
        return this.response.code();
    }

    public String getReasonPhrase() throws Exception {
        return this.response.message();
    }

    public InputStream getContent() throws IOException {
        return this.response.body().byteStream();
    }

    public Object unwrap() {
        return this.response;
    }
}
