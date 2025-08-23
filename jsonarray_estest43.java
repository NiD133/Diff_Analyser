package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link JsonArray} class, focusing on its type conversion methods.
 */
public class JsonArrayTest {

    @Test
    public void getAsBigInteger_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        // According to JsonArray documentation, adding a null value results in a JsonNull element.
        jsonArray.add((Number) null);

        // Act & Assert
        try {
            // The getAsBigInteger() method on JsonArray delegates to the single element within it.
            jsonArray.getAsBigInteger();
            fail("Expected an UnsupportedOperationException to be thrown, but it was not.");
        } catch (UnsupportedOperationException e) {
            // Verify that the exception is thrown because JsonNull does not support this conversion.
            // The exception message from JsonElement's default implementation is the simple class name.
            assertEquals("JsonNull", e.getMessage());
        }
    }
}