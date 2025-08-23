package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@code NumberOutput.toString(int)} correctly converts a 
     * typical positive integer into its string representation.
     *
     * This test case covers a standard, multi-digit positive number to ensure
     * the basic conversion logic is working as expected.
     */
    @Test
    public void toString_shouldConvertPositiveInteger() {
        // Arrange
        int numberToConvert = 2420;
        String expectedRepresentation = "2420";

        // Act
        String actualRepresentation = NumberOutput.toString(numberToConvert);

        // Assert
        assertEquals(expectedRepresentation, actualRepresentation);
    }
}