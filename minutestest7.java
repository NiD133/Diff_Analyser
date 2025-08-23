package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes#parseMinutes(String)} factory method.
 */
public class MinutesTest {

    @Test
    public void parseMinutes_givenNullString_shouldReturnZeroMinutes() {
        // The method contract specifies that a null input string results in a zero-minute period.
        assertEquals(Minutes.ZERO, Minutes.parseMinutes(null));
    }

    @Test
    public void parseMinutes_givenValidISOStrings_shouldReturnCorrectMinutes() {
        // Asserts that standard ISO-8601 period formats for minutes are parsed correctly.
        assertEquals("Parsing PT0M should result in zero minutes",
                Minutes.ZERO, Minutes.parseMinutes("PT0M"));
        assertEquals("Parsing PT1M should result in one minute",
                Minutes.ONE, Minutes.parseMinutes("PT1M"));
        assertEquals("Parsing a negative duration PT-3M should be handled",
                Minutes.minutes(-3), Minutes.parseMinutes("PT-3M"));

        // Asserts that more complex ISO formats are parsed correctly,
        // as long as only the minute component is non-zero.
        assertEquals("Parsing a full format with only minutes like P0Y0M0DT2M",
                Minutes.TWO, Minutes.parseMinutes("P0Y0M0DT2M"));
        assertEquals("Parsing a time format with zero hours like PT0H2M",
                Minutes.TWO, Minutes.parseMinutes("PT0H2M"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_givenStringWithNonZeroYearsAndDays_shouldThrowException() {
        // According to the Javadoc, the parse accepts the full ISO syntax,
        // but only the minutes component may be non-zero.
        // This string has non-zero year and day components, so it is invalid.
        Minutes.parseMinutes("P1Y1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_givenStringWithNonZeroDays_shouldThrowException() {
        // This string has a non-zero day component, which is not allowed.
        Minutes.parseMinutes("P1DT1M");
    }
}