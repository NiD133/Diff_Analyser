package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the {@link Seconds#seconds(int)} factory method correctly handles
     * the minimum possible integer value.
     */
    @Test
    public void secondsFactory_withMinValue_createsInstanceWithCorrectValue() {
        // Arrange
        final int minValue = Integer.MIN_VALUE;

        // Act
        Seconds minSeconds = Seconds.seconds(minValue);

        // Assert
        assertEquals("The number of seconds should match the minimum integer value",
                minValue, minSeconds.getSeconds());
    }
}