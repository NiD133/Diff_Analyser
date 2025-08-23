package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the withUTC() method of {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // To ensure tests are deterministic, we save the original default time zone
        // and set a known one for the duration of the test.
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void withUTC_shouldAlwaysReturnTheSameSingletonUtcInstance() {
        // Arrange: Create chronology instances for various time zones.
        // The withUTC() method is expected to return a canonical, singleton instance.
        Chronology chronologyInLondon = GregorianChronology.getInstance(LONDON);
        Chronology chronologyInTokyo = GregorianChronology.getInstance(TOKYO);
        Chronology chronologyInDefaultZone = GregorianChronology.getInstance(); // Uses LONDON via setUp
        Chronology chronologyInUtc = GregorianChronology.getInstanceUTC();

        // Act & Assert:
        // Verify that calling withUTC() on any instance returns the exact same UTC object.
        // We use assertSame to check for object identity, not just equality.
        final Chronology expectedUtcChronology = GregorianChronology.getInstanceUTC();

        assertSame("withUTC() on a London instance should return the UTC singleton",
                expectedUtcChronology, chronologyInLondon.withUTC());

        assertSame("withUTC() on a Tokyo instance should return the UTC singleton",
                expectedUtcChronology, chronologyInTokyo.withUTC());

        assertSame("withUTC() on a default-zone instance should return the UTC singleton",
                expectedUtcChronology, chronologyInDefaultZone.withUTC());

        assertSame("withUTC() on the UTC instance should return itself",
                expectedUtcChronology, chronologyInUtc.withUTC());
    }
}