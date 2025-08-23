package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonArrayTest {

    @Test
    public void setShouldReturnThePreviouslyStoredElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive originalElement = new JsonPrimitive(0.0f);
        jsonArray.add(originalElement);

        // The element that will replace the original one.
        JsonElement newElement = new JsonPrimitive("new value");

        // Act
        // The `set` method replaces the element at the specified index
        // and is expected to return the element that was previously at that position.
        JsonElement returnedElement = jsonArray.set(0, newElement);

        // Assert
        // Verify that the returned element is the one we originally added.
        assertEquals(originalElement, returnedElement);

        // For completeness, also verify the array's current state.
        assertEquals(1, jsonArray.size());
        assertEquals(newElement, jsonArray.get(0));
    }
}