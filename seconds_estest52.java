package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the factory method `secondsIn()` returns a zero-second period
     * when the input interval is null, as specified by its contract.
     */
    @Test
    public void secondsIn_givenNullInterval_returnsZeroSeconds() {
        // Arrange: No setup is needed as we are testing a static method with a null argument.

        // Act: Create a Seconds instance from a null ReadableInterval.
        Seconds result = Seconds.secondsIn(null);

        // Assert: The resulting period should be zero seconds.
        assertEquals(0, result.getSeconds());
    }
}