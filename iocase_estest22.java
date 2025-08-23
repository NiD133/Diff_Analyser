package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkStartsWith() returns false when the prefix argument is null.
     * This behavior should be consistent regardless of the case-sensitivity rule.
     */
    @Test
    public void checkStartsWithShouldReturnFalseForNullPrefix() {
        // Arrange: Set up the test case and inputs.
        // The specific IOCase (e.g., INSENSITIVE) should not affect the outcome for a null input.
        final IOCase ioCase = IOCase.INSENSITIVE;
        final String text = "Some text";
        final String nullPrefix = null;

        // Act: Call the method under test.
        final boolean result = ioCase.checkStartsWith(text, nullPrefix);

        // Assert: Verify the result.
        assertFalse("checkStartsWith should return false when the prefix to check against is null.", result);
    }
}