package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the valid range for the DAY_OF_MONTH field is correct.
     * <p>
     * In the International Fixed calendar, most months have 28 days, but some
     * can have a 29th day (e.g., in a leap year). Therefore, the overall valid
     * range for the day-of-month is 1 to 29.
     */
    @Test
    public void range_forDayOfMonth_returnsCorrectRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 29);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_MONTH);

        // Assert
        assertEquals("The range for DAY_OF_MONTH should be 1-29", expectedRange, actualRange);
    }
}