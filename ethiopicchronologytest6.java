package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the behavior of the withZone() method in EthiopicChronology.
 * It verifies that the method correctly returns cached chronology instances for different time zones.
 */
public class EthiopicChronologyWithZoneTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save original default time zone and locale
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a known default time zone for the tests
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore original default time zone and locale
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void testWithZone_givenSameZone_returnsSameInstance() {
        // Arrange
        Chronology tokyoChronology = EthiopicChronology.getInstance(TOKYO);

        // Act
        Chronology result = tokyoChronology.withZone(TOKYO);

        // Assert
        assertSame("Calling withZone with the same zone should return the same instance", tokyoChronology, result);
    }

    @Test
    public void testWithZone_givenDifferentZone_returnsCorrectCachedInstance() {
        // Arrange
        Chronology tokyoChronology = EthiopicChronology.getInstance(TOKYO);
        Chronology expectedParisChronology = EthiopicChronology.getInstance(PARIS);

        // Act
        Chronology result = tokyoChronology.withZone(PARIS);

        // Assert
        assertSame("Calling withZone with a different zone should return the correct cached instance for that zone",
                expectedParisChronology, result);
    }

    @Test
    public void testWithZone_givenNullZone_returnsDefaultZoneInstance() {
        // Arrange: The default zone is set to LONDON in setUp()
        Chronology tokyoChronology = EthiopicChronology.getInstance(TOKYO);
        Chronology expectedDefaultChronology = EthiopicChronology.getInstance(LONDON);

        // Act
        Chronology result = tokyoChronology.withZone(null);

        // Assert
        assertSame("Calling withZone(null) should return the instance for the default time zone",
                expectedDefaultChronology, result);
    }

    @Test
    public void testWithZone_onDefaultInstance_returnsCorrectCachedInstance() {
        // Arrange: getInstance() uses the default zone, which is LONDON
        Chronology defaultChronology = EthiopicChronology.getInstance();
        Chronology expectedParisChronology = EthiopicChronology.getInstance(PARIS);

        // Act
        Chronology result = defaultChronology.withZone(PARIS);

        // Assert
        assertSame("Calling withZone on a default instance should return the correct cached instance",
                expectedParisChronology, result);
    }

    @Test
    public void testWithZone_onUTCInstance_returnsCorrectCachedInstance() {
        // Arrange
        Chronology utcChronology = EthiopicChronology.getInstanceUTC();
        Chronology expectedParisChronology = EthiopicChronology.getInstance(PARIS);

        // Act
        Chronology result = utcChronology.withZone(PARIS);

        // Assert
        assertSame("Calling withZone on a UTC instance should return the correct cached instance",
                expectedParisChronology, result);
    }
}