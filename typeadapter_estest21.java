package com.google.gson;

import org.junit.Test;

/**
 * Contains tests for the {@link TypeAdapter} class, focusing on its public API.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling fromJson(String) with a null input throws a NullPointerException.
     * <p>
     * This behavior is expected because the method internally attempts to create a
     * {@code new StringReader(null)}, which is an invalid operation.
     */
    @Test(expected = NullPointerException.class)
    public void fromJson_withNullString_throwsNullPointerException() {
        // Arrange: Create an instance of a TypeAdapter.
        // Gson.FutureTypeAdapter is a convenient, accessible implementation for this test.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>();

        // Act: Call the method under test with a null argument.
        typeAdapter.fromJson((String) null);

        // Assert: The test expects a NullPointerException, which is declared in the @Test annotation.
        // If no exception is thrown, the test will fail.
    }
}