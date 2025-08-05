package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    // Precomputed test data
    private static final String LONG_INVALID_STRING = generateLongInvalidString();
    private static final String LONG_VALID_STRING = generateLongValidString(500);
    private static final BigDecimal LONG_VALID_EXPECTED = new BigDecimal(LONG_VALID_STRING);
    private static final String ISSUE_DATABIND_4694_STRING = generateIssueDatabind4694String();
    private static final BigDecimal ISSUE_DATABIND_4694_EXPECTED = new BigDecimal(ISSUE_DATABIND_4694_STRING);

    // Tests for invalid inputs ================================================

    @Test
    void longInvalidStringParse() {
        NumberFormatException nfe = assertThrows(
            NumberFormatException.class,
            () -> BigDecimalParser.parse(LONG_INVALID_STRING),
            "Should throw NumberFormatException for invalid input"
        );
        verifyExceptionMessage(nfe);
    }

    @Test
    void longInvalidStringFastParse() {
        NumberFormatException nfe = assertThrows(
            NumberFormatException.class,
            () -> BigDecimalParser.parseWithFastParser(LONG_INVALID_STRING),
            "Should throw NumberFormatException for invalid input"
        );
        verifyExceptionMessage(nfe);
    }

    // Tests for valid inputs =================================================

    @Test
    void longValidStringParse() {
        // Test both String and char[] parsing methods
        assertEquals(LONG_VALID_EXPECTED, BigDecimalParser.parse(LONG_VALID_STRING));
        assertEquals(LONG_VALID_EXPECTED, 
            BigDecimalParser.parse(
                LONG_VALID_STRING.toCharArray(), 
                0, 
                LONG_VALID_STRING.length()
            )
        );
    }

    @Test
    void longValidStringFastParse() {
        // Test both String and char[] parsing methods
        assertEquals(LONG_VALID_EXPECTED, 
            BigDecimalParser.parseWithFastParser(LONG_VALID_STRING)
        );
        assertEquals(LONG_VALID_EXPECTED,
            BigDecimalParser.parseWithFastParser(
                LONG_VALID_STRING.toCharArray(), 
                0, 
                LONG_VALID_STRING.length()
            )
        );
    }

    // Regression test for specific issue =====================================
    @Test
    void issueDatabind4694() {
        // Verify all parsing methods handle extremely long decimals correctly
        assertEquals(ISSUE_DATABIND_4694_EXPECTED, 
            JavaBigDecimalParser.parseBigDecimal(ISSUE_DATABIND_4694_STRING)
        );
        assertEquals(ISSUE_DATABIND_4694_EXPECTED, 
            BigDecimalParser.parse(ISSUE_DATABIND_4694_STRING)
        );
        assertEquals(ISSUE_DATABIND_4694_EXPECTED,
            BigDecimalParser.parseWithFastParser(ISSUE_DATABIND_4694_STRING)
        );
        
        final char[] arr = ISSUE_DATABIND_4694_STRING.toCharArray();
        assertEquals(ISSUE_DATABIND_4694_EXPECTED, 
            BigDecimalParser.parse(arr, 0, arr.length)
        );
        assertEquals(ISSUE_DATABIND_4694_EXPECTED,
            BigDecimalParser.parseWithFastParser(arr, 0, arr.length)
        );
    }

    // Helper methods =========================================================

    /**
     * Verifies common properties of NumberFormatException messages
     */
    private void verifyExceptionMessage(NumberFormatException nfe) {
        assertTrue(nfe.getMessage().startsWith("Value \"AAAAA"),
            "Exception message should start with prefix");
        assertTrue(nfe.getMessage().contains("truncated"),
            "Exception message should indicate truncation");
    }

    /**
     * Generates an invalid numeric string of 1500 'A' characters
     */
    private static String generateLongInvalidString() {
        return "A".repeat(1500);
    }

    /**
     * Generates a valid decimal string in the format "0.000...01" with specified zeros
     */
    private static String generateLongValidString(int zeroCount) {
        return "0." + "0".repeat(zeroCount) + "1";
    }

    /**
     * Provides the decimal string from Jackson databind issue #4694
     */
    private static String generateIssueDatabind4694String() {
        return "-11000" + "0".repeat(1000);
    }
}