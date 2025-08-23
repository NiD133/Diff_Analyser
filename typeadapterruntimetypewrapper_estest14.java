package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TypeAdapterRuntimeTypeWrapper}.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    /**
     * Verifies that the read() method propagates an IOException thrown by the underlying JsonReader.
     */
    @Test
    public void read_whenReaderThrowsIOException_propagatesException() {
        // Arrange
        Gson gson = new Gson();
        Class<Object> objectType = Object.class;
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(objectType);
        TypeAdapter<Object> typeAdapterWrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, objectType);

        // Create a reader that is guaranteed to throw an IOException on read,
        // because it's not connected to a writer.
        PipedReader unconnectedReader = new PipedReader();
        JsonReader jsonReader = gson.newJsonReader(unconnectedReader);

        // Act & Assert
        try {
            typeAdapterWrapper.read(jsonReader);
            fail("Expected an IOException to be thrown because the reader is not connected.");
        } catch (IOException expected) {
            // Verify that the propagated exception is the one we expect from the PipedReader.
            assertEquals("Pipe not connected", expected.getMessage());
        }
    }
}