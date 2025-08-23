package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * This test suite focuses on the behavior of the ByteArrayBuilder class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that attempting to append a byte after the internal buffer pointer
     * has been set to an invalid negative index throws an
     * {@link ArrayIndexOutOfBoundsException}.
     *
     * This test case simulates a scenario where the builder's internal state
     * has been corrupted.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendShouldThrowExceptionWhenSegmentLengthIsNegative() {
        // Arrange: Create a builder and manually set its internal state to be invalid.
        // The current segment length (which acts as a pointer) is set to a negative value.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-1);

        // Act: Attempt to append a byte. This operation should try to access the
        // backing array at a negative index, causing an exception.
        builder.append(42);

        // Assert: The test framework verifies that an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the `expected` parameter in the @Test annotation.
    }
}