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
 * Tests the {@code withUTC()} method of {@link ISOChronology}.
 */
public class ISOChronologyWithUTCTest extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time for consistent test results: June 9, 2002, at midnight UTC.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(ISOChronologyWithUTCTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Fix the system time to a known value for predictable test execution.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Save the original default settings.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set default settings to a known state (London) to avoid test failures
        // due to the host machine's environment.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original system and default settings.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    /**
     * Tests that calling withUTC() on any ISOChronology instance returns
     * the singleton UTC-zoned instance.
     */
    public void testWithUTC() {
        // Arrange
        final Chronology expectedUtcChronology = ISOChronology.getInstanceUTC();
        final Chronology londonChronology = ISOChronology.getInstance(LONDON);
        final Chronology tokyoChronology = ISOChronology.getInstance(TOKYO);
        final Chronology defaultChronology = ISOChronology.getInstance(); // Set to LONDON in setUp

        // Act & Assert: All calls to withUTC() should return the same UTC instance.
        assertSame("withUTC() on a non-UTC chronology should return the UTC instance",
                expectedUtcChronology, londonChronology.withUTC());

        assertSame("withUTC() on another non-UTC chronology should return the UTC instance",
                expectedUtcChronology, tokyoChronology.withUTC());

        assertSame("withUTC() on the default chronology should return the UTC instance",
                expectedUtcChronology, defaultChronology.withUTC());

        assertSame("withUTC() on the UTC chronology itself should return the same instance",
                expectedUtcChronology, expectedUtcChronology.withUTC());
    }
}