package org.jsoup.helper;

import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test suite for {@link CookieUtil}.
 */
public class CookieUtilTest {

    /**
     * Verifies that storeCookies throws a MalformedURLException when given a URL
     * that contains characters that are illegal for a URI. The method under test
     * must convert the URL to a URI, which is where the failure is expected.
     */
    @Test(expected = MalformedURLException.class)
    public void storeCookiesWithUrlContainingIllegalCharactersThrowsException() throws MalformedURLException {
        // Arrange
        // Create a URL that is a valid URL object but contains a space, which is an
        // illegal character when the URL is converted to a URI string.
        URL urlWithIllegalChars = new URL("http://example.com/path with space");

        // Create dummy request, response, and header objects required by the method signature.
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> responseHeaders = Collections.emptyMap();

        // Act
        // This call is expected to throw a MalformedURLException because the URL cannot be
        // converted to a valid URI. The assertion is handled by the @Test(expected=...) annotation.
        CookieUtil.storeCookies(request, response, urlWithIllegalChars, responseHeaders);
    }
}