package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the caching and singleton behavior of the EthiopicChronology class.
 *
 * <p>This test suite verifies that the factory methods like {@link EthiopicChronology#getInstance(DateTimeZone)}
 * return cached, singleton instances for the same time zone, which is an important performance feature.
 */
public class EthiopicChronologyGetInstanceTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalSystemDateTimeZone;
    private TimeZone originalSystemTimeZone;
    private Locale originalSystemLocale;

    @Before
    public void setUp() {
        // Save the original system defaults to restore them after the test.
        originalSystemDateTimeZone = DateTimeZone.getDefault();
        originalSystemTimeZone = TimeZone.getDefault();
        originalSystemLocale = Locale.getDefault();

        // Set a known default time zone for the tests that rely on the default.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original system defaults to ensure test isolation.
        DateTimeZone.setDefault(originalSystemDateTimeZone);
        TimeZone.setDefault(originalSystemTimeZone);
        Locale.setDefault(originalSystemLocale);
    }

    @Test
    public void getInstance_withSpecificZone_shouldReturnCachedSingleton() {
        // The getInstance(DateTimeZone) method should return the same instance for the same zone.
        assertSame("Instances for TOKYO should be cached",
                EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame("Instances for LONDON should be cached",
                EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame("Instances for PARIS should be cached",
                EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(PARIS));
    }

    @Test
    public void getInstance_forUTC_shouldReturnCachedSingleton() {
        // The getInstanceUTC() method should always return the same singleton instance.
        assertSame("Instances for UTC should be cached",
                EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
    }

    @Test
    public void getInstance_forDefaultZone_shouldMatchExplicitZoneInstance() {
        // Given the default time zone is set to LONDON in setUp,
        // getInstance() should return the same instance as getInstance(LONDON).
        assertSame("Default instance should match the explicit LONDON instance",
                EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }
}