package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests the withUTC() method of IslamicChronology to ensure it consistently
 * returns the singleton UTC-zoned instance.
 */
public class IslamicChronologyWithUtcTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        originalDefaultZone = DateTimeZone.getDefault();
        // Set a known default zone to ensure getInstance() is predictable for the test.
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default zone to avoid side-effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    /**
     * Verifies that withUTC() is optimized to return the same singleton instance of the
     * UTC-based IslamicChronology, regardless of the original instance's time zone.
     * This is important for performance and memory management.
     */
    @Test
    public void withUTC_onAnyInstance_returnsSingletonUTCInstance() {
        Chronology islamicUtcSingleton = IslamicChronology.getInstanceUTC();

        // Test with an instance in a specific, non-UTC time zone (London)
        assertSame(islamicUtcSingleton, IslamicChronology.getInstance(LONDON).withUTC());

        // Test with an instance in another specific, non-UTC time zone (Tokyo)
        assertSame(islamicUtcSingleton, IslamicChronology.getInstance(TOKYO).withUTC());

        // Test with an instance created using the default time zone (set to LONDON in setUp)
        assertSame(islamicUtcSingleton, IslamicChronology.getInstance().withUTC());

        // Test that calling withUTC() on the UTC instance itself returns the same instance
        assertSame(islamicUtcSingleton, IslamicChronology.getInstanceUTC().withUTC());
    }
}