package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void get_afterWritingEmptyObject_returnsEmptyJsonObject() throws IOException {
        // Arrange: Create a new JsonTreeWriter
        JsonTreeWriter writer = new JsonTreeWriter();
        
        // Act: Write an empty JSON object and retrieve the result
        writer.beginObject();
        writer.endObject();
        JsonElement result = writer.get();

        // Assert: The result should be an empty JsonObject
        JsonObject expected = new JsonObject();
        assertEquals(expected, result);

        // A more detailed assertion could also be used:
        // assertTrue("The result should be a JsonObject", result.isJsonObject());
        // assertTrue("The JsonObject should be empty", result.getAsJsonObject().isEmpty());
    }
}