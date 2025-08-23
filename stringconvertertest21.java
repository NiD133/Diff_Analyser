package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;

/**
 * Unit tests for {@link StringConverter}.
 * This class focuses on the setInto(ReadWritablePeriod, ...) method.
 */
public class StringConverterTest extends TestCase {

    /**
     * Tests that setInto correctly parses a complete ISO 8601 period string
     * and populates a MutablePeriod instance.
     */
    public void testSetInto_populatesMutablePeriod_fromCompleteISOString() {
        // Arrange
        // An ISO 8601 format period string with year, week, day, and time components.
        final String isoPeriodString = "P2Y4W3DT12H24M48S";
        final MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        
        // Act
        StringConverter.INSTANCE.setInto(period, isoPeriodString, null);

        // Assert
        assertEquals("Years should match", 2, period.getYears());
        assertEquals("Weeks should match", 4, period.getWeeks());
        assertEquals("Days should match", 3, period.getDays());
        assertEquals("Hours should match", 12, period.getHours());
        assertEquals("Minutes should match", 24, period.getMinutes());
        assertEquals("Seconds should match", 48, period.getSeconds());
        assertEquals("Millis should be zero", 0, period.getMillis());
    }
}