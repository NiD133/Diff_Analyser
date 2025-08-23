package org.joda.time.convert;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 * This class focuses on testing the conversion of a String into a Period.
 */
public class StringConverterTest {

    /**
     * Tests that setInto() correctly parses a complete ISO 8601 period string
     * and populates the fields of a MutablePeriod.
     */
    @Test
    public void testSetIntoPeriod_parsesIsoStringCorrectly() {
        // Arrange
        // An ISO 8601 period string representing 2 years, 4 weeks, 3 days,
        // 12 hours, 24 minutes, and 56 milliseconds.
        String periodString = "P2Y4W3DT12H24M.056S";
        MutablePeriod actualPeriod = new MutablePeriod(PeriodType.yearWeekDayTime());
        
        // The converter instance under test
        PeriodConverter converter = StringConverter.INSTANCE;

        // Act
        converter.setInto(actualPeriod, periodString, null);

        // Assert
        assertEquals(2, actualPeriod.getYears(), "Years should be parsed correctly");
        assertEquals(4, actualPeriod.getWeeks(), "Weeks should be parsed correctly");
        assertEquals(3, actualPeriod.getDays(), "Days should be parsed correctly");
        assertEquals(12, actualPeriod.getHours(), "Hours should be parsed correctly");
        assertEquals(24, actualPeriod.getMinutes(), "Minutes should be parsed correctly");
        assertEquals(0, actualPeriod.getSeconds(), "Seconds part of fractional value should be 0");
        assertEquals(56, actualPeriod.getMillis(), "Milliseconds part of fractional value should be 56");
    }
}