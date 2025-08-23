package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the factory methods of {@link IslamicChronology}.
 * This test focuses on verifying that the getInstance() methods return cached, singleton-like
 * instances for each time zone.
 */
public class IslamicChronologyGetInstanceTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fields to restore system defaults after test execution
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Back up original system defaults and set a known default for the test
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set the default time zone to LONDON to test getInstance() without arguments
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original system settings to ensure test isolation
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    /**
     * Tests that IslamicChronology.getInstance() returns the same cached instance for the same time zone.
     * This verifies the singleton pattern used for performance.
     */
    @Test
    public void testGetInstance_cachesInstancesByZone() {
        // Calling getInstance with the same zone should return the exact same object
        assertSame("Instances for TOKYO should be cached",
                IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO));
        assertSame("Instances for LONDON should be cached",
                IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(LONDON));
        assertSame("Instances for PARIS should be cached",
                IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(PARIS));
        assertSame("Instances for UTC should be cached",
                IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());

        // Verify that getInstance() uses the default time zone (set to LONDON in setUp)
        assertSame("getInstance() should return the instance for the default zone",
                IslamicChronology.getInstance(), IslamicChronology.getInstance(LONDON));
    }
}