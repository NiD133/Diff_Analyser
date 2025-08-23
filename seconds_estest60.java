package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Verifies that getSeconds() on the constant Seconds.TWO returns the integer value 2.
     */
    @Test
    public void getSeconds_shouldReturnCorrectValueForConstantTwo() {
        // Arrange
        Seconds twoSeconds = Seconds.TWO;
        int expectedSeconds = 2;

        // Act
        int actualSeconds = twoSeconds.getSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }
}