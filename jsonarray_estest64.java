package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonArray} class, focusing on convenience getter methods.
 */
public class JsonArrayTest {

    /**
     * Tests that {@link JsonArray#getAsBoolean()} correctly returns true
     * when the array contains a single boolean element with the value {@code true}.
     */
    @Test
    public void getAsBoolean_returnsTrue_whenArrayContainsSingleTrueElement() {
        // Arrange: Create a JsonArray containing a single boolean `true` element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);

        // Act: Call the method under test.
        boolean result = jsonArray.getAsBoolean();

        // Assert: Verify that the returned value is true.
        assertTrue("Expected getAsBoolean() to return true for an array with a single true element.", result);
    }
}