package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
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
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CookieUtil_ESTest extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testConvertFtpUrlToUri() throws Throwable {
        URL ftpUrl = MockURL.getFtpExample();
        URI uri = CookieUtil.asUri(ftpUrl);
        assertEquals("ftp", uri.getScheme());
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithMalformedUrl() throws Throwable {
        HttpConnection.Response response = new HttpConnection.Response();
        HttpConnection.Request request = new HttpConnection.Request();
        Map<String, List<String>> headers = request.headers;
        MockFile mockFile = new MockFile("@]?3#", "@]?3#");
        URL malformedUrl = mockFile.toURL();

        try {
            CookieUtil.storeCookies(request, response, malformedUrl, headers);
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithNullUrl() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> headers = request.headers;

        try {
            CookieUtil.storeCookies(request, response, null, headers);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithNullHeaders() throws Throwable {
        URL ftpUrl = MockURL.getFtpExample();
        HttpConnection.Response response = new HttpConnection.Response();
        HttpConnection.Request request = new HttpConnection.Request();

        try {
            CookieUtil.storeCookies(request, response, ftpUrl, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.net.CookieManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseCookieWithNullResponse() throws Throwable {
        try {
            CookieUtil.parseCookie("del", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertMalformedUrlToUri() throws Throwable {
        MockFile mockFile = new MockFile("\u0005!/", "\u0005!/");
        URL malformedUrl = mockFile.toURL();

        try {
            CookieUtil.asUri(malformedUrl);
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertNullUrlToUri() throws Throwable {
        try {
            CookieUtil.asUri(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithEmptyString() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        TextNode textNode = TextNode.createFromEncoded("");
        Attributes attributes = textNode.attributes();
        attributes.put("", "");
        Map<String, String> cookies = attributes.dataset();
        request.cookies = cookies;

        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyCookie() throws Throwable {
        HttpConnection.Response response = new HttpConnection.Response();
        CookieUtil.parseCookie("", response);
        assertEquals(400, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testParseNullCookie() throws Throwable {
        HttpConnection.Response response = new HttpConnection.Response();
        CookieUtil.parseCookie(null, response);
        assertEquals(400, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithValidData() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = request.url;
        HashMap<String, List<String>> headers = new HashMap<>();
        LinkedList<String> cookies = new LinkedList<>();
        cookies.add("XtzONrG&vb");
        headers.put("Set-Cookie", cookies);
        HttpConnection.Response response = new HttpConnection.Response(request);

        CookieUtil.storeCookies(request, response, url, headers);
        assertEquals(0, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testStoreCookiesWithEmptyHeaders() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = request.url;
        HashMap<String, List<String>> headers = new HashMap<>();
        LinkedList<String> emptyList = new LinkedList<>();
        headers.put("inputStream", emptyList);
        HttpConnection.Response response = new HttpConnection.Response(request);

        CookieUtil.storeCookies(request, response, url, headers);
        assertNull(request.requestBody());
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithNullBiConsumer() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, String> cookies = request.cookies;
        response.cookies = cookies;
        CookieUtil.parseCookie("wToM>AI", response);

        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testCookieUtilConstructor() throws Throwable {
        CookieUtil cookieUtil = new CookieUtil();
        assertNotNull(cookieUtil);
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequestWithMockBiConsumer() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        BiConsumer<String, String> mockBiConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());
        CookieUtil.applyCookiesToRequest(request, mockBiConsumer);
        assertNull(request.requestBody());
    }
}