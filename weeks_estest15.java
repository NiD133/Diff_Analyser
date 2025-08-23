package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Weeks class.
 */
public class WeeksTest {

    /**
     * Tests that adding the maximum possible Weeks value to the minimum value
     * correctly results in -1, demonstrating proper integer arithmetic without overflow.
     * This is equivalent to Integer.MAX_VALUE + Integer.MIN_VALUE.
     */
    @Test
    public void plus_whenAddingMaxAndMinValues_returnsNegativeOne() {
        // Arrange
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks minWeeks = Weeks.MIN_VALUE;
        int expectedWeeks = -1;

        // Act
        Weeks result = maxWeeks.plus(minWeeks);

        // Assert
        assertEquals("Adding MAX_VALUE and MIN_VALUE should result in -1",
                     expectedWeeks, result.getWeeks());
    }
}