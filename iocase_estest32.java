package org.apache.commons.io;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.SENSITIVE.checkEquals() correctly returns false
     * when comparing an empty string with a non-empty string.
     */
    @Test
    public void checkEqualsWithSensitiveCaseShouldReturnFalseForEmptyAndNonEmptyString() {
        // Arrange: Define the case-sensitivity and the strings to compare.
        final IOCase sensitiveCase = IOCase.SENSITIVE;
        final String emptyString = "";
        final String nonEmptyString = "&b";

        // Act: Perform the case-sensitive comparison.
        final boolean areEqual = sensitiveCase.checkEquals(emptyString, nonEmptyString);

        // Assert: Verify that the strings are not considered equal.
        assertFalse("An empty string should not be equal to a non-empty string.", areEqual);
    }
}