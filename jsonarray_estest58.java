package com.google.gson;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on the {@code set} method.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling {@code set()} with a negative index throws an
     * {@link IndexOutOfBoundsException}, as the index is out of the array's bounds.
     */
    @Test
    public void set_withNegativeIndex_throwsIndexOutOfBoundsException() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        JsonElement elementToSet = new JsonPrimitive("any-value");

        // Act & Assert
        // The set method is expected to throw an exception when the index is invalid.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            jsonArray.set(-1, elementToSet);
        });
    }
}