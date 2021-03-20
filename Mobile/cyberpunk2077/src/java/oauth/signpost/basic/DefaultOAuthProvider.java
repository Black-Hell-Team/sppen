package oauth.signpost.basic;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import oauth.signpost.AbstractOAuthProvider;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

public class DefaultOAuthProvider extends AbstractOAuthProvider {
    private static final long serialVersionUID = 1;

    public DefaultOAuthProvider(String str, String str2, String str3) {
        super(str, str2, str3);
    }

    /* Access modifiers changed, original: protected */
    public HttpRequest createRequest(String str) throws MalformedURLException, IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setRequestProperty("Content-Length", "0");
        return new HttpURLConnectionRequestAdapter(httpURLConnection);
    }

    /* Access modifiers changed, original: protected */
    public HttpResponse sendRequest(HttpRequest httpRequest) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpRequest.unwrap();
        httpURLConnection.connect();
        return new HttpURLConnectionResponseAdapter(httpURLConnection);
    }

    /* Access modifiers changed, original: protected */
    public void closeConnection(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpRequest.unwrap();
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }
}
