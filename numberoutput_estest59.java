package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link NumberOutput} class, focusing on double-to-string conversion.
 */
public class NumberOutputTest {

    /**
     * Tests that a large double value is correctly converted to its scientific notation string representation
     * when using the "fast writer" (Schubfach) algorithm.
     */
    @Test
    public void shouldConvertLargeDoubleToScientificNotationUsingFastWriter() {
        // Arrange
        // This value is (2^53) * 1000. 2^53 is Double.MAX_SAFE_INTEGER, the largest
        // integer that can be represented exactly as a double. This tests the formatting
        // of large numbers that require scientific notation.
        double largeDoubleValue = 9007199254740992000.0;
        String expectedRepresentation = "9.007199254740992E18";
        boolean useFastWriter = true;

        // Act
        String actualRepresentation = NumberOutput.toString(largeDoubleValue, useFastWriter);

        // Assert
        assertEquals(expectedRepresentation, actualRepresentation);
    }
}