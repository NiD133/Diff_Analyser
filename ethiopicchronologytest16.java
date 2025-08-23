package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the year DurationField in EthiopicChronology.
 * This test focuses on how the duration field handles leap years.
 * The Ethiopic calendar has a leap year every four years, similar to the Julian calendar.
 */
public class EthiopicChronologyYearFieldTest {

    // In the Ethiopic calendar, year 1999 is a leap year.
    private static final int LEAP_YEAR = 1999;
    private static final int PRECEDING_NORMAL_YEAR = 1998;
    private static final int FOLLOWING_NORMAL_YEAR = 2000;

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final long MILLIS_IN_NORMAL_YEAR = 365L * DateTimeConstants.MILLIS_PER_DAY;
    private static final long MILLIS_IN_LEAP_YEAR = 366L * DateTimeConstants.MILLIS_PER_DAY;
    private static final long MILLIS_IN_FOUR_YEAR_CYCLE = 3 * MILLIS_IN_NORMAL_YEAR + MILLIS_IN_LEAP_YEAR;

    // A fixed date for "now" to ensure tests are repeatable.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, ISOChronology.getInstanceUTC()).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testGetMillis_forSpecificDuration() {
        // Arrange: Start from a year that is not a leap year.
        DateTime startOfYear1996 = new DateTime(1996, 1, 1, 0, 0, ETHIOPIC_UTC);
        DurationField yearField = startOfYear1996.year().getDurationField();
        long startMillis = startOfYear1996.getMillis();

        // Act & Assert: Check durations, accounting for the leap year 1999.
        assertEquals(MILLIS_IN_NORMAL_YEAR, yearField.getMillis(1, startMillis));
        assertEquals(2 * MILLIS_IN_NORMAL_YEAR, yearField.getMillis(2, startMillis));
        assertEquals(3 * MILLIS_IN_NORMAL_YEAR, yearField.getMillis(3, startMillis));
        assertEquals(MILLIS_IN_FOUR_YEAR_CYCLE, yearField.getMillis(4, startMillis));

        // Assert the same for the long overload
        assertEquals(MILLIS_IN_FOUR_YEAR_CYCLE, yearField.getMillis(4L, startMillis));
    }

    @Test
    public void testGetMillis_forAverageDuration() {
        // Arrange
        DurationField yearField = ETHIOPIC_UTC.years();
        long averageYearMillis = (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);

        // Act & Assert: getMillis() without a start instant returns an average.
        assertEquals(averageYearMillis, yearField.getMillis(1));
        assertEquals(2 * averageYearMillis, yearField.getMillis(2));
        assertEquals(averageYearMillis, yearField.getMillis(1L));
        assertEquals(2 * averageYearMillis, yearField.getMillis(2L));
    }

    @Test
    public void testGetValue_convertsMillisToYears() {
        // Arrange
        DateTime startOfYear1996 = new DateTime(1996, 1, 1, 0, 0, ETHIOPIC_UTC);
        DurationField yearField = startOfYear1996.year().getDurationField();
        long startMillis = startOfYear1996.getMillis();

        // Act & Assert: Check values at year boundaries.
        assertEquals(0, yearField.getValue(MILLIS_IN_NORMAL_YEAR - 1, startMillis));
        assertEquals(1, yearField.getValue(MILLIS_IN_NORMAL_YEAR, startMillis));
        assertEquals(1, yearField.getValue(MILLIS_IN_NORMAL_YEAR + 1, startMillis));

        long twoNormalYears = 2 * MILLIS_IN_NORMAL_YEAR;
        assertEquals(1, yearField.getValue(twoNormalYears - 1, startMillis));
        assertEquals(2, yearField.getValue(twoNormalYears, startMillis));
        assertEquals(2, yearField.getValue(twoNormalYears + 1, startMillis));

        assertEquals(3, yearField.getValue(MILLIS_IN_FOUR_YEAR_CYCLE - 1, startMillis));
        assertEquals(4, yearField.getValue(MILLIS_IN_FOUR_YEAR_CYCLE, startMillis));
        assertEquals(4, yearField.getValue(MILLIS_IN_FOUR_YEAR_CYCLE + 1, startMillis));
    }

    @Test
    public void testAdd_addsYearsToInstant() {
        // Arrange
        DateTime startDateTime = new DateTime(PRECEDING_NORMAL_YEAR, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DurationField yearField = startDateTime.year().getDurationField();

        // Adding 1 year crosses into the leap year 1999
        DateTime expectedAfter1Year = new DateTime(LEAP_YEAR, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        // Adding 2 years crosses the leap day of year 1999
        DateTime expectedAfter2Years = new DateTime(FOLLOWING_NORMAL_YEAR, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        // Act & Assert (int overload)
        assertEquals(expectedAfter1Year.getMillis(), yearField.add(startDateTime.getMillis(), 1));
        assertEquals(expectedAfter2Years.getMillis(), yearField.add(startDateTime.getMillis(), 2));

        // Act & Assert (long overload)
        assertEquals(expectedAfter1Year.getMillis(), yearField.add(startDateTime.getMillis(), 1L));
        assertEquals(expectedAfter2Years.getMillis(), yearField.add(startDateTime.getMillis(), 2L));
    }

    @Test
    public void testGetUnitMillis_isAverageYear() {
        // Arrange
        DurationField yearField = ETHIOPIC_UTC.years();
        // Unit millis is the average length of a year: (365*3 + 366) / 4 = 365.25 days
        long expectedAverageMillis = (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);

        // Act & Assert
        assertEquals(expectedAverageMillis, yearField.getUnitMillis());
    }
}