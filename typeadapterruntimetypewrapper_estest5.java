package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.PipedWriter;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * This test class contains improved versions of tests for {@link TypeAdapterRuntimeTypeWrapper}.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class TypeAdapterRuntimeTypeWrapper_ESTestTest5 {

    /**
     * Tests that the write method correctly propagates an IOException thrown by the underlying JsonWriter.
     */
    @Test
    public void write_whenJsonWriterThrowsIOException_propagatesException() {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(Object.class);
        Type runtimeType = String.class; // A sample runtime type for the wrapper
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterWrapper =
                new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, runtimeType);

        // To simulate a faulty output stream, we use a PipedWriter that is not connected
        // to a PipedReader. Any attempt to write to it will throw an IOException.
        PipedWriter unconnectedWriter = new PipedWriter();
        JsonWriter jsonWriter = new JsonWriter(unconnectedWriter);

        Object valueToWrite = "some value";

        // Act & Assert
        // Verify that the IOException from the unconnected pipe is thrown and propagated.
        IOException exception = assertThrows(IOException.class, () -> {
            typeAdapterWrapper.write(jsonWriter, valueToWrite);
        });

        // Check that the exception is the one we expect from the PipedWriter.
        assertEquals("Pipe not connected", exception.getMessage());
    }
}