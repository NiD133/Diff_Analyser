package com.google.gson;

import org.junit.Test;

/**
 * Test suite for the {@link TypeAdapter} class, focusing on specific edge cases.
 */
public class TypeAdapter_ESTestTest9 { // Note: In a real project, this class would be named TypeAdapterTest

    /**
     * Tests that calling toJson on a FutureTypeAdapter that delegates to itself
     * results in a StackOverflowError due to infinite recursion. This is a valid
     * test for recursive data structures.
     *
     * Note: Gson.FutureTypeAdapter is a package-private inner class used by Gson
     * to handle circular dependencies. This test has access to it because it resides
     * in the same 'com.google.gson' package.
     */
    @Test(expected = StackOverflowError.class)
    public void toJsonWithRecursiveDelegateShouldCauseStackOverflow() {
        // Arrange: Create a FutureTypeAdapter and set its delegate to itself,
        // creating a recursive dependency.
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        futureAdapter.setDelegate(futureAdapter);

        // Act & Assert: Calling toJson on the adapter with itself as the value
        // should cause infinite recursion. The @Test(expected=...) annotation
        // asserts that a StackOverflowError is thrown.
        futureAdapter.toJson(futureAdapter);
    }
}