package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that calling {@link Minutes#minutesIn(ReadableInterval)} with a null interval
     * correctly returns zero minutes. It also verifies that this zero-minute object
     * can be successfully converted to a standard duration and back without changing its value.
     */
    @Test
    public void minutesIn_withNullInterval_returnsZeroAndConvertsCorrectly() {
        // Arrange: The Joda-Time documentation states that a null interval should result in zero minutes.
        final ReadableInterval nullInterval = null;

        // Act: Create a Minutes object from the null interval.
        final Minutes minutes = Minutes.minutesIn(nullInterval);

        // Assert: The result should be the constant Minutes.ZERO.
        assertEquals("Minutes.minutesIn(null) should return ZERO.", Minutes.ZERO, minutes);

        // Arrange & Act: Perform a round-trip conversion to Duration and back to Minutes.
        final Duration duration = minutes.toStandardDuration();
        final Minutes resultAfterConversion = duration.toStandardMinutes();

        // Assert: The value should remain zero after the conversion.
        assertEquals("Value should remain zero after converting to Duration and back.",
                Minutes.ZERO, resultAfterConversion);
    }
}