package org.jsoup.internal;

import org.junit.jupiter.api.Test;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringUtil#resolve(String, String)}.
 */
public class StringUtilTest {

    @Test
    void resolveUrlShouldPreserveSpacesInPathComponent() {
        // Arrange: Define a base URL and a relative URL containing a space.
        // The relative path "../" is used to test path traversal.
        String baseUrl = "https://example.com/example/";
        String relativeUrlWithSpace = "../foo bar/";
        String expectedUrl = "https://example.com/foo bar/";

        // Act: Resolve the relative URL against the base URL.
        String actualUrl = resolve(baseUrl, relativeUrlWithSpace);

        // Assert: Verify the resulting URL is correctly formed and preserves the space.
        assertEquals(expectedUrl, actualUrl);
    }
}