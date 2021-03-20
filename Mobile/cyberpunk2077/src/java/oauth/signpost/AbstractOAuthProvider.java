package oauth.signpost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

public abstract class AbstractOAuthProvider implements OAuthProvider {
    private static final long serialVersionUID = 1;
    private String accessTokenEndpointUrl;
    private String authorizationWebsiteUrl;
    private Map<String, String> defaultHeaders = new HashMap();
    private boolean isOAuth10a;
    private transient OAuthProviderListener listener;
    private String requestTokenEndpointUrl;
    private HttpParameters responseParameters = new HttpParameters();

    /* Access modifiers changed, original: protected */
    public void closeConnection(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
    }

    public abstract HttpRequest createRequest(String str) throws Exception;

    public abstract HttpResponse sendRequest(HttpRequest httpRequest) throws Exception;

    public AbstractOAuthProvider(String str, String str2, String str3) {
        this.requestTokenEndpointUrl = str;
        this.accessTokenEndpointUrl = str2;
        this.authorizationWebsiteUrl = str3;
    }

    public synchronized String retrieveRequestToken(OAuthConsumer oAuthConsumer, String str, String... strArr) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
        oAuthConsumer.setTokenWithSecret(null, null);
        HttpParameters httpParameters = new HttpParameters();
        httpParameters.putAll(strArr, true);
        httpParameters.put(OAuth.OAUTH_CALLBACK, str, true);
        retrieveToken(oAuthConsumer, this.requestTokenEndpointUrl, httpParameters);
        String first = this.responseParameters.getFirst(OAuth.OAUTH_CALLBACK_CONFIRMED);
        this.responseParameters.remove(OAuth.OAUTH_CALLBACK_CONFIRMED);
        boolean equals = Boolean.TRUE.toString().equals(first);
        this.isOAuth10a = equals;
        if (equals) {
            return OAuth.addQueryParameters(this.authorizationWebsiteUrl, OAuth.OAUTH_TOKEN, oAuthConsumer.getToken());
        }
        return OAuth.addQueryParameters(this.authorizationWebsiteUrl, OAuth.OAUTH_TOKEN, oAuthConsumer.getToken(), OAuth.OAUTH_CALLBACK, str);
    }

    public synchronized void retrieveAccessToken(OAuthConsumer oAuthConsumer, String str, String... strArr) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
        if (oAuthConsumer.getToken() == null || oAuthConsumer.getTokenSecret() == null) {
            throw new OAuthExpectationFailedException("Authorized request token or token secret not set. Did you retrieve an authorized request token before?");
        }
        HttpParameters httpParameters = new HttpParameters();
        httpParameters.putAll(strArr, true);
        if (this.isOAuth10a && str != null) {
            httpParameters.put(OAuth.OAUTH_VERIFIER, str, true);
        }
        retrieveToken(oAuthConsumer, this.accessTokenEndpointUrl, httpParameters);
    }

    /* Access modifiers changed, original: protected */
    public void retrieveToken(OAuthConsumer oAuthConsumer, String str, HttpParameters httpParameters) throws OAuthMessageSignerException, OAuthCommunicationException, OAuthNotAuthorizedException, OAuthExpectationFailedException {
        Exception e;
        OAuthNotAuthorizedException e2;
        HttpResponse httpResponse;
        HttpResponse httpResponse2;
        OAuthExpectationFailedException e3;
        Throwable th;
        Object obj = OAuth.OAUTH_TOKEN_SECRET;
        Object obj2 = OAuth.OAUTH_TOKEN;
        Map requestHeaders = getRequestHeaders();
        if (oAuthConsumer.getConsumerKey() == null || oAuthConsumer.getConsumerSecret() == null) {
            throw new OAuthExpectationFailedException("Consumer key or secret not set");
        }
        HttpRequest httpRequest = null;
        HttpRequest createRequest;
        try {
            createRequest = createRequest(str);
            HttpResponse sendRequest;
            try {
                for (String str2 : requestHeaders.keySet()) {
                    createRequest.setHeader(str2, (String) requestHeaders.get(str2));
                }
                if (!(httpParameters == null || httpParameters.isEmpty())) {
                    oAuthConsumer.setAdditionalParameters(httpParameters);
                }
                OAuthProviderListener oAuthProviderListener = this.listener;
                if (oAuthProviderListener != null) {
                    oAuthProviderListener.prepareRequest(createRequest);
                }
                oAuthConsumer.sign(createRequest);
                oAuthProviderListener = this.listener;
                if (oAuthProviderListener != null) {
                    oAuthProviderListener.prepareSubmission(createRequest);
                }
                sendRequest = sendRequest(createRequest);
                int statusCode = sendRequest.getStatusCode();
                boolean z = false;
                OAuthProviderListener oAuthProviderListener2 = this.listener;
                if (oAuthProviderListener2 != null) {
                    z = oAuthProviderListener2.onResponseReceived(createRequest, sendRequest);
                }
                if (z) {
                    try {
                        closeConnection(createRequest, sendRequest);
                        return;
                    } catch (Exception e4) {
                        throw new OAuthCommunicationException(e4);
                    }
                }
                if (statusCode >= 300) {
                    handleUnexpectedResponse(statusCode, sendRequest);
                }
                httpParameters = OAuth.decodeForm(sendRequest.getContent());
                String first = httpParameters.getFirst(obj2);
                String first2 = httpParameters.getFirst(obj);
                httpParameters.remove(obj2);
                httpParameters.remove(obj);
                setResponseParameters(httpParameters);
                if (first == null || first2 == null) {
                    throw new OAuthExpectationFailedException("Request token or token secret not set in server reply. The service provider you use is probably buggy.");
                }
                oAuthConsumer.setTokenWithSecret(first, first2);
                try {
                    closeConnection(createRequest, sendRequest);
                } catch (Exception e42) {
                    throw new OAuthCommunicationException(e42);
                }
            } catch (OAuthNotAuthorizedException e5) {
                e2 = e5;
                httpResponse = sendRequest;
                httpRequest = createRequest;
                httpResponse2 = httpResponse;
                throw e2;
            } catch (OAuthExpectationFailedException e6) {
                e3 = e6;
                httpResponse = sendRequest;
                httpRequest = createRequest;
                httpResponse2 = httpResponse;
                throw e3;
            } catch (Exception e7) {
                e42 = e7;
                httpResponse = sendRequest;
                httpRequest = createRequest;
                httpResponse2 = httpResponse;
                throw new OAuthCommunicationException(e42);
            } catch (Throwable th2) {
                th = th2;
                httpResponse = sendRequest;
                httpRequest = createRequest;
                httpResponse2 = httpResponse;
                try {
                    closeConnection(httpRequest, httpResponse2);
                    throw th;
                } catch (Exception e422) {
                    throw new OAuthCommunicationException(e422);
                }
            }
        } catch (OAuthNotAuthorizedException e8) {
            e2 = e8;
            createRequest = null;
            throw e2;
        } catch (OAuthExpectationFailedException e9) {
            e3 = e9;
            createRequest = null;
            throw e3;
        } catch (Exception e10) {
            e422 = e10;
            httpResponse2 = null;
            throw new OAuthCommunicationException(e422);
        } catch (Throwable th3) {
            th = th3;
            closeConnection(httpRequest, httpResponse2);
            throw th;
        }
    }

    /* Access modifiers changed, original: protected */
    public void handleUnexpectedResponse(int i, HttpResponse httpResponse) throws Exception {
        if (httpResponse != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getContent()));
            StringBuilder stringBuilder = new StringBuilder();
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                stringBuilder.append(readLine);
            }
            if (i != 401) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Service provider responded in error: ");
                stringBuilder2.append(i);
                stringBuilder2.append(" (");
                stringBuilder2.append(httpResponse.getReasonPhrase());
                stringBuilder2.append(")");
                throw new OAuthCommunicationException(stringBuilder2.toString(), stringBuilder.toString());
            }
            throw new OAuthNotAuthorizedException(stringBuilder.toString());
        }
    }

    public HttpParameters getResponseParameters() {
        return this.responseParameters;
    }

    /* Access modifiers changed, original: protected */
    public String getResponseParameter(String str) {
        return this.responseParameters.getFirst(str);
    }

    public void setResponseParameters(HttpParameters httpParameters) {
        this.responseParameters = httpParameters;
    }

    public void setOAuth10a(boolean z) {
        this.isOAuth10a = z;
    }

    public boolean isOAuth10a() {
        return this.isOAuth10a;
    }

    public String getRequestTokenEndpointUrl() {
        return this.requestTokenEndpointUrl;
    }

    public String getAccessTokenEndpointUrl() {
        return this.accessTokenEndpointUrl;
    }

    public String getAuthorizationWebsiteUrl() {
        return this.authorizationWebsiteUrl;
    }

    public void setRequestHeader(String str, String str2) {
        this.defaultHeaders.put(str, str2);
    }

    public Map<String, String> getRequestHeaders() {
        return this.defaultHeaders;
    }

    public void setListener(OAuthProviderListener oAuthProviderListener) {
        this.listener = oAuthProviderListener;
    }

    public void removeListener(OAuthProviderListener oAuthProviderListener) {
        this.listener = null;
    }
}
