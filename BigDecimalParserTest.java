package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Test method for parsing an invalid long string using the standard parser
    @Test
    void testParseInvalidLongString() {
        String invalidString = generateInvalidString();
        assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parse(invalidString);
        }, "Expected NumberFormatException for invalid input");
    }

    // Test method for parsing an invalid long string using the fast parser
    @Test
    void testFastParseInvalidLongString() {
        String invalidString = generateInvalidString();
        assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parseWithFastParser(invalidString);
        }, "Expected NumberFormatException for invalid input");
    }

    // Test method for parsing a valid long string using the standard parser
    @Test
    void testParseValidLongString() {
        String validString = generateValidString(500);
        BigDecimal expectedValue = new BigDecimal(validString);

        // Test parsing from String
        assertEquals(expectedValue, BigDecimalParser.parse(validString), "Parsed value should match expected");

        // Test parsing from char array
        assertEquals(expectedValue, BigDecimalParser.parse(validString.toCharArray(), 0, validString.length()), "Parsed value should match expected");
    }

    // Test method for parsing a valid long string using the fast parser
    @Test
    void testFastParseValidLongString() {
        String validString = generateValidString(500);
        BigDecimal expectedValue = new BigDecimal(validString);

        // Test parsing from String
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(validString), "Parsed value should match expected");

        // Test parsing from char array
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(validString.toCharArray(), 0, validString.length()), "Parsed value should match expected");
    }

    // Test method for a specific issue case
    @Test
    void testIssueDatabind4694() {
        final String largeNumberString = "-11000." + "0".repeat(1000) + "0";
        BigDecimal expectedValue = new BigDecimal(largeNumberString);

        // Test parsing with different methods
        assertEquals(expectedValue, JavaBigDecimalParser.parseBigDecimal(largeNumberString), "Parsed value should match expected");
        assertEquals(expectedValue, BigDecimalParser.parse(largeNumberString), "Parsed value should match expected");
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(largeNumberString), "Parsed value should match expected");

        char[] charArray = largeNumberString.toCharArray();
        assertEquals(expectedValue, BigDecimalParser.parse(charArray, 0, charArray.length), "Parsed value should match expected");
        assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(charArray, 0, charArray.length), "Parsed value should match expected");
    }

    // Helper method to generate an invalid string of length 1500
    static String generateInvalidString() {
        return "A".repeat(1500);
    }

    // Helper method to generate a valid decimal string of specified length
    static String generateValidString(int length) {
        return "0." + "0".repeat(length) + "1";
    }
}