package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the factory method {@link Years#yearsIn(ReadableInterval)}.
 */
public class YearsTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    public void yearsIn_givenNullInterval_shouldReturnZero() {
        // The yearsIn method should handle null input gracefully by returning zero.
        assertEquals(Years.ZERO, Years.yearsIn(null));
    }

    @Test
    public void yearsIn_givenZeroLengthInterval_shouldReturnZero() {
        // An interval with the same start and end time has a duration of zero years.
        DateTime pointInTime = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        Interval zeroLengthInterval = new Interval(pointInTime, pointInTime);
        
        assertEquals(Years.ZERO, Years.yearsIn(zeroLengthInterval));
    }

    @Test
    public void yearsIn_givenThreeYearInterval_shouldReturnThree() {
        // Arrange: Create an interval that spans exactly three years.
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        Interval threeYearInterval = new Interval(start, end);

        // Act
        Years result = Years.yearsIn(threeYearInterval);

        // Assert
        assertEquals(Years.THREE, result);
    }

    @Test
    public void yearsIn_givenSixYearInterval_shouldReturnSix() {
        // Arrange: Create an interval that spans exactly six years.
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS);
        Interval sixYearInterval = new Interval(start, end);

        // Act
        Years result = Years.yearsIn(sixYearInterval);

        // Assert
        assertEquals(6, result.getYears());
    }
}