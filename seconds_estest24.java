package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Seconds} class, focusing on understandability.
 */
public class SecondsTest {

    /**
     * Verifies that the minus() method correctly subtracts a value from Seconds.MAX_VALUE.
     *
     * This test is structured using the Arrange-Act-Assert pattern for clarity.
     * It uses descriptive variable names and calculates the expected result dynamically
     * to avoid "magic numbers," making the test's logic self-evident.
     */
    @Test
    public void minus_whenSubtractingFromMaxValue_returnsCorrectResult() {
        // Arrange
        final int valueToSubtract = 352831696;
        final int expectedSeconds = Integer.MAX_VALUE - valueToSubtract;

        // Act
        final Seconds result = Seconds.MAX_VALUE.minus(valueToSubtract);

        // Assert
        assertEquals(expectedSeconds, result.getSeconds());
    }
}