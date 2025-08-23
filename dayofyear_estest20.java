package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that getLong() returns the correct value when queried with the DAY_OF_YEAR field.
     */
    @Test
    public void getLong_withDayOfYearField_returnsTheDayOfYearValue() {
        // Arrange: Create a DayOfYear instance for a specific day.
        int expectedDay = 45;
        DayOfYear dayOfYear = DayOfYear.of(expectedDay);

        // Act: Retrieve the value using the getLong() method.
        long actualDay = dayOfYear.getLong(ChronoField.DAY_OF_YEAR);

        // Assert: Verify that the returned value matches the original.
        assertEquals(expectedDay, actualDay);
    }
}