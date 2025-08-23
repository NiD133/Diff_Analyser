package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeReader}.
 * This refactored version replaces the original EvoSuite-generated class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that peeking at a Character element within a JsonArray
     * correctly identifies its type as a STRING token. This confirms
     * that Gson handles single characters as strings when reading a JSON tree.
     */
    @Test
    public void peek_whenArrayContainsCharacter_returnsStringToken() throws IOException {
        // Arrange: Create a JSON array containing a single character.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('7');

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray(); // Position the reader inside the array.

        // Act: Peek at the next token in the stream.
        JsonToken actualToken = reader.peek();

        // Assert: The token should be of type STRING.
        assertEquals(JsonToken.STRING, actualToken);
    }
}