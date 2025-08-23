package org.jsoup.helper;

import org.jsoup.helper.HttpConnection.Request;
import org.junit.Test;
import java.io.IOException;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;

/**
 * Test suite for {@link CookieUtil}.
 * This class focuses on improving a single, tool-generated test case for clarity.
 */
public class CookieUtilTest {

    /**
     * Verifies that applyCookiesToRequest does nothing when the request has no URL.
     * <p>
     * The method should return early without attempting to set any cookie headers
     * because cookies are domain-specific and require a URL for context.
     * </p>
     */
    @Test
    @SuppressWarnings("unchecked") // Required for mocking a generic BiConsumer
    public void applyCookiesToRequestDoesNothingWhenRequestHasNoUrl() throws IOException {
        // Arrange: Create a request without a URL and a mock for setting headers.
        Request request = new Request();
        BiConsumer<String, String> mockHeaderSetter = mock(BiConsumer.class);

        // Act: Call the method under test.
        CookieUtil.applyCookiesToRequest(request, mockHeaderSetter);

        // Assert: Verify that the header setter was never called.
        verify(mockHeaderSetter, never()).accept(anyString(), anyString());
    }
}