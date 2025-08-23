package com.google.gson;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter} class, focusing on the behavior of the
 * {@link TypeAdapter#nullSafe()} wrapper.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling {@code write} on a null-safe adapter with a null {@link JsonWriter}
     * results in a {@link NullPointerException}.
     *
     * <p>The internal {@code NullSafeTypeAdapter} implementation first checks if the value to be
     * written is null. If it is, it immediately tries to call {@code out.nullValue()}. When the
     * provided {@code JsonWriter} instance ('out') is null, this invocation causes the NPE.
     */
    @Test(expected = NullPointerException.class)
    public void writeOnNullSafeAdapterWithNullWriterThrowsNPE() throws IOException {
        // Arrange: Create a base TypeAdapter and wrap it to be null-safe.
        // Gson.FutureTypeAdapter is a convenient, existing concrete implementation.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();

        // Act & Assert: Call write with a null JsonWriter.
        // The value being written is also null to ensure the null-handling branch
        // within the null-safe adapter is executed.
        nullSafeAdapter.write(null, null);
    }
}