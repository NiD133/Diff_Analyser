package org.threeten.extra;

import org.junit.Test;
import java.time.YearMonth;
import static org.junit.Assert.assertFalse;

/**
 * This test class evaluates the behavior of the DayOfMonth class.
 */
public class DayOfMonthTest {

    /**
     * Tests that isValidYearMonth() returns false when the input YearMonth is null.
     *
     * This test case ensures that the method correctly handles null inputs,
     * which is a fundamental aspect of robust software design. A null YearMonth
     * cannot represent a valid date, so the method is expected to return false.
     */
    @Test
    public void isValidYearMonth_shouldReturnFalse_whenYearMonthIsNull() {
        // Arrange: Create a DayOfMonth instance. The specific day is not relevant for this test.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the method under test with a null YearMonth.
        boolean isValid = dayOfMonth.isValidYearMonth(null);

        // Assert: The method should return false, indicating that a null YearMonth is not valid.
        assertFalse(isValid);
    }
}