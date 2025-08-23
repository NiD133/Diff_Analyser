package com.google.gson.internal.bind;

import static org.junit.Assert.assertSame;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void fluentApiReturnsSameInstance() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        // Build a simple JSON structure: an array containing an empty object.
        // Each method call should return the original writer instance to allow chaining.
        writer.beginArray();
        JsonWriter writerAfterBeginObject = writer.beginObject();
        JsonWriter writerAfterEndObject = writer.endObject();
        writer.endArray();

        // Assert
        // Verify that the writer instance is returned after each call.
        assertSame("beginObject() should return the same writer instance for chaining",
                writer, writerAfterBeginObject);
        assertSame("endObject() should return the same writer instance for chaining",
                writer, writerAfterEndObject);
    }
}