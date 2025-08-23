package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;

/**
 * Tests for {@link StringConverter#setInto(org.joda.time.ReadWritablePeriod, Object, org.joda.time.Chronology)}.
 */
public class StringConverter_SetIntoPeriodTest extends TestCase {

    /**
     * Tests that a standard ISO 8601 period string correctly populates all the fields
     * of a MutablePeriod object.
     */
    public void testSetInto_forPeriod_givenCompleteISOString_populatesAllFieldsCorrectly() {
        // Arrange
        final String periodString = "P2Y6M9DT12H24M48S";
        final PeriodType periodType = PeriodType.yearMonthDayTime();

        // The object to be populated by the converter
        MutablePeriod actualPeriod = new MutablePeriod(periodType);

        // The expected state of the object after the conversion
        MutablePeriod expectedPeriod = new MutablePeriod(periodType);
        expectedPeriod.setYears(2);
        expectedPeriod.setMonths(6);
        expectedPeriod.setDays(9);
        expectedPeriod.setHours(12);
        expectedPeriod.setMinutes(24);
        expectedPeriod.setSeconds(48);
        expectedPeriod.setMillis(0);

        // Act
        StringConverter.INSTANCE.setInto(actualPeriod, periodString, null);

        // Assert
        assertEquals(expectedPeriod, actualPeriod);
    }
}