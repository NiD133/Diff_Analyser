package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that minutesBetween() throws an IllegalArgumentException
     * when null is passed for the start and end instant arguments.
     */
    @Test
    public void minutesBetween_shouldThrowIllegalArgumentException_whenInstantsAreNull() {
        // The method under test is expected to validate its arguments and
        // throw an exception for null ReadableInstant objects.
        try {
            Minutes.minutesBetween(null, null);
            fail("Expected an IllegalArgumentException to be thrown for null instants.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is correct, confirming the reason for the failure.
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
}