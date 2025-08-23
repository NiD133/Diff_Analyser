package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

/**
 * This test focuses on the behavior of the null-safe wrapper for a TypeAdapter,
 * specifically when its read method is called with invalid input.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling read() on a null-safe TypeAdapter with a null JsonReader
     * throws a NullPointerException.
     *
     * This test ensures the method correctly validates its arguments before attempting
     * to process a JSON stream.
     */
    @Test(expected = NullPointerException.class)
    public void nullSafeRead_whenReaderIsNull_throwsNullPointerException() throws Exception {
        // Arrange: Create a TypeAdapter and wrap it with nullSafe() to get the
        // adapter under test. Any TypeAdapter can be used as the delegate.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();

        // Act & Assert: Call the read method with a null reader.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        nullSafeAdapter.read(null);
    }
}