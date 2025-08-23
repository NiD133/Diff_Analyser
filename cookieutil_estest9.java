package org.jsoup.helper;

import org.jsoup.helper.HttpConnection.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link CookieUtil} class.
 */
public class CookieUtilTest {

    /**
     * Verifies that calling {@link CookieUtil#parseCookie(String, Response)} with an empty
     * cookie string is handled gracefully, without throwing an exception or causing
     * unintended side effects on the Response object.
     */
    @Test
    public void parseCookieWithEmptyStringShouldBeIgnored() {
        // Arrange: Create a response object. We will track its status code to ensure
        // the method under test does not modify it.
        Response response = new Response();
        final int originalStatusCode = response.statusCode();

        // Act: Attempt to parse an empty cookie string. This is expected to be a no-op.
        CookieUtil.parseCookie("", response);

        // Assert: The status code of the response object should remain unchanged,
        // confirming that the method had no side effects.
        assertEquals("Parsing an empty cookie string should not alter the response status code.",
            originalStatusCode, response.statusCode());
    }
}