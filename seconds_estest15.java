package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that adding zero seconds to zero seconds results in zero seconds.
     */
    @Test
    public void plus_whenAddingZeroToZero_thenResultIsZero() {
        // Arrange
        Seconds zero = Seconds.ZERO;

        // Act
        Seconds result = zero.plus(zero);

        // Assert
        assertEquals(Seconds.ZERO, result);
    }
}