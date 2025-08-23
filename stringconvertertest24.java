package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

/**
 * Test class for {@link StringConverter}, focusing on setting a period from a String.
 */
public class StringConverterTest {

    /**
     * Tests that StringConverter can parse a complete ISO 8601 period format string
     * and correctly populate a MutablePeriod instance.
     */
    @Test
    public void setInto_shouldParseIsoPeriodString_whenAllFieldsArePresent() {
        // Arrange
        final String periodString = "P2Y4W3DT12H24M56.S";
        final PeriodType periodType = PeriodType.yearWeekDayTime();
        
        // The MutablePeriod to be populated by the converter.
        MutablePeriod actualPeriod = new MutablePeriod(periodType);

        // The expected state of the period after parsing the string.
        // P2Y  -> 2 Years
        // 4W   -> 4 Weeks
        // 3D   -> 3 Days
        // T    -> Time separator
        // 12H  -> 12 Hours
        // 24M  -> 24 Minutes
        // 56.S -> 56 Seconds and 0 Milliseconds
        MutablePeriod expectedPeriod = new MutablePeriod(periodType);
        expectedPeriod.setYears(2);
        expectedPeriod.setWeeks(4);
        expectedPeriod.setDays(3);
        expectedPeriod.setHours(12);
        expectedPeriod.setMinutes(24);
        expectedPeriod.setSeconds(56);
        expectedPeriod.setMillis(0);

        // Act
        StringConverter.INSTANCE.setInto(actualPeriod, periodString, null);

        // Assert
        assertEquals(expectedPeriod, actualPeriod);
    }
}