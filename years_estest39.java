package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that parsing an empty string throws an IllegalArgumentException,
     * as the ISO8601 period format requires a valid, non-empty input.
     */
    @Test
    public void parseYears_givenEmptyString_throwsIllegalArgumentException() {
        try {
            Years.parseYears("");
            fail("Expected an IllegalArgumentException to be thrown for an empty string input.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            // We assert the message to ensure the exception is thrown for the correct reason.
            assertEquals("Invalid format: \"\"", e.getMessage());
        }
    }
}