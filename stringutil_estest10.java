package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the URL resolution functionality in {@link StringUtil}.
 */
public class StringUtilResolveTest {

    @Test
    public void resolveShouldReturnEmptyStringForMalformedBaseUrl() {
        // The documentation for StringUtil.resolve(baseUrl, relUrl) specifies that it should
        // return an empty string if a valid absolute URL cannot be generated.
        // This test case verifies this behavior when the provided base URL is malformed.

        // Arrange: Define a base URL that is not a valid URL format.
        String malformedBaseUrl = "org.jsoup.internal.SoftPool"; // This string lacks a protocol and is not a valid URL.
        String relativeUrl = "q/R|zQ`/Gq zKzj";

        // Act: Attempt to resolve the URL.
        String result = StringUtil.resolve(malformedBaseUrl, relativeUrl);

        // Assert: The result should be an empty string, indicating failure.
        assertEquals("Expected an empty string when the base URL is malformed.", "", result);
    }
}