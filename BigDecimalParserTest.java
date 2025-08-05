package com.fasterxml.jackson.core.io;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BigDecimalParser}. This suite uses parameterized tests to verify the behavior
 * of both the default {@code parse} method and the {@code parseWithFastParser} variant,
 * reducing code duplication and improving maintainability.
 */
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    private static final int LONG_INVALID_STRING_LENGTH = 1500;
    private static final int LONG_VALID_STRING_LENGTH = 500;

    // Defines a functional interface for methods that parse a char array segment into a BigDecimal.
    // This improves readability in the parameterized test setup.
    @FunctionalInterface
    private interface CharArrayParser {
        BigDecimal parse(char[] chars, int offset, int length);
    }

    // Provides the different string parsing methods to be tested.
    private static Stream<Arguments> stringParsers() {
        return Stream.of(
                Arguments.of(Named.of("BigDecimalParser::parse", (Function<String, BigDecimal>) BigDecimalParser::parse)),
                Arguments.of(Named.of("BigDecimalParser::parseWithFastParser", BigDecimalParser::parseWithFastParser))
        );
    }

    // Provides the different char array parsing methods to be tested.
    private static Stream<Arguments> charArrayParsers() {
        return Stream.of(
                Arguments.of(Named.of("BigDecimalParser::parse", (CharArrayParser) BigDecimalParser::parse)),
                Arguments.of(Named.of("BigDecimalParser::parseWithFastParser", BigDecimalParser::parseWithFastParser))
        );
    }

    @DisplayName("Parsing a long invalid string should throw NumberFormatException")
    @ParameterizedTest(name = "using {0}")
    @MethodSource("stringParsers")
    void parse_withLongInvalidString_shouldThrowNumberFormatException(Function<String, BigDecimal> parser) {
        // Arrange
        final String invalidString = genLongInvalidString();

        // Act & Assert
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> parser.apply(invalidString));

        // Assert on the details of the exception message
        String message = ex.getMessage();
        assertAll("Exception message validation",
                () -> assertTrue(message.startsWith("Value \"AAAAA"), "Exception message should start with the truncated value"),
                () -> assertTrue(message.contains("truncated"), "Exception message should indicate the value was truncated")
        );
    }

    @DisplayName("Parsing a long valid number from a String should succeed")
    @ParameterizedTest(name = "using {0}")
    @MethodSource("stringParsers")
    void parse_withLongValidString_shouldReturnCorrectBigDecimal(Function<String, BigDecimal> parser) {
        // Arrange
        String numStr = genLongValidString(LONG_VALID_STRING_LENGTH);
        final BigDecimal expected = new BigDecimal(numStr);

        // Act
        BigDecimal result = parser.apply(numStr);

        // Assert
        assertEquals(expected, result);
    }

    @DisplayName("Parsing a long valid number from a char[] should succeed")
    @ParameterizedTest(name = "using {0}")
    @MethodSource("charArrayParsers")
    void parse_withLongValidCharArray_shouldReturnCorrectBigDecimal(CharArrayParser parser) {
        // Arrange
        String numStr = genLongValidString(LONG_VALID_STRING_LENGTH);
        char[] numChars = numStr.toCharArray();
        final BigDecimal expected = new BigDecimal(numStr);

        // Act
        BigDecimal result = parser.parse(numChars, 0, numChars.length);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Regression test for a long negative number (databind#4694)")
    void parse_withLongNegativeValueFromIssue_shouldReturnCorrectBigDecimal() {
        // This value is from a regression test for issue jackson-databind#4694
        final String longNumberStr = "-11000.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000-11000.000...0";
        final BigDecimal expected = new BigDecimal(longNumberStr);
        final char[] longNumberChars = longNumberStr.toCharArray();

        assertAll("Verify all parser variants for a specific long negative number",
                // Baseline check with the third-party parser to ensure correctness
                () -> assertEquals(expected, JavaBigDecimalParser.parseBigDecimal(longNumberStr),
                        "Baseline FastDoubleParser should match"),

                // Test BigDecimalParser with String input
                () -> assertEquals(expected, BigDecimalParser.parse(longNumberStr),
                        "parse(String) should match"),
                () -> assertEquals(expected, BigDecimalParser.parseWithFastParser(longNumberStr),
                        "parseWithFastParser(String) should match"),

                // Test BigDecimalParser with char[] input, including all overloads
                () -> assertEquals(expected, BigDecimalParser.parse(longNumberChars),
                        "parse(char[]) overload should match"),
                () -> assertEquals(expected, BigDecimalParser.parse(longNumberChars, 0, longNumberChars.length),
                        "parse(char[], int, int) should match"),
                () -> assertEquals(expected, BigDecimalParser.parseWithFastParser(longNumberChars, 0, longNumberChars.length),
                        "parseWithFastParser(char[], int, int) should match")
        );
    }

    // Helper methods for generating test data

    private String genLongInvalidString() {
        return "A".repeat(LONG_INVALID_STRING_LENGTH);
    }

    private String genLongValidString(int len) {
        return "0." + "0".repeat(len) + "1";
    }
}