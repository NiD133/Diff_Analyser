package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test suite for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsBigInteger() on an empty array throws an IllegalStateException.
     * This method is only valid for arrays containing a single element.
     */
    @Test
    public void getAsBigIntegerOnEmptyArrayShouldThrowIllegalStateException() {
        // Arrange: Create an empty JsonArray.
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert: Verify that the expected exception is thrown.
        try {
            emptyArray.getAsBigInteger();
            fail("Expected an IllegalStateException to be thrown, but it wasn't.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}