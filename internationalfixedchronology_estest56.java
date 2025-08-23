package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the range of fields in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyRangeTest {

    /**
     * Verifies the valid value range for the ALIGNED_WEEK_OF_YEAR field.
     * <p>
     * The International Fixed calendar has 13 months of 28 days, plus one "Year Day".
     * This totals 365 days (13 * 28 + 1), which is exactly 52 weeks plus one day.
     * Therefore, the number of aligned weeks in any year is consistently 52.
     */
    @Test
    public void rangeForAlignedWeekOfYearShouldBe1To52() {
        // Arrange
        InternationalFixedChronology ifc = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 52);

        // Act
        ValueRange actualRange = ifc.range(ChronoField.ALIGNED_WEEK_OF_YEAR);

        // Assert
        assertEquals("The range for ALIGNED_WEEK_OF_YEAR should be 1-52.", expectedRange, actualRange);
    }
}