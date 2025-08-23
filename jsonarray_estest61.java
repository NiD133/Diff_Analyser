package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that calling `remove(JsonElement)` with an element that is not
     * present in the array correctly returns `false` and does not modify the array.
     */
    @Test
    public void remove_shouldReturnFalse_whenElementIsNotPresent() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(846)); // Add an element to the array.

        // The test attempts to remove the array from itself. A JsonArray is not considered
        // equal to any of its elements unless it contains another identical array.
        // This serves as a reliable way to test removal of a non-existent element.
        JsonElement elementThatIsNotInTheArray = jsonArray;

        // Act
        boolean wasRemoved = jsonArray.remove(elementThatIsNotInTheArray);

        // Assert
        assertFalse("Removing an element that is not in the array should return false.", wasRemoved);
        assertEquals("The array's size should not change if the element was not found.", 1, jsonArray.size());
    }
}