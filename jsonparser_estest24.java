package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Verifies that {@code parseReader} correctly parses a JSON array containing an
     * unquoted string. This behavior is expected because the underlying {@link JsonReader}
     * operates in lenient mode by default, which permits unquoted literals.
     */
    @Test
    public void parseReader_whenGivenArrayWithUnquotedString_shouldParseAsJsonArray() {
        // Arrange
        String jsonWithUnquotedString = "[GoyG.G]";
        StringReader reader = new StringReader(jsonWithUnquotedString);
        JsonReader jsonReader = new JsonReader(reader);

        // Act
        JsonElement parsedElement = JsonParser.parseReader(jsonReader);

        // Assert
        // The original assertion only checked that the result was not a primitive.
        // A more specific assertion confirms it is a JsonArray, which is more informative.
        assertNotNull(parsedElement);
        assertTrue("The parsed element should be a JsonArray", parsedElement.isJsonArray());

        // Further validation of the array's content makes the test more robust.
        JsonArray jsonArray = parsedElement.getAsJsonArray();
        assertEquals("The array should contain exactly one element", 1, jsonArray.size());
        assertEquals("The element should be the unquoted string value", "GoyG.G", jsonArray.get(0).getAsString());
    }
}