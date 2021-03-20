package oauth.signpost;

import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

public interface OAuthProviderListener {
    boolean onResponseReceived(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception;

    void prepareRequest(HttpRequest httpRequest) throws Exception;

    void prepareSubmission(HttpRequest httpRequest) throws Exception;
}
