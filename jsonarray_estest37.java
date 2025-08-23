package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on type conversion methods for single-element arrays.
 */
public class JsonArrayTest {

    @Test
    public void getAsDoubleOnArrayWithSingleNullElementThrowsException() {
        // Arrange: Create a JsonArray containing a single JsonNull element.
        JsonArray jsonArray = new JsonArray();
        // According to JsonArray's contract, adding a null value converts it to a JsonNull instance.
        jsonArray.add((Number) null);

        // Act & Assert: Verify that calling getAsDouble() throws the expected exception.
        try {
            jsonArray.getAsDouble();
            fail("Expected an UnsupportedOperationException to be thrown, but no exception was thrown.");
        } catch (UnsupportedOperationException expected) {
            // JsonArray's getAsDouble() delegates to the single element's getAsDouble() method.
            // For a JsonNull element, this throws an exception with the message "JsonNull".
            assertEquals("JsonNull", expected.getMessage());
        }
    }
}