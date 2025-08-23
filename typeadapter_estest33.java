package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Tests that attempting to use a {@link Gson.FutureTypeAdapter} before it has been
     * resolved (by setting its delegate) throws an {@link IllegalStateException}.
     * This behavior is expected even when the adapter is wrapped by {@link #nullSafe()}.
     */
    @Test
    public void toJsonTreeOnNullSafeUnresolvedFutureAdapterThrowsIllegalStateException() {
        // Arrange: Create a FutureTypeAdapter, which acts as a placeholder for a real
        // TypeAdapter, typically to resolve cyclic dependencies.
        // It remains "unresolved" because we do not call setDelegate() on it.
        Gson.FutureTypeAdapter<Object> unresolvedFutureAdapter = new Gson.FutureTypeAdapter<>();

        // Wrap the unresolved adapter with nullSafe(). The resulting adapter will
        // delegate calls to the unresolved one.
        TypeAdapter<Object> nullSafeAdapter = unresolvedFutureAdapter.nullSafe();

        // The object being serialized is not important for this test; any object would
        // trigger the exception. We use the adapter itself as the value for simplicity.
        Object valueToSerialize = unresolvedFutureAdapter;

        // Act & Assert: Attempting to use the adapter should fail because its delegate
        // has not been set.
        try {
            nullSafeAdapter.toJsonTree(valueToSerialize);
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException expected) {
            assertEquals(
                "Adapter for type with cyclic dependency has been used before dependency has been resolved",
                expected.getMessage()
            );
        }
    }
}