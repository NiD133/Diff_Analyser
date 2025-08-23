package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#parseWeeks(String)} factory method.
 * This class focuses on verifying the parsing of ISO 8601 period strings.
 */
public class Weeks_ParseStringTest {

    @Test
    public void parseWeeks_givenNullString_returnsZeroWeeks() {
        assertEquals(Weeks.ZERO, Weeks.parseWeeks(null));
    }

    @Test
    public void parseWeeks_givenPositiveWeeksString_parsesCorrectly() {
        assertEquals(Weeks.ONE, Weeks.parseWeeks("P1W"));
    }

    @Test
    public void parseWeeks_givenNegativeWeeksString_parsesCorrectly() {
        assertEquals(Weeks.weeks(-3), Weeks.parseWeeks("P-3W"));
    }

    @Test
    public void parseWeeks_givenZeroWeeksString_parsesAsZero() {
        assertEquals(Weeks.ZERO, Weeks.parseWeeks("P0W"));
    }

    @Test
    public void parseWeeks_givenFullPeriodStringWithOnlyWeeks_parsesCorrectly() {
        // The parser accepts the full ISO format, but only the 'W' field can be non-zero.
        // This test confirms that other zero-valued fields are ignored.
        assertEquals(Weeks.TWO, Weeks.parseWeeks("P0Y0M2W"));
    }

    @Test
    public void parseWeeks_givenPeriodStringWithZeroTime_parsesCorrectly() {
        // Similar to the above, the time component is allowed if its value is zero.
        assertEquals(Weeks.TWO, Weeks.parseWeeks("P2WT0H0M"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWeeks_whenStringContainsNonZeroYearField_throwsException() {
        // An exception is expected because the 'Y' (years) field is non-zero.
        Weeks.parseWeeks("P1Y1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWeeks_whenStringContainsNonZeroHourField_throwsException() {
        // An exception is expected because the 'H' (hours) field is non-zero.
        Weeks.parseWeeks("P1WT1H");
    }
}