package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Tests the behavior of {@link TypeAdapter} when a circular delegation is created
 * using the internal {@code Gson.FutureTypeAdapter}.
 */
public class TypeAdapterCircularDelegationTest {

    /**
     * Tests that calling fromJson on a FutureTypeAdapter with a circular reference
     * to another FutureTypeAdapter results in a StackOverflowError.
     */
    @Test
    public void fromJson_withCircularDelegation_shouldThrowStackOverflowError() {
        // Arrange: Create two FutureTypeAdapters and set them as delegates for each other,
        // creating a circular dependency.
        Gson.FutureTypeAdapter<Object> adapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> adapter2 = new Gson.FutureTypeAdapter<>();
        adapter1.setDelegate(adapter2);
        adapter2.setDelegate(adapter1);

        // Act & Assert: Calling fromJson should cause infinite recursion, leading to a
        // StackOverflowError. The content of the JSON string is irrelevant.
        assertThrows(StackOverflowError.class, () -> {
            adapter1.fromJson("any string");
        });
    }
}