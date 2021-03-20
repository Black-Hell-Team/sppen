package oauth.signpost.exception;

public class OAuthMessageSignerException extends OAuthException {
    public OAuthMessageSignerException(String str) {
        super(str);
    }

    public OAuthMessageSignerException(Exception exception) {
        super((Throwable) exception);
    }
}
