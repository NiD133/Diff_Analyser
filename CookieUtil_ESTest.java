package org.jsoup.helper;

import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable and maintainable tests for CookieUtil.
 *
 * Focus:
 * - Clear test names describing behavior
 * - Minimal mocking and no EvoSuite-specific scaffolding
 * - Assertions that verify meaningful, user-visible effects
 */
public class CookieUtilTest {

    @Test
    public void asUri_convertsUrlToUriAndKeepsSchemeAndPath() throws Exception {
        URL url = new URL("https://example.com/path?q=1");
        URI uri = CookieUtil.asUri(url);

        assertEquals("https", uri.getScheme());
        assertEquals("/path", uri.getPath());
    }

    @Test(expected = NullPointerException.class)
    public void asUri_nullUrl_throwsNullPointerException() throws Exception {
        CookieUtil.asUri(null);
    }

    @Test
    public void applyCookiesToRequest_includesRequestCookiesInCookieHeader() throws Exception {
        HttpConnection.Request req = new HttpConnection.Request();
        // Add a simple request cookie
        req.cookies.put("session", "abc123");

        List<AbstractMap.SimpleEntry<String, String>> headers = new ArrayList<>();
        CookieUtil.applyCookiesToRequest(req, (name, value) ->
            headers.add(new AbstractMap.SimpleEntry<>(name, value)));

        String cookieHeaderValue = headers.stream()
            .filter(e -> "Cookie".equals(e.getKey()))
            .map(AbstractMap.SimpleEntry::getValue)
            .findFirst()
            .orElse(null);

        assertNotNull("Cookie header should be added when request has cookies", cookieHeaderValue);
        assertTrue("Cookie header should contain the request cookie",
            cookieHeaderValue.contains("session=abc123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyCookiesToRequest_rejectsEmptyCookieName() throws Exception {
        HttpConnection.Request req = new HttpConnection.Request();
        // Invalid cookie name (empty) should be rejected by validation
        req.cookies.put("", "value");

        CookieUtil.applyCookiesToRequest(req, (k, v) -> { /* no-op */ });
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeCookies_nullHeaders_throwsIllegalArgumentException() throws Exception {
        HttpConnection.Request req = new HttpConnection.Request();
        HttpConnection.Response res = new HttpConnection.Response();
        URL url = new URL("https://example.com/");

        CookieUtil.storeCookies(req, res, url, null);
    }

    @Test
    public void storeCookies_withSetCookieHeader_populatesResponseCookies() throws Exception {
        HttpConnection.Request req = new HttpConnection.Request();
        HttpConnection.Response res = new HttpConnection.Response(req);
        URL url = new URL("https://example.com/");

        HashMap<String, List<String>> headers = new HashMap<>();
        headers.put("Set-Cookie", Collections.singletonList("theme=light; Path=/"));

        CookieUtil.storeCookies(req, res, url, headers);

        assertEquals("Expected cookie parsed from Set-Cookie header", "light", res.cookies.get("theme"));
    }

    @Test
    public void storeCookies_withUnrelatedHeaders_doesNotModifyResponseCookies() throws Exception {
        HttpConnection.Request req = new HttpConnection.Request();
        HttpConnection.Response res = new HttpConnection.Response(req);
        URL url = new URL("https://example.com/");

        HashMap<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("text/html"));

        CookieUtil.storeCookies(req, res, url, headers);

        assertTrue("No cookies should be added when no Set-Cookie headers are present", res.cookies.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void parseCookie_nullResponse_throwsNullPointerException() {
        CookieUtil.parseCookie("a=b", null);
    }

    @Test
    public void parseCookie_nullOrEmptyValue_doesNotAddCookies() {
        HttpConnection.Response res = new HttpConnection.Response();

        CookieUtil.parseCookie(null, res);
        CookieUtil.parseCookie("", res);

        assertTrue("Response cookies should remain empty when value is null or empty", res.cookies.isEmpty());
    }
}