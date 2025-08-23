package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonArray} class, focusing on specific behaviors.
 */
public class JsonArrayTest {

    /**
     * Tests that the contains() method returns true when an array is added to itself
     * as an element. This verifies the handling of self-referential arrays.
     */
    @Test
    public void contains_whenArrayContainsItself_shouldReturnTrue() {
        // Arrange: Create a new JsonArray and add it to itself.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonArray);

        // Act: Check if the array contains itself.
        boolean found = jsonArray.contains(jsonArray);

        // Assert: The contains() method should return true.
        assertTrue("The array should report that it contains itself.", found);
    }
}