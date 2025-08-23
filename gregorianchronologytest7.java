package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * Tests for the withZone() method of GregorianChronology.
 *
 * <p>This test suite focuses on verifying the caching behavior of GregorianChronology,
 * ensuring that {@code withZone()} returns the expected singleton instance for a given time zone.
 */
public class TestGregorianChronologyWithZone extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time, 2002-06-09T00:00:00Z, for predictable test results.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestGregorianChronologyWithZone.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Fix the current time to ensure tests are deterministic.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Store original system defaults to restore them after the test.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set predictable defaults for the tests. This is crucial for tests
        // that rely on the "default" time zone.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original system defaults to avoid side-effects on other tests.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    //-----------------------------------------------------------------------

    /**
     * Tests that calling withZone() with the chronology's current zone returns the same instance.
     * This verifies the optimization to avoid creating a new object unnecessarily.
     */
    public void testWithZone_givenSameZone_returnsSameInstance() {
        Chronology tokyoChronology = GregorianChronology.getInstance(TOKYO);
        assertSame("Calling withZone with the same zone should return the same instance",
                tokyoChronology, tokyoChronology.withZone(TOKYO));
    }

    /**
     * Tests that calling withZone() with a different zone returns the correct cached instance for that new zone.
     */
    public void testWithZone_givenDifferentZone_returnsCachedInstanceForNewZone() {
        Chronology tokyoChronology = GregorianChronology.getInstance(TOKYO);

        Chronology londonChronology = GregorianChronology.getInstance(LONDON);
        assertSame("withZone(LONDON) should return the cached LONDON instance",
                londonChronology, tokyoChronology.withZone(LONDON));
    }

    /**
     * Tests that calling withZone(null) returns the instance for the default time zone.
     */
    public void testWithZone_givenNullZone_returnsDefaultZoneInstance() {
        // The default time zone is set to LONDON in setUp()
        Chronology tokyoChronology = GregorianChronology.getInstance(TOKYO);
        Chronology defaultChronology = GregorianChronology.getInstance(LONDON);

        assertSame("withZone(null) should return the instance for the default zone (LONDON)",
                defaultChronology, tokyoChronology.withZone(null));
    }

    /**
     * Tests calling withZone() on an instance that was created using the default time zone.
     */
    public void testWithZone_onDefaultInstance() {
        // The default time zone is set to LONDON in setUp()
        Chronology defaultChronology = GregorianChronology.getInstance();
        assertEquals("getInstance() should return a chronology with the default zone",
                LONDON, defaultChronology.getZone());

        Chronology parisChronology = GregorianChronology.getInstance(PARIS);
        assertSame("Calling withZone(PARIS) on the default instance should return the PARIS instance",
                parisChronology, defaultChronology.withZone(PARIS));
    }

    /**
     * Tests calling withZone() on the UTC instance.
     */
    public void testWithZone_onUTCInstance() {
        Chronology utcChronology = GregorianChronology.getInstanceUTC();
        Chronology parisChronology = GregorianChronology.getInstance(PARIS);

        assertSame("Calling withZone(PARIS) on the UTC instance should return the PARIS instance",
                parisChronology, utcChronology.withZone(PARIS));
    }
}