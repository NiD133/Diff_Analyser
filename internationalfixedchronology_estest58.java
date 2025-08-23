package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in the InternationalFixedChronology.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void range_forDayOfWeek_shouldReturnStandardWeekRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_WEEK);

        // Assert
        assertEquals("The range for DAY_OF_WEEK should be the standard 1-7.", expectedRange, actualRange);
    }
}