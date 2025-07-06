package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    private static final String COOKIE_STRING1 = "foo=bar qux; Domain=.example.com; Path=/; Secure";
    private static final String COOKIE_STRING2 = "bar=foo qux";
    private static final String COOKIE_STRING3 = "=bar; Domain=.example.com; Path=/; Secure";
    private static final String COOKIE_STRING4 = "; Domain=.example.com; Path=/";
    private static final String COOKIE_STRING5 = "";
    private static final String COOKIE_STRING6 = null;

    @Test
    void testParseCookieMultipleValidCookies() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Test parsing multiple valid cookies
        CookieUtil.parseCookie(COOKIE_STRING1, response);
        CookieUtil.parseCookie(COOKIE_STRING2, response);

        assertEquals(2, response.cookies().size());
        assertEquals("bar qux", response.cookies().get("foo"));
        assertEquals("foo qux", response.cookies().get("bar"));
    }

    @Test
    void testParseCookieInvalidCookieNames() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Test parsing cookies with invalid names
        CookieUtil.parseCookie(COOKIE_STRING3, response);
        CookieUtil.parseCookie(COOKIE_STRING4, response);

        assertEquals(0, response.cookies().size());
    }

    @Test
    void testParseCookieEmptyAndNullCookies() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Test parsing empty and null cookies
        CookieUtil.parseCookie(COOKIE_STRING5, response);
        CookieUtil.parseCookie(COOKIE_STRING6, response);

        assertEquals(0, response.cookies().size());
    }

    @Test
    void testParseCookieMixedCases() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Test parsing a mix of valid and invalid cookies
        CookieUtil.parseCookie(COOKIE_STRING1, response);
        CookieUtil.parseCookie(COOKIE_STRING2, response);
        CookieUtil.parseCookie(COOKIE_STRING3, response);
        CookieUtil.parseCookie(COOKIE_STRING4, response);
        CookieUtil.parseCookie(COOKIE_STRING5, response);
        CookieUtil.parseCookie(COOKIE_STRING6, response);

        assertEquals(2, response.cookies().size());
        assertEquals("bar qux", response.cookies().get("foo"));
        assertEquals("foo qux", response.cookies().get("bar"));
    }
}