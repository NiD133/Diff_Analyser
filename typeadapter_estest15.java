package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TypeAdapter} class, focusing on I/O error handling.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling read() on a TypeAdapter backed by a closed reader
     * throws an IOException.
     */
    @Test
    public void readFromClosedReaderThrowsIOException() {
        // Arrange
        // Create a TypeAdapter and wrap it with nullSafe() to test the wrapper's behavior.
        // The specific adapter (FutureTypeAdapter) is not important, as the exception
        // occurs before its read() method is ever called.
        TypeAdapter<Integer> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> nullSafeAdapter = delegateAdapter.nullSafe();

        // Create a reader and immediately close it to simulate a closed stream.
        StringReader closedReader = new StringReader("");
        try {
            closedReader.close();
        } catch (IOException e) {
            // This should not happen with a StringReader, but we fail fast if it does.
            fail("Test setup failed: " + e.getMessage());
        }
        JsonReader jsonReader = new JsonReader(closedReader);

        // Act & Assert
        try {
            nullSafeAdapter.read(jsonReader);
            fail("Expected an IOException because the underlying reader is closed.");
        } catch (IOException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Stream closed", e.getMessage());
        }
    }
}