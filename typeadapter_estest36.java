package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the internal {@link Gson.FutureTypeAdapter}, a specialized {@link TypeAdapter}.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling {@code toJson} on a {@code FutureTypeAdapter} before its delegate
     * adapter has been set results in an {@link IllegalStateException}. This scenario typically
     * occurs when Gson handles cyclic type dependencies.
     */
    @Test
    public void toJsonOnUnresolvedFutureAdapterThrowsIllegalStateException() {
        // Arrange: A FutureTypeAdapter is a placeholder used by Gson to handle
        // recursive type definitions. It is "unresolved" until its delegate
        // TypeAdapter is set.
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();

        try {
            // Act: Attempting to use the adapter before it is resolved (by passing itself
            // to be serialized) should trigger an exception.
            futureAdapter.toJson(futureAdapter);
            fail("Expected an IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            // Assert: The exception message should clearly indicate the cause of the error.
            String expectedMessage = "Adapter for type with cyclic dependency has been used "
                                   + "before dependency has been resolved";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}