package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 * Unit tests for {@link BigDecimalParser} to verify its correctness and robustness
 * in parsing string and char array representations of {@link BigDecimal} values.
 */
public class BigDecimalParserTest {

    // --- Tests for parse(String) ---

    @Test
    public void parse_stringAsSimpleInteger_shouldReturnCorrectBigDecimal() {
        // Given
        String input = "8";
        BigDecimal expected = new BigDecimal("8");

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_stringWithLeadingDecimalPoint_shouldReturnCorrectBigDecimal() {
        // Given
        String input = ".1";
        BigDecimal expected = new BigDecimal("0.1");

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_stringWithExponent_shouldReturnCorrectBigDecimal() {
        // Given
        String input = "7e2";
        BigDecimal expected = new BigDecimal("700");

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_longNumericString_shouldParseCorrectly() {
        // This test covers the code path that delegates to the fastdoubleparser library
        // for strings longer than 500 characters, assuming it's on the classpath.
        // Given
        String longNumberStr = "1234567890".repeat(51) + ".123"; // 513 chars
        assertTrue("Test setup: long number string must be > 500 chars", longNumberStr.length() > 500);
        BigDecimal expected = new BigDecimal(longNumberStr);

        // When
        BigDecimal actual = BigDecimalParser.parse(longNumberStr);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_invalidString_shouldThrowNumberFormatException() {
        // Given
        String invalidInput = "eA8ojpN";

        // When & Then
        try {
            BigDecimalParser.parse(invalidInput);
            fail("Expected a NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Verify that the exception message is informative
            assertTrue("Exception message should indicate the reason for failure.",
                    e.getMessage().contains("Not a valid number representation"));
            assertTrue("Exception message should contain the invalid value.",
                    e.getMessage().contains(invalidInput));
        }
    }

    @Test(expected = NullPointerException.class)
    public void parse_nullString_shouldThrowNullPointerException() {
        // When
        BigDecimalParser.parse((String) null);
        // Then: expects NullPointerException
    }


    // --- Tests for parse(char[]) ---

    @Test
    public void parse_fullCharArray_shouldReturnCorrectBigDecimal() {
        // Given
        char[] input = "4".toCharArray();
        BigDecimal expected = new BigDecimal("4");

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_charArrayWithNegativeZero_shouldReturnZero() {
        // Given
        char[] input = "-0".toCharArray();

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        // Use compareTo to check for numerical equality, ignoring scale differences.
        assertEquals(0, actual.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void parse_charArrayWithExponent_shouldReturnCorrectBigDecimal() {
        // Given
        char[] input = "2E2".toCharArray();
        BigDecimal expected = new BigDecimal("200");

        // When
        BigDecimal actual = BigDecimalParser.parse(input);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_invalidCharArray_shouldThrowNumberFormatException() {
        // Given
        char[] invalidInput = new char[]{'\0', '\0', '\0'};

        // When & Then
        try {
            BigDecimalParser.parse(invalidInput);
            fail("Expected a NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Verify that the exception message is informative
            assertTrue("Exception message should indicate the reason for failure.",
                    e.getMessage().contains("Not a valid number representation"));
        }
    }

    @Test(expected = NullPointerException.class)
    public void parse_nullCharArray_shouldThrowNullPointerException() {
        // When
        BigDecimalParser.parse((char[]) null);
        // Then: expects NullPointerException
    }


    // --- Tests for parse(char[], int, int) ---

    @Test
    public void parse_charArraySegment_shouldReturnCorrectBigDecimal() {
        // Given
        char[] input = "---2---".toCharArray();
        int offset = 3;
        int length = 1;
        BigDecimal expected = new BigDecimal("2");

        // When
        BigDecimal actual = BigDecimalParser.parse(input, offset, length);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_longCharArraySegment_shouldParseCorrectly() {
        // This test covers the code path that delegates to the fastdoubleparser library
        // for character arrays longer than 500 characters, assuming it's on the classpath.
        // Given
        String longNumberStr = "9876543210".repeat(51); // 510 chars
        char[] input = ("prefix" + longNumberStr + "suffix").toCharArray();
        int offset = "prefix".length();
        int length = longNumberStr.length();
        assertTrue("Test setup: segment length must be > 500 chars", length > 500);
        BigDecimal expected = new BigDecimal(longNumberStr);

        // When
        BigDecimal actual = BigDecimalParser.parse(input, offset, length);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void parse_emptyCharArraySegment_shouldThrowNumberFormatException() {
        // Given
        char[] input = "123".toCharArray();
        int offset = 1;
        int length = 0;

        // When & Then
        try {
            BigDecimalParser.parse(input, offset, length);
            fail("Expected a NumberFormatException to be thrown for an empty segment");
        } catch (NumberFormatException e) {
            // Verify that the exception message is informative
            assertTrue(e.getMessage().contains("Not a valid number representation"));
            assertTrue(e.getMessage().contains("Value \"\""));
        }
    }

    @Test(expected = NullPointerException.class)
    public void parse_nullCharArraySegment_shouldThrowNullPointerException() {
        // When
        BigDecimalParser.parse(null, 0, 1);
        // Then: expects NullPointerException
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void parse_charArraySegmentWithInvalidLength_shouldThrowException() {
        // Given
        char[] input = new char[5];
        int offset = 0;
        int length = 10; // length is out of bounds

        // When
        BigDecimalParser.parse(input, offset, length);
        // Then: expects StringIndexOutOfBoundsException
    }
    
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void parse_charArraySegmentWithNegativeOffset_shouldThrowException() {
        // Given
        char[] input = new char[5];
        int offset = -1;
        int length = 2;

        // When
        BigDecimalParser.parse(input, offset, length);
        // Then: expects StringIndexOutOfBoundsException
    }
}