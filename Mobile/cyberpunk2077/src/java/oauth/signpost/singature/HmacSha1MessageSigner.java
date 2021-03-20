package oauth.signpost.signature;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public class HmacSha1MessageSigner extends OAuthMessageSigner {
    private static final String MAC_NAME = "HmacSHA1";

    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }

    public String sign(HttpRequest httpRequest, HttpParameters httpParameters) throws OAuthMessageSignerException {
        String str = MAC_NAME;
        String str2 = OAuth.ENCODING;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(OAuth.percentEncode(getConsumerSecret()));
            stringBuilder.append('&');
            stringBuilder.append(OAuth.percentEncode(getTokenSecret()));
            SecretKeySpec secretKeySpec = new SecretKeySpec(stringBuilder.toString().getBytes(str2), str);
            Mac instance = Mac.getInstance(str);
            instance.init(secretKeySpec);
            String generate = new SignatureBaseString(httpRequest, httpParameters).generate();
            OAuth.debugOut("SBS", generate);
            return base64Encode(instance.doFinal(generate.getBytes(str2))).trim();
        } catch (GeneralSecurityException e) {
            throw new OAuthMessageSignerException(e);
        } catch (UnsupportedEncodingException e2) {
            throw new OAuthMessageSignerException(e2);
        }
    }
}
