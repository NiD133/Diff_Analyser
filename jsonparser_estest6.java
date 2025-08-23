package com.google.gson;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void parseString_withEmptyString_returnsJsonNull() {
        // Arrange
        String emptyJson = "";

        // Act
        // The deprecated `new JsonParser().parse(String)` is replaced with the
        // recommended static method `JsonParser.parseString(String)`.
        JsonElement result = JsonParser.parseString(emptyJson);

        // Assert
        // For backward compatibility, Gson's lenient parser treats an empty string
        // as a JSON null value.
        assertEquals(JsonNull.INSTANCE, result);
    }
}