package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Verifies that parsing an empty string throws an IllegalArgumentException
     * because it is not a valid ISO8601 period format.
     */
    @Test
    public void parseSeconds_withEmptyString_shouldThrowIllegalArgumentException() {
        try {
            Seconds.parseSeconds("");
            fail("Expected an IllegalArgumentException to be thrown for an empty input string.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome.
            // We assert the message to ensure the exception is thrown for the correct reason.
            assertEquals("Invalid format: \"\"", e.getMessage());
        }
    }
}