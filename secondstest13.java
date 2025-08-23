package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test cases for the {@link Seconds#toString()} method.
 */
public class SecondsTest {

    @Test
    public void toString_shouldReturnISO8601FormatForPositiveValue() {
        // Arrange
        Seconds twentySeconds = Seconds.seconds(20);
        String expected = "PT20S";

        // Act
        String actual = twentySeconds.toString();

        // Assert
        assertEquals("The string format for positive seconds should be 'PTnS'.", expected, actual);
    }

    @Test
    public void toString_shouldReturnISO8601FormatForNegativeValue() {
        // Arrange
        Seconds negativeTwentySeconds = Seconds.seconds(-20);
        String expected = "PT-20S";

        // Act
        String actual = negativeTwentySeconds.toString();

        // Assert
        assertEquals("The string format for negative seconds should be 'PT-nS'.", expected, actual);
    }
}