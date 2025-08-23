package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void toString_forMinValue_returnsCorrectISOFormat() {
        // Arrange
        Seconds minSeconds = Seconds.MIN_VALUE;
        String expectedString = "PT" + Integer.MIN_VALUE + "S";

        // Act
        String actualString = minSeconds.toString();

        // Assert
        // The toString() method should return the value in the ISO8601 period format (e.g., PTnS).
        assertEquals(expectedString, actualString);
    }
}