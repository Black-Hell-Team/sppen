package oauth.signpost.signature;

import java.util.Iterator;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class AuthorizationHeaderSigningStrategy implements SigningStrategy {
    private static final long serialVersionUID = 1;

    public String writeSignature(String str, HttpRequest httpRequest, HttpParameters httpParameters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OAuth ");
        String str2 = "realm";
        String str3 = ", ";
        if (httpParameters.containsKey(str2)) {
            stringBuilder.append(httpParameters.getAsHeaderElement(str2));
            stringBuilder.append(str3);
        }
        httpParameters = httpParameters.getOAuthParameters();
        httpParameters.put(OAuth.OAUTH_SIGNATURE, str, true);
        Iterator it = httpParameters.keySet().iterator();
        while (it.hasNext()) {
            stringBuilder.append(httpParameters.getAsHeaderElement((String) it.next()));
            if (it.hasNext()) {
                stringBuilder.append(str3);
            }
        }
        str = stringBuilder.toString();
        OAuth.debugOut("Auth Header", str);
        httpRequest.setHeader(OAuth.HTTP_AUTHORIZATION_HEADER, str);
        return str;
    }
}
