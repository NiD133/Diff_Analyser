package org.jsoup.helper;

import org.jsoup.Connection;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CookieUtilTest {

    @Test
    void parseCookie_parsesCookiesCorrectly() {
        // Arrange
        HttpConnection.Response response = new HttpConnection.Response();

        // Act
        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", response);
        CookieUtil.parseCookie("bar=foo qux", response);
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", response); // Cookie with empty name
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", response);  // Cookie with no name or value
        CookieUtil.parseCookie("", response); // Empty cookie string
        CookieUtil.parseCookie(null, response); // Null cookie string

        // Assert
        Map<String, String> cookies = response.cookies();
        assertEquals(3, cookies.size(), "Should have parsed three cookies.");
        assertEquals("bar qux", cookies.get("foo"), "The 'foo' cookie should have the correct value.");
        assertEquals("foo qux", cookies.get("bar"), "The 'bar' cookie should have the correct value.");
        assertEquals(".example.com", cookies.get("; Domain"), "The '; Domain' cookie should have the correct value (from empty name cookie).");
    }
}