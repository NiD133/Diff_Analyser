package com.google.gson;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter} class, focusing on its utility methods.
 */
public class TypeAdapterTest {

    /**
     * Verifies that a TypeAdapter wrapped by {@link TypeAdapter#nullSafe()}
     * correctly deserializes a JSON 'null' literal into a Java null object
     * when reading from a {@link Reader}.
     */
    @Test
    public void nullSafeAdapter_whenReadingJsonNullFromReader_shouldReturnNull() throws IOException {
        // Arrange: Create a base TypeAdapter and wrap it with nullSafe().
        // The specific implementation of the delegate adapter (FutureTypeAdapter) is not
        // important, as we are testing the behavior of the nullSafe wrapper.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();
        Reader jsonReader = new StringReader("null");

        // Act: Deserialize the JSON input using the null-safe adapter.
        Object result = nullSafeAdapter.fromJson(jsonReader);

        // Assert: The deserialized object should be null.
        assertNull("The result of deserializing 'null' should be a null object.", result);
    }
}