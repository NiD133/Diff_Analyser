package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the toString() method of {@link GregorianChronology}.
 * This test class focuses on verifying that the string representation of a GregorianChronology
 * instance correctly reflects its configuration, such as its time zone and minimum days in the first week.
 */
public class GregorianChronologyToStringTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // Store the original default time zone to restore it after the test.
        originalDefaultZone = DateTimeZone.getDefault();
        // Set a known default time zone for tests that rely on the default.
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void toString_withSpecificZone_shouldReturnChronologyNameWithZoneId() {
        // Arrange
        Chronology chrono = GregorianChronology.getInstance(TOKYO);
        String expected = "GregorianChronology[Asia/Tokyo]";

        // Act
        String actual = chrono.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void toString_withUTCZone_shouldReturnChronologyNameWithUTC() {
        // Arrange
        Chronology chrono = GregorianChronology.getInstanceUTC();
        String expected = "GregorianChronology[UTC]";

        // Act
        String actual = chrono.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void toString_withDefaultZone_shouldReturnChronologyNameWithDefaultZoneId() {
        // This test relies on the @Before setup, which sets the default zone to LONDON.
        // Arrange
        Chronology chrono = GregorianChronology.getInstance();
        String expected = "GregorianChronology[Europe/London]";

        // Act
        String actual = chrono.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void toString_withZoneAndMinDays_shouldReturnNameWithZoneAndMinDays() {
        // Arrange
        Chronology chrono = GregorianChronology.getInstance(DateTimeZone.UTC, 2);
        String expected = "GregorianChronology[UTC,mdfw=2]";

        // Act
        String actual = chrono.toString();

        // Assert
        assertEquals(expected, actual);
    }
}