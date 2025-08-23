package org.jsoup.helper;

import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the CookieUtil helper class.
 */
public class CookieUtilTest {

    /**
     * Verifies that calling parseCookie with a null input string
     * does not throw an exception or modify the response object.
     */
    @Test
    public void parseCookieWithNullStringShouldDoNothing() {
        // Arrange: Create a new Response object.
        // By default, it has a status code of 0 and an empty cookie map.
        HttpConnection.Response response = new HttpConnection.Response();
        int initialStatusCode = response.statusCode();

        // Act: Call the method under test with a null cookie string.
        CookieUtil.parseCookie(null, response);

        // Assert: Verify that the response object remains unchanged.
        assertTrue("The cookie map should remain empty.", response.cookies().isEmpty());
        assertEquals("The status code should not be modified.", initialStatusCode, response.statusCode());
    }
}