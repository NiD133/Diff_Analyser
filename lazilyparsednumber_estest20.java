package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests that two LazilyParsedNumber instances are considered equal
     * if they are constructed with the same string value.
     */
    @Test
    public void equals_whenObjectsHaveSameStringValue_returnsTrue() {
        // Arrange
        String numericString = "123.45";
        LazilyParsedNumber number1 = new LazilyParsedNumber(numericString);
        LazilyParsedNumber number2 = new LazilyParsedNumber(numericString);

        // Act & Assert
        // The equals() method should return true because the underlying string values are identical.
        assertEquals(number1, number2);
        // Also verify the hashCode contract
        assertEquals(number1.hashCode(), number2.hashCode());
    }

    /**
     * Tests that two LazilyParsedNumber instances are not considered equal
     * if they are constructed with different string values.
     */
    @Test
    public void equals_whenObjectsHaveDifferentStringValue_returnsFalse() {
        // Arrange
        LazilyParsedNumber number1 = new LazilyParsedNumber("123.45");
        LazilyParsedNumber number2 = new LazilyParsedNumber("678.90");

        // Act & Assert
        assertNotEquals(number1, number2);
    }
}