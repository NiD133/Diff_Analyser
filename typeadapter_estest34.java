package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Tests that calling nullSafe() on a TypeAdapter that is already null-safe
     * returns the same instance, rather than wrapping it again. This ensures
     * the method is idempotent and avoids unnecessary object creation.
     */
    @Test
    public void nullSafe_onAlreadyNullSafeAdapter_returnsSameInstance() {
        // Arrange: Create a base TypeAdapter and then its null-safe wrapper.
        // We use FutureTypeAdapter as a simple, concrete implementation for this test.
        TypeAdapter<Integer> originalAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> nullSafeAdapter = originalAdapter.nullSafe();

        // Act: Call nullSafe() again on the adapter that is already null-safe.
        TypeAdapter<Integer> resultAdapter = nullSafeAdapter.nullSafe();

        // Assert: The result should be the exact same instance as the first
        // null-safe adapter, demonstrating idempotency.
        assertSame("Calling nullSafe() multiple times should not create extra wrappers",
                nullSafeAdapter, resultAdapter);
    }
}