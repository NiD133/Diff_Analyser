package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link JulianChronology}.
 * This class focuses on testing the value ranges for different temporal fields.
 */
public class JulianChronologyTest {

    /**
     * Tests that the range() method returns the correct, documented range for the proleptic YEAR field.
     *
     * The JulianChronology defines a specific valid range for the proleptic year. This test
     * verifies that the implementation adheres to that definition.
     */
    @Test
    public void range_forYearField_returnsCorrectRange() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        // According to the JulianChronology.YEAR_RANGE constant, the valid range for the
        // proleptic year is -999,998 to 999,999.
        ValueRange expectedRange = ValueRange.of(-999_998, 999_999);

        // Act
        ValueRange actualRange = julianChronology.range(ChronoField.YEAR);

        // Assert
        // The original test only checked for non-null. This assertion is much more specific,
        // ensuring the exact range is returned, which makes the test more valuable.
        assertEquals("The range for ChronoField.YEAR should match the defined constant",
                     expectedRange, actualRange);
    }
}