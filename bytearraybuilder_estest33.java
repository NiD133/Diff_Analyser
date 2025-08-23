package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on edge cases and exception handling.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#completeAndCoalesce(int)} with a negative
     * length argument correctly throws a {@link NegativeArraySizeException}. This is expected
     * behavior, as it's impossible to create an array with a negative size.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void completeAndCoalesce_withNegativeLength_shouldThrowException() {
        // Given: A new ByteArrayBuilder instance
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int negativeLastBlockLength = -270;

        // When: The method under test is called with an invalid negative argument
        builder.completeAndCoalesce(negativeLastBlockLength);

        // Then: A NegativeArraySizeException is expected, as declared by the @Test annotation.
    }
}