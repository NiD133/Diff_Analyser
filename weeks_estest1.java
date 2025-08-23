package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that isLessThan() returns false when the current instance (2 weeks)
     * is greater than the other instance (0 weeks).
     */
    @Test
    public void isLessThan_shouldReturnFalse_whenComparingGreaterToLesser() {
        // Arrange
        Weeks twoWeeks = Weeks.TWO;
        Weeks zeroWeeks = Weeks.ZERO;

        // Act
        boolean result = twoWeeks.isLessThan(zeroWeeks);

        // Assert
        assertFalse("Two weeks should not be considered less than zero weeks", result);
    }
}