package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link Seconds} class, focusing on factory methods.
 */
public class SecondsTest {

    /**
     * Tests that secondsBetween() throws an IllegalArgumentException
     * when both start and end instants are null.
     */
    @Test
    public void secondsBetween_shouldThrowIllegalArgumentException_whenBothInstantsAreNull() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
            "Calling secondsBetween with null arguments should throw an exception.",
            IllegalArgumentException.class,
            () -> Seconds.secondsBetween((ReadableInstant) null, (ReadableInstant) null)
        );

        // Verify the exception message for correctness
        assertEquals("ReadableInstant objects must not be null", thrown.getMessage());
    }
}