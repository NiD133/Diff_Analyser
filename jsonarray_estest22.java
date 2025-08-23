package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonArray#getAsBoolean()} method.
 */
public class JsonArrayTest {

    /**
     * Tests that `getAsBoolean()` on a JsonArray containing a single string element
     * that is not "true" (case-insensitive) correctly returns false.
     *
     * <p>The `JsonArray.getAsBoolean()` method is a convenience for single-element arrays.
     * It delegates to the element's `getAsBoolean()` method. For a `JsonPrimitive` containing
     * a string, this is equivalent to `Boolean.parseBoolean(stringValue)`, which returns
     * `false` for any string other than "true".
     */
    @Test
    public void getAsBoolean_whenArrayContainsSingleNonTrueString_shouldReturnFalse() {
        // Arrange: Create a JsonArray with a single string element that is not "true".
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("+");

        // Act: Call the method under test.
        boolean result = jsonArray.getAsBoolean();

        // Assert: Verify that the result is false.
        assertFalse("Expected getAsBoolean() to be false for a non-'true' string.", result);
    }
}