package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.IOException;

/**
 * Contains tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Verifies that a null-safe adapter, when deserializing the JSON string "null",
     * correctly returns a Java null object.
     */
    @Test
    public void nullSafeFromJsonString_withNullInput_shouldReturnNull() throws IOException {
        // Arrange: Create a null-safe TypeAdapter.
        // We use Gson.FutureTypeAdapter as a convenient, concrete implementation of TypeAdapter
        // for this test, but any TypeAdapter would exhibit the same null-safe behavior.
        TypeAdapter<Object> originalAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = originalAdapter.nullSafe();
        String jsonNull = "null";

        // Act: Deserialize the JSON "null" string.
        Object result = nullSafeAdapter.fromJson(jsonNull);

        // Assert: The resulting object should be null.
        assertNull("Deserializing 'null' should result in a null object.", result);
    }
}