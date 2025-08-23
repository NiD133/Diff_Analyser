package com.google.gson;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class, focusing on the equals method.
 */
public class JsonArrayTest {

    /**
     * Tests that a JsonArray instance is not equal to null,
     * which is a standard contract for the Object.equals() method.
     */
    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange: Create an empty JsonArray instance.
        JsonArray jsonArray = new JsonArray();

        // Act & Assert: Verify that comparing the array to null returns false.
        assertFalse("A JsonArray instance should not be equal to null.", jsonArray.equals(null));
    }
}