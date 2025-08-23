package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BigDecimalParser} focusing on its handling of very long number strings,
 * which triggers a specific optimization path.
 */
@DisplayName("BigDecimalParser: Long String Parsing")
class BigDecimalParserLongStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * The SUT (System Under Test), {@link BigDecimalParser}, uses an optimized parsing strategy
     * for strings longer than 500 characters. This test uses a length of 500 for the
     * fractional part, resulting in a total string length of 503, to ensure this
     * optimization path is tested.
     */
    private static final int LONG_STRING_TEST_LENGTH = 500;

    /**
     * Generates a valid, long decimal string in the format "0.0...01".
     *
     * @param fractionalZeros The number of zeros to place after the decimal point.
     * @return A long decimal string.
     */
    private String generateLongDecimalString(int fractionalZeros) {
        final StringBuilder sb = new StringBuilder(fractionalZeros + 3);
        sb.append("0.");
        for (int i = 0; i < fractionalZeros; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }

    @Test
    @DisplayName("should correctly parse a long number from a String")
    void parsingLongStringShouldSucceed() {
        // Arrange
        final String longNumberString = generateLongDecimalString(LONG_STRING_TEST_LENGTH);
        final BigDecimal expectedValue = new BigDecimal(longNumberString);

        // Act
        final BigDecimal actualValue = BigDecimalParser.parse(longNumberString);

        // Assert
        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("should correctly parse a long number from a char array")
    void parsingLongCharArrayShouldSucceed() {
        // Arrange
        final String longNumberString = generateLongDecimalString(LONG_STRING_TEST_LENGTH);
        final char[] numberChars = longNumberString.toCharArray();
        final BigDecimal expectedValue = new BigDecimal(longNumberString);

        // Act
        final BigDecimal actualValue = BigDecimalParser.parse(numberChars, 0, numberChars.length);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}