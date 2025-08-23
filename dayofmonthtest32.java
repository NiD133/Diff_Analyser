package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.Month;
import java.time.YearMonth;
import org.junit.Test;

/**
 * Test suite for the DayOfMonth class, focusing on understandability and maintainability.
 */
public class DayOfMonthTest {

    /**
     * Tests that isValidYearMonth() returns false for a day that does not exist in the given month.
     *
     * For example, the 31st is not a valid day in April.
     */
    @Test
    public void testIsValidYearMonthForInvalidDay() {
        // Arrange: Create a DayOfMonth for the 31st and a YearMonth for April,
        // which only has 30 days.
        DayOfMonth dayOfMonth31 = DayOfMonth.of(31);
        YearMonth april = YearMonth.of(2023, Month.APRIL);

        // Act: Check if the 31st is a valid day for April.
        boolean isValid = dayOfMonth31.isValidYearMonth(april);

        // Assert: The result should be false, and the original DayOfMonth object remains unchanged.
        assertFalse("The 31st should not be a valid day for April.", isValid);
        assertEquals("The value of the DayOfMonth object should not change.", 31, dayOfMonth31.getValue());
    }
}