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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CookieUtil_ESTest extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAsUriWithHttpExample() throws Throwable {
        // Test conversion of a mock HTTP URL to URI
        URL url = MockURL.getHttpExample();
        URI uri = CookieUtil.asUri(url);
        assertNull(uri.getRawQuery());
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithNullURL() throws Throwable {
        // Test storing cookies with a null URL, expecting NullPointerException
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        try {
            CookieUtil.storeCookies(request, response, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithNullResponse() throws Throwable {
        // Test storing cookies with a null response, expecting IllegalArgumentException
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = MockURL.getFtpExample();
        try {
            CookieUtil.storeCookies(request, null, url, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.net.CookieManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseCookieWithNullResponse() throws Throwable {
        // Test parsing a cookie with a null response, expecting NullPointerException
        try {
            CookieUtil.parseCookie("Set-Cookie", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testAsUriWithMalformedURL() throws Throwable {
        // Test conversion of a malformed URL to URI, expecting MalformedURLException
        MockFile mockFile = new MockFile("vnb'xy>7#JQ4_gZn");
        URL url = mockFile.toURL();
        try {
            CookieUtil.asUri(url);
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testAsUriWithNullURL() throws Throwable {
        // Test conversion of a null URL to URI, expecting NullPointerException
        try {
            CookieUtil.asUri(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithEmptyString() throws Throwable {
        // Test applying cookies with an empty string, expecting IllegalArgumentException
        HttpConnection.Request request = new HttpConnection.Request();
        Attributes attributes = new Attributes();
        attributes.put("", "|noixdm3!");
        request.cookies = attributes.dataset();
        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithNullRequest() throws Throwable {
        // Test applying cookies with a null request, expecting NullPointerException
        try {
            CookieUtil.applyCookiesToRequest(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseCookieWithEmptyString() throws Throwable {
        // Test parsing a cookie with an empty string
        CookieUtil.parseCookie("", null);
    }

    @Test(timeout = 4000)
    public void testParseCookieWithNullString() throws Throwable {
        // Test parsing a cookie with a null string
        CookieUtil.parseCookie(null, null);
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithValidData() throws Throwable {
        // Test storing cookies with valid data
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = MockURL.getFtpExample();
        HashMap<String, List<String>> headers = new HashMap<>();
        LinkedList<String> cookies = new LinkedList<>();
        cookies.add("9&{v4vgAJcV4T11n-");
        headers.put("set-cookie", cookies);
        CookieUtil.storeCookies(request, response, url, headers);
        assertEquals("ftp://ftp.someFakeButWellFormedURL.org/fooExample", url.toExternalForm());
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithRequestHeaders() throws Throwable {
        // Test storing cookies with request headers
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = MockURL.getHttpExample();
        Map<String, List<String>> headers = request.headers;
        CookieUtil.storeCookies(request, null, url, headers);
        assertEquals(2097152, request.maxBodySize());
    }

    @Test(timeout = 4000)
    public void testParseAndApplyCookies() throws Throwable {
        // Test parsing and applying cookies
        HttpConnection.Response response = new HttpConnection.Response();
        HttpConnection.Request request = new HttpConnection.Request();
        request.cookies = response.cookies;
        CookieUtil.parseCookie("org.jsoup.helper.CookieUtil", response);
        BiConsumer<String, String> biConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());
        CookieUtil.applyCookiesToRequest(request, biConsumer);
        assertEquals(30000, request.timeout());
    }

    @Test(timeout = 4000)
    public void testCookieUtilConstructor() throws Throwable {
        // Test CookieUtil constructor
        CookieUtil cookieUtil = new CookieUtil();
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithMockBiConsumer() throws Throwable {
        // Test applying cookies to request with a mock BiConsumer
        HttpConnection.Request request = new HttpConnection.Request();
        BiConsumer<String, String> biConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());
        CookieUtil.applyCookiesToRequest(request, biConsumer);
        assertTrue(request.followRedirects());
    }
}