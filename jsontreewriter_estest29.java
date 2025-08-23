package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that endArray() returns the same writer instance to support a fluent API.
     */
    @Test
    public void endArray_returnsSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Act
        JsonWriter result = writer.endArray();

        // Assert
        // The method should return the same instance to allow method chaining.
        assertSame("endArray should return the same writer instance", writer, result);
    }
}