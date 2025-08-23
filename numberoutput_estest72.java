package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that the {@code toString(double)} method correctly converts a
     * simple negative double value to its string representation.
     */
    @Test
    public void toString_shouldConvertNegativeDoubleToString() {
        // Arrange
        double negativeDouble = -1.0;
        String expected = "-1.0";

        // Act
        String actual = NumberOutput.toString(negativeDouble);

        // Assert
        assertEquals(expected, actual);
    }
}