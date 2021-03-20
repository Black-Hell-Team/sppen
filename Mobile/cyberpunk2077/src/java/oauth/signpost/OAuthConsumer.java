package oauth.signpost;

import java.io.Serializable;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SigningStrategy;

public interface OAuthConsumer extends Serializable {
    String getConsumerKey();

    String getConsumerSecret();

    HttpParameters getRequestParameters();

    String getToken();

    String getTokenSecret();

    void setAdditionalParameters(HttpParameters httpParameters);

    void setMessageSigner(OAuthMessageSigner oAuthMessageSigner);

    void setSendEmptyTokens(boolean z);

    void setSigningStrategy(SigningStrategy signingStrategy);

    void setTokenWithSecret(String str, String str2);

    String sign(String str) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;

    HttpRequest sign(Object obj) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;

    HttpRequest sign(HttpRequest httpRequest) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
}
