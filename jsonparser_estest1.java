package com.google.gson;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class.
 */
public class JsonParserTest {

    @Test
    public void parseString_withEmptyString_returnsJsonNull() {
        // Arrange
        String emptyJson = "";

        // Act
        JsonElement result = JsonParser.parseString(emptyJson);

        // Assert
        assertTrue("Parsing an empty string should result in a JsonNull element.", result.isJsonNull());
    }
}