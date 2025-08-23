package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class is a cleaned-up, human-readable version of an auto-generated test.
 * The original class was `JsonTreeWriter_ESTestTest23`.
 */
public class JsonTreeWriterTest {

    @Test
    public void value_withDouble_createsNumberPrimitive() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        double expectedValue = 495.5255;

        // Act
        writer.value(expectedValue);
        JsonElement result = writer.get();

        // Assert
        assertTrue("The resulting element should be a JsonPrimitive", result.isJsonPrimitive());

        JsonPrimitive resultPrimitive = result.getAsJsonPrimitive();
        assertTrue("The primitive should represent a number", resultPrimitive.isNumber());
        assertEquals("The primitive's double value should match the input",
                expectedValue, resultPrimitive.getAsDouble(), 0.0);
    }
}