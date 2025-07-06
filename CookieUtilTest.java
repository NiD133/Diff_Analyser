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
    void testParseCookieHandlesVariousCookieFormats() {
        HttpConnection.Response response = new HttpConnection.Response();

        // Parse cookies with different formats
        CookieUtil.parseCookie(COOKIE_WITH_DOMAIN_AND_PATH, response);
        CookieUtil.parseCookie(COOKIE_WITHOUT_DOMAIN_AND_PATH, response);
        CookieUtil.parseCookie(COOKIE_WITHOUT_NAME, response);
        CookieUtil.parseCookie(COOKIE_WITHOUT_NAME_AND_VALUE, response);
        CookieUtil.parseCookie(EMPTY_COOKIE, response);
        CookieUtil.parseCookie(NULL_COOKIE, response);

        // Assert the number of cookies parsed
        assertEquals(3, response.cookies().size(), "Expected 3 cookies to be parsed");

        // Assert specific cookie values
        assertEquals("bar qux", response.cookies.get("foo"), "Expected 'foo' cookie to have value 'bar qux'");
        assertEquals("foo qux", response.cookies.get("bar"), "Expected 'bar' cookie to have value 'foo qux'");
        
        // Assert handling of cookies without a name
        assertEquals(".example.com", response.cookies.get("; Domain"), "Expected cookie with no name to have domain '.example.com'");
    }
}