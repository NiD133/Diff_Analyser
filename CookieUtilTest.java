package org.jsoup.helper;

import org.jsoup.helper.HttpConnection.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CookieUtil.parseCookie")
class CookieUtilTest {

    @Test
    @DisplayName("should parse a well-formed cookie string with attributes")
    void parseWellFormedCookieWithAttributes() {
        // Arrange
        Response response = new Response();
        String cookieString = "foo=bar qux; Domain=.example.com; Path=/; Secure";

        // Act
        CookieUtil.parseCookie(cookieString, response);

        // Assert
        Map<String, String> cookies = response.cookies();
        assertEquals(1, cookies.size());
        assertEquals("bar qux", cookies.get("foo"));
    }

    @Test
    @DisplayName("should parse a simple cookie string without attributes")
    void parseSimpleCookieWithoutAttributes() {
        // Arrange
        Response response = new Response();
        String cookieString = "bar=foo qux";

        // Act
        CookieUtil.parseCookie(cookieString, response);

        // Assert
        Map<String, String> cookies = response.cookies();
        assertEquals(1, cookies.size());
        assertEquals("foo qux", cookies.get("bar"));
    }

    @Test
    @DisplayName("should ignore a cookie string with a value but no name")
    void parseCookieWithNoNameIsIgnored() {
        // Arrange
        Response response = new Response();
        String cookieString = "=bar; Domain=.example.com; Path=/; Secure";

        // Act
        CookieUtil.parseCookie(cookieString, response);

        // Assert
        assertTrue(response.cookies().isEmpty(), "Cookies should be empty for a cookie string with no name.");
    }

    @Test
    @DisplayName("should treat the first attribute as a cookie if no name/value pair exists")
    void parseCookieWithOnlyAttributesStoresFirstAttribute() {
        // Arrange
        Response response = new Response();
        // This documents the quirky behavior where an attribute-only string is parsed,
        // and the first attribute is treated as the cookie itself.
        String cookieString = "; Domain=.example.com; Path=/";

        // Act
        CookieUtil.parseCookie(cookieString, response);

        // Assert
        Map<String, String> cookies = response.cookies();
        assertEquals(1, cookies.size());
        assertEquals(".example.com", cookies.get("; Domain"), "The key should be the attribute name and the value its value.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should do nothing when parsing a null or empty string")
    void parseNullOrEmptyCookieStringDoesNothing(String cookieString) {
        // Arrange
        Response response = new Response();

        // Act
        CookieUtil.parseCookie(cookieString, response);

        // Assert
        assertTrue(response.cookies().isEmpty(), "Cookies should be empty for null or empty input.");
    }

    @Test
    @DisplayName("should accumulate cookies from multiple parse calls")
    void parseMultipleCookieStringsShouldAccumulate() {
        // Arrange
        Response response = new Response();

        // Act
        CookieUtil.parseCookie("foo=bar", response);
        CookieUtil.parseCookie("bar=foo qux", response);

        // Assert
        Map<String, String> cookies = response.cookies();
        assertEquals(2, cookies.size());
        assertEquals("bar", cookies.get("foo"));
        assertEquals("foo qux", cookies.get("bar"));
    }
}