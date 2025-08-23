package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on integer division helper methods.
 */
public class NumberOutputTest {

    /**
     * Tests that the {@code divBy1000} method correctly returns 0
     * when the input number is 0.
     */
    @Test
    public void divBy1000_shouldReturnZero_whenInputIsZero() {
        // Arrange
        int number = 0;
        int expectedQuotient = 0;

        // Act
        int actualQuotient = NumberOutput.divBy1000(number);

        // Assert
        assertEquals(expectedQuotient, actualQuotient);
    }
}