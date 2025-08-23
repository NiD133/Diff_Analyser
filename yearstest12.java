package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the string representation of the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void toString_shouldReturnISO8601FormatForPositiveYears() {
        // Arrange
        final Years twentyYears = Years.years(20);
        final String expectedFormat = "P20Y";

        // Act
        final String actualFormat = twentyYears.toString();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    public void toString_shouldReturnISO8601FormatForNegativeYears() {
        // Arrange
        final Years minusTwentyYears = Years.years(-20);
        final String expectedFormat = "P-20Y";

        // Act
        final String actualFormat = minusTwentyYears.toString();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }
}