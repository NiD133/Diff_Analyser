package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;
import org.junit.Test;

/**
 * Tests for GregorianChronology, focusing on date field calculations.
 */
public class GregorianChronologyTest {

    // A stateless, thread-safe instance of the chronology under test.
    private static final Chronology GREGORIAN_CHRONOLOGY = GregorianChronology.getInstance();

    @Test
    public void dayOfMonth_getMaximumValue_returns28ForFebruaryInNonLeapYear_usingPartial() {
        // Arrange: 1999 is a non-leap year, so February has 28 days.
        // This test provides the date context as a ReadablePartial.
        final int expectedDaysInFebruary = 28;
        YearMonthDay february1999 = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1);

        // Act
        int actualMaxDays = GREGORIAN_CHRONOLOGY.dayOfMonth().getMaximumValue(february1999);

        // Assert
        assertEquals(expectedDaysInFebruary, actualMaxDays);
    }

    @Test
    public void dayOfMonth_getMaximumValue_returns28ForFebruaryInNonLeapYear_usingInstant() {
        // Arrange: 1999 is a non-leap year, so February has 28 days.
        // This test provides the date context as an instant in milliseconds.
        final int expectedDaysInFebruary = 28;
        DateMidnight february1999 = new DateMidnight(1999, DateTimeConstants.FEBRUARY, 1);
        long instantInMillis = february1999.getMillis();

        // Act
        int actualMaxDays = GREGORIAN_CHRONOLOGY.dayOfMonth().getMaximumValue(instantInMillis);

        // Assert
        assertEquals(expectedDaysInFebruary, actualMaxDays);
    }
}