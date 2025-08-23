package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void isLessThanShouldReturnFalseWhenComparingInstanceToItself() {
        // Arrange
        Minutes thirtyOneMinutes = Minutes.minutes(31);

        // Act
        boolean result = thirtyOneMinutes.isLessThan(thirtyOneMinutes);

        // Assert
        assertFalse("A Minutes instance should not be considered less than itself.", result);
        assertEquals(31, thirtyOneMinutes.getMinutes()); // Verify state remains unchanged
    }
}