package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that multiplying a Seconds instance by a negative number
     * results in a new instance with the correctly calculated negative value.
     */
    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectlyNegativeSeconds() {
        // Arrange
        final Seconds oneSecond = Seconds.ONE;
        final int multiplier = -701;
        final int expectedSeconds = -701;

        // Act
        Seconds result = oneSecond.multipliedBy(multiplier);

        // Assert
        assertEquals(expectedSeconds, result.getSeconds());
    }
}