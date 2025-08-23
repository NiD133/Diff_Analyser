package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the range of fields in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the range for ALIGNED_DAY_OF_WEEK_IN_YEAR is correctly handled.
     * This field is delegated to the default ISO implementation, which has a range of 1-7.
     */
    @Test
    public void range_forAlignedDayOfWeekInYear_returnsCorrectRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);

        // Assert
        assertEquals("The range for ALIGNED_DAY_OF_WEEK_IN_YEAR should be 1-7", expectedRange, actualRange);
    }
}