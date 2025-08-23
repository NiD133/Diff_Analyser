package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
public class BigDecimalParserTest {

    /**
     * Tests that {@link BigDecimalParser#parse(char[], int, int)} correctly parses a single digit
     * from a sub-section of a character array.
     */
    @Test
    public void parse_whenGivenSliceOfCharArrayWithSingleDigit_shouldReturnCorrectBigDecimal() {
        // Arrange
        // The input array contains the target number '2' surrounded by other characters
        // to ensure the offset and length parameters are correctly handled.
        char[] inputChars = "x2y".toCharArray();
        int offset = 1; // Start parsing at index 1
        int length = 1; // Parse one character
        BigDecimal expectedResult = new BigDecimal("2");

        // Act
        BigDecimal actualResult = BigDecimalParser.parse(inputChars, offset, length);

        // Assert
        // We use assertEquals on BigDecimal objects for a precise comparison.
        assertEquals(expectedResult, actualResult);
    }
}