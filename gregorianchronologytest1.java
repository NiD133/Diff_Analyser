package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for GregorianChronology.
 */
public class GregorianChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time for consistent test results: 2002-06-09
    private static final long TEST_TIME_NOW =
            new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    /**
     * Sets up the test environment by fixing the current time and setting default
     * time zone and locale. This ensures tests are repeatable.
     */
    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    /**
     * Tears down the test environment by resetting the system properties
     * to their original states.
     */
    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    //-----------------------------------------------------------------------

    /**
     * Tests that getInstanceUTC() returns a singleton instance of
     * GregorianChronology configured for the UTC time zone.
     */
    @Test
    public void getInstanceUTC_shouldReturnChronologyInUTC() {
        // Act
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();

        // Assert
        assertEquals(DateTimeZone.UTC, chronology.getZone());
        assertSame(GregorianChronology.class, chronology.getClass());
    }
}