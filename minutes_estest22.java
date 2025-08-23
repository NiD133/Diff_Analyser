package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that multiplying a Minutes object by a negative scalar
     * correctly calculates the resulting negative number of minutes.
     */
    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectNegativeResult() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;
        final int scalar = -5447;
        final int expectedMinutes = -5447;

        // Act
        final Minutes result = oneMinute.multipliedBy(scalar);

        // Assert
        assertEquals(expectedMinutes, result.getMinutes());
    }
}