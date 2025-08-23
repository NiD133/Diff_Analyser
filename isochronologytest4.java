package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertSame;

/**
 * Tests the factory methods of {@link ISOChronology}, focusing on instance caching.
 * It verifies that {@code getInstance()} returns the same, cached instance for a given time zone.
 */
public class ISOChronologyGetInstanceTest {

    // Time zone constants used for testing
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fields to store and restore the original system default settings
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Save original default settings to ensure tests are isolated
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set a known default time zone for predictable test results
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London")); // For java.util.TimeZone
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore original default settings to avoid side-effects on other tests
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void getInstance_withExplicitZone_shouldReturnCachedInstance() {
        // Verifies that getInstance(DateTimeZone) returns the same singleton instance
        // for the same time zone, demonstrating that instances are cached.

        // Test with specific time zones
        assertSame("Instances for TOKYO should be cached",
                ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame("Instances for LONDON should be cached",
                ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame("Instances for PARIS should be cached",
                ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));

        // Test with UTC, which has a dedicated factory method
        assertSame("Instances for UTC should be cached",
                ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
    }

    @Test
    public void getInstance_withDefaultZone_shouldReturnCorrectCachedInstance() {
        // Verifies that the no-argument getInstance() returns the instance for the default time zone.
        // This test relies on the default zone being set to LONDON in the setUp() method.
        assertSame("getInstance() should return the instance for the default zone (LONDON)",
                ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }
}