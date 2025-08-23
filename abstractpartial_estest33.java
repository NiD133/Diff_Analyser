package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AbstractPartial_ESTestTest33 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that the isEqual() method, inherited from AbstractPartial,
     * correctly returns false when comparing two YearMonth instances
     * with different year values.
     */
    @Test
    public void isEqual_shouldReturnFalse_whenComparingPartialsWithDifferentValues() {
        // Arrange
        // Create a YearMonth for April of year 4.
        YearMonth yearMonth1 = new YearMonth(4, 4);

        // Create another YearMonth by subtracting 4 years, resulting in April of year 0.
        YearMonth yearMonth2 = yearMonth1.minusYears(4);

        // Sanity check to ensure the 'minusYears' operation worked as expected and didn't alter the month.
        assertEquals("The month should remain unchanged after subtracting years", 4, yearMonth2.getMonthOfYear());

        // Act & Assert
        // The isEqual method should return false because the years are different (0 vs 4).
        assertFalse("Two YearMonth objects with different years should not be equal",
                yearMonth2.isEqual(yearMonth1));
    }
}