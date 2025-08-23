package org.joda.time.chrono;

import java.util.TimeZone;
import junit.framework.TestCase;
import org.joda.time.DateTimeZone;

/**
 * Tests the caching and singleton-like behavior of the CopticChronology factory methods.
 *
 * <p>The {@link CopticChronology#getInstance(DateTimeZone)} factory method is expected to return
 * the same cached instance for the same time zone, ensuring efficiency and object identity.
 */
public class CopticChronologyGetInstanceTest extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone = null;
    private TimeZone originalDefaultTimeZone = null;

    @Override
    protected void setUp() throws Exception {
        // Store the original default zones to restore them after the test.
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultTimeZone = TimeZone.getDefault();

        // Set a predictable default zone for tests that depend on it.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(LONDON.toTimeZone());
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original default zones.
        DateTimeZone.setDefault(originalDefaultZone);
        TimeZone.setDefault(originalDefaultTimeZone);
    }

    //-----------------------------------------------------------------------

    public void testGetInstance_withZone_returnsCachedInstance() {
        // Act: Request the chronology for the same zone multiple times.
        CopticChronology chrono1 = CopticChronology.getInstance(TOKYO);
        CopticChronology chrono2 = CopticChronology.getInstance(TOKYO);

        // Assert: The same instance should be returned, confirming it's cached.
        assertSame("getInstance(DateTimeZone) should be cached", chrono1, chrono2);
    }

    public void testGetInstance_utc_returnsCachedInstance() {
        // Act: Request the UTC chronology multiple times.
        CopticChronology chrono1 = CopticChronology.getInstanceUTC();
        CopticChronology chrono2 = CopticChronology.getInstanceUTC();

        // Assert: The same instance should be returned.
        assertSame("getInstanceUTC() should be cached", chrono1, chrono2);
    }

    public void testGetInstance_withDefaultZone_usesDefaultZone() {
        // The setUp method sets the default zone to LONDON.
        // This test verifies that getInstance() respects that default.

        // Act: Get an instance using the default zone and one explicitly for London.
        CopticChronology chronoDefault = CopticChronology.getInstance();
        CopticChronology chronoLondon = CopticChronology.getInstance(LONDON);

        // Assert: Both methods should return the same London-based instance.
        assertSame("getInstance() should use the default time zone", chronoDefault, chronoLondon);
    }

    public void testGetInstance_withDifferentZones_returnsDifferentInstances() {
        // Act: Request chronologies for two different time zones.
        CopticChronology chronoLondon = CopticChronology.getInstance(LONDON);
        CopticChronology chronoParis = CopticChronology.getInstance(PARIS);

        // Assert: Instances for different zones should be different objects.
        assertNotSame("Instances for different time zones should be different", chronoLondon, chronoParis);
    }
}