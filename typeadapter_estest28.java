package com.google.gson;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;

/**
 * Unit tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling {@link TypeAdapter#fromJson(Reader)} with a null Reader
     * throws a {@link NullPointerException}.
     *
     * <p>The {@code fromJson} method is expected to reject null inputs early to prevent
     * errors deeper in the deserialization process.
     */
    @Test(expected = NullPointerException.class)
    public void fromJson_withNullReader_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create a concrete instance of the abstract TypeAdapter.
        // Gson.FutureTypeAdapter is a convenient, existing implementation for this test.
        TypeAdapter<String> typeAdapter = new Gson.FutureTypeAdapter<>();

        // Act: Attempt to deserialize from a null Reader.
        // The cast to (Reader) is necessary to resolve method ambiguity.
        typeAdapter.fromJson((Reader) null);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // which is handled by the `expected` attribute of the @Test annotation.
    }
}