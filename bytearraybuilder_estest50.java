package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Tests for the {@link ByteArrayBuilder} class, focusing on edge cases.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@code toByteArray()} on a builder initialized with a
     * null buffer via the {@code fromInitial()} factory method throws a
     * {@code NullPointerException}.
     *
     * This scenario tests an inconsistent state where the builder is given a non-zero
     * length but a null buffer to start with.
     */
    @Test(expected = NullPointerException.class)
    public void toByteArray_whenInitializedWithNullBuffer_shouldThrowNullPointerException() {
        // Arrange: Create a ByteArrayBuilder with a null initial buffer but a non-zero length.
        // The specific length (497) is arbitrary; any non-zero value would likely trigger the issue.
        final int initialLength = 497;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, initialLength);

        // Act: Attempt to convert the builder's contents to a byte array.
        // This is expected to throw a NullPointerException because the internal buffer is null.
        builder.toByteArray();

        // Assert: The test is successful if a NullPointerException is thrown,
        // as declared by the @Test(expected=...) annotation.
    }
}