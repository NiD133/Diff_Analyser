package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonArray} class, focusing on exception-throwing behavior.
 */
public class JsonArrayTest {

    @Test
    public void getAsShortShouldThrowIllegalStateExceptionForEmptyArray() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();

        // Act & Assert: Verify that calling getAsShort() on an empty array throws the expected exception.
        try {
            emptyArray.getAsShort();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct, as it's part of the public API contract.
            assertEquals("Array must have size 1, but has size 0", e.getMessage());
        }
    }
}