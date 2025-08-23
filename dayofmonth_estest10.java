package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.MonthDay;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that atMonth() correctly combines a DayOfMonth with a month
     * to create a valid MonthDay object.
     */
    @Test
    public void atMonth_combinesWithMonthIntToCreateCorrectMonthDay() {
        // Arrange: Define a specific day of the month.
        DayOfMonth day21 = DayOfMonth.of(21);
        int january = Month.JANUARY.getValue();

        // Act: Call the method under test to combine the day with the month.
        MonthDay actualMonthDay = day21.atMonth(january);

        // Assert: Verify that the resulting MonthDay is correct.
        MonthDay expectedMonthDay = MonthDay.of(Month.JANUARY, 21);
        assertEquals(expectedMonthDay, actualMonthDay);
    }
}