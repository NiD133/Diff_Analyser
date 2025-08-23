package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonArray} class, focusing on the equals method.
 */
public class JsonArrayTest {

    /**
     * Tests the reflexive property of the equals method, ensuring that a
     * JsonArray instance is always equal to itself.
     */
    @Test
    public void equals_onSameInstance_returnsTrue() {
        // Arrange
        JsonArray jsonArray = new JsonArray();

        // Assert
        // According to the contract of Object.equals(), an object must be equal to itself.
        assertEquals(jsonArray, jsonArray);
    }
}