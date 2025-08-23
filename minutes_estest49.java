package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that isGreaterThan() correctly handles a null comparison value.
     * The method's contract specifies that a null input is treated as a zero-value Minutes object.
     */
    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveMinutesToNull() {
        // Arrange: Create a Minutes instance with a positive value.
        // Using the constant ONE is clearer than a "magic number" like 1431.
        Minutes positiveMinutes = Minutes.ONE;

        // Act & Assert: A positive number of minutes should be greater than null (which represents zero).
        assertTrue(
            "Minutes.ONE should be greater than null",
            positiveMinutes.isGreaterThan(null)
        );
    }
}