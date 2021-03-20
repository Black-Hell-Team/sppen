package oauth.signpost.signature;

import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class PlainTextMessageSigner extends OAuthMessageSigner {
    public String getSignatureMethod() {
        return "PLAINTEXT";
    }

    public String sign(HttpRequest httpRequest, HttpParameters httpParameters) throws OAuthMessageSignerException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OAuth.percentEncode(getConsumerSecret()));
        stringBuilder.append('&');
        stringBuilder.append(OAuth.percentEncode(getTokenSecret()));
        return stringBuilder.toString();
    }
}
