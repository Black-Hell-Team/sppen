package oauth.signpost.exception;

public class OAuthCommunicationException extends OAuthException {
    private String responseBody;

    public OAuthCommunicationException(Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Communication with the service provider failed: ");
        stringBuilder.append(exception.getLocalizedMessage());
        super(stringBuilder.toString(), exception);
    }

    public OAuthCommunicationException(String str, String str2) {
        super(str);
        this.responseBody = str2;
    }

    public String getResponseBody() {
        return this.responseBody;
    }
}
