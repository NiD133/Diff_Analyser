package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    private static final String COOKIE_WITH_DOMAIN_AND_PATH = "foo=bar qux; Domain=.example.com; Path=/; Secure";
    private static final String COOKIE_WITHOUT_DOMAIN_AND_PATH = "bar=foo qux";
    private static final String COOKIE_WITHOUT_NAME = "=bar; Domain=.example.com; Path=/; Secure";
    private static final String COOKIE_WITHOUT_NAME_AND_VALUE = "; Domain=.example.com; Path=/";
    private static final String EMPTY_COOKIE = "";
    private static final String NULL_COOKIE = null;

    @Test
    void testParseCookieWithVariousInputs() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Test parsing a cookie with domain and path
        CookieUtil.parseCookie(COOKIE_WITH_DOMAIN_AND_PATH, response);
        // Test parsing a cookie without domain and path
        CookieUtil.parseCookie(COOKIE_WITHOUT_DOMAIN_AND_PATH, response);
        // Test parsing a cookie without a name
        CookieUtil.parseCookie(COOKIE_WITHOUT_NAME, response);
        // Test parsing a cookie without a name and value
        CookieUtil.parseCookie(COOKIE_WITHOUT_NAME_AND_VALUE, response);
        // Test parsing an empty cookie string
        CookieUtil.parseCookie(EMPTY_COOKIE, response);
        // Test parsing a null cookie string
        CookieUtil.parseCookie(NULL_COOKIE, response);

        // Verify the number of cookies parsed
        assertEquals(3, response.cookies().size(), "Expected 3 cookies to be parsed");

        // Verify the values of the parsed cookies
        assertEquals("bar qux", response.cookies.get("foo"), "Expected value for cookie 'foo' to be 'bar qux'");
        assertEquals("foo qux", response.cookies.get("bar"), "Expected value for cookie 'bar' to be 'foo qux'");
        assertEquals(".example.com", response.cookies.get("; Domain"), "Expected value for cookie '; Domain' to be '.example.com'");
    }
}