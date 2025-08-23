package com.fasterxml.jackson.core.io;

import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link BigDecimalParser} class, focusing on specific edge cases.
 */
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    @DisplayName("Should correctly parse a very long decimal string (regression for databind#4694)")
    void shouldParseVeryLongDecimalString_databind4694() {
        // This specific long number string caused issues, as reported in databind#4694.
        // It is used here to ensure all parsing methods behave consistently and correctly.
        final String longDecimalString = "-11000.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000-";
        final char[] longDecimalChars = longDecimalString.toCharArray();

        // The expected value is derived from the standard Java BigDecimal constructor,
        // which serves as the ground truth for this test.
        final BigDecimal expected = new BigDecimal(longDecimalString);

        // All parsing methods should produce the same, correct BigDecimal instance.
        // We also verify against the reference FastDoubleParser implementation to ensure correctness.
        assertAll("Verify all BigDecimalParser variants for a long decimal string",
            () -> assertEquals(expected, JavaBigDecimalParser.parseBigDecimal(longDecimalString),
                "Verification against reference FastDoubleParser failed."),
            () -> assertEquals(expected, BigDecimalParser.parse(longDecimalString),
                "BigDecimalParser.parse(String) failed."),
            () -> assertEquals(expected, BigDecimalParser.parseWithFastParser(longDecimalString),
                "BigDecimalParser.parseWithFastParser(String) failed."),
            () -> assertEquals(expected, BigDecimalParser.parse(longDecimalChars, 0, longDecimalChars.length),
                "BigDecimalParser.parse(char[]) failed."),
            () -> assertEquals(expected, BigDecimalParser.parseWithFastParser(longDecimalChars, 0, longDecimalChars.length),
                "BigDecimalParser.parseWithFastParser(char[]) failed.")
        );
    }
}