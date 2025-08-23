package org.jsoup.helper;

import org.junit.Test;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.evosuite.runtime.mock.java.net.MockURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link CookieUtil}.
 */
// Note: The class name has been improved from the generated 'CookieUtil_ESTestTest4'.
public class CookieUtilTest {

    /**
     * Verifies that storeCookies throws an IllegalArgumentException when the response headers map is null,
     * as this is the expected behavior from the underlying java.net.CookieManager.
     */
    @Test
    public void storeCookiesWithNullHeadersThrowsIllegalArgumentException() throws IOException {
        // Arrange: Create the necessary objects for the method call.
        URL url = MockURL.getFtpExample(); // A mock URL is sufficient.
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> nullHeaders = null;

        try {
            // Act: Call the method under test with a null headers map.
            CookieUtil.storeCookies(request, response, url, nullHeaders);
            fail("Expected an IllegalArgumentException to be thrown due to null headers.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            assertEquals("Argument is null", e.getMessage());
        }
    }
}