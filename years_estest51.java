package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that yearsBetween() throws an IllegalArgumentException
     * when both start and end instant arguments are null.
     */
    @Test
    public void yearsBetween_shouldThrowIllegalArgumentException_whenInstantsAreNull() {
        try {
            Years.yearsBetween(null, null);
            fail("Expected an IllegalArgumentException to be thrown for null instants.");
        } catch (IllegalArgumentException e) {
            // The method contract requires non-null arguments, so we verify the exception
            // message to ensure the correct validation is in place.
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
}