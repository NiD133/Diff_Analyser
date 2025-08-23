package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that the non-breaking space character (code point 160) is correctly
     * identified as "actual" whitespace by the isActuallyWhitespace method.
     */
    @Test
    public void isActuallyWhitespaceRecognizesNonBreakingSpace() {
        // Arrange: The code point for a non-breaking space is 160.
        final int nonBreakingSpaceCodePoint = 160;

        // Act: Check if the code point is considered whitespace.
        boolean result = StringUtil.isActuallyWhitespace(nonBreakingSpaceCodePoint);

        // Assert: The method should return true.
        assertTrue("The non-breaking space (code point 160) should be identified as whitespace.", result);
    }
}