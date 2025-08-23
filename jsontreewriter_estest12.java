package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void value_withBoolean_shouldWriteBooleanPrimitiveAndReturnSameInstance() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        JsonWriter resultWriter = writer.value(Boolean.TRUE);

        // Assert
        // 1. Verify the method returns the same instance for a fluent API.
        assertSame("The value() method should return the same writer instance for chaining.", writer, resultWriter);

        // 2. Verify the correct JSON element was written.
        JsonElement producedElement = writer.get();
        assertEquals(new JsonPrimitive(true), producedElement);
    }
}