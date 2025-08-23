package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the factory methods of {@link EthiopicChronology}.
 * This test suite focuses on how the chronology is instantiated, particularly
 * its behavior with respect to the default system time zone.
 */
public class EthiopicChronologyTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // A fixed point in time for deterministic tests: 2002-06-09T00:00:00Z.
    // Using a DateTime object makes the date explicit and easy to understand.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, ISO_UTC).getMillis();

    // Fields to store and restore the original system state.
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    /**
     * Sets up the test environment. This involves fixing the current time and
     * setting the default time zone and locale to a known state.
     */
    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Store original system defaults to restore them after the test.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set system defaults to a known state for predictable test results.
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    /**
     * Tears down the test environment, restoring the original system state.
     */
    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();

        // Restore original system defaults.
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    //-----------------------------------------------------------------------

    @Test
    public void getInstance_noArgs_usesDefaultZone() {
        // Arrange: The default time zone is set to LONDON in setUp().

        // Act
        EthiopicChronology chronology = EthiopicChronology.getInstance();

        // Assert
        assertEquals("getInstance() should use the default time zone", LONDON, chronology.getZone());
    }

    @Test
    public void getInstance_noArgs_returnsInstanceOfEthiopicChronology() {
        // Act
        Chronology chronology = EthiopicChronology.getInstance();

        // Assert
        assertSame("getInstance() must return an instance of EthiopicChronology",
                   EthiopicChronology.class, chronology.getClass());
    }
}