package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the withZone() method of IslamicChronology.
 *
 * <p>This test focuses on verifying that the method returns correctly cached
 * instances of the chronology for different time zones, which is a key
 * performance and memory optimization in Joda-Time.
 */
public class TestIslamicChronologyWithZone {

    // Time zone constants for creating chronologies in specific zones
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fields to store and restore the original default settings
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // To ensure tests are predictable, we must control the environment.
        // We save the original defaults and set a known default time zone (LONDON),
        // as one test case specifically relies on the behavior of withZone(null).
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore original default settings to prevent side-effects on other tests.
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void withZone_givenSameZone_returnsSameInstance() {
        // Calling withZone with the chronology's current zone should be a no-op and return 'this'.
        IslamicChronology tokyoChronology = IslamicChronology.getInstance(TOKYO);
        assertSame(tokyoChronology, tokyoChronology.withZone(TOKYO));
    }

    @Test
    public void withZone_givenDifferentZone_returnsCachedInstanceForNewZone() {
        // Requesting a different zone should return the globally cached instance for that zone.
        IslamicChronology tokyoChronology = IslamicChronology.getInstance(TOKYO);
        
        IslamicChronology expectedLondon = IslamicChronology.getInstance(LONDON);
        assertSame(expectedLondon, tokyoChronology.withZone(LONDON));

        IslamicChronology expectedParis = IslamicChronology.getInstance(PARIS);
        assertSame(expectedParis, tokyoChronology.withZone(PARIS));
    }

    @Test
    public void withZone_givenNull_returnsInstanceInDefaultZone() {
        // Calling withZone(null) should return the chronology in the default time zone.
        // The default zone is explicitly set to LONDON in the setUp method for this test.
        IslamicChronology tokyoChronology = IslamicChronology.getInstance(TOKYO);
        IslamicChronology expectedDefault = IslamicChronology.getInstance(LONDON);
        assertSame(expectedDefault, tokyoChronology.withZone(null));
    }

    @Test
    public void withZone_onDefaultInstance_returnsCachedInstanceForNewZone() {
        // getInstance() returns the chronology in the default time zone (LONDON).
        // Calling withZone on it should return the cached instance for the new zone.
        IslamicChronology defaultChronology = IslamicChronology.getInstance();
        IslamicChronology expectedParis = IslamicChronology.getInstance(PARIS);
        assertSame(expectedParis, defaultChronology.withZone(PARIS));
    }

    @Test
    public void withZone_onUtcInstance_returnsCachedInstanceForNewZone() {
        // Starting with the UTC instance and calling withZone should return the
        // cached instance for the new zone.
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        IslamicChronology expectedParis = IslamicChronology.getInstance(PARIS);
        assertSame(expectedParis, utcChronology.withZone(PARIS));
    }
}