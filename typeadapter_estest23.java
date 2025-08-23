package com.google.gson;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * This class contains tests for the {@link TypeAdapter} class.
 */
public class ImprovedTypeAdapterTest {

    /**
     * Tests that calling {@link TypeAdapter#fromJson(java.io.Reader)} with a reader
     * containing malformed JSON data throws an {@link IOException}.
     *
     * The underlying {@link com.google.gson.stream.JsonReader} is strict by default and
     * should reject any input that is not a valid JSON literal.
     */
    @Test
    public void fromJsonReaderWithMalformedJsonShouldThrowIOException() {
        // Arrange: Create a TypeAdapter and a reader with an invalid JSON string.
        // The specific TypeAdapter implementation (FutureTypeAdapter) is not critical here,
        // as the parsing logic is handled by the fromJson method.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>().nullSafe();
        String malformedJson = "this is not valid json";
        StringReader jsonReader = new StringReader(malformedJson);

        // Act & Assert: Verify that an IOException is thrown.
        IOException exception = assertThrows(IOException.class, () -> {
            typeAdapter.fromJson(jsonReader);
        });

        // Further assert that the exception message is the one expected for parsing errors,
        // making the test more specific and robust.
        String expectedMessagePrefix = "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON";
        assertTrue(
            "Exception message should indicate a malformed JSON error.",
            exception.getMessage().startsWith(expectedMessagePrefix)
        );
    }
}