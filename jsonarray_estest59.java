package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test for {@link JsonArray#getAsNumber()}.
 */
public class JsonArrayTest {

    @Test
    public void getAsNumber_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(JsonNull.INSTANCE); // Create an array with a single JsonNull element.

        // Act & Assert
        try {
            // getAsNumber() on a single-element array delegates to the element's getAsNumber() method.
            // For a JsonNull element, this is an unsupported operation.
            jsonArray.getAsNumber();
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException e) {
            // The exception message from JsonElement is expected to be the simple name of the class.
            assertEquals("JsonNull", e.getMessage());
        }
    }
}