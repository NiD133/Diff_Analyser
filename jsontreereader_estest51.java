package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that after starting to read a JSON object, peeking at the next token
     * correctly identifies it as a NAME token.
     */
    @Test
    public void peekAfterBeginObjectOnObjectWithPropertyReturnsName() throws IOException {
        // Arrange: Create a JsonObject with one property and a JsonTreeReader for it.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        // Position the reader inside the object.
        reader.beginObject();

        // Act: Peek at the next token without consuming it.
        JsonToken nextToken = reader.peek();

        // Assert: The next token should be the name of the property.
        assertEquals(JsonToken.NAME, nextToken);
    }
}