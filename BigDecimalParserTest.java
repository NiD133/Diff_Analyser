package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import java.util.stream.Stream;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BigDecimalParser that focus on understandability and reduce duplication.
 * The suite validates behavior for both implementations:
 * - parse(...)               (standard)
 * - parseWithFastParser(...) (fast path for long inputs)
 */
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Lengths chosen to exercise the "very long" code paths in the parser
    private static final int INVALID_ALPHA_LENGTH = 1500;
    private static final int VALID_FRACTION_ZEROS = 500;

    // Functional adapters so the same tests can run against both parser variants.
    @FunctionalInterface
    interface StringToBigDecimal {
        BigDecimal apply(String s);
    }

    @FunctionalInterface
    interface CharArrayToBigDecimal {
        BigDecimal apply(char[] arr, int off, int len);
    }

    static Stream<Arguments> parsers() {
        return Stream.of(
                Arguments.of(
                        "standard parse(...)",
                        (StringToBigDecimal) BigDecimalParser::parse,
                        (CharArrayToBigDecimal) BigDecimalParser::parse
                ),
                Arguments.of(
                        "fast parseWithFastParser(...)",
                        (StringToBigDecimal) BigDecimalParser::parseWithFastParser,
                        (CharArrayToBigDecimal) BigDecimalParser::parseWithFastParser
                )
        );
    }

    @ParameterizedTest(name = "{0} rejects very long invalid input")
    @MethodSource("parsers")
    void rejectsLongInvalidString(String name,
                                  StringToBigDecimal stringParser,
                                  CharArrayToBigDecimal arrayParser) {
        String invalid = generateLongInvalidAlphaString();

        NumberFormatException nfe1 = assertThrows(NumberFormatException.class, () -> stringParser.apply(invalid));
        assertInvalidMessageForAlphaValue(nfe1);

        char[] arr = invalid.toCharArray();
        NumberFormatException nfe2 = assertThrows(NumberFormatException.class,
                () -> arrayParser.apply(arr, 0, arr.length));
        assertInvalidMessageForAlphaValue(nfe2);
    }

    @ParameterizedTest(name = "{0} parses long valid decimal")
    @MethodSource("parsers")
    void parsesLongValidString(String name,
                               StringToBigDecimal stringParser,
                               CharArrayToBigDecimal arrayParser) {
        String num = generateLongValidDecimal(VALID_FRACTION_ZEROS);
        BigDecimal expected = new BigDecimal(num);

        assertEquals(expected, stringParser.apply(num), "String parsing mismatch");

        char[] arr = num.toCharArray();
        assertEquals(expected, arrayParser.apply(arr, 0, arr.length), "char[] parsing mismatch");
    }

    @Test
    void issueDatabind4694() {
        final String str = "-11000.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        final BigDecimal expected = new BigDecimal(str);

        // Baseline check with reference implementation
        assertEquals(expected, JavaBigDecimalParser.parseBigDecimal(str));

        // Standard parser
        assertEquals(expected, BigDecimalParser.parse(str));
        char[] arr = str.toCharArray();
        assertEquals(expected, BigDecimalParser.parse(arr, 0, arr.length));

        // Fast parser
        assertEquals(expected, BigDecimalParser.parseWithFastParser(str));
        assertEquals(expected, BigDecimalParser.parseWithFastParser(arr, 0, arr.length));
    }

    // Helpers

    private static void assertInvalidMessageForAlphaValue(NumberFormatException nfe) {
        String msg = nfe.getMessage();
        assertNotNull(msg, "exception message should not be null");
        assertTrue(msg.startsWith("Value \"AAAAA"), "message should start with the truncated offending value");
        assertTrue(msg.contains("truncated"), "message should indicate that the value was truncated");
    }

    /**
     * Produces an invalid numeric string of letters 'A' with large length to force error handling paths.
     */
    static String generateLongInvalidAlphaString() {
        final StringBuilder sb = new StringBuilder(INVALID_ALPHA_LENGTH);
        for (int i = 0; i < INVALID_ALPHA_LENGTH; i++) {
            sb.append('A');
        }
        return sb.toString();
    }

    /**
     * Produces a valid decimal of the form "0.<zeros>1", which is long enough to select the optimized path.
     * Example for len=3 -> "0.0001"
     */
    static String generateLongValidDecimal(int fractionZeroCount) {
        final StringBuilder sb = new StringBuilder(fractionZeroCount + 5);
        sb.append("0.");
        for (int i = 0; i < fractionZeroCount; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }
}