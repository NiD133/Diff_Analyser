package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that the floatValue() method correctly converts a string
     * representation of a negative integer into its float equivalent.
     */
    @Test
    public void floatValue_whenInputIsNegativeInteger_returnsCorrectFloat() {
        // Arrange
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        float expected = -6.0F;

        // Act
        float actual = number.floatValue();

        // Assert
        // A delta of 0.0F is used because the conversion from "-6" should be exact.
        assertEquals(expected, actual, 0.0F);
    }
}