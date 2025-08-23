package com.google.gson;

import static org.junit.Assert.assertEquals;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.junit.Test;

/**
 * This is a test case for the JsonParser class.
 * In a real project, this would be part of a larger JsonParserTest suite.
 */
public class JsonParserTest {

    /**
     * Verifies that parsing an empty or fully consumed JsonReader results in a JsonNull object.
     * This is a specific behavior for backward compatibility.
     */
    @Test
    public void parseReader_withEmptyJsonReader_returnsJsonNull() {
        // Arrange: Create a JsonReader for an empty input stream.
        JsonReader emptyJsonReader = new JsonReader(new StringReader(""));

        // Act: Parse the empty reader.
        JsonElement result = JsonParser.parseReader(emptyJsonReader);

        // Assert: The parser should return a JsonNull instance for an empty stream.
        assertEquals(JsonNull.INSTANCE, result);
    }
}