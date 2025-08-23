package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Verifies that isBlank() returns false for a string that contains
     * non-whitespace characters.
     */
    @Test
    public void isBlank_shouldReturnFalse_forNonBlankString() {
        // Arrange: A string with various non-whitespace characters
        String nonBlankString = "_5s]9,";

        // Act & Assert: The string should not be considered blank
        assertFalse("A string with content should not be identified as blank.", StringUtil.isBlank(nonBlankString));
    }
}