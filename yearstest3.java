package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for the {@link Years} class, focusing on the {@link Years#yearsBetween(ReadableInstant, ReadableInstant)} factory method.
 */
public class YearsTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    public void yearsBetween_shouldCalculatePositiveYears_whenEndIsAfterStart() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        Years expected = Years.THREE;

        // Act
        Years actual = Years.yearsBetween(start, end);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void yearsBetween_shouldReturnZero_whenStartAndEndAreSame() {
        // Arrange
        DateTime instant = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        Years expected = Years.ZERO;

        // Act
        Years actual = Years.yearsBetween(instant, instant);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void yearsBetween_shouldCalculateNegativeYears_whenEndIsBeforeStart() {
        // Arrange
        DateTime start = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        Years expected = Years.years(-3);

        // Act
        Years actual = Years.yearsBetween(start, end);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void yearsBetween_shouldCalculateAcrossLongerInterval() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS);
        Years expected = Years.years(6);

        // Act
        Years actual = Years.yearsBetween(start, end);

        // Assert
        assertEquals(expected, actual);
    }
}