package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void isLessThan_returnsFalse_whenComparingLargerToSmaller() {
        // Arrange
        Minutes largerMinutes = Minutes.MAX_VALUE;
        Minutes smallerMinutes = Minutes.TWO;

        // Act
        boolean result = largerMinutes.isLessThan(smallerMinutes);

        // Assert
        assertFalse("MAX_VALUE should not be less than TWO.", result);
    }
}