package org.jsoup.helper;

import org.junit.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/**
 * Test suite for {@link CookieUtil}.
 * This suite focuses on verifying the utility methods for handling cookies and URLs
 * in Jsoup's HTTP connection logic.
 */
public class CookieUtilTest {

    // --- Tests for asUri() ---

    @Test
    public void asUri_withValidUrl_convertsSuccessfully() throws IOException {
        // Arrange
        URL url = new URL("https://jsoup.org/path?query=1");

        // Act
        URI uri = CookieUtil.asUri(url);

        // Assert
        assertNotNull(uri);
        assertEquals("https", uri.getScheme());
        assertEquals("jsoup.org", uri.getHost());
        assertEquals("/path", uri.getPath());
        assertEquals("query=1", uri.getQuery());
    }

    @Test(expected = MalformedURLException.class)
    public void asUri_withUrlContainingSpaces_throwsMalformedURLException() throws IOException {
        // Arrange
        // Create a URL object that contains characters illegal for a URI (like a space).
        // This can happen with file URLs. The conversion to URI should fail.
        URL invalidUrl = new URL("file", "localhost", "/path with space");

        // Act
        CookieUtil.asUri(invalidUrl); // Should throw
    }

    @Test(expected = NullPointerException.class)
    public void asUri_withNullUrl_throwsNullPointerException() throws IOException {
        // Act
        CookieUtil.asUri(null); // Should throw
    }

    // --- Tests for storeCookies() ---

    @Test
    public void storeCookies_withSetCookieHeader_parsesAndStoresCookieInResponse() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = new URL("http://example.com");
        String cookieString = "token=12345; Path=/";
        Map<String, List<String>> headers = Collections.singletonMap("Set-Cookie", Collections.singletonList(cookieString));

        // Act
        CookieUtil.storeCookies(request, response, url, headers);

        // Assert
        // The method should parse the cookie from the header and add it to the response's cookie map.
        assertEquals(1, response.cookies().size());
        assertEquals("12345", response.cookie("token"));
    }

    @Test
    public void storeCookies_withoutSetCookieHeader_doesNothing() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = new URL("http://example.com");
        Map<String, List<String>> headers = Collections.singletonMap("Content-Type", Collections.singletonList("text/html"));

        // Act
        CookieUtil.storeCookies(request, response, url, headers);

        // Assert
        // With no 'Set-Cookie' header, the response's cookie map should remain empty.
        assertTrue(response.cookies().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void storeCookies_withNullUrl_throwsNullPointerException() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> headers = Collections.emptyMap();

        // Act
        CookieUtil.storeCookies(request, response, null, headers); // Should throw
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeCookies_withNullHeaders_throwsIllegalArgumentException() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = new URL("http://example.com");

        // Act
        // The underlying java.net.CookieManager throws IAE on null headers.
        CookieUtil.storeCookies(request, response, url, null); // Should throw
    }

    // --- Tests for applyCookiesToRequest() ---

    @Test
    public void applyCookiesToRequest_withExistingCookies_addsCookieHeader() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        request.cookie("user", "test");
        request.cookie("session", "abc");

        Map<String, String> receivedHeaders = new HashMap<>();
        // The BiConsumer will store the header key/value in our map for verification.
        BiConsumer<String, String> headerSetter = receivedHeaders::put;

        // Act
        CookieUtil.applyCookiesToRequest(request, headerSetter);

        // Assert
        // The method should format the cookies into a single "Cookie" header string.
        assertEquals(1, receivedHeaders.size());
        assertTrue(receivedHeaders.containsKey("Cookie"));

        String cookieHeader = receivedHeaders.get("Cookie");
        assertTrue(cookieHeader.contains("user=test"));
        assertTrue(cookieHeader.contains("session=abc"));
        assertTrue(cookieHeader.contains("; "));
    }

    @Test
    public void applyCookiesToRequest_withNoCookies_doesNothing() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request(); // Has no cookies
        Map<String, String> receivedHeaders = new HashMap<>();
        BiConsumer<String, String> headerSetter = receivedHeaders::put;

        // Act
        CookieUtil.applyCookiesToRequest(request, headerSetter);

        // Assert
        // No cookies in the request, so no "Cookie" header should be set.
        assertTrue(receivedHeaders.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyCookiesToRequest_withEmptyCookieName_throwsIllegalArgumentException() throws IOException {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        // An empty cookie name is invalid.
        request.cookies().put("", "some-value");
        BiConsumer<String, String> headerSetter = (k, v) -> {}; // Dummy consumer

        // Act
        CookieUtil.applyCookiesToRequest(request, headerSetter); // Should throw
    }

    // --- Trivial Tests ---

    @Test
    public void constructor_forCoverage_instantiates() {
        // Call constructor for test coverage, as this is a utility class.
        CookieUtil cookieUtil = new CookieUtil();
        assertNotNull(cookieUtil);
    }
}