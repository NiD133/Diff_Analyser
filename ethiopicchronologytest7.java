package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the toString() method of EthiopicChronology.
 */
public class EthiopicChronologyToStringTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fields to store the original default JVM settings
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Save the original default settings before each test
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set a known, predictable default time zone for the tests
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original default settings after each test to avoid side-effects
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void toString_forSpecificZone_shouldReturnChronologyNameAndZoneId() {
        // Arrange
        String expectedLondon = "EthiopicChronology[Europe/London]";
        String expectedTokyo = "EthiopicChronology[Asia/Tokyo]";

        // Act
        String actualLondon = EthiopicChronology.getInstance(LONDON).toString();
        String actualTokyo = EthiopicChronology.getInstance(TOKYO).toString();

        // Assert
        assertEquals("toString() with a specific zone should include the zone ID.",
                expectedLondon, actualLondon);
        assertEquals("toString() with a specific zone should include the zone ID.",
                expectedTokyo, actualTokyo);
    }

    @Test
    public void toString_forDefaultZone_shouldReturnNameAndDefaultZoneId() {
        // Arrange: The default zone is set to LONDON in setUp()
        String expected = "EthiopicChronology[Europe/London]";

        // Act
        String actual = EthiopicChronology.getInstance().toString();

        // Assert
        assertEquals("toString() for the default zone should use the default zone's ID.",
                expected, actual);
    }

    @Test
    public void toString_forUTC_shouldReturnNameAndUTC() {
        // Arrange
        String expected = "EthiopicChronology[UTC]";

        // Act
        String actual = EthiopicChronology.getInstanceUTC().toString();

        // Assert
        assertEquals("toString() for the UTC instance should include 'UTC'.",
                expected, actual);
    }
}