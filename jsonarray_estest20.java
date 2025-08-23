package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that {@link JsonArray#getAsByte()} correctly returns the byte representation
     * when the array contains a single element that is a floating-point number.
     */
    @Test
    public void getAsByte_whenArrayContainsSingleFloat_shouldReturnCorrespondingByte() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(0.0f); // Add a float that can be safely converted to a byte

        // Act
        byte actualByte = jsonArray.getAsByte();

        // Assert
        byte expectedByte = 0;
        assertEquals(expectedByte, actualByte);
    }
}