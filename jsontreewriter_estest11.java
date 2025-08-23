package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 * This class provides a more understandable version of the original generated test.
 */
public class JsonTreeWriterTest {

    /**
     * Tests that calling value() with a null Boolean correctly produces a JsonNull element
     * and maintains the fluent API by returning itself.
     */
    @Test
    public void value_withNullBoolean_writesJsonNull() throws IOException {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Write a null Boolean value. The cast is necessary to resolve
        // method overloading for `value()`.
        JsonWriter result = writer.value((Boolean) null);

        // Assert:
        // 1. The method should return the same instance to allow for method chaining.
        assertSame("The writer instance should be returned for a fluent API.", writer, result);

        // 2. The internal state should reflect that a JsonNull was written.
        JsonElement product = writer.get();
        assertEquals("Writing a null Boolean should result in a JsonNull element.", JsonNull.INSTANCE, product);
    }
}