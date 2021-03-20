package oauth.signpost.basic;

import java.net.HttpURLConnection;
import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;

public class DefaultOAuthConsumer extends AbstractOAuthConsumer {
    private static final long serialVersionUID = 1;

    public DefaultOAuthConsumer(String str, String str2) {
        super(str, str2);
    }

    /* Access modifiers changed, original: protected */
    public HttpRequest wrap(Object obj) {
        if (obj instanceof HttpURLConnection) {
            return new HttpURLConnectionRequestAdapter((HttpURLConnection) obj);
        }
        throw new IllegalArgumentException("The default consumer expects requests of type java.net.HttpURLConnection");
    }
}
