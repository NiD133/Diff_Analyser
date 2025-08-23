package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter} class, focusing on its helper methods.
 */
public class TypeAdapterTest {

    @Test
    public void nullSafe_whenToJsonIsCalledWithNull_writesNullLiteral() throws IOException {
        // Arrange
        // Create a delegate TypeAdapter that fails if its write method is ever called.
        // This ensures that the nullSafe() wrapper handles the null case itself
        // without delegating to the original adapter.
        TypeAdapter<Object> delegateAdapter = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) {
                fail("The delegate adapter's write method should not be called when writing a null value.");
            }

            @Override
            public Object read(JsonReader in) {
                // This method is not used in this test.
                throw new AssertionError("The delegate adapter's read method should not be called.");
            }
        };

        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();
        StringWriter writer = new StringWriter();

        // Act
        // Call the toJson method with a null value on the null-safe adapter.
        nullSafeAdapter.toJson(writer, null);

        // Assert
        // Verify that the output is the JSON literal "null".
        String expectedJson = "null";
        assertEquals(expectedJson, writer.toString());
    }
}