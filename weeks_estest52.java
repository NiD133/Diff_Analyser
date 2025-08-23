package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link Weeks#parseWeeks(String)} method.
 */
public class WeeksTest {

    /**
     * Verifies that parseWeeks() throws an IllegalArgumentException when provided with a string
     * that does not conform to the expected ISO8601 period format (e.g., 'PnW').
     */
    @Test
    public void parseWeeks_shouldThrowIllegalArgumentException_forInvalidFormatString() {
        // Arrange: Define an input string that has an invalid format.
        String invalidPeriodString = ")%X[WS";
        String expectedErrorMessage = "Invalid format: \")%X[WS\"";

        // Act & Assert: Attempt to parse the invalid string and verify the exception.
        try {
            Weeks.parseWeeks(invalidPeriodString);
            fail("Expected an IllegalArgumentException to be thrown due to invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, making the test more specific.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}