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
 * Tests the factory methods of {@link GregorianChronology}.
 * This test focuses on the behavior of getInstance() with respect to the default system settings.
 */
public class GregorianChronologyTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // A fixed date and time to ensure tests are deterministic.
    // Using a DateTime object is much clearer than calculating millis manually.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Fix the current time to a known value for predictable test results.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Save the original default settings to restore them after the test.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set specific default settings for this test.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original settings to avoid side effects on other tests.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void getInstance_withoutArgs_usesDefaultTimeZone() {
        // This test ensures that GregorianChronology.getInstance() correctly
        // uses the system's default time zone, which is set to LONDON in setUp().

        // Act: Get an instance of the chronology.
        GregorianChronology chronology = GregorianChronology.getInstance();

        // Assert: Verify the chronology has the expected time zone and class.
        assertEquals("Chronology should use the default time zone", LONDON, chronology.getZone());
        assertSame("getInstance() should return a GregorianChronology instance",
                GregorianChronology.class, chronology.getClass());
    }
}