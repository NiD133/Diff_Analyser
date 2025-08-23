package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 * This class contains a refactored version of an auto-generated test case.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that writing a long value results in the correct JsonPrimitive
     * and that the writer instance is returned to allow for a fluent API.
     */
    @Test
    public void value_whenWritingLong_createsJsonPrimitiveAndReturnsSelf() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        long expectedValue = -825L;

        // Act
        JsonWriter returnedWriter = writer.value(expectedValue);
        JsonElement result = writer.get();

        // Assert
        // 1. The method should return the same instance for method chaining.
        assertSame("The writer should return itself to support a fluent API.", writer, returnedWriter);

        // 2. The writer should produce the correct JsonElement.
        assertTrue("The resulting element should be a JsonPrimitive.", result.isJsonPrimitive());
        JsonPrimitive primitive = result.getAsJsonPrimitive();
        assertTrue("The primitive should represent a number.", primitive.isNumber());
        assertEquals("The primitive should hold the correct long value.", expectedValue, primitive.getAsLong());
    }
}