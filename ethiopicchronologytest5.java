package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link EthiopicChronology} focusing on time zone behavior.
 */
public class EthiopicChronologyTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // To ensure tests are predictable, we save the original default time zone
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
    public void withUTC_shouldAlwaysReturnTheSingletonUTCInstance() {
        // Arrange: Create EthiopicChronology instances for various time zones.
        Chronology londonChrono = EthiopicChronology.getInstance(LONDON);
        Chronology tokyoChrono = EthiopicChronology.getInstance(TOKYO);
        Chronology utcChrono = EthiopicChronology.getInstanceUTC();
        // getInstance() uses the default zone, which is set to LONDON in setUp().
        Chronology defaultZoneChrono = EthiopicChronology.getInstance();

        // Act & Assert:
        // The withUTC() method should return the canonical UTC instance, regardless of the
        // original time zone. We use assertSame to verify it's the exact same object instance.
        assertSame("withUTC() on a London instance should return the singleton UTC instance",
                EthiopicChronology.getInstanceUTC(), londonChrono.withUTC());

        assertSame("withUTC() on a Tokyo instance should return the singleton UTC instance",
                EthiopicChronology.getInstanceUTC(), tokyoChrono.withUTC());

        assertSame("withUTC() on the UTC instance should return itself",
                EthiopicChronology.getInstanceUTC(), utcChrono.withUTC());

        assertSame("withUTC() on a default zone instance should return the singleton UTC instance",
                EthiopicChronology.getInstanceUTC(), defaultZoneChrono.withUTC());
    }
}