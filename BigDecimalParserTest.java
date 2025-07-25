package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    private static final int LONG_STRING_LENGTH = 1500;
    private static final int VALID_LONG_STRING_LENGTH = 500;

    @Test
    void parseLongInvalidStringShouldThrowNumberFormatException() {
        String invalidString = generateLongInvalidString();
        try {
            BigDecimalParser.parse(invalidString);
            fail("Expected NumberFormatException was not thrown.");
        } catch (NumberFormatException nfe) {
            String errorMessage = nfe.getMessage();
            assertNotNull(errorMessage, "Error message should not be null.");
            assertTrue(errorMessage.startsWith("Value \"AAAAA"), "Error message should start with 'Value \"AAAAA'.");
            assertTrue(errorMessage.contains("truncated"), "Error message should contain 'truncated'.");
        }
    }

    @Test
    void parseWithFastParserLongInvalidStringShouldThrowNumberFormatException() {
        String invalidString = generateLongInvalidString();
        try {
            BigDecimalParser.parseWithFastParser(invalidString);
            fail("Expected NumberFormatException was not thrown.");
        } catch (NumberFormatException nfe) {
            String errorMessage = nfe.getMessage();
            assertNotNull(errorMessage, "Error message should not be null.");
            assertTrue(errorMessage.startsWith("Value \"AAAAA"), "Error message should start with 'Value \"AAAAA'.");
            assertTrue(errorMessage.contains("truncated"), "Error message should contain 'truncated'.");
        }
    }

    @Test
    void parseLongValidStringShouldReturnCorrectBigDecimal() {
        String validNumberString = generateLongValidString(VALID_LONG_STRING_LENGTH);
        BigDecimal expectedBigDecimal = new BigDecimal(validNumberString);

        // Parse from String
        BigDecimal parsedFromString = BigDecimalParser.parse(validNumberString);
        assertEquals(expectedBigDecimal, parsedFromString, "Parsed BigDecimal from String should match expected value.");

        // Parse from char[]
        BigDecimal parsedFromCharArray = BigDecimalParser.parse(validNumberString.toCharArray(), 0, validNumberString.length());
        assertEquals(expectedBigDecimal, parsedFromCharArray, "Parsed BigDecimal from char[] should match expected value.");
    }

    @Test
    void parseWithFastParserLongValidStringShouldReturnCorrectBigDecimal() {
        String validNumberString = generateLongValidString(VALID_LONG_STRING_LENGTH);
        BigDecimal expectedBigDecimal = new BigDecimal(validNumberString);

        // Parse from String
        BigDecimal parsedFromString = BigDecimalParser.parseWithFastParser(validNumberString);
        assertEquals(expectedBigDecimal, parsedFromString, "Parsed BigDecimal from String (using FastParser) should match expected value.");

        // Parse from char[]
        BigDecimal parsedFromCharArray = BigDecimalParser.parseWithFastParser(validNumberString.toCharArray(), 0, validNumberString.length());
        assertEquals(expectedBigDecimal, parsedFromCharArray, "Parsed BigDecimal from char[] (using FastParser) should match expected value.");
    }

    @Test
    void issueDatabind4694_shouldParseCorrectly() {
        final String inputString = "-11000.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        final BigDecimal expectedBigDecimal = new BigDecimal(inputString);

        assertEquals(expectedBigDecimal, JavaBigDecimalParser.parseBigDecimal(inputString), "JavaBigDecimalParser should parse correctly.");
        assertEquals(expectedBigDecimal, BigDecimalParser.parse(inputString), "BigDecimalParser.parse(String) should parse correctly.");
        assertEquals(expectedBigDecimal, BigDecimalParser.parseWithFastParser(inputString), "BigDecimalParser.parseWithFastParser(String) should parse correctly.");

        final char[] charArray = inputString.toCharArray();
        assertEquals(expectedBigDecimal, BigDecimalParser.parse(charArray, 0, charArray.length), "BigDecimalParser.parse(char[], int, int) should parse correctly.");
        assertEquals(expectedBigDecimal, BigDecimalParser.parseWithFastParser(charArray, 0, charArray.length), "BigDecimalParser.parseWithFastParser(char[], int, int) should parse correctly.");
    }

    private static String generateLongInvalidString() {
        StringBuilder stringBuilder = new StringBuilder(LONG_STRING_LENGTH);
        for (int i = 0; i < LONG_STRING_LENGTH; i++) {
            stringBuilder.append("A");
        }
        return stringBuilder.toString();
    }

    private static String generateLongValidString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length + 5);
        stringBuilder.append("0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append('0');
        }
        stringBuilder.append('1');
        return stringBuilder.toString();
    }
}