package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class, focusing on the minutesBetween method.
 */
public class MinutesTest {

    /**
     * Tests that calculating the minutes between two identical ReadablePartial objects
     * results in zero minutes.
     */
    @Test
    public void minutesBetween_withIdenticalReadablePartials_returnsZero() {
        // Arrange: Create a single ReadablePartial instance.
        // Using LocalTime is a clear and concrete example of a ReadablePartial.
        ReadablePartial samePartial = new LocalTime(10, 30);

        // Act: Calculate the minutes between the partial and itself.
        Minutes result = Minutes.minutesBetween(samePartial, samePartial);

        // Assert: The result should be the constant for zero minutes.
        assertEquals(Minutes.ZERO, result);
    }
}