package oauth.signpost;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import oauth.signpost.basic.UrlStringRequestAdapter;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.QueryStringSigningStrategy;
import oauth.signpost.signature.SigningStrategy;

public abstract class AbstractOAuthConsumer implements OAuthConsumer {
    private static final long serialVersionUID = 1;
    private HttpParameters additionalParameters;
    private String consumerKey;
    private String consumerSecret;
    private OAuthMessageSigner messageSigner;
    private final Random random = new Random(System.nanoTime());
    private HttpParameters requestParameters;
    private boolean sendEmptyTokens;
    private SigningStrategy signingStrategy;
    private String token;

    public abstract HttpRequest wrap(Object obj);

    public AbstractOAuthConsumer(String str, String str2) {
        this.consumerKey = str;
        this.consumerSecret = str2;
        setMessageSigner(new HmacSha1MessageSigner());
        setSigningStrategy(new AuthorizationHeaderSigningStrategy());
    }

    public void setMessageSigner(OAuthMessageSigner oAuthMessageSigner) {
        this.messageSigner = oAuthMessageSigner;
        oAuthMessageSigner.setConsumerSecret(this.consumerSecret);
    }

    public void setSigningStrategy(SigningStrategy signingStrategy) {
        this.signingStrategy = signingStrategy;
    }

    public void setAdditionalParameters(HttpParameters httpParameters) {
        this.additionalParameters = httpParameters;
    }

    public synchronized HttpRequest sign(HttpRequest httpRequest) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        if (this.consumerKey == null) {
            throw new OAuthExpectationFailedException("consumer key not set");
        } else if (this.consumerSecret != null) {
            HttpParameters httpParameters = new HttpParameters();
            this.requestParameters = httpParameters;
            try {
                Map map = this.additionalParameters;
                if (map != null) {
                    httpParameters.putAll(map, false);
                }
                collectHeaderParameters(httpRequest, this.requestParameters);
                collectQueryParameters(httpRequest, this.requestParameters);
                collectBodyParameters(httpRequest, this.requestParameters);
                completeOAuthParameters(this.requestParameters);
                this.requestParameters.remove(OAuth.OAUTH_SIGNATURE);
                String sign = this.messageSigner.sign(httpRequest, this.requestParameters);
                OAuth.debugOut("signature", sign);
                this.signingStrategy.writeSignature(sign, httpRequest, this.requestParameters);
                OAuth.debugOut("Request URL", httpRequest.getRequestUrl());
            } catch (IOException e) {
                throw new OAuthCommunicationException(e);
            }
        } else {
            throw new OAuthExpectationFailedException("consumer secret not set");
        }
        return httpRequest;
    }

    public synchronized HttpRequest sign(Object obj) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        return sign(wrap(obj));
    }

    public synchronized String sign(String str) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        HttpRequest urlStringRequestAdapter;
        urlStringRequestAdapter = new UrlStringRequestAdapter(str);
        SigningStrategy signingStrategy = this.signingStrategy;
        this.signingStrategy = new QueryStringSigningStrategy();
        sign(urlStringRequestAdapter);
        this.signingStrategy = signingStrategy;
        return urlStringRequestAdapter.getRequestUrl();
    }

    public void setTokenWithSecret(String str, String str2) {
        this.token = str;
        this.messageSigner.setTokenSecret(str2);
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.messageSigner.getTokenSecret();
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    /* Access modifiers changed, original: protected */
    public void completeOAuthParameters(HttpParameters httpParameters) {
        String str = OAuth.OAUTH_CONSUMER_KEY;
        if (!httpParameters.containsKey(str)) {
            httpParameters.put(str, this.consumerKey, true);
        }
        str = OAuth.OAUTH_SIGNATURE_METHOD;
        if (!httpParameters.containsKey(str)) {
            httpParameters.put(str, this.messageSigner.getSignatureMethod(), true);
        }
        str = OAuth.OAUTH_TIMESTAMP;
        if (!httpParameters.containsKey(str)) {
            httpParameters.put(str, generateTimestamp(), true);
        }
        str = OAuth.OAUTH_NONCE;
        if (!httpParameters.containsKey(str)) {
            httpParameters.put(str, generateNonce(), true);
        }
        str = OAuth.OAUTH_VERSION;
        if (!httpParameters.containsKey(str)) {
            httpParameters.put(str, OAuth.VERSION_1_0, true);
        }
        str = OAuth.OAUTH_TOKEN;
        if (!httpParameters.containsKey(str)) {
            String str2 = this.token;
            if ((str2 != null && !str2.equals("")) || this.sendEmptyTokens) {
                httpParameters.put(str, this.token, true);
            }
        }
    }

    public HttpParameters getRequestParameters() {
        return this.requestParameters;
    }

    public void setSendEmptyTokens(boolean z) {
        this.sendEmptyTokens = z;
    }

    /* Access modifiers changed, original: protected */
    public void collectHeaderParameters(HttpRequest httpRequest, HttpParameters httpParameters) {
        httpParameters.putAll(OAuth.oauthHeaderToParamsMap(httpRequest.getHeader(OAuth.HTTP_AUTHORIZATION_HEADER)), false);
    }

    /* Access modifiers changed, original: protected */
    public void collectBodyParameters(HttpRequest httpRequest, HttpParameters httpParameters) throws IOException {
        String contentType = httpRequest.getContentType();
        if (contentType != null && contentType.startsWith(OAuth.FORM_ENCODED)) {
            httpParameters.putAll(OAuth.decodeForm(httpRequest.getMessagePayload()), true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void collectQueryParameters(HttpRequest httpRequest, HttpParameters httpParameters) {
        String requestUrl = httpRequest.getRequestUrl();
        int indexOf = requestUrl.indexOf(63);
        if (indexOf >= 0) {
            httpParameters.putAll(OAuth.decodeForm(requestUrl.substring(indexOf + 1)), true);
        }
    }

    /* Access modifiers changed, original: protected */
    public String generateTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /* Access modifiers changed, original: protected */
    public String generateNonce() {
        return Long.toString(this.random.nextLong());
    }
}
