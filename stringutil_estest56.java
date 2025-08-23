package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil#resolve(String, String)} method.
 */
public class StringUtilResolveTest {

    /**
     * Tests that if the relative URL argument is already an absolute URL (i.e., it contains a protocol scheme),
     * the method should return it unchanged, ignoring the base URL.
     */
    @Test
    public void resolveShouldReturnAbsoluteUrlUnchanged() {
        // Arrange
        String baseUrl = "https://example.com/some/path";
        // The following string is treated as an absolute URL because it starts with "l:",
        // which is parsed as a valid URI scheme.
        String absoluteUrl = "l:h06g#US h|";

        // Act
        String resolvedUrl = StringUtil.resolve(baseUrl, absoluteUrl);

        // Assert
        assertEquals("The absolute URL should be returned as-is.", absoluteUrl, resolvedUrl);
    }
}