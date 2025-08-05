package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BigDecimalParser Tests")
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    
    // Test data constants
    private static final int LONG_VALID_STRING_LENGTH = 500;
    private static final int LONG_INVALID_STRING_LENGTH = 1500;
    private static final String INVALID_STRING_CHAR = "A";
    
    // Expected error message patterns
    private static final String EXPECTED_ERROR_PREFIX = "Value \"AAAAA";
    private static final String EXPECTED_ERROR_CONTAINS = "truncated";
    
    @Nested
    @DisplayName("Invalid Input Handling")
    class InvalidInputTests {
        
        @Test
        @DisplayName("Should throw NumberFormatException with descriptive message when parsing long invalid string")
        void shouldThrowNumberFormatExceptionWhenParsingLongInvalidString() {
            // Given
            String invalidInput = generateStringWithRepeatedCharacter(INVALID_STRING_CHAR, LONG_INVALID_STRING_LENGTH);
            
            // When & Then
            NumberFormatException exception = assertThrows(
                NumberFormatException.class,
                () -> BigDecimalParser.parse(invalidInput),
                "Expected NumberFormatException when parsing invalid string"
            );
            
            assertExceptionMessageFormat(exception);
        }
        
        @Test
        @DisplayName("Should throw NumberFormatException with descriptive message when fast parsing long invalid string")
        void shouldThrowNumberFormatExceptionWhenFastParsingLongInvalidString() {
            // Given
            String invalidInput = generateStringWithRepeatedCharacter(INVALID_STRING_CHAR, LONG_INVALID_STRING_LENGTH);
            
            // When & Then
            NumberFormatException exception = assertThrows(
                NumberFormatException.class,
                () -> BigDecimalParser.parseWithFastParser(invalidInput),
                "Expected NumberFormatException when fast parsing invalid string"
            );
            
            assertExceptionMessageFormat(exception);
        }
        
        private void assertExceptionMessageFormat(NumberFormatException exception) {
            String message = exception.getMessage();
            assertTrue(
                message.startsWith(EXPECTED_ERROR_PREFIX),
                String.format("Exception message should start with '%s', but was: %s", EXPECTED_ERROR_PREFIX, message)
            );
            assertTrue(
                message.contains(EXPECTED_ERROR_CONTAINS),
                String.format("Exception message should contain '%s', but was: %s", EXPECTED_ERROR_CONTAINS, message)
            );
        }
    }
    
    @Nested
    @DisplayName("Valid Input Parsing")
    class ValidInputTests {
        
        @Test
        @DisplayName("Should correctly parse long valid decimal string using standard parser")
        void shouldParseLongValidDecimalStringWithStandardParser() {
            // Given
            String validDecimalNumber = generateLongDecimalString(LONG_VALID_STRING_LENGTH);
            BigDecimal expectedValue = new BigDecimal(validDecimalNumber);
            
            // When & Then - Test both String and char[] parsing
            assertEquals(expectedValue, BigDecimalParser.parse(validDecimalNumber),
                "String parsing should return correct BigDecimal");
            
            char[] charArray = validDecimalNumber.toCharArray();
            assertEquals(expectedValue, BigDecimalParser.parse(charArray, 0, charArray.length),
                "Char array parsing should return correct BigDecimal");
        }
        
        @Test
        @DisplayName("Should correctly parse long valid decimal string using fast parser")
        void shouldParseLongValidDecimalStringWithFastParser() {
            // Given
            String validDecimalNumber = generateLongDecimalString(LONG_VALID_STRING_LENGTH);
            BigDecimal expectedValue = new BigDecimal(validDecimalNumber);
            
            // When & Then - Test both String and char[] parsing
            assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(validDecimalNumber),
                "Fast string parsing should return correct BigDecimal");
            
            char[] charArray = validDecimalNumber.toCharArray();
            assertEquals(expectedValue, BigDecimalParser.parseWithFastParser(charArray, 0, charArray.length),
                "Fast char array parsing should return correct BigDecimal");
        }
    }
    
    @Nested
    @DisplayName("Regression Tests")
    class RegressionTests {
        
        @Test
        @DisplayName("Should correctly parse very long decimal with many trailing zeros (databind issue #4694)")
        void shouldParseVeryLongDecimalWithTrailingZeros_databind4694() {
            // Given - A negative number with 650+ trailing zeros after decimal point
            final String veryLongDecimalString = "-11000." + "0".repeat(650);
            final BigDecimal expectedValue = new BigDecimal(veryLongDecimalString);
            
            // When & Then - Verify all parsing methods produce correct result
            assertAllParsingMethodsReturnExpectedValue(veryLongDecimalString, expectedValue);
        }
        
        private void assertAllParsingMethodsReturnExpectedValue(String input, BigDecimal expected) {
            // Test third-party parser
            assertEquals(expected, JavaBigDecimalParser.parseBigDecimal(input),
                "JavaBigDecimalParser should parse correctly");
            
            // Test standard parser with String
            assertEquals(expected, BigDecimalParser.parse(input),
                "Standard parser should parse string correctly");
            
            // Test fast parser with String
            assertEquals(expected, BigDecimalParser.parseWithFastParser(input),
                "Fast parser should parse string correctly");
            
            // Test parsers with char array
            char[] charArray = input.toCharArray();
            assertEquals(expected, BigDecimalParser.parse(charArray, 0, charArray.length),
                "Standard parser should parse char array correctly");
            assertEquals(expected, BigDecimalParser.parseWithFastParser(charArray, 0, charArray.length),
                "Fast parser should parse char array correctly");
        }
    }
    
    // Helper methods with descriptive names
    
    /**
     * Generates a string consisting of repeated characters.
     * Used to create invalid numeric input for testing error handling.
     */
    private static String generateStringWithRepeatedCharacter(String character, int length) {
        return character.repeat(length);
    }
    
    /**
     * Generates a valid decimal string in the format "0.000...001"
     * with the specified number of zeros between decimal point and the trailing 1.
     * This tests the parser's ability to handle very long but valid decimal numbers.
     */
    private static String generateLongDecimalString(int numberOfZerosAfterDecimal) {
        StringBuilder sb = new StringBuilder(numberOfZerosAfterDecimal + 5);
        sb.append("0.");
        sb.append("0".repeat(numberOfZerosAfterDecimal));
        sb.append("1");
        return sb.toString();
    }
}