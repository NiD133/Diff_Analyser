package com.google.gson.internal;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests that the toString() method returns the underlying string value.
     * In this specific case, it verifies that constructing the object with a null
     * value results in toString() returning null.
     */
    @Test
    public void toString_shouldReturnNull_whenConstructedWithNull() {
        // Arrange
        // Note: The constructor's Javadoc states that the value must not be null.
        // This test verifies the behavior when this contract is violated.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act
        String result = number.toString();

        // Assert
        assertNull("The string representation should be null when the object is constructed with null.", result);
    }
}