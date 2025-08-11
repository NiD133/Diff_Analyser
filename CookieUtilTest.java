package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    @Test
    void parseCookie_handlesValidAndMalformedHeaders() {
        // Given: an empty Response that will collect parsed cookies
        HttpConnection.Response res = new HttpConnection.Response();

        // When: parsing a mix of well-formed, malformed, empty, and null cookie header values

        // well-formed cookie with attributes (attributes are not stored in the cookies map)
        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", res);

        // well-formed cookie without attributes
        CookieUtil.parseCookie("bar=foo qux", res);

        // malformed: missing cookie name before '='; current behavior records the attribute pair
        // 'Domain=.example.com' as a cookie with literal key '; Domain'
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", res);

        // malformed/empty cookie followed only by attributes (should not add additional cookies)
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", res);

        // empty and null inputs should be ignored without error
        CookieUtil.parseCookie("", res);
        CookieUtil.parseCookie(null, res);

        // Then: only the expected cookies are present
        Map<String, String> cookies = res.cookies();

        assertAll("parsed cookies",
            () -> assertEquals(3, cookies.size(), "Unexpected number of stored cookies"),
            () -> assertEquals("bar qux", cookies.get("foo"), "value for 'foo'"),
            () -> assertEquals("foo qux", cookies.get("bar"), "value for 'bar'"),
            () -> assertEquals(".example.com", cookies.get("; Domain"),
                "malformed input yields a literal '; Domain' key with the domain value")
        );
    }
}