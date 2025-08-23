package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link JsonParser} focusing on parsing multiple elements from a stream.
 */
public class JsonParserTest {

    @Test
    public void parseReader_whenStreamContainsMultipleJsonElements_parsesElementsSequentially() {
        // Arrange
        // A string containing two consecutive, top-level JSON elements: a string and an array.
        // The parseReader(JsonReader) method is designed to handle such streams.
        String multiElementJson = "\"first\" [1, 2, 3]";
        JsonReader jsonReader = new JsonReader(new StringReader(multiElementJson));

        // Act
        // The first call to parseReader() should consume the string element.
        JsonElement firstElement = JsonParser.parseReader(jsonReader);
        // The second call should consume the array element.
        JsonElement secondElement = JsonParser.parseReader(jsonReader);

        // Assert
        // Verify the first element was parsed correctly.
        assertTrue("First element should be a JsonPrimitive", firstElement.isJsonPrimitive());
        assertEquals("first", firstElement.getAsString());

        // Verify the second element was parsed correctly.
        assertTrue("Second element should be a JsonArray", secondElement.isJsonArray());

        // For completeness, verify the content of the array.
        JsonArray jsonArray = secondElement.getAsJsonArray();
        assertEquals(3, jsonArray.size());
        assertEquals(1, jsonArray.get(0).getAsInt());
        assertEquals(2, jsonArray.get(1).getAsInt());
        assertEquals(3, jsonArray.get(2).getAsInt());
    }
}