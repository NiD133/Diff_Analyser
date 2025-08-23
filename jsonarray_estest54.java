package com.google.gson;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that the isEmpty() method correctly identifies a newly created
     * JsonArray as empty.
     */
    @Test
    public void testIsEmptyReturnsTrueForNewArray() {
        // Arrange: Create a new, empty JsonArray.
        JsonArray jsonArray = new JsonArray();

        // Act & Assert: Verify that the array is empty.
        assertTrue("A newly created JsonArray should be empty", jsonArray.isEmpty());
    }
}