package oauth.signpost.signature;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class SignatureBaseString {
    private HttpRequest request;
    private HttpParameters requestParameters;

    public SignatureBaseString(HttpRequest httpRequest, HttpParameters httpParameters) {
        this.request = httpRequest;
        this.requestParameters = httpParameters;
    }

    public String generate() throws OAuthMessageSignerException {
        try {
            String normalizeRequestUrl = normalizeRequestUrl();
            String normalizeRequestParameters = normalizeRequestParameters();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.request.getMethod());
            stringBuilder.append('&');
            stringBuilder.append(OAuth.percentEncode(normalizeRequestUrl));
            stringBuilder.append('&');
            stringBuilder.append(OAuth.percentEncode(normalizeRequestParameters));
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new OAuthMessageSignerException(e);
        }
    }

    public String normalizeRequestUrl() throws URISyntaxException {
        URI uri = new URI(this.request.getRequestUrl());
        String toLowerCase = uri.getScheme().toLowerCase();
        String toLowerCase2 = uri.getAuthority().toLowerCase();
        int i = ((toLowerCase.equals("http") && uri.getPort() == 80) || (toLowerCase.equals("https") && uri.getPort() == 443)) ? 1 : 0;
        if (i != 0) {
            i = toLowerCase2.lastIndexOf(":");
            if (i >= 0) {
                toLowerCase2 = toLowerCase2.substring(0, i);
            }
        }
        String rawPath = uri.getRawPath();
        if (rawPath == null || rawPath.length() <= 0) {
            rawPath = "/";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toLowerCase);
        stringBuilder.append("://");
        stringBuilder.append(toLowerCase2);
        stringBuilder.append(rawPath);
        return stringBuilder.toString();
    }

    public String normalizeRequestParameters() throws IOException {
        if (this.requestParameters == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (String str : this.requestParameters.keySet()) {
            if (!(OAuth.OAUTH_SIGNATURE.equals(str) || "realm".equals(str))) {
                if (i > 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(this.requestParameters.getAsQueryString(str, false));
            }
            i++;
        }
        return stringBuilder.toString();
    }
}
