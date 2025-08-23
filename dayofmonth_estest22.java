package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that getLong() with ChronoField.DAY_OF_MONTH returns the correct day value.
     */
    @Test
    public void getLongWithDayOfMonthFieldReturnsCorrectValue() {
        // Arrange: Create a DayOfMonth instance representing the 14th day of the month.
        DayOfMonth dayOfMonth = DayOfMonth.of(14);
        long expectedDay = 14L;

        // Act: Get the value using the getLong() method.
        long actualDay = dayOfMonth.getLong(ChronoField.DAY_OF_MONTH);

        // Assert: The returned value should match the day the instance was created with.
        assertEquals(expectedDay, actualDay);
    }
}