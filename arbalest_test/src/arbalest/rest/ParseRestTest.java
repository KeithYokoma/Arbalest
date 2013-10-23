package arbalest.rest;

import android.app.Application;
import android.test.AndroidTestCase;

import arbalest.rest.constant.ParseApiConstants;
import arbalest.rest.exception.ParseRestAuthorizationException;
import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;
import arbalest.rest.http.ParseRestClient;
import arbalest.rest.http.ParseRestClientFactory;
import arbalest.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ParseRestTest extends AndroidTestCase {
    private ParseRest mRest;
    private Map<String, String> mHeaders;

    private String mUrl;
    private String mMethod;
    private Map<String, String> mRequestHeaders;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mHeaders = new HashMap<String, String>();
        mHeaders.put(ParseRest.HEADER_PARSE_APPLICATION_ID, "123456789");
        mHeaders.put(ParseRest.HEADER_PARSE_REST_API_KEY, "abcdefg");
        mRest = new ParseRest((Application) getContext().getApplicationContext(), new ParseRestContext(new ParseRestClientFactory() {
            @Override
            public ParseRestClient create(String url) throws ParseRestNetworkException {
                mUrl = url;
                return new ParseRestClient() {
                    @Override
                    public void close() throws IOException {}

                    @Override
                    public int getResponseCode() throws ParseRestNetworkException {
                        return 0;
                    }

                    @Override
                    public OutputStream getOutputStream(String method, Map<String, String> headers) throws ParseRestNetworkException {
                        mMethod = method;
                        mRequestHeaders = headers;
                        return null;
                    }

                    @Override
                    public InputStream getInputStream() throws ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
                        return new ByteArrayInputStream(StringUtils.getBytes("{\"AAA\" : \"aaa\"}"));
                    }
                };
            }
        }, mHeaders));
    }

    public void testBlockingPost() throws Exception {
        String url = ParseApiConstants.PARSE_API_INSTALLATION_URL;
        String result = mRest.blockingPost(url, null, String.class);
        assertEquals(url, mUrl);
        assertEquals("POST", mMethod);
        assertEquals("123456789", mRequestHeaders.get(ParseRest.HEADER_PARSE_APPLICATION_ID));
        assertEquals("abcdefg", mRequestHeaders.get(ParseRest.HEADER_PARSE_REST_API_KEY));
        assertEquals("{\"AAA\" : \"aaa\"}", result);
    }
}