package org.jsoup.helper;

import org.junit.Test;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a test for CookieUtil.
 * The original test was automatically generated and lacked clarity.
 */
public class CookieUtil_ESTestTest11 extends CookieUtil_ESTest_scaffolding {

    /**
     * Verifies that CookieUtil.storeCookies correctly parses a 'Set-Cookie' header
     * from a response and stores the resulting cookie in both the
     * HttpConnection.Response object and the HttpConnection.Request's cookie manager.
     */
    @Test
    public void storeCookies_whenSetCookieHeaderIsPresent_storesCookieInResponseAndRequest() throws Exception {
        // Arrange: Set up the necessary objects for the test.
        URL url = new URL("http://example.com/");

        // Create a request and associate it with the URL.
        HttpConnection.Request request = new HttpConnection.Request();
        request.url(url);

        // Create a map of response headers containing a simple 'Set-Cookie' directive.
        Map<String, List<String>> responseHeaders = new HashMap<>();
        responseHeaders.put("Set-Cookie", Collections.singletonList("user_id=12345"));

        // Create a response object linked to the original request.
        HttpConnection.Response response = new HttpConnection.Response(request);

        // Act: Call the method under test.
        CookieUtil.storeCookies(request, response, url, responseHeaders);

        // Assert: Verify that the cookie was stored correctly.
        // The cookie should be available in the response object...
        assertEquals("12345", response.cookie("user_id"));
        // ...and also in the request's cookie manager for subsequent requests.
        assertEquals("12345", request.cookie("user_id"));
    }
}