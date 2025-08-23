package com.google.gson;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link JsonArray}.
 */
public class JsonArrayTest {

    @Test
    public void asList_onEmptyArray_returnsEmptyList() {
        // Arrange
        JsonArray emptyArray = new JsonArray();

        // Act
        List<JsonElement> resultList = emptyArray.asList();

        // Assert
        assertTrue("The returned list should be empty.", resultList.isEmpty());
    }
}