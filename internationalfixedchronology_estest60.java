package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the valid range for the MONTH_OF_YEAR field is correct.
     * <p>
     * The International Fixed Chronology has 13 months in a year. Therefore,
     * the valid range for this field should be from 1 to 13.
     */
    @Test
    public void range_forMonthOfYear_shouldBe1To13() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 13);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.MONTH_OF_YEAR);

        // Assert
        assertEquals("The range for MONTH_OF_YEAR should be 1-13", expectedRange, actualRange);
    }
}