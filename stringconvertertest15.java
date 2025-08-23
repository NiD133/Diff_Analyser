package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StringConverter} focusing on its integration with the DateTime constructor.
 * This test verifies the parsing of string representations of datetimes.
 */
public class StringConverterTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    /**
     * This test verifies that the DateTime constructor can parse a full ISO 8601 formatted string
     * and correctly apply a specified Chronology. The string's date and time components
     * should be interpreted according to the provided Chronology (Julian in this case),
     * not the default ISOChronology.
     */
    @Test
    public void shouldCreateCorrectDateTimeWhenParsingStringWithExplicitChronology() {
        // Arrange
        final String isoDateTimeString = "2004-06-09T12:24:48.501+02:00";
        final JulianChronology julianChronologyInParis = JulianChronology.getInstance(PARIS);

        // Act
        // The DateTime constructor uses the StringConverter internally to handle the string input.
        // The provided chronology should be used for parsing.
        DateTime result = new DateTime(isoDateTimeString, julianChronologyInParis);

        // Assert
        // Verify that the date components are interpreted in the Julian calendar
        assertEquals("Year should match", 2004, result.getYear());
        assertEquals("Month should match", 6, result.getMonthOfYear());
        assertEquals("Day should match", 9, result.getDayOfMonth());

        // Verify that the time components are parsed correctly
        assertEquals("Hour should match", 12, result.getHourOfDay());
        assertEquals("Minute should match", 24, result.getMinuteOfHour());
        assertEquals("Second should match", 48, result.getSecondOfMinute());
        assertEquals("Millis should match", 501, result.getMillisOfSecond());

        // Verify that the specified chronology and its time zone are set on the resulting DateTime
        assertEquals("Chronology should be Julian", julianChronologyInParis, result.getChronology());
        assertEquals("Time zone should be Paris", PARIS, result.getZone());
    }
}