package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    private HttpConnection.Response response;

    @BeforeEach
    void setUp() {
        response = new HttpConnection.Response();
    }

    @Test 
    void parseCookie_shouldHandleVariousCookieFormats() {
        // Test valid cookie with all attributes
        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", response);
        
        // Test simple cookie without attributes
        CookieUtil.parseCookie("bar=foo qux", response);
        
        // Test cookie with empty name (malformed)
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", response);
        
        // Test cookie with no name or value (malformed)
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", response);
        
        // Test empty cookie string
        CookieUtil.parseCookie("", response);
        
        // Test null cookie string
        CookieUtil.parseCookie(null, response);

        // Verify results
        assertParsedCookiesAreCorrect();
    }

    private void assertParsedCookiesAreCorrect() {
        // Only 3 cookies should be parsed (valid ones + one malformed that creates an entry)
        assertEquals(3, response.cookies().size(), 
            "Should parse exactly 3 cookie entries from the input");
        
        // Verify valid cookies are parsed correctly
        assertEquals("bar qux", response.cookies.get("foo"), 
            "Cookie 'foo' should have value 'bar qux'");
        assertEquals("foo qux", response.cookies.get("bar"), 
            "Cookie 'bar' should have value 'foo qux'");
        
        // Verify malformed cookie behavior - when no name is provided, 
        // the parser treats the first attribute as the cookie name
        assertEquals(".example.com", response.cookies.get("; Domain"), 
            "Malformed cookie without name should create entry with '; Domain' as key");
    }
}