package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent API of {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void endArray_shouldReturnSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        // Create a nested array structure `[[]]` to have an array to close.
        writer.beginArray();
        writer.beginArray();

        // Act
        // The endArray() method is called to close the inner array.
        JsonWriter returnedWriter = writer.endArray();

        // Assert
        // The method should return the writer instance itself to support a fluent,
        // chainable API.
        assertSame("Expected endArray() to return the same writer instance", writer, returnedWriter);
    }
}