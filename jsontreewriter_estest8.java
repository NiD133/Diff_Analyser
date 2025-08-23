package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This class contains tests for the JsonTreeWriter.
 * This particular test was refactored for clarity.
 */
public class JsonTreeWriter_ESTestTest8 { // Note: Class name kept for context

    /**
     * Tests that calling `value(Number)` correctly creates a JsonPrimitive
     * and returns the same writer instance to support a fluent API.
     */
    @Test
    public void valueWithNumber_shouldSetProductAndReturnSameInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        Number numberValue = 0L;
        JsonElement expectedProduct = new JsonPrimitive(numberValue);

        // Act
        JsonWriter resultWriter = writer.value(numberValue);
        JsonElement actualProduct = writer.get();

        // Assert
        // 1. Verify the method supports a fluent interface by returning the same instance.
        assertSame("The writer instance should be returned to allow for method chaining.", writer, resultWriter);

        // 2. Verify the correct JsonElement was created.
        assertEquals("The written value should be a JsonPrimitive containing the number.", expectedProduct, actualProduct);
    }
}