package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Note: The original test was auto-generated and appeared to be testing for a
 * resource-exhaustion error under specific, unclear conditions.
 * This version has been rewritten to be a clear, deterministic functional test
 * that verifies the correctness of `normaliseWhitespace` with large input.
 */
public class StringUtilTest { // Renamed for clarity

    @Test
    public void normaliseWhitespaceWithLargePaddedStringReturnsSingleSpace() {
        // Arrange: Create a very long string consisting only of spaces.
        // The original test used 8170, which we preserve here in case of significance.
        final int largePaddingWidth = 8170;
        String largeWhitespaceString = StringUtil.padding(largePaddingWidth, -1); // -1 means unlimited padding

        // Act: Normalize the whitespace in the large string.
        String result = StringUtil.normaliseWhitespace(largeWhitespaceString);

        // Assert: The entire string of spaces should be collapsed into a single space.
        assertEquals(" ", result);
    }
}