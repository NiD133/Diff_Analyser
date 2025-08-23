package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the StringUtil.resolve() method.
 */
class StringUtilTest {

    @Test
    @DisplayName("resolve() should strip control characters from URLs before processing")
    void resolveStripsControlCharsBeforeProcessing() {
        // Arrange: Define base and relative URLs containing various control characters
        // (e.g., \n, \t, \r, \b).
        String baseUrlWithControlChars = "\nhttps://\texample.com/";
        String relUrlWithControlChars = "\r\nfo\to:ba\br";

        // The `resolve` method is expected to strip control characters from the relative URL,
        // resulting in "foo:bar". Because this string contains a URL scheme ("foo:"),
        // it is treated as an absolute URL, and the base URL is ignored.
        String expectedResolvedUrl = "foo:bar";

        // Act: Resolve the URL.
        String actualResolvedUrl = resolve(baseUrlWithControlChars, relUrlWithControlChars);

        // Assert: The result should be the cleaned-up relative URL.
        assertEquals(expectedResolvedUrl, actualResolvedUrl);
    }
}