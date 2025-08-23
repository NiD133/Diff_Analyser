package org.jsoup.helper;

import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import java.io.IOException;

/**
 * Test suite for {@link CookieUtil}.
 */
public class CookieUtilTest {

    /**
     * Verifies that applyCookiesToRequest throws a NullPointerException
     * when the provided BiConsumer for setting headers is null.
     */
    @Test(expected = NullPointerException.class)
    public void applyCookiesToRequestThrowsNullPointerExceptionForNullSetter() throws IOException {
        // Arrange: Create a request object. The state of its cookies is irrelevant for this test.
        HttpConnection.Request request = new HttpConnection.Request();

        // Act: Call the method under test with a null setter.
        // Assert: The test expects a NullPointerException, as declared by the @Test annotation.
        CookieUtil.applyCookiesToRequest(request, null);
    }
}