package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that after starting to read an empty JSON object,
     * peeking at the next token correctly identifies the end of the object.
     */
    @Test
    public void peekAfterBeginObjectOnEmptyObjectReturnsEndObject() throws IOException {
        // Arrange: Create a reader for an empty JSON object (`{}`) and consume the
        // initial BEGIN_OBJECT token.
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);
        reader.beginObject();

        // Act: Peek at the next token in the stream without consuming it.
        JsonToken nextToken = reader.peek();

        // Assert: The next token should be END_OBJECT, as the object is empty.
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }
}