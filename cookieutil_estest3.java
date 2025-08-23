package org.jsoup.helper;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test suite for {@link CookieUtil}.
 */
public class CookieUtilTest {

    /**
     * Verifies that storeCookies throws a NullPointerException when the provided URL is null,
     * as this is a required parameter for processing cookies.
     */
    @Test(expected = NullPointerException.class)
    public void storeCookiesThrowsNullPointerExceptionForNullUrl() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> responseHeaders = new HashMap<>();
        URL nullUrl = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException because the URL is null.
        CookieUtil.storeCookies(request, response, nullUrl, responseHeaders);
    }
}