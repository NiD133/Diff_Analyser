package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void isGreaterThan_shouldReturnFalse_whenNegativeSecondsIsComparedToNull() {
        // The isGreaterThan() method's contract specifies that a null argument
        // is treated as a zero-value period (Seconds.ZERO).
        
        // Arrange
        Seconds negativeSeconds = Seconds.seconds(-1);

        // Act
        // Compare a negative Seconds instance to null.
        boolean result = negativeSeconds.isGreaterThan(null);

        // Assert
        // A negative value (-1) should not be greater than zero.
        assertFalse("-1 seconds should not be greater than null (treated as 0 seconds)", result);
    }
}