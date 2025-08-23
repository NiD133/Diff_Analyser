package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the {@link Years#parseYears(String)} factory method.
 */
public class YearsTest {

    @Test
    public void parseYears_shouldReturnZeroForNullInput() {
        // The Joda-Time specification states that a null input string results in a zero-year period.
        assertEquals(Years.ZERO, Years.parseYears(null));
    }

    @Test
    public void parseYears_shouldParseZeroYearPeriod() {
        assertEquals(0, Years.parseYears("P0Y").getYears());
    }

    @Test
    public void parseYears_shouldParsePositiveYearPeriod() {
        assertEquals(1, Years.parseYears("P1Y").getYears());
    }

    @Test
    public void parseYears_shouldParseNegativeYearPeriod() {
        assertEquals(-3, Years.parseYears("P-3Y").getYears());
    }

    @Test
    public void parseYears_shouldParsePeriodWithZeroMonthComponent() {
        // The parser should correctly handle and ignore other valid ISO8601 components if their value is zero.
        assertEquals(2, Years.parseYears("P2Y0M").getYears());
    }

    @Test
    public void parseYears_shouldParsePeriodWithZeroTimeComponents() {
        // The parser should also correctly handle zero-value time components.
        assertEquals(2, Years.parseYears("P2YT0H0M").getYears());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_shouldThrowExceptionForPeriodWithNonZeroMonthsAndDays() {
        // Years.parseYears can only parse strings where components other than years are zero.
        Years.parseYears("P1M1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_shouldThrowExceptionForPeriodWithNonZeroTime() {
        // A non-zero time component should also result in an exception.
        Years.parseYears("P1YT1H");
    }
}