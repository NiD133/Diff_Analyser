package com.google.gson;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Tests that parsing an empty string correctly results in a {@link JsonNull} element.
     * This behavior is maintained for backward compatibility.
     */
    @Test
    public void parse_emptyString_returnsJsonNull() {
        // Arrange
        String emptyJson = "";

        // Act
        // Use the modern, static `parseString` method, which is the recommended API.
        JsonElement result = JsonParser.parseString(emptyJson);

        // Assert
        // The assertion is specific: the result must be a JsonNull instance.
        // This is more informative than simply checking that it's not a JsonArray.
        assertTrue("Parsing an empty string should result in JsonNull", result.isJsonNull());
    }
}