package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void peekOnJsonNull_shouldReturnNullToken() throws IOException {
        // Arrange: Create a JsonTreeReader with a JsonNull element.
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

        // Act: Peek at the next token in the stream.
        JsonToken token = reader.peek();

        // Assert: The token should be of type NULL.
        assertEquals(JsonToken.NULL, token);
    }
}