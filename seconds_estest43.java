package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Seconds class.
 */
public class SecondsTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveSecondsWithNull() {
        // The Javadoc for isGreaterThan states that a null input is treated as zero.
        // This test verifies that a positive Seconds value is correctly evaluated as greater than null.

        // Arrange
        Seconds oneSecond = Seconds.ONE;

        // Act & Assert
        assertTrue("Seconds.ONE should be considered greater than a null Seconds object.", oneSecond.isGreaterThan(null));
    }
}