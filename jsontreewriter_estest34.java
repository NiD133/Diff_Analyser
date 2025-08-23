package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the JsonTreeWriter.
 * The original test was refactored for improved clarity.
 */
public class JsonTreeWriter_ESTestTest34 { // Note: In a real-world scenario, this test would be part of a comprehensive JsonTreeWriterTest class.

    /**
     * Verifies that beginArray() returns the same writer instance to support a fluent API.
     */
    @Test
    public void beginArray_returnsSameWriterInstanceForChaining() throws IOException {
        // Arrange: Create a writer and start an object, ready to add a named array.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("data");

        // Act: Call the method under test.
        JsonWriter returnedWriter = writer.beginArray();

        // Assert: The returned writer should be the exact same instance as the original.
        assertSame("The beginArray() method should return the same writer instance to allow for method chaining.",
                writer, returnedWriter);
    }
}