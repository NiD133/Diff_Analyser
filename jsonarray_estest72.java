package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link JsonArray} class, focusing on its behavior as a single-element container.
 */
public class JsonArrayTest {

    @Test
    public void getAsByte_onEmptyArray_shouldThrowIllegalStateException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Verify that calling getAsByte() on an empty array throws an exception.
        try {
            emptyArray.getAsByte();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}