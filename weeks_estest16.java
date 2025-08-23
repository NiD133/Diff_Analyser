package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that adding a small positive integer to Weeks.MIN_VALUE
     * produces the correct arithmetic result without overflow.
     */
    @Test
    public void plus_addingToMinValue_returnsCorrectSum() {
        // Arrange
        Weeks minWeeks = Weeks.MIN_VALUE;
        int weeksToAdd = 4;
        int expectedWeeks = Integer.MIN_VALUE + weeksToAdd;

        // Act
        Weeks result = minWeeks.plus(weeksToAdd);

        // Assert
        assertEquals(expectedWeeks, result.getWeeks());
    }
}