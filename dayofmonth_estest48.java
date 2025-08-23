package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the DayOfMonth class.
 */
public class DayOfMonthTest {

    @Test
    public void get_withDayOfMonthField_returnsCorrectValue() {
        // Arrange: Create a DayOfMonth instance with a known, fixed value.
        final int expectedDay = 15;
        DayOfMonth dayOfMonth = DayOfMonth.of(expectedDay);

        // Act: Call the method under test to get the value for the DAY_OF_MONTH field.
        int actualDay = dayOfMonth.get(ChronoField.DAY_OF_MONTH);

        // Assert: Verify that the returned value matches the expected value.
        assertEquals(expectedDay, actualDay);
    }
}