package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the behavior of {@link TypeAdapter}.
 * The original test class name and inheritance are kept to focus on the test case improvement.
 * Unused imports and EvoSuite-specific annotations have been removed for clarity.
 */
public class TypeAdapter_ESTestTest2 extends TypeAdapter_ESTest_scaffolding {

    /**
     * Tests that calling {@link TypeAdapter#toJsonTree(Object)} on a null-safe adapter
     * with a null value correctly returns a {@link JsonNull} instance.
     */
    @Test
    public void toJsonTree_onNullSafeAdapterWithNullInput_returnsJsonNull() {
        // Arrange
        // We need a concrete TypeAdapter instance to call nullSafe() on.
        // Gson.FutureTypeAdapter is a convenient, accessible implementation for this purpose.
        TypeAdapter<Integer> originalAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> nullSafeAdapter = originalAdapter.nullSafe();

        // Act
        // Convert a null Integer to a JsonElement using the null-safe adapter.
        JsonElement result = nullSafeAdapter.toJsonTree(null);

        // Assert
        // The nullSafe() wrapper should handle the null input and produce a JsonNull object,
        // rather than a Java null or throwing an exception.
        assertEquals(JsonNull.INSTANCE, result);
    }
}