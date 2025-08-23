package org.threeten.extra;

import org.junit.Test;
import java.time.YearMonth;

/**
 * Unit tests for the atYearMonth method in the DayOfMonth class.
 */
public class DayOfMonthTest {

    /**
     * Tests that atYearMonth() throws a NullPointerException when the YearMonth argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void atYearMonth_whenYearMonthIsNull_thenThrowNullPointerException() {
        // Arrange: Create a DayOfMonth instance. The specific day is not important for this test.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        dayOfMonth.atYearMonth((YearMonth) null);
    }
}