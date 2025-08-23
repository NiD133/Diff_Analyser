package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling {@link JsonArray#addAll(JsonArray)} with a null argument
     * throws a {@link NullPointerException}. This is the expected behavior for
     * collection-like methods that do not explicitly handle null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void addAll_withNullArgument_shouldThrowNullPointerException() {
        // Arrange
        JsonArray jsonArray = new JsonArray();

        // Act
        // The @Test annotation asserts that a NullPointerException is thrown by this call.
        jsonArray.addAll(null);
    }
}