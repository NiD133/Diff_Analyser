package com.google.gson;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void size_shouldReturnZero_forNewJsonArray() {
        // Arrange
        JsonArray emptyArray = new JsonArray();

        // Act & Assert
        assertEquals(0, emptyArray.size());
    }
}