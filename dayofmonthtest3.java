package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * This test suite verifies the behavior of the range() method in the DayOfMonth class.
 */
public class DayOfMonthTest {

    /**
     * Verifies that calling range() with the DAY_OF_MONTH field
     * returns the expected full range of possible days in a month.
     */
    @Test
    public void range_withDayOfMonthField_returnsCorrectRange() {
        // Arrange: Create a specific DayOfMonth instance and define the expected result.
        // The range for DAY_OF_MONTH is constant (1-31) and does not depend on the specific day.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        ValueRange expectedRange = ValueRange.of(1, 31);

        // Act: Call the method under test.
        ValueRange actualRange = dayOfMonth.range(ChronoField.DAY_OF_MONTH);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals("The range for DAY_OF_MONTH should always be 1-31.", expectedRange, actualRange);
    }
}