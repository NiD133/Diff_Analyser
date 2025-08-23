package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.Test;

/**
 * Tests the behavior of the year duration field in CopticChronology,
 * particularly its handling of leap years.
 *
 * <p>The Coptic calendar has a leap year every four years, specifically when (year % 4 == 3).
 * This test focuses on a 4-year cycle starting from Coptic year 1720,
 * where 1723 is the leap year.
 */
public class CopticChronologyYearDurationFieldTest {

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final long MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;

    private static final long COMMON_YEAR_MILLIS = 365L * MILLIS_PER_DAY;
    private static final long LEAP_YEAR_MILLIS = 366L * MILLIS_PER_DAY;
    private static final long FOUR_YEAR_CYCLE_MILLIS = 3 * COMMON_YEAR_MILLIS + LEAP_YEAR_MILLIS;

    // A date within a Coptic year that is not a leap year.
    // Coptic year 1723 is the leap year in the 1720-1723 cycle.
    private static final DateTime CYCLE_START_DATE = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    private static final DurationField yearDurationField = CYCLE_START_DATE.year().getDurationField();

    @Test
    public void testIsYearsField() {
        // The duration field for a 'year' property should be the same as the chronology's 'years' field.
        assertSame(COPTIC_UTC.years(), yearDurationField);
    }

    @Test
    public void testGetUnitMillis_isAverageYearDuration() {
        // getUnitMillis should return the average length of a year in the Coptic calendar (365.25 days).
        long expectedAverageMillis = FOUR_YEAR_CYCLE_MILLIS / 4;
        assertEquals(expectedAverageMillis, yearDurationField.getUnitMillis());
    }

    @Test
    public void testGetMillis_calculatesAverageDuration() {
        // getMillis(long) calculates duration based on the average year length.
        long expectedAverageMillis = FOUR_YEAR_CYCLE_MILLIS / 4;
        assertEquals(expectedAverageMillis, yearDurationField.getMillis(1));
        assertEquals(expectedAverageMillis * 2, yearDurationField.getMillis(2));
        assertEquals(expectedAverageMillis, yearDurationField.getMillis(1L));
        assertEquals(expectedAverageMillis * 2, yearDurationField.getMillis(2L));
    }

    @Test
    public void testGetMillis_calculatesPreciseDurationFromInstant() {
        // getMillis(long, long) calculates the precise duration from a specific start instant,
        // accounting for the exact placement of leap years.
        long startMillis = CYCLE_START_DATE.getMillis();

        // Years 1720, 1721, 1722 are common years.
        assertEquals(COMMON_YEAR_MILLIS, yearDurationField.getMillis(1, startMillis));
        assertEquals(COMMON_YEAR_MILLIS * 2, yearDurationField.getMillis(2, startMillis));
        assertEquals(COMMON_YEAR_MILLIS * 3, yearDurationField.getMillis(3, startMillis));

        // The 4-year duration from 1720 includes the leap year 1723.
        assertEquals(FOUR_YEAR_CYCLE_MILLIS, yearDurationField.getMillis(4, startMillis));

        // Also test the long overload
        assertEquals(FOUR_YEAR_CYCLE_MILLIS, yearDurationField.getMillis(4L, startMillis));
    }

    @Test
    public void testGetValue_convertsMillisToYearsFromInstant() {
        // getValue(long, long) determines how many full years fit into a given millisecond duration
        // starting from a specific instant.
        long startMillis = CYCLE_START_DATE.getMillis();

        // Test durations around the 1-year mark (common year)
        assertEquals(0, yearDurationField.getValue(COMMON_YEAR_MILLIS - 1, startMillis));
        assertEquals(1, yearDurationField.getValue(COMMON_YEAR_MILLIS, startMillis));
        assertEquals(1, yearDurationField.getValue(COMMON_YEAR_MILLIS + 1, startMillis));

        // Test durations around the 4-year cycle mark (3 common + 1 leap)
        assertEquals(3, yearDurationField.getValue(FOUR_YEAR_CYCLE_MILLIS - 1, startMillis));
        assertEquals(4, yearDurationField.getValue(FOUR_YEAR_CYCLE_MILLIS, startMillis));
        assertEquals(4, yearDurationField.getValue(FOUR_YEAR_CYCLE_MILLIS + 1, startMillis));
    }

    @Test
    public void testAdd_addsYearsToInstant() {
        // add() should correctly add years to an instant, preserving the date fields.
        long startMillis = CYCLE_START_DATE.getMillis();

        DateTime expectedAfter1Year = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime expectedAfter2Years = new DateTime(1722, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime expectedAfter3Years = new DateTime(1723, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime expectedAfter4Years = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);

        // Test with int parameter
        assertEquals(expectedAfter1Year.getMillis(), yearDurationField.add(startMillis, 1));
        assertEquals(expectedAfter2Years.getMillis(), yearDurationField.add(startMillis, 2));
        assertEquals(expectedAfter3Years.getMillis(), yearDurationField.add(startMillis, 3));
        assertEquals(expectedAfter4Years.getMillis(), yearDurationField.add(startMillis, 4));

        // Test with long parameter
        assertEquals(expectedAfter4Years.getMillis(), yearDurationField.add(startMillis, 4L));
    }
}