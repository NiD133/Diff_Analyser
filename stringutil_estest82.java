package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 * This improved version focuses on clarity, maintainability, and adherence to best practices.
 */
public class StringUtilTest {

    /**
     * Verifies that the startsWithNewline() method correctly handles an empty string.
     * An empty string cannot start with a newline, so the method is expected to return false.
     */
    @Test
    public void startsWithNewline_withEmptyString_shouldReturnFalse() {
        // Arrange: Define the input for the test case.
        String input = "";

        // Act: Call the method under test.
        boolean result = StringUtil.startsWithNewline(input);

        // Assert: Verify that the result is the expected value.
        assertFalse("An empty string should not be considered as starting with a newline.", result);
    }
}