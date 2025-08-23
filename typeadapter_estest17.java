package com.google.gson;

import org.junit.Test;

/**
 * Tests for the behavior of {@link TypeAdapter}, particularly focusing on recursive delegation scenarios.
 */
public class TypeAdapterTest {

    /**
     * Verifies that a FutureTypeAdapter which delegates to itself will cause a
     * StackOverflowError when its methods are called. This tests the behavior of
     * infinitely recursive type adapter setups.
     */
    @Test(expected = StackOverflowError.class)
    public void fromJsonTreeWithRecursiveDelegateThrowsStackOverflowError() {
        // Arrange: Create a FutureTypeAdapter and set it as its own delegate
        // to establish an infinite recursive loop.
        Gson.FutureTypeAdapter<Object> recursiveAdapter = new Gson.FutureTypeAdapter<>();
        recursiveAdapter.setDelegate(recursiveAdapter);

        // Act: Call a method on the adapter. This will trigger the infinite recursion.
        // The @Test(expected=...) annotation asserts that a StackOverflowError is thrown.
        recursiveAdapter.fromJsonTree(null);
    }
}