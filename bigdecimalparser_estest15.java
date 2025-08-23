package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that {@link BigDecimalParser#parse(String)} correctly handles
     * a numeric string that starts with a decimal point.
     */
    @Test
    public void parseShouldHandleNumberWithLeadingDecimalPoint() {
        // Arrange
        String numberAsString = ".1";
        BigDecimal expectedValue = new BigDecimal("0.1");

        // Act
        BigDecimal actualValue = BigDecimalParser.parse(numberAsString);

        // Assert
        // We use assertEquals directly, which for BigDecimal compares both value and scale.
        // This is a more precise and clearer assertion than the original test's byteValue comparison.
        assertEquals(expectedValue, actualValue);
    }
}