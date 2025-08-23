package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BigDecimalParser}, focusing on its "fast path" for very long number strings.
 *
 * @see BigDecimalParser
 */
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * Verifies that {@link BigDecimalParser#parseWithFastParser(String)} and its
     * {@code char[]} overload correctly parse a valid number string that is longer
     * than 500 characters, which is the threshold to trigger the optimized parsing logic.
     */
    @Test
    void shouldCorrectlyParseLongValidBigDecimalString() {
        // Arrange
        // The threshold for switching to the fast parser in BigDecimalParser is 500 characters.
        // We generate a string with 500 leading zeros, making the total length 503,
        // which ensures the fast path is taken.
        final int zeroCount = 500;
        String longNumberString = generateLongDecimalString(zeroCount);
        BigDecimal expectedBigDecimal = new BigDecimal(longNumberString);

        // Act & Assert
        // 1. Test the String overload
        BigDecimal fromString = BigDecimalParser.parseWithFastParser(longNumberString);
        assertEquals(expectedBigDecimal, fromString, "Parsing from String should match BigDecimal constructor");

        // 2. Test the char[] overload
        char[] chars = longNumberString.toCharArray();
        BigDecimal fromChars = BigDecimalParser.parseWithFastParser(chars, 0, chars.length);
        assertEquals(expectedBigDecimal, fromChars, "Parsing from char[] should match BigDecimal constructor");
    }

    /**
     * Generates a valid, long BigDecimal string representation in the format "0.0...01".
     * This is used to create strings that are long enough to trigger specific parsing logic.
     *
     * @param zeroCount The number of zeros to place between the decimal point and the final '1'.
     * @return A long, valid BigDecimal string (e.g., "0.001" for zeroCount = 2).
     */
    private String generateLongDecimalString(int zeroCount) {
        // The total length will be zeroCount + 3 ("0." + zeros + "1")
        final StringBuilder sb = new StringBuilder(zeroCount + 3);
        sb.append("0.");
        for (int i = 0; i < zeroCount; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }
}