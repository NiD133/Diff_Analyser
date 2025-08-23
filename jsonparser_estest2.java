package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link JsonParser} class.
 */
public class JsonParserTest {

    @Test
    public void parseString_withLenientArrayContainingUnquotedString_parsesSuccessfully() {
        // Arrange
        // The JsonParser is designed to be lenient, which allows for syntax
        // like unquoted strings inside this array.
        String lenientJsonArray = "[GoyG.G]";

        // Act
        JsonElement parsedElement = JsonParser.parseString(lenientJsonArray);

        // Assert
        assertTrue("The parsed element should be a JsonArray", parsedElement.isJsonArray());

        // For completeness, verify the content of the array
        JsonArray array = parsedElement.getAsJsonArray();
        assertEquals("Array should contain one element", 1, array.size());
        assertEquals("The unquoted string should be parsed correctly", "GoyG.G", array.get(0).getAsString());
    }
}