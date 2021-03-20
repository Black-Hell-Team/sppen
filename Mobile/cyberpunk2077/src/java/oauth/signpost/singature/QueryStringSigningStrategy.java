package oauth.signpost.signature;

import java.util.Iterator;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class QueryStringSigningStrategy implements SigningStrategy {
    private static final long serialVersionUID = 1;

    public String writeSignature(String str, HttpRequest httpRequest, HttpParameters httpParameters) {
        httpParameters = httpParameters.getOAuthParameters();
        httpParameters.put(OAuth.OAUTH_SIGNATURE, str, true);
        Iterator it = httpParameters.keySet().iterator();
        StringBuilder stringBuilder = new StringBuilder(OAuth.addQueryString(httpRequest.getRequestUrl(), httpParameters.getAsQueryString((String) it.next())));
        while (it.hasNext()) {
            stringBuilder.append("&");
            stringBuilder.append(httpParameters.getAsQueryString((String) it.next()));
        }
        str = stringBuilder.toString();
        httpRequest.setRequestUrl(str);
        return str;
    }
}
