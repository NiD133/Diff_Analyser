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
     * Verifies that calling peek() on a reader for a JsonArray
     * correctly identifies the token as BEGIN_ARRAY.
     */
    @Test
    public void peek_onJsonArray_returnsBeginArray() throws IOException {
        // Arrange: Create a JsonTreeReader with an empty JsonArray.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Act: Peek at the next token in the stream.
        JsonToken actualToken = reader.peek();

        // Assert: The token should be BEGIN_ARRAY.
        assertEquals(JsonToken.BEGIN_ARRAY, actualToken);
    }
}