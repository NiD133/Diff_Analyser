package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.MonthDay;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DayOfMonth} class.
 * This class focuses on the behavior of the atMonth() method.
 */
public class DayOfMonth_ESTestTest41 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling atMonth() creates a correct MonthDay
     * and does not modify the original DayOfMonth instance, confirming its immutability.
     */
    @Test
    public void atMonth_shouldReturnCorrectMonthDay_andRemainImmutable() {
        // Arrange: Create a DayOfMonth instance with a fixed, known value.
        final int dayValue = 15;
        DayOfMonth dayOfMonth = DayOfMonth.of(dayValue);
        Month month = Month.AUGUST;
        MonthDay expectedMonthDay = MonthDay.of(month, dayValue);

        // Act: Call the method under test.
        MonthDay actualMonthDay = dayOfMonth.atMonth(month);

        // Assert: Verify both the returned value and the state of the original object.
        assertEquals("The returned MonthDay should combine the month and day.", expectedMonthDay, actualMonthDay);
        assertEquals("The original DayOfMonth object should not be modified.", dayValue, dayOfMonth.getValue());
    }
}