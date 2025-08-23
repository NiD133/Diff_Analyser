package com.google.gson;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the final convenience methods of the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling {@link TypeAdapter#fromJson(String)} with a malformed JSON string
     * throws an {@link IOException}. This behavior is expected because the underlying
     * {@link com.google.gson.stream.JsonReader} operates in a strict mode by default.
     */
    @Test
    public void fromJson_withMalformedJsonString_shouldThrowIOException() {
        // Arrange
        // We need a concrete TypeAdapter instance to test the final `fromJson` method.
        // A FutureTypeAdapter is a simple internal class that serves this purpose.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>().nullSafe();
        String malformedJson = "This is not valid JSON";

        // Act & Assert
        // The fromJson method should fail because the input string is not a valid JSON value.
        IOException thrownException = assertThrows(IOException.class, () -> {
            typeAdapter.fromJson(malformedJson);
        });

        // For a more robust test, we can check that the exception message contains
        // the helpful hint provided by Gson's parser.
        String expectedMessageHint = "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON";
        assertTrue(
            "The exception message should guide the user to use lenient parsing.",
            thrownException.getMessage().contains(expectedMessageHint)
        );
    }
}