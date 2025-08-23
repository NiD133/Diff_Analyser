package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TypeAdapterRuntimeTypeWrapper_ESTestTest8 {

    /**
     * Tests that calling read() on a wrapper with an empty JSON input
     * correctly propagates the EOFException from the underlying reader.
     */
    @Test
    public void read_withEmptyJsonInput_throwsEofException() throws IOException {
        // Arrange
        Gson gson = new Gson();
        // The specific delegate adapter doesn't matter, as long as it attempts to read.
        // The standard Object adapter is a simple and realistic choice.
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterWrapper =
                new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, Object.class);

        StringReader emptyInput = new StringReader("");
        JsonReader jsonReader = gson.newJsonReader(emptyInput);

        // Act & Assert
        // Reading from an empty stream should result in an End of File exception.
        EOFException exception = assertThrows(EOFException.class, () -> {
            typeAdapterWrapper.read(jsonReader);
        });

        // Verify the exception message for more precise testing.
        assertEquals("End of input at line 1 column 1 path $", exception.getMessage());
    }
}