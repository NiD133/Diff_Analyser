package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link JsonParser}.
 */
public class JsonParserTest {

    /**
     * Tests that {@link JsonParser#parseReader(JsonReader)} successfully parses the first
     * valid JSON element from a stream and ignores any subsequent, non-JSON trailing characters.
     * This is the documented behavior for this specific method overload.
     */
    @Test
    public void parseReaderWithJsonReaderShouldIgnoreTrailingData() {
        // Arrange
        String jsonWithTrailingGarbage = "{}extra-characters";
        StringReader reader = new StringReader(jsonWithTrailingGarbage);
        JsonReader jsonReader = new JsonReader(reader);

        JsonObject expectedObject = new JsonObject();

        // Act
        JsonElement parsedElement = JsonParser.parseReader(jsonReader);

        // Assert
        // Verify that the parser correctly identified the element as a JsonObject.
        assertTrue(parsedElement.isJsonObject());
        // Verify that the parsed object is the expected empty object, confirming
        // that the trailing data was successfully ignored.
        assertEquals(expectedObject, parsedElement);
    }
}