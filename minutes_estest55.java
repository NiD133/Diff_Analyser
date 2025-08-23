package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Minutes} class, focusing on parsing functionality.
 */
public class MinutesTest {

    /**
     * Verifies that parseMinutes() throws an IllegalArgumentException
     * for an input string that does not match the expected ISO8601 format.
     */
    @Test
    public void parseMinutes_shouldThrowIllegalArgumentException_forInvalidFormat() {
        // The Joda-Time ISO8601 format for minutes is 'PTnM'.
        // The string "UT" does not conform to this format.
        String invalidPeriodString = "UT";

        try {
            Minutes.parseMinutes(invalidPeriodString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            // Now, we verify that the exception message is clear and informative.
            String expectedMessage = "Invalid format: \"" + invalidPeriodString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}