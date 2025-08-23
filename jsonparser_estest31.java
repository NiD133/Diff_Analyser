package com.google.gson;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test suite for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Verifies that parsing a JSON array containing an unquoted string literal
     * succeeds, as the parser operates in lenient mode by default.
     */
    @Test
    public void parseString_whenInputIsArrayWithUnquotedString_shouldParseAsJsonArray() {
        // Arrange
        // This string represents a JSON array with an unquoted string,
        // which is valid in Gson's default lenient parsing mode.
        String lenientJsonArray = "[GoyG.G]";

        // Act
        // Use the modern, static parseString() method instead of the deprecated instance method.
        JsonElement parsedElement = JsonParser.parseString(lenientJsonArray);

        // Assert
        // The result should be correctly identified as a JsonArray, not just "not a JsonObject".
        assertTrue("The parsed element should be a JsonArray", parsedElement.isJsonArray());
    }
}