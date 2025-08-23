package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that the equals() method returns false when a LazilyParsedNumber
     * is compared to an object of a different type (e.g., a String), even if
     * the string's content matches the internal value of the number.
     */
    @Test
    public void equals_returnsFalse_whenComparedWithDifferentType() {
        // Arrange: Create a LazilyParsedNumber and a String with the same content.
        String value = "d<Hh";
        LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber(value);
        Object objectOfDifferentType = value;

        // Act & Assert: The equals method should return false due to the type mismatch,
        // as per the general contract of Object.equals().
        assertFalse(lazilyParsedNumber.equals(objectOfDifferentType));
    }
}