package org.threeten.extra;

import static org.junit.Assert.fail;

import java.time.YearMonth;
import org.junit.Test;

/**
 * This test suite focuses on verifying the behavior of the {@link DayOfMonth#atYearMonth(YearMonth)} method.
 */
public class DayOfMonthTest {

    /**
     * Tests that calling atYearMonth with a null argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void atYearMonth_whenYearMonthIsNull_throwsNullPointerException() {
        // Arrange: Create an instance of DayOfMonth for an arbitrary day.
        // The specific day does not matter for this test case.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the method under test with a null YearMonth.
        // The @Test(expected) annotation will automatically handle the assertion.
        dayOfMonth.atYearMonth(null);
    }
}