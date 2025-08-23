package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the {@link JsonArray#isEmpty()} method.
 */
public class JsonArrayIsEmptyTest {

    @Test
    public void isEmpty_returnsTrueForNewArray_andFalseAfterAddingElement() {
        // Arrange: Create a new, empty JsonArray.
        JsonArray jsonArray = new JsonArray();

        // Assert: A newly created JsonArray should be empty.
        assertTrue("A new JsonArray should be empty", jsonArray.isEmpty());

        // Act: Add an element to the array.
        jsonArray.add('6');

        // Assert: The JsonArray should no longer be empty.
        assertFalse("JsonArray should not be empty after adding an element", jsonArray.isEmpty());
    }
}