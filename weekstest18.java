package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the {@link Weeks} class, focusing on conversions to standard units.
 */
public class WeeksTest {

    /**
     * Tests that a standard conversion from weeks to seconds yields the correct result.
     */
    @Test
    public void toStandardSeconds_forPositiveWeeks_convertsCorrectly() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        Seconds expectedSeconds = Seconds.seconds(2 * DateTimeConstants.SECONDS_PER_WEEK);

        // Act
        Seconds actualSeconds = twoWeeks.toStandardSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds, "2 weeks should convert to the correct number of standard seconds.");
    }

    /**
     * Tests that converting the maximum possible number of weeks to seconds
     * throws an ArithmeticException due to integer overflow.
     */
    @Test
    public void toStandardSeconds_forMaxValue_throwsArithmeticException() {
        // Arrange
        Weeks maxWeeks = Weeks.MAX_VALUE;

        // Act & Assert
        // The conversion should cause an integer overflow.
        assertThrows(ArithmeticException.class, () -> {
            maxWeeks.toStandardSeconds();
        }, "Conversion of MAX_VALUE weeks to seconds should cause an overflow.");
    }
}