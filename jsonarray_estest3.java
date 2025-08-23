package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void remove_whenElementExists_returnsTrueAndRemovesElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        // Add the array as an element to itself to test removal by object reference.
        jsonArray.add(jsonArray);

        // Act
        boolean wasRemoved = jsonArray.remove(jsonArray);

        // Assert
        assertTrue("The remove operation should return true to indicate success.", wasRemoved);
        assertEquals("The array should be empty after removing its only element.", 0, jsonArray.size());
    }
}