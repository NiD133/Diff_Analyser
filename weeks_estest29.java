package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that calling weeksBetween() with null start and end instants 
     * throws an IllegalArgumentException.
     */
    @Test
    public void weeksBetween_withNullInstants_shouldThrowIllegalArgumentException() {
        try {
            // Act: Attempt to calculate the period between two null instants.
            Weeks.weeksBetween(null, null);
            fail("Expected an IllegalArgumentException to be thrown for null instants.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify the exception message is correct.
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
}