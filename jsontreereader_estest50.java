package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that after starting to read an empty array, peeking at the next
     * token correctly identifies the end of the array.
     */
    @Test
    public void peekOnEmptyArrayReturnsEndArray() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);
        
        // Consume the token that begins the array.
        reader.beginArray();

        // Act: Peek at the next token in the stream.
        JsonToken nextToken = reader.peek();

        // Assert: The next token should be END_ARRAY, as the array is empty.
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }
}