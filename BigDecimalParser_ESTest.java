package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeTrue;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * Unit tests for BigDecimalParser with a focus on clarity and maintainability.
 *
 * Notes:
 * - These tests avoid EvoSuite-specific scaffolding and runners.
 * - Tests that would require the optional FastDoubleParser dependency are conditionally
 *   executed only if that dependency is present on the classpath.
 * - Assertions use readable numeric expectations instead of relying on primitive casts.
 */
public class BigDecimalParserTest {

    private static final String FAST_PARSER_CLASS =
            "ch.randelshofer.fastdoubleparser.JavaBigDecimalParser";

    private static boolean isFastParserAvailable() {
        try {
            Class.forName(FAST_PARSER_CLASS);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private static void assertBigDecimalEquals(String expected, BigDecimal actual) {
        assertNotNull("parsed BigDecimal must not be null", actual);
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }

    // ---------- Happy-path parsing ----------

    @Test
    public void parse_string_simpleInteger() {
        BigDecimal result = BigDecimalParser.parse("8");
        assertBigDecimalEquals("8", result);
    }

    @Test
    public void parse_string_withExponent() {
        BigDecimal result = BigDecimalParser.parse("7e2");
        assertBigDecimalEquals("700", result);
    }

    @Test
    public void parse_string_fractionLeadingDot() {
        BigDecimal result = BigDecimalParser.parse(".1");
        assertBigDecimalEquals("0.1", result);
    }

    @Test
    public void parse_charArray_fullArray() {
        char[] chars = new char[] { '4' };
        BigDecimal result = BigDecimalParser.parse(chars);
        assertBigDecimalEquals("4", result);
    }

    @Test
    public void parse_charArray_withOffsetAndLength() {
        char[] chars = new char[] { 'x', '2', 'y' };
        BigDecimal result = BigDecimalParser.parse(chars, 1, 1);
        assertBigDecimalEquals("2", result);
    }

    @Test
    public void parse_charArray_negativeZero() {
        char[] chars = new char[] { '-', '0' };
        BigDecimal result = BigDecimalParser.parse(chars);
        assertBigDecimalEquals("0", result);
    }

    @Test
    public void parse_charArray_withExponent() {
        char[] chars = new char[] { '2', 'E', '2' };
        BigDecimal result = BigDecimalParser.parse(chars);
        assertBigDecimalEquals("200", result);
    }

    // ---------- Input validation / error cases ----------

    @Test
    public void parse_string_invalidNumber_throwsNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> BigDecimalParser.parse("eA8ojpN"));
    }

    @Test
    public void parse_string_empty_throwsNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> BigDecimalParser.parse(""));
    }

    @Test
    public void parse_charArray_emptySlice_throwsNumberFormatException() {
        // An empty slice (len = 0) should be considered invalid.
        char[] chars = new char[] { '0' };
        assertThrows(NumberFormatException.class, () -> BigDecimalParser.parse(chars, 0, 0));
    }

    @Test
    public void parse_charArray_outOfBounds_throwsStringIndexOutOfBoundsException() {
        char[] empty = new char[0];
        assertThrows(StringIndexOutOfBoundsException.class, () -> BigDecimalParser.parse(empty, 265, 265));
    }

    @Test
    public void parse_charArray_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BigDecimalParser.parse((char[]) null));
    }

    @Test
    public void parse_charArray_nullWithRange_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BigDecimalParser.parse((char[]) null, 0, 1));
    }

    @Test
    public void parse_string_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BigDecimalParser.parse((String) null));
    }

    // ---------- Fast parser entry points (run only if dependency is present) ----------

    @Test
    public void parseWithFastParser_string_simpleInteger_whenFastParserAvailable() {
        assumeTrue("Fast parser not on classpath, skipping test", isFastParserAvailable());
        BigDecimal result = BigDecimalParser.parseWithFastParser("42");
        assertBigDecimalEquals("42", result);
    }

    @Test
    public void parseWithFastParser_charArray_whenFastParserAvailable() {
        assumeTrue("Fast parser not on classpath, skipping test", isFastParserAvailable());
        char[] chars = new char[] { '1', '2', '3' };
        BigDecimal result = BigDecimalParser.parseWithFastParser(chars, 0, 3);
        assertBigDecimalEquals("123", result);
    }

    @Test
    public void parse_veryLargeNumber_delegatesToFastParserWhenAvailable() {
        assumeTrue("Fast parser not on classpath, skipping test", isFastParserAvailable());

        // Build a 600-digit number. BigDecimal should parse it correctly.
        StringBuilder sb = new StringBuilder(600);
        for (int i = 0; i < 600; i++) {
            sb.append('7');
        }
        String large = sb.toString();

        BigDecimal result = BigDecimalParser.parse(large);
        assertBigDecimalEquals(large, result);
    }
}