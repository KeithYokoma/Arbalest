package arbalest.rest.http;

import arbalest.rest.exception.ParseRestNetworkException;

public interface ParseRestClientFactory {
    public ParseRestClient create(String url) throws ParseRestNetworkException;
}
