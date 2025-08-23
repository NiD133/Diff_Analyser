package com.google.gson;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Writer;

/**
 * Tests for the {@link TypeAdapter} class, focusing on its public API contracts.
 */
class TypeAdapterTest {

    @Test
    void toJson_whenWriterIsNull_throwsNullPointerException() {
        // Arrange: Create a concrete instance of the abstract TypeAdapter.
        // Gson.FutureTypeAdapter is a convenient, accessible implementation for this test.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>();
        Object valueToWrite = "some-value";

        // Act & Assert: Verify that calling toJson with a null Writer throws a NullPointerException.
        // The TypeAdapter.toJson method delegates to a new JsonWriter, which is where
        // the null check for the writer is performed.
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            typeAdapter.toJson((Writer) null, valueToWrite);
        });

        // Further assert that the exception message is as expected, confirming the
        // source of the error.
        assertEquals("out == null", exception.getMessage());
    }
}