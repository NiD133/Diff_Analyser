package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that an element retrieved from a JsonArray preserves its original type.
     * Specifically, a JsonPrimitive should not be misidentified as a JsonObject.
     */
    @Test
    public void get_retrievedPrimitiveElement_shouldNotBeJsonObject() {
        // Arrange: Create a JsonArray and add a JsonPrimitive element to it.
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive stringPrimitive = new JsonPrimitive("a string value");
        jsonArray.add(stringPrimitive);

        // Act: Retrieve the element from the array by its index.
        JsonElement retrievedElement = jsonArray.get(0);

        // Assert: Confirm that the retrieved element is not a JsonObject,
        // ensuring its type integrity is maintained.
        assertFalse("A retrieved JsonPrimitive should not be identified as a JsonObject.",
                retrievedElement.isJsonObject());
    }
}