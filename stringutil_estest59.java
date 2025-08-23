package org.jsoup.internal;

import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the URL resolution methods in {@link StringUtil}.
 */
public class StringUtilResolveUrlTest {

    /**
     * Verifies that StringUtil.resolve correctly parses a relative URL
     * containing a fragment identifier (the part after the '#').
     */
    @Test
    public void resolveShouldCorrectlyParseUrlWithFragment() throws MalformedURLException {
        // Arrange: Define a base URL and a relative URL with a fragment.
        final URL baseUrl = new URL("http://example.com/path/page.html");
        final String relativeUrlWithFragment = "another-page.html#data-section";

        final String expectedFullUrl = "http://example.com/path/another-page.html#data-section";
        final String expectedFragment = "data-section";

        // Act: Resolve the relative URL against the base URL.
        URL resolvedUrl = StringUtil.resolve(baseUrl, relativeUrlWithFragment);

        // Assert: Verify that the resulting URL and its fragment are correct.
        assertEquals("The full URL should be resolved correctly",
            expectedFullUrl, resolvedUrl.toString());
        assertEquals("The URL fragment should be extracted correctly",
            expectedFragment, resolvedUrl.getRef());
    }
}