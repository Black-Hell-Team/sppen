package oauth.signpost.signature;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import org.apache.commons.codec.binary.Base64;

public abstract class OAuthMessageSigner implements Serializable {
    private static final long serialVersionUID = 4445779788786131202L;
    private transient Base64 base64 = new Base64();
    private String consumerSecret;
    private String tokenSecret;

    public abstract String getSignatureMethod();

    public abstract String sign(HttpRequest httpRequest, HttpParameters httpParameters) throws OAuthMessageSignerException;

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    public void setConsumerSecret(String str) {
        this.consumerSecret = str;
    }

    public void setTokenSecret(String str) {
        this.tokenSecret = str;
    }

    /* Access modifiers changed, original: protected */
    public byte[] decodeBase64(String str) {
        return this.base64.decode(str.getBytes());
    }

    /* Access modifiers changed, original: protected */
    public String base64Encode(byte[] bArr) {
        return new String(this.base64.encode(bArr));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.base64 = new Base64();
    }
}
