package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that adding a null string to a JsonArray results in a JsonNull element,
     * and that removing this element returns the expected JsonNull instance.
     */
    @Test
    public void remove_returnsJsonNull_whenNullStringWasAdded() {
        // Arrange: Create a JsonArray and add a null string.
        // Per JsonArray's contract, this should be stored as a JsonNull object.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((String) null);

        // Act: Remove the element at the first position.
        JsonElement removedElement = jsonArray.remove(0);

        // Assert: The removed element should be the singleton instance of JsonNull.
        // This is a more specific and informative assertion than the original,
        // which only checked that the element was not a JsonArray.
        assertEquals(JsonNull.INSTANCE, removedElement);
    }
}