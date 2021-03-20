package oauth.signpost;

import java.io.Serializable;
import java.util.Map;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

public interface OAuthProvider extends Serializable {
    String getAccessTokenEndpointUrl();

    String getAuthorizationWebsiteUrl();

    @Deprecated
    Map<String, String> getRequestHeaders();

    String getRequestTokenEndpointUrl();

    HttpParameters getResponseParameters();

    boolean isOAuth10a();

    void removeListener(OAuthProviderListener oAuthProviderListener);

    void retrieveAccessToken(OAuthConsumer oAuthConsumer, String str, String... strArr) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;

    String retrieveRequestToken(OAuthConsumer oAuthConsumer, String str, String... strArr) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;

    void setListener(OAuthProviderListener oAuthProviderListener);

    void setOAuth10a(boolean z);

    @Deprecated
    void setRequestHeader(String str, String str2);

    void setResponseParameters(HttpParameters httpParameters);
}
