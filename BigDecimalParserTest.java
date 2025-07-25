package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void testParseInvalidLongString() {
        String invalidString = generateLongInvalidString();
        assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parse(invalidString);
        }, "Expected NumberFormatException for invalid input string");

        assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parseWithFastParser(invalidString);
        }, "Expected NumberFormatException for invalid input string with fast parser");
    }

    @Test
    void testParseValidLongString() {
        String validString = generateLongValidString(500);
        BigDecimal expectedValue = new BigDecimal(validString);

        // Test parsing with standard parser
        assertEquals(expectedValue, BigDecimalParser.parse(validString), "Parsed value should match expected BigDecimal");
        assertEquals(expectedValue, BigDecimalParser.parse(validString.toCharArray(), 0, validString.length()), "Parsed char[] value should match expected BigDecimal");

        // Test parsing with fast parser
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(validString), "Parsed value with fast parser should match expected BigDecimal");
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(validString.toCharArray(), 0, validString.length()), "Parsed char[] value with fast parser should match expected BigDecimal");
    }

    @Test
    void testIssueDatabind4694() {
        final String largeNumberString = "-11000." + "0".repeat(1000) + "0";
        BigDecimal expectedValue = new BigDecimal(largeNumberString);

        // Test parsing with JavaBigDecimalParser
        assertEquals(expectedValue, JavaBigDecimalParser.parseBigDecimal(largeNumberString), "Parsed value with JavaBigDecimalParser should match expected BigDecimal");

        // Test parsing with standard parser
        assertEquals(expectedValue, BigDecimalParser.parse(largeNumberString), "Parsed value should match expected BigDecimal");
        assertEquals(expectedValue, BigDecimalParser.parse(largeNumberString.toCharArray(), 0, largeNumberString.length()), "Parsed char[] value should match expected BigDecimal");

        // Test parsing with fast parser
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(largeNumberString), "Parsed value with fast parser should match expected BigDecimal");
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(largeNumberString.toCharArray(), 0, largeNumberString.length()), "Parsed char[] value with fast parser should match expected BigDecimal");
    }

    /**
     * Generates a long invalid string for testing purposes.
     * The string consists of 1500 'A' characters.
     *
     * @return a long invalid string
     */
    static String generateLongInvalidString() {
        return "A".repeat(1500);
    }

    /**
     * Generates a long valid string for testing purposes.
     * The string is a valid BigDecimal representation with a specified number of zeros.
     *
     * @param length the number of zeros in the decimal part
     * @return a long valid string
     */
    static String generateLongValidString(int length) {
        return "0." + "0".repeat(length) + "1";
    }
}