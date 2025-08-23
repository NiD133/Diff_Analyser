package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void getSeconds_forZeroConstant_shouldReturnZero() {
        // Arrange
        Seconds zeroSeconds = Seconds.ZERO;
        int expectedSeconds = 0;

        // Act
        int actualSeconds = zeroSeconds.getSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }
}