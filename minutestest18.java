package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the toStandardSeconds() method in the {@link Minutes} class.
 */
public class MinutesTest {

    private static final int SECONDS_IN_A_MINUTE = 60;

    /**
     * Tests that a standard conversion from minutes to seconds is calculated correctly.
     */
    @Test
    public void toStandardSeconds_shouldConvertMinutesToSeconds() {
        // Arrange
        Minutes threeMinutes = Minutes.minutes(3);
        Seconds expectedSeconds = Seconds.seconds(3 * SECONDS_IN_A_MINUTE);

        // Act
        Seconds actualSeconds = threeMinutes.toStandardSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }

    /**
     * Tests that converting a value that would cause an integer overflow
     * throws an ArithmeticException.
     */
    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_whenResultOverflows_shouldThrowArithmeticException() {
        // Act: This conversion will overflow the integer capacity for seconds.
        // For example, Integer.MAX_VALUE minutes * 60 > Integer.MAX_VALUE seconds.
        Minutes.MAX_VALUE.toStandardSeconds();
        
        // Assert: The test expects an ArithmeticException to be thrown.
    }
}