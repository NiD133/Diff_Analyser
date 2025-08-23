package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
public class BigDecimalParserTest {

    @Test
    public void parse_whenGivenSingleDigitInCharArray_returnsCorrectBigDecimal() {
        // Arrange
        char[] numberAsChars = new char[]{'4'};
        BigDecimal expected = new BigDecimal("4");

        // Act
        BigDecimal result = BigDecimalParser.parse(numberAsChars);

        // Assert
        assertEquals(expected, result);
    }
}