package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A focused test suite for the division operations in the {@link Seconds} class.
 */
public class SecondsDividedByTest {

    @Test
    public void dividedBy_withMinValue_returnsCorrectlyTruncatedResult() {
        // Arrange
        // The Seconds.dividedBy() method uses integer division. This test verifies
        // that dividing the minimum possible seconds value by a given divisor
        // produces the correctly truncated integer result.
        final int divisor = 3600;
        final int expectedSeconds = Integer.MIN_VALUE / divisor; // -2,147,483,648 / 3600 = -596523

        // Act
        Seconds result = Seconds.MIN_VALUE.dividedBy(divisor);

        // Assert
        assertEquals("Division of MIN_VALUE should be correctly truncated",
                     expectedSeconds, result.getSeconds());
    }
}