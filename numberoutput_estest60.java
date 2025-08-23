package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on long-to-string conversion.
 */
public class NumberOutputTest {

    /**
     * Tests that the {@code toString(long)} method correctly converts a large negative
     * long value, which is close to {@code Long.MIN_VALUE}, into its string representation.
     */
    @Test
    public void shouldConvertLargeNegativeLongToString() {
        // Arrange
        long largeNegativeValue = -9223372036854775805L;
        String expectedString = "-9223372036854775805";

        // Act
        String actualString = NumberOutput.toString(largeNegativeValue);

        // Assert
        assertEquals(expectedString, actualString);
    }
}