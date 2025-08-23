package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that the range() method returns the correct, fixed range for the DAY_OF_YEAR field.
     * The DayOfYear class supports values from 1 to 366.
     */
    @Test
    public void range_forDayOfYearField_returnsValidRangeFrom1To366() {
        // Arrange: Create a DayOfYear instance. The specific value doesn't affect the range of the field itself.
        DayOfYear dayOfYear = DayOfYear.of(45);
        ValueRange expectedRange = ValueRange.of(1, 366);

        // Act: Get the range for the DAY_OF_YEAR field.
        ValueRange actualRange = dayOfYear.range(ChronoField.DAY_OF_YEAR);

        // Assert: Verify that the returned range is correct.
        assertEquals(expectedRange, actualRange);
    }
}