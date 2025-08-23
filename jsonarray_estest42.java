package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the behavior of the {@link JsonArray} class, specifically focusing on
 * its type-casting "getAs..." methods.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsBoolean() on a JsonArray containing a single JsonNull
     * element throws an UnsupportedOperationException.
     *
     * The getAs...() methods on a JsonArray are designed to work only when the array
     * contains a single element, and they delegate the type conversion to that element.
     * A JsonNull element does not have a boolean representation, so it throws an exception.
     */
    @Test
    public void getAsBoolean_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange: Create a JsonArray and add a null value.
        // Per the JsonArray documentation, adding a null reference results in a JsonNull element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);

        // Act & Assert: Verify that calling getAsBoolean() throws the expected exception.
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> jsonArray.getAsBoolean()
        );

        // Also assert the exception message for more precise verification.
        // The message "JsonNull" comes from the JsonNull class itself.
        assertEquals("JsonNull", exception.getMessage());
    }
}