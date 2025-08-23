package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on specific
 * edge cases and behaviors.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@code completeAndCoalesce} on a builder that was
     * initialized with a null buffer throws a NullPointerException.
     *
     * This test ensures the builder correctly handles an invalid state where it's
     * created with a null internal buffer, which is not a standard use case but
     * should be handled gracefully.
     */
    @Test(expected = NullPointerException.class)
    public void completeAndCoalesce_whenInitializedWithNullBuffer_shouldThrowNullPointerException() {
        // Arrange: Create a ByteArrayBuilder from a null initial byte array.
        // This sets up an invalid internal state where the current block is null.
        // The length parameter is arbitrary for this test, as the null buffer is the key factor.
        int invalidInitialLength = 2;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, invalidInitialLength);

        // Act & Assert: Attempting to complete the operation on the null buffer.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        builder.completeAndCoalesce(invalidInitialLength);
    }
}