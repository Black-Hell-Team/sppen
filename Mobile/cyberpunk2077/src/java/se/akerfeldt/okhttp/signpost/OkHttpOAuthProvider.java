package se.akerfeldt.okhttp.signpost;

import oauth.signpost.AbstractOAuthProvider;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;

public class OkHttpOAuthProvider extends AbstractOAuthProvider {
    private OkHttpClient okHttpClient;

    public OkHttpOAuthProvider(String str, String str2, String str3) {
        super(str, str2, str3);
        this.okHttpClient = new OkHttpClient();
    }

    public OkHttpOAuthProvider(String str, String str2, String str3, OkHttpClient okHttpClient) {
        super(str, str2, str3);
        this.okHttpClient = okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /* Access modifiers changed, original: protected */
    public HttpRequest createRequest(String str) throws Exception {
        return new OkHttpRequestAdapter(new Builder().url(str).build());
    }

    /* Access modifiers changed, original: protected */
    public HttpResponse sendRequest(HttpRequest httpRequest) throws Exception {
        return new OkHttpResponseAdapter(this.okHttpClient.newCall((Request) httpRequest.unwrap()).execute());
    }
}
