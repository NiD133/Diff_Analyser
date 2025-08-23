package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the factory methods of GregorianChronology, focusing on its caching and singleton behavior.
 */
public class GregorianChronologyGetInstanceTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // Save the original default time zone to restore it after the test.
        originalDefaultZone = DateTimeZone.getDefault();
        // Set a known default time zone for tests that depend on it.
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    /**
     * Tests that getInstance(DateTimeZone) returns a cached instance for the same zone.
     */
    @Test
    public void testGetInstance_withZone_returnsCachedInstances() {
        // The factory method should return the same, cached instance for a given time zone.
        assertSame("Instances for TOKYO should be cached",
                GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO));
        assertSame("Instances for LONDON should be cached",
                GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(LONDON));
        assertSame("Instances for PARIS should be cached",
                GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(PARIS));
    }

    /**
     * Tests that getInstanceUTC() consistently returns the same singleton instance.
     */
    @Test
    public void testGetInstanceUTC_returnsSingletonInstance() {
        // The UTC instance is a singleton and should always be the same object.
        assertSame("UTC instance should be a singleton",
                GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
    }

    /**
     * Tests that getInstance() returns the chronology for the default system time zone.
     */
    @Test
    public void testGetInstance_noArgs_returnsInstanceForDefaultZone() {
        // This test relies on the setUp method, which sets the default time zone to LONDON.
        // We verify that getInstance() correctly picks up this default.
        assertSame("getInstance() should return the instance for the default zone",
                GregorianChronology.getInstance(), GregorianChronology.getInstance(LONDON));
    }
}