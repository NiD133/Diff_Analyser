package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void removeAtIndex_returnsTheRemovedElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        Character characterToAdd = 'a';
        jsonArray.add(characterToAdd);

        JsonElement expectedElement = new JsonPrimitive(characterToAdd);

        // Act
        JsonElement removedElement = jsonArray.remove(0);

        // Assert
        assertEquals("The removed element should be the one that was added.", expectedElement, removedElement);
        assertTrue("The array should be empty after removing its only element.", jsonArray.isEmpty());
    }
}