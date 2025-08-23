package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
// The original test class name and inheritance are kept for context.
// In a real-world scenario, these would likely be simplified.
public class BigDecimalParser_ESTestTest3 extends BigDecimalParser_ESTest_scaffolding {

    /**
     * Verifies that parsing a character array representing negative zero ("-0")
     * correctly results in a BigDecimal value of ZERO.
     */
    @Test
    public void parse_whenGivenNegativeZeroChars_shouldReturnZero() {
        // Arrange: Create the input character array for "-0"
        char[] numberChars = "-0".toCharArray();
        BigDecimal expectedValue = BigDecimal.ZERO;

        // Act: Parse the character array into a BigDecimal
        BigDecimal actualValue = BigDecimalParser.parse(numberChars);

        // Assert: The parsed value should be exactly equal to BigDecimal.ZERO.
        // A direct comparison is more precise than checking a primitive conversion
        // like shortValue(), as it also implicitly verifies the scale.
        assertEquals(expectedValue, actualValue);
    }
}