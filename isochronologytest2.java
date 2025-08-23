package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the factory methods of {@link ISOChronology}.
 *
 * <p>This test focuses on how {@code ISOChronology.getInstance()} behaves with respect to the
 * default system time zone.
 */
public class ISOChronologyFactoryTest {

    private static final DateTimeZone ZONE_LONDON = DateTimeZone.forID("Europe/London");

    // This instant is used to fix the "current" time for test consistency.
    // The original calculation was complex; this is a more direct and readable
    // way to define the same instant: 2002-06-09T00:00:00Z.
    private static final long JUNE_9_2002_UTC_MILLIS =
            new DateTime(2002, 6, 9, 0, 0, DateTimeZone.UTC).getMillis();

    // Fields to store original system defaults
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Fix the "current" time to a known value to ensure tests are repeatable.
        DateTimeUtils.setCurrentMillisFixed(JUNE_9_2002_UTC_MILLIS);

        // Store original system defaults before modifying them for the test.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set system defaults to a known state (London) for this test.
        DateTimeZone.setDefault(ZONE_LONDON);
        TimeZone.setDefault(ZONE_LONDON.toTimeZone());
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore system defaults to their original state to avoid side effects on other tests.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    // Note: The private helper method `testAdd` from the original code was removed
    // as it was unused within this test file, improving clarity by removing dead code.

    @Test
    public void getInstance_shouldReturnChronologyWithDefaultZone() {
        // This test verifies that getInstance() correctly uses the default time zone,
        // which was set to London in the setUp method.

        // Act
        Chronology chronology = ISOChronology.getInstance();

        // Assert
        assertEquals("Chronology should have the default time zone", ZONE_LONDON, chronology.getZone());
        assertSame("The returned instance should be of type ISOChronology",
                ISOChronology.class, chronology.getClass());
    }
}