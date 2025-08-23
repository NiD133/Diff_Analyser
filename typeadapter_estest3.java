package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link TypeAdapter} class, focusing on the behavior of the null-safe wrapper.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling toJson(null) on a null-safe TypeAdapter
     * correctly serializes the null value to the JSON literal "null".
     */
    @Test
    public void toJsonWithNullOnNullSafeAdapterReturnsJsonNullString() {
        // Arrange
        // Create a base TypeAdapter. The specific implementation (FutureTypeAdapter)
        // is not important here, as we are testing the nullSafe() wrapper's behavior.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();

        // Act
        // Serialize a null object using the null-safe adapter.
        String jsonOutput = nullSafeAdapter.toJson(null);

        // Assert
        // The null-safe adapter should handle the null input and produce the JSON literal "null".
        assertEquals("null", jsonOutput);
    }
}