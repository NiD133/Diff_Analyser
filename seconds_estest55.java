package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the factory method {@link Seconds#seconds(int)} can successfully
     * create an instance with the maximum possible integer value.
     */
    @Test
    public void secondsFactory_shouldCreateInstanceWithMaxValue() {
        // Arrange: Define the maximum value to be used.
        final int maxIntValue = Integer.MAX_VALUE;

        // Act: Create a Seconds instance using the factory method with the max value.
        Seconds maxSeconds = Seconds.seconds(maxIntValue);

        // Assert: Verify that the created instance holds the correct value.
        assertEquals(maxIntValue, maxSeconds.getSeconds());
    }
}