package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Contains unit tests for the {@link ByteArrayBuilder} class, focusing on
 * its behavior in edge cases and invalid states.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that {@link ByteArrayBuilder#getClearAndRelease()} throws a
     * {@link NegativeArraySizeException} if the current segment length is
     * manually set to a negative value.
     *
     * This test ensures that the builder correctly handles an internally inconsistent
     * state that would otherwise lead to an attempt to allocate a negative-sized array.
     */
    @Test
    public void getClearAndRelease_whenCurrentSegmentLengthIsNegative_shouldThrowException() {
        // Arrange: Create a builder and put it into an invalid state by setting a negative segment length.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-1);

        // Act & Assert: Verify that attempting to retrieve the byte array throws the expected exception.
        // The method getClearAndRelease() internally tries to create a new byte array whose size
        // is based on the current segment length, which is now negative.
        assertThrows(NegativeArraySizeException.class, builder::getClearAndRelease);
    }
}