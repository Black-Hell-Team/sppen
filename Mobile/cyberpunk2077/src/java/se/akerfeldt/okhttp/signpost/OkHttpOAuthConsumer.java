package se.akerfeldt.okhttp.signpost;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import okhttp3.Request;

public class OkHttpOAuthConsumer extends AbstractOAuthConsumer {
    public OkHttpOAuthConsumer(String str, String str2) {
        super(str, str2);
    }

    /* Access modifiers changed, original: protected */
    public HttpRequest wrap(Object obj) {
        if (obj instanceof Request) {
            return new OkHttpRequestAdapter((Request) obj);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("This consumer expects requests of type ");
        stringBuilder.append(Request.class.getCanonicalName());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
