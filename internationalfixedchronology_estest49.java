package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the range() method returns the correct, documented value range for the YEAR field.
     * The International Fixed Chronology is defined to support years from 1 to 1,000,000.
     */
    @Test
    public void range_forYearField_returnsCorrectSupportedRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedYearRange = ValueRange.of(1, 1_000_000L);

        // Act
        ValueRange actualYearRange = chronology.range(ChronoField.YEAR);

        // Assert
        assertEquals("The range for the YEAR field should match the documented supported range.",
                expectedYearRange, actualYearRange);
    }
}