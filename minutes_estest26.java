package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that subtracting a negative number of minutes is equivalent to adding
     * the positive (absolute) value of that number.
     * For example, 1 minute - (-241 minutes) should result in 242 minutes.
     */
    @Test
    public void minus_whenSubtractingNegativeValue_isEquivalentToAddition() {
        // Arrange
        final Minutes initialMinutes = Minutes.ONE;
        final int negativeMinutesToSubtract = -241;
        final int expectedTotalMinutes = 242;

        // Act
        final Minutes result = initialMinutes.minus(negativeMinutesToSubtract);

        // Assert
        assertEquals(expectedTotalMinutes, result.getMinutes());
    }
}