package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on edge cases.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#toByteArray()} throws a
     * {@link NegativeArraySizeException} if the builder was created with a negative
     * initial length.
     *
     * The factory method {@code fromInitial} allows setting an initial length, which
     * is used to calculate the total size. If this length is negative, the subsequent
     * array allocation in {@code toByteArray} should fail.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void toByteArrayShouldThrowExceptionWhenInitializedWithNegativeLength() {
        // Arrange: Create a ByteArrayBuilder instance using a factory method that allows
        // setting a negative initial length.
        final int negativeLength = -538;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, negativeLength);

        // Act: Attempt to create the final byte array. This operation should fail
        // because it will try to allocate an array with a negative size.
        builder.toByteArray();

        // Assert: The test succeeds if a NegativeArraySizeException is thrown,
        // as declared in the @Test annotation.
    }
}