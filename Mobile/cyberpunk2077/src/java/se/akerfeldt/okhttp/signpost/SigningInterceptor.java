package se.akerfeldt.okhttp.signpost;

import java.io.IOException;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;

public class SigningInterceptor implements Interceptor {
    private final OkHttpOAuthConsumer consumer;

    public SigningInterceptor(OkHttpOAuthConsumer okHttpOAuthConsumer) {
        this.consumer = okHttpOAuthConsumer;
    }

    public Response intercept(Chain chain) throws IOException {
        try {
            return chain.proceed((Request) this.consumer.sign((Object) chain.request()).unwrap());
        } catch (OAuthException e) {
            throw new IOException("Could not sign request", e);
        }
    }
}
