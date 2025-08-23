package org.jsoup.helper;

import org.jsoup.helper.HttpConnection.Request;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CookieUtil_ESTestTest8 extends CookieUtil_ESTest_scaffolding {

    /**
     * Verifies that applyCookiesToRequest throws an IllegalArgumentException
     * when a request contains a cookie with an empty name.
     */
    @Test(timeout = 4000)
    public void applyCookiesToRequestThrowsForEmptyCookieName() throws IOException {
        // Arrange: Create a request containing a cookie with an empty string as its name.
        // The method under test is expected to validate that cookie names are not empty.
        Request request = new Request();
        Map<String, String> cookiesWithEmptyName = new HashMap<>();
        cookiesWithEmptyName.put("", "some-value"); // Invalid cookie name
        request.cookies(cookiesWithEmptyName);

        // Act & Assert: Expect an IllegalArgumentException with a specific message.
        try {
            CookieUtil.applyCookiesToRequest(request, (key, value) -> {
                // This header-setting consumer will not be called, as the exception is thrown first.
            });
            fail("Expected an IllegalArgumentException for the empty cookie name, but none was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is the one expected from the validation logic.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}