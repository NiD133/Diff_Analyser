package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling set() with an out-of-bounds index on an empty array
     * throws an IndexOutOfBoundsException.
     */
    @Test
    public void set_withOutOfBoundsIndex_throwsIndexOutOfBoundsException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        int outOfBoundsIndex = 1;

        // Act & Assert
        try {
            emptyArray.set(outOfBoundsIndex, null);
            fail("Expected an IndexOutOfBoundsException to be thrown, but it was not.");
        } catch (IndexOutOfBoundsException e) {
            // The exception message is delegated to the underlying ArrayList.
            // Asserting the message makes the test more specific and robust.
            assertEquals("Index: 1, Size: 0", e.getMessage());
        }
    }
}