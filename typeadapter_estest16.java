package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.EOFException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TypeAdapter_ESTestTest16 extends TypeAdapter_ESTest_scaffolding {

    /**
     * Tests that calling {@code read()} on a null-safe adapter with an empty input stream
     * throws an {@link EOFException}.
     * <p>
     * The {@code nullSafe()} wrapper first checks the next token in the stream. For an empty
     * input, this check immediately results in an end-of-file condition before any
     * delegation to the wrapped adapter can occur.
     */
    @Test
    public void nullSafeReadFromEmptyInputShouldThrowEofException() {
        // Arrange: Create a null-safe adapter and a JSON reader with empty input.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();
        JsonReader jsonReader = new JsonReader(new StringReader(""));

        // Act & Assert: Verify that reading from the empty stream throws an EOFException.
        EOFException exception = assertThrows(EOFException.class, () -> {
            nullSafeAdapter.read(jsonReader);
        });

        // Also assert the exception message to ensure it's the expected error.
        assertEquals("End of input at line 1 column 1 path $", exception.getMessage());
    }
}