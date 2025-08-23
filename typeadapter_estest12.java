package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link TypeAdapter} class.
 * This class focuses on specific edge cases and behaviors.
 */
public class TypeAdapterTest {

    /**
     * Tests that calling read() on a FutureTypeAdapter that delegates to itself
     * results in a StackOverflowError due to infinite recursion.
     *
     * This scenario can occur with circular object dependencies, and this test ensures
     * the behavior is a stack overflow, as expected from a recursive call loop.
     */
    @Test
    public void readWithRecursiveDelegateShouldThrowStackOverflowError() {
        // Arrange: Create a FutureTypeAdapter and set it as its own delegate,
        // creating an infinite recursive loop.
        Gson.FutureTypeAdapter<Object> recursiveAdapter = new Gson.FutureTypeAdapter<>();
        recursiveAdapter.setDelegate(recursiveAdapter);

        // Act & Assert: Verify that calling read() causes a StackOverflowError.
        // The JsonReader argument is null because the error occurs before it's ever used.
        assertThrows(StackOverflowError.class, () -> {
            recursiveAdapter.read((JsonReader) null);
        });
    }
}