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

    /**
     * Verifies that writing a Number value correctly creates a JsonPrimitive
     * and that the method supports a fluent interface by returning itself.
     */
    @Test
    public void value_whenWritingNumber_buildsJsonPrimitiveAndReturnsSelf() {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        Number testNumber = 1831.275f;

        // Act
        JsonWriter resultWriter = jsonTreeWriter.value(testNumber);

        // Assert
        // 1. The method should return the same instance to allow for method chaining.
        assertSame("The writer instance should be returned for chaining.", jsonTreeWriter, resultWriter);

        // 2. The writer should produce the correct JsonElement.
        JsonElement writtenElement = jsonTreeWriter.get();
        JsonPrimitive expectedPrimitive = new JsonPrimitive(testNumber);
        assertEquals("The written element should be a JsonPrimitive representing the number.",
                expectedPrimitive, writtenElement);
    }
}