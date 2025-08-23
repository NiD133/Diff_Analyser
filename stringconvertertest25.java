package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 * This test focuses on parsing period strings.
 */
public class StringConverterTest {

    /**
     * Tests that parsing an ISO 8601 period string with more than three fractional
     * second digits correctly populates a MutablePeriod, truncating the fractions
     * to millisecond precision.
     */
    @Test
    public void setInto_periodFromString_shouldTruncateToMillisecondPrecision() {
        // Arrange
        // An ISO period string with nanosecond precision in the seconds field.
        final String ISO_PERIOD_STRING_WITH_NANOS = "P2Y4W3DT12H24M56.1234567S";
        
        // The period type must include all fields present in the string.
        final PeriodType periodType = PeriodType.yearWeekDayTime();
        
        // The expected period after parsing and truncation.
        final Period expectedPeriod = new Period(2, 0, 4, 3, 12, 24, 56, 123, periodType);
        
        MutablePeriod actualPeriod = new MutablePeriod(periodType);

        // Act
        StringConverter.INSTANCE.setInto(actualPeriod, ISO_PERIOD_STRING_WITH_NANOS, null);

        // Assert
        // The converter should parse all fields correctly and truncate the
        // fractional seconds (.1234567S) to milliseconds (123).
        assertEquals(expectedPeriod, actualPeriod);
    }
}