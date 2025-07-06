package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.jsoup.helper.CookieUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.junit.runner.RunWith;

/**
 * Test class for CookieUtil.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CookieUtilTest {

    /**
     * Tests that the URL is correctly converted to a URI.
     */
    @Test(timeout = 4000)
    public void testUrlToUriConversion() throws Throwable {
        // Create a URL
        URL url = MockURL.getHttpExample();

        // Convert the URL to a URI
        URI uri = CookieUtil.asUri(url);

        // Check that the raw query is null
        assertNull(uri.getRawQuery());
    }

    /**
     * Tests that a NullPointerException is thrown when null URL is passed to storeCookies.
     */
    @Test(timeout = 4000)
    public void testStoreCookiesNullUrl() throws Throwable {
        // Create a request and response
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();

        // Try to store cookies with a null URL
        try {
            CookieUtil.storeCookies(request, response, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    /**
     * Tests that an IllegalArgumentException is thrown when null response is passed to storeCookies.
     */
    @Test(timeout = 4000)
    public void testStoreCookiesNullResponse() throws Throwable {
        // Create a request
        HttpConnection.Request request = new HttpConnection.Request();

        // Create a URL
        URL url = MockURL.getFtpExample();

        // Try to store cookies with a null response
        try {
            CookieUtil.storeCookies(request, null, url, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Verify the exception
            verifyException("java.net.CookieManager", e);
        }
    }

    /**
     * Tests that a NullPointerException is thrown when null response is passed to parseCookie.
     */
    @Test(timeout = 4000)
    public void testParseCookieNullResponse() throws Throwable {
        // Try to parse cookie with a null response
        try {
            CookieUtil.parseCookie("Set-Cookie", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    /**
     * Tests that a MalformedURLException is thrown when an invalid URL is passed to asUri.
     */
    @Test(timeout = 4000)
    public void testAsUriInvalidUrl() throws Throwable {
        // Create an invalid URL
        MockFile mockFile = new MockFile("vnb'xy>7#JQ4_gZn");
        URL url = mockFile.toURL();

        // Try to convert the invalid URL to a URI
        try {
            CookieUtil.asUri(url);
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            // Verify the exception
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    /**
     * Tests that a NullPointerException is thrown when null URL is passed to asUri.
     */
    @Test(timeout = 4000)
    public void testAsUriNullUrl() throws Throwable {
        // Try to convert a null URL to a URI
        try {
            CookieUtil.asUri(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    /**
     * Tests that an IllegalArgumentException is thrown when null BiConsumer is passed to applyCookiesToRequest.
     */
    @Test(timeout = 4000)
    public void testApplyCookiesToRequestNullBiConsumer() throws Throwable {
        // Create a request
        HttpConnection.Request request = new HttpConnection.Request();

        // Create attributes
        Attributes attributes = new Attributes();
        attributes.put("", "|noixdm3!");
        Map<String, String> cookies = attributes.dataset();
        request.cookies = cookies;

        // Try to apply cookies with a null BiConsumer
        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Verify the exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    /**
     * Tests that a NullPointerException is thrown when null request is passed to applyCookiesToRequest.
     */
    @Test(timeout = 4000)
    public void testApplyCookiesToRequestNullRequest() throws Throwable {
        // Try to apply cookies with a null request
        try {
            CookieUtil.applyCookiesToRequest(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    /**
     * Tests that parseCookie handles null cookie value.
     */
    @Test(timeout = 4000)
    public void testParseCookieNullValue() throws Throwable {
        CookieUtil.parseCookie(null, null);
    }

    /**
     * Tests that parseCookie handles empty cookie value.
     */
    @Test(timeout = 4000)
    public void testParseCookieEmptyValue() throws Throwable {
        CookieUtil.parseCookie("", null);
    }

    /**
     * Tests storeCookies with a valid URL and response headers.
     */
    @Test(timeout = 4000)
    public void testStoreCookiesValidUrlAndResponse() throws Throwable {
        // Create a request and response
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();

        // Create a URL
        URL url = MockURL.getFtpExample();

        // Create response headers
        HashMap<String, List<String>> responseHeaders = new HashMap<>();
        LinkedList<String> setCookieValues = new LinkedList<>();
        setCookieValues.add("9&{v4vgAJcV4T11n-");
        responseHeaders.put("set-cookie", setCookieValues);

        CookieUtil.storeCookies(request, response, url, responseHeaders);

        // Verify the URL
        assertEquals("ftp://ftp.someFakeButWellFormedURL.org/fooExample", url.toExternalForm());
    }

    /**
     * Tests storeCookies with a valid URL and null response.
     */
    @Test(timeout = 4000)
    public void testStoreCookiesValidUrlAndNullResponse() throws Throwable {
        // Create a request
        HttpConnection.Request request = new HttpConnection.Request();

        // Create a URL
        URL url = MockURL.getHttpExample();

        // Create response headers
        Map<String, List<String>> responseHeaders = request.headers;

        CookieUtil.storeCookies(request, null, url, responseHeaders);

        // Verify the request
        assertEquals(2097152, request.maxBodySize());
    }

    /**
     * Tests applyCookiesToRequest with a valid request and BiConsumer.
     */
    @Test(timeout = 4000)
    public void testApplyCookiesToRequestValidRequestAndBiConsumer() throws Throwable {
        // Create a response
        HttpConnection.Response response = new HttpResponse();

        // Create a request
        HttpConnection.Request request = new HttpRequest();

        // Create cookies
        Map<String, String> cookies = request.cookies;
        response.cookies = cookies;

        // Create a BiConsumer
        BiConsumer<String, String> biConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());

        CookieUtil.parseCookie("org.jsoup.helper.CookieUtil", response);
        CookieUtil.applyCookiesToRequest(request, biConsumer);

        // Verify the request
        assertEquals(30000, request.timeout());
    }

    /**
     * Tests that a new instance of CookieUtil can be created.
     */
    @Test(timeout = 4000)
    public void testNewCookieUtilInstance() throws Throwable {
        new CookieUtil();
    }

    /**
     * Tests applyCookiesToRequest with a valid request and BiConsumer.
     */
    @Test(timeout = 4000)
    public void testApplyCookiesToRequestValidRequestAndBiConsumerValidFollowRedirects() throws Throwable {
        // Create a request
        HttpConnection.Request request = new HttpConnection.Request();

        // Create a BiConsumer
        BiConsumer<String, String> biConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());

        CookieUtil.applyCookiesToRequest(request, biConsumer);

        // Verify the request
        assertTrue(request.followRedirects());
    }
}