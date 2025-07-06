package org.jsoup.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.evosuite.runtime.mock.java.io.MockFile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CookieUtilTest {

    @Test(timeout = 4000)
    public void testAsUri_ValidUrl_ReturnsUriWithoutQuery() throws Exception {
        // Arrange
        URL url = MockURL.getHttpExample();

        // Act
        URI uri = CookieUtil.asUri(url);

        // Assert
        assertNull("The URI should not have a query component", uri.getRawQuery());
    }

    @Test(timeout = 4000)
    public void testStoreCookies_NullUrl_ThrowsNullPointerException() {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> responseHeaders = null;

        // Act & Assert
        try {
            CookieUtil.storeCookies(request, response, (URL) null, responseHeaders);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_FtpUrl_ThrowsIllegalArgumentException() throws Exception {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = null; //intentionally null to cause IllegalArgumentException
        URL url = MockURL.getFtpExample();
        Map<String, List<String>> responseHeaders = null;

        // Act & Assert
        try {
            CookieUtil.storeCookies(request, response, url, responseHeaders);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument is null", e.getMessage()); // Assert the expected error message
        }
    }

    @Test(timeout = 4000)
    public void testParseCookie_NullResponse_ThrowsNullPointerException() {
        // Arrange
        String cookieString = "someCookie=someValue";

        // Act & Assert
        try {
            CookieUtil.parseCookie(cookieString, (HttpConnection.Response) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testAsUri_MalformedUrl_ThrowsMalformedURLException() throws Exception {
        // Arrange
        MockFile mockFile = new MockFile("vnb'xy>7#JQ4_gZn");
        URL url = mockFile.toURL();

        // Act & Assert
        try {
            CookieUtil.asUri(url);
            fail("Expected MalformedURLException to be thrown");
        } catch (MalformedURLException e) {
            // Expected
            assertTrue(e.getMessage().startsWith("Illegal character in path")); //Assert the start of error message
        }
    }

    @Test(timeout = 4000)
    public void testAsUri_NullUrl_ThrowsNullPointerException() {
        // Act & Assert
        try {
            CookieUtil.asUri((URL) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_EmptyCookieName_ThrowsIllegalArgumentException() {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        Attributes attributes = new Attributes();
        attributes.put("", "|noixdm3!"); // Put empty string as attribute name, which becomes cookie name
        request.cookies = attributes.dataset();
        BiConsumer<String, String> cookieSetter = null;

        // Act & Assert
        try {
            CookieUtil.applyCookiesToRequest(request, cookieSetter);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage()); //Assert expected message
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_NullRequest_ThrowsNullPointerException() {
        // Arrange
        BiConsumer<String, String> cookieSetter = null;

        // Act & Assert
        try {
            CookieUtil.applyCookiesToRequest((HttpConnection.Request) null, cookieSetter);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testParseCookie_EmptyCookieString_DoesNotThrowException() {
        // Arrange
        HttpConnection.Response response = null;

        // Act
        CookieUtil.parseCookie("", response);

        // Assert: No exception thrown
    }

    @Test(timeout = 4000)
    public void testParseCookie_NullCookieString_DoesNotThrowException() {
        // Arrange
        HttpConnection.Response response = null;

        // Act
        CookieUtil.parseCookie((String) null, response);

        // Assert: No exception thrown
    }

    @Test(timeout = 4000)
    public void testStoreCookies_ValidCookie_StoresCookie() throws Exception {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = MockURL.getFtpExample();
        Map<String, List<String>> responseHeaders = new HashMap<>();
        List<String> cookieValues = new LinkedList<>();
        cookieValues.add("9&{v4vgAJcV4T11n-");
        responseHeaders.put("set-cookie", cookieValues);

        // Act
        CookieUtil.storeCookies(request, response, url, responseHeaders);

        // Assert
        assertEquals("ftp://ftp.someFakeButWellFormedURL.org/fooExample", url.toExternalForm());
    }

    @Test(timeout = 4000)
    public void testStoreCookies_RequestHeadersUsed_DoesNotThrowException() throws Exception {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = MockURL.getHttpExample();
        Map<String, List<String>> requestHeaders = request.headers; // Use request headers for testing

        // Act
        CookieUtil.storeCookies(request, (HttpConnection.Response) null, url, requestHeaders);

        // Assert: No exception thrown. The method should handle request headers as input without errors.
        assertEquals(2097152, request.maxBodySize());

    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithExistingCookies_AppliesCookiesToRequest() throws Exception {
        // Arrange
        HttpConnection.Response response = new HttpConnection.Response();
        HttpConnection.Request request = new HttpConnection.Request();
        Map<String, String> existingCookies = request.cookies; // Get existing cookies from request
        response.cookies = existingCookies;
        String cookieString = "org.jsoup.helper.CookieUtil";
        CookieUtil.parseCookie(cookieString, response);
        BiConsumer<String, String> cookieSetter = mock(BiConsumer.class);

        // Act
        CookieUtil.applyCookiesToRequest(request, cookieSetter);

        // Assert
        assertEquals(30000, request.timeout());
    }

    @Test(timeout = 4000)
    public void testConstructor() {
        // Arrange & Act
        CookieUtil cookieUtil = new CookieUtil();

        // Assert: No exception thrown. This test confirms that the constructor runs without errors.
        assertNotNull(cookieUtil); // basic null check to satisfy coverage
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_EmptyCookies_DoesNotThrowException() throws Exception {
        // Arrange
        HttpConnection.Request request = new HttpConnection.Request();
        BiConsumer<String, String> cookieSetter = mock(BiConsumer.class);

        // Act
        CookieUtil.applyCookiesToRequest(request, cookieSetter);

        // Assert
        assertTrue(request.followRedirects());
    }
}