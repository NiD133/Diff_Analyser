package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class JsonTreeWriter_ESTestTest17 {

    @Test
    public void valueFloat_providesFluentInterfaceAndWritesCorrectValue() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        float testValue = 331.5379F;

        writer.beginObject();
        writer.name("testProperty");

        // Act
        JsonWriter returnedWriter = writer.value(testValue);
        writer.endObject();

        // Assert
        // 1. Verify the fluent interface by checking if the same instance is returned.
        assertSame("The value() method should return the same writer instance for method chaining.",
            writer, returnedWriter);

        // 2. Verify the correctness of the generated JSON structure.
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("testProperty", testValue);
        JsonElement actualJson = writer.get();

        assertEquals("The generated JSON object does not match the expected output.",
            expectedJson, actualJson);
    }
}