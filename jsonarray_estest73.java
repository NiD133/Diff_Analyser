package com.google.gson;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Contains tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling addAll() on an empty JsonArray with itself as the argument
     * results in the array remaining empty. This tests a specific edge case of
     * self-modification.
     */
    @Test
    public void addAll_onEmptyArrayWithSelfAsArgument_shouldRemainEmpty() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();

        // Act: Add all elements of the array to itself.
        emptyArray.addAll(emptyArray);

        // Assert: The array should still be empty after the operation.
        assertTrue("The array should remain empty after adding itself to it.", emptyArray.isEmpty());
    }
}