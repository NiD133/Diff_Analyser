package com.google.gson;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Contains tests for {@link TypeAdapter}, focusing on recursive delegation scenarios.
 */
// Note: The original test class name and scaffolding are kept for context.
public class TypeAdapter_ESTestTest24 extends TypeAdapter_ESTest_scaffolding {

    /**
     * Verifies that calling {@link TypeAdapter#fromJson(Reader)} on a {@code FutureTypeAdapter}
     * that delegates to itself results in a {@link StackOverflowError}.
     *
     * This test case simulates a misconfigured circular dependency where a type adapter
     * recursively calls itself, leading to infinite recursion.
     */
    @Test(expected = StackOverflowError.class, timeout = 4000)
    public void fromJsonOnRecursiveAdapterThrowsStackOverflowError() throws IOException {
        // Arrange: Create a FutureTypeAdapter and set its delegate to itself.
        // This setup creates an infinite loop: fromJson() -> read() -> delegate.read() -> read() ...
        Gson.FutureTypeAdapter<Object> recursiveAdapter = new Gson.FutureTypeAdapter<>();
        recursiveAdapter.setDelegate(recursiveAdapter);

        // The content of the reader is irrelevant, as the stack overflow occurs
        // before any data is processed. An empty string is used for clarity.
        Reader dummyReader = new StringReader("");

        // Act & Assert: Calling fromJson is expected to cause a StackOverflowError.
        // The @Test(expected=...) annotation asserts this behavior.
        recursiveAdapter.fromJson(dummyReader);
    }
}