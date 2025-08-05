package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    @Test
    void parseCookie_validWithAttributes() {
        HttpConnection.Response res = new HttpConnection.Response();

        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", res);

        assertEquals(1, res.cookies().size());
        assertEquals("bar qux", res.cookies().get("foo"));
    }

    @Test
    void parseCookie_validWithoutAttributes() {
        HttpConnection.Response res = new HttpConnection.Response();

        CookieUtil.parseCookie("bar=foo qux", res);

        assertEquals(1, res.cookies().size());
        assertEquals("foo qux", res.cookies().get("bar"));
    }

    @Test
    void parseCookie_emptyNameWithAttributes() {
        HttpConnection.Response res = new HttpConnection.Response();

        // This malformed cookie string results in the first attribute being treated as the cookie name
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", res);

        assertEquals(1, res.cookies().size());
        // The first attribute becomes the cookie name since there's no proper name=value pair
        assertEquals(".example.com", res.cookies().get("; Domain"));
    }

    @Test
    void parseCookie_onlyAttributes() {
        HttpConnection.Response res = new HttpConnection.Response();

        // No valid cookie name=value pair, but attributes are present
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", res);

        assertEquals(1, res.cookies().size());
        // The first attribute becomes the cookie name
        assertEquals(".example.com", res.cookies().get("; Domain"));
    }

    @Test
    void parseCookie_emptyOrNull_doesNotAddCookies() {
        HttpConnection.Response res = new HttpConnection.Response();

        CookieUtil.parseCookie("", res);
        CookieUtil.parseCookie(null, res);

        assertTrue(res.cookies().isEmpty(), "Should not add cookies for empty or null inputs");
    }

    @Test
    void parseCookie_multipleCalls_accumulatesValidCookies() {
        HttpConnection.Response res = new HttpConnection.Response();

        // Valid cookies
        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", res);
        CookieUtil.parseCookie("bar=foo qux", res);
        
        // Malformed cookies that still produce entries
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", res);
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", res);
        
        // Should be ignored
        CookieUtil.parseCookie("", res);
        CookieUtil.parseCookie(null, res);

        // Verify all valid cookies are present
        assertEquals(3, res.cookies().size());
        assertEquals("bar qux", res.cookies().get("foo"));
        assertEquals("foo qux", res.cookies().get("bar"));
        assertEquals(".example.com", res.cookies().get("; Domain"));
    }
}