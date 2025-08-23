package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ISOChronology#withZone(DateTimeZone)}.
 * This test focuses on the caching and instance management behavior of the method,
 * ensuring it returns the correct, shared chronology instances.
 */
public class ISOChronologyWithZoneTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone = null;

    @Before
    public void setUp() {
        // Save the original default time zone to ensure tests are isolated.
        originalDefaultZone = DateTimeZone.getDefault();
        // Set a known default time zone for predictable test outcomes.
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone to prevent side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void withZone_whenZoneIsSame_returnsSameInstance() {
        // Calling withZone with the chronology's current zone should be a no-op.
        ISOChronology tokyoChronology = ISOChronology.getInstance(TOKYO);
        
        assertSame("Calling withZone with the same zone should return the same instance",
                tokyoChronology, tokyoChronology.withZone(TOKYO));
    }

    @Test
    public void withZone_whenZoneIsDifferent_returnsCorrectCachedInstance() {
        // Calling withZone should return the correct cached instance for the new zone.
        ISOChronology tokyoChronology = ISOChronology.getInstance(TOKYO);
        ISOChronology londonChronology = ISOChronology.getInstance(LONDON);

        assertSame("withZone(LONDON) should return the singleton LONDON instance",
                londonChronology, tokyoChronology.withZone(LONDON));
    }

    @Test
    public void withZone_whenZoneIsNull_returnsDefaultZoneInstance() {
        // Calling withZone(null) should return the instance for the default time zone.
        // The default zone is set to LONDON in the setUp method.
        ISOChronology tokyoChronology = ISOChronology.getInstance(TOKYO);
        ISOChronology defaultChronology = ISOChronology.getInstance(LONDON);

        assertSame("withZone(null) should return the instance for the default zone (LONDON)",
                defaultChronology, tokyoChronology.withZone(null));
    }

    @Test
    public void withZone_fromDefaultInstance_returnsCorrectInstance() {
        // Start with the default-zone instance and switch to a specific zone.
        ISOChronology defaultChronology = ISOChronology.getInstance(); // Gets LONDON instance
        ISOChronology parisChronology = ISOChronology.getInstance(PARIS);

        assertSame("Switching from default to PARIS should return the PARIS instance",
                parisChronology, defaultChronology.withZone(PARIS));
    }

    @Test
    public void withZone_fromUTCInstance_returnsCorrectInstance() {
        // Start with the UTC instance and switch to a specific zone.
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        ISOChronology parisChronology = ISOChronology.getInstance(PARIS);

        assertSame("Switching from UTC to PARIS should return the PARIS instance",
                parisChronology, utcChronology.withZone(PARIS));
    }
}