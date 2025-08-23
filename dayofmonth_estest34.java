package org.threeten.extra;

import org.junit.Test;
import java.time.YearMonth;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link DayOfMonth#isValidYearMonth(YearMonth)} method.
 */
public class DayOfMonth_ESTestTest34 {

    /**
     * Tests that isValidYearMonth returns false when the input YearMonth is null.
     * The method is expected to handle null input gracefully without throwing an exception.
     */
    @Test
    public void isValidYearMonth_whenYearMonthIsNull_shouldReturnFalse() {
        // Arrange: Create an arbitrary DayOfMonth instance. The specific day does not
        // matter for this test, as we are only checking null handling.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the method under test with a null YearMonth.
        boolean isValid = dayOfMonth.isValidYearMonth(null);

        // Assert: The result should be false, as a null YearMonth cannot be valid.
        assertFalse("isValidYearMonth(null) should return false.", isValid);
    }
}