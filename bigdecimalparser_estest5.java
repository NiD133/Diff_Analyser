package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;

/**
 * Test suite for {@link BigDecimalParser}.
 *
 * Note: The original class name 'BigDecimalParser_ESTestTest5' and its scaffolding
 * are artifacts from an automated test generation tool. For clarity, the boilerplate
 * has been removed in this improved version.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that the parser can correctly handle a number in scientific E-notation.
     */
    @Test
    public void parseShouldCorrectlyHandleScientificNotation() {
        // Arrange
        // The input "2E2" represents the number 2 * 10^2, which equals 200.
        char[] numberChars = "2E2".toCharArray();
        BigDecimal expected = new BigDecimal("2E2");

        // Act
        BigDecimal result = BigDecimalParser.parse(numberChars);

        // Assert
        // The assertion now directly compares the parsed result with an expected
        // BigDecimal instance. This is clear and directly tests the parser's behavior.
        //
        // The original assertion, `assertEquals((byte) -56, result.byteValue())`,
        // was confusing because it tested an obscure side effect of byte overflow
        // (200 cast to a byte is -56) rather than the direct parsing result.
        assertEquals(expected, result);
    }
}