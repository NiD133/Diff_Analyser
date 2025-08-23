package com.google.gson;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TypeAdapter#fromJson(Reader)} method.
 */
public class TypeAdapterFromJsonTest {

    /**
     * Verifies that fromJson(Reader) throws an IOException if the provided Reader is already closed.
     */
    @Test
    public void fromJson_whenReaderIsClosed_throwsIOException() throws Exception {
        // Arrange: Create a TypeAdapter and a reader that is immediately closed.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>().nullSafe();
        Reader reader = new StringReader("any string");
        reader.close();

        // Act & Assert: Attempting to read from the closed reader should throw an IOException.
        try {
            typeAdapter.fromJson(reader);
            fail("Expected an IOException to be thrown when reading from a closed stream.");
        } catch (IOException expected) {
            // The underlying StringReader is expected to throw an exception with this message.
            assertEquals("Stream closed", expected.getMessage());
        }
    }
}