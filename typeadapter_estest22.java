package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import org.junit.Test;

// The original test class name and inheritance are kept to match the request context.
public class TypeAdapter_ESTestTest22 extends TypeAdapter_ESTest_scaffolding {

    /**
     * Verifies that attempting to deserialize an empty string throws an EOFException,
     * as an empty string is not a valid JSON document.
     */
    @Test
    public void fromJson_withEmptyString_shouldThrowEOFException() throws IOException {
        // Arrange: Create a base TypeAdapter. Its internal read/write logic is irrelevant
        // for this test, as the exception occurs before they are called.
        TypeAdapter<Object> baseAdapter = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) {
                throw new AssertionError("write should not be called during this test");
            }
            @Override
            public Object read(JsonReader in) {
                throw new AssertionError("read should not be called during this test");
            }
        };

        // The nullSafe() wrapper is used here. Its implementation calls reader.peek(),
        // which is the operation that fails on an empty input stream.
        TypeAdapter<Object> nullSafeAdapter = baseAdapter.nullSafe();
        String emptyJson = "";

        // Act & Assert
        try {
            nullSafeAdapter.fromJson(emptyJson);
            fail("Expected an EOFException for empty input, but no exception was thrown.");
        } catch (EOFException e) {
            // The JsonReader, used internally by fromJson, throws this specific exception.
            // Verifying the message confirms the source and context of the error.
            String expectedMessage = "End of input at line 1 column 1 path $";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}