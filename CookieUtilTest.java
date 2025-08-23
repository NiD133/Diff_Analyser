package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    @Test
    void testParseCookie() {
        // Create a new Response object to store cookies
        HttpConnection.Response response = new HttpConnection.Response();

        // Test case 1: Valid cookie with domain, path, and secure attributes
        CookieUtil.parseCookie("foo=bar qux; Domain=.example.com; Path=/; Secure", response);
        
        // Test case 2: Valid cookie without domain and path attributes
        CookieUtil.parseCookie("bar=foo qux", response);
        
        // Test case 3: Invalid cookie with missing name
        CookieUtil.parseCookie("=bar; Domain=.example.com; Path=/; Secure", response);
        
        // Test case 4: Invalid cookie with missing name and value
        CookieUtil.parseCookie("; Domain=.example.com; Path=/", response);
        
        // Test case 5: Empty cookie string
        CookieUtil.parseCookie("", response);
        
        // Test case 6: Null cookie string
        CookieUtil.parseCookie(null, response);

        // Verify the number of cookies parsed
        assertEquals(3, response.cookies().size(), "Expected 3 cookies to be parsed");

        // Verify the value of the 'foo' cookie
        assertEquals("bar qux", response.cookies.get("foo"), "Expected 'foo' cookie to have value 'bar qux'");

        // Verify the value of the 'bar' cookie
        assertEquals("foo qux", response.cookies.get("bar"), "Expected 'bar' cookie to have value 'foo qux'");

        // Verify the handling of an invalid cookie with no actual name or value
        assertEquals(".example.com", response.cookies.get("; Domain"), "Expected invalid cookie to have domain '.example.com'");
    }
}