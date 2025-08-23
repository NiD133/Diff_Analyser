package com.google.gson;

import org.junit.Test;

/**
 * Tests for specific scenarios in {@link TypeAdapter}, focusing on the behavior of delegated adapters.
 */
public class TypeAdapterTest {

    /**
     * Verifies that a circular dependency between two delegating TypeAdapters
     * causes a {@link StackOverflowError} when used for serialization.
     *
     * <p>This test simulates a scenario that can occur with recursive data structures where a
     * TypeAdapter for a type might indirectly delegate back to itself.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void toJsonTree_withCircularDelegation_throwsStackOverflowError() {
        // Arrange: Create two TypeAdapters that delegate to each other, forming a cycle.
        // Gson.FutureTypeAdapter is a placeholder that delegates calls to another adapter
        // once it's set.
        Gson.FutureTypeAdapter<Object> adapterA = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> adapterB = new Gson.FutureTypeAdapter<>();

        adapterA.setDelegate(adapterB);
        adapterB.setDelegate(adapterA); // This creates the circular dependency.

        // Act & Assert: Attempt to serialize an object using one of the adapters.
        // The call to toJsonTree should trigger an infinite recursion between adapterA and
        // adapterB, resulting in a StackOverflowError. The test will pass if this
        // specific error is thrown.
        adapterA.toJsonTree("any object");
    }
}