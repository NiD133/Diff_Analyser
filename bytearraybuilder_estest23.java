package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * This test class verifies the behavior of the {@link ByteArrayBuilder} class,
 * specifically focusing on edge cases related to its internal state.
 *
 * This version is a refactoring of an auto-generated EvoSuite test to improve
 * clarity and maintainability.
 */
public class ByteArrayBuilder_ESTestTest23 extends ByteArrayBuilder_ESTest_scaffolding {

    /**
     * Tests that calling {@code write(byte[])} on a {@link ByteArrayBuilder}
     * with a negative internal segment length pointer throws an
     * {@link ArrayIndexOutOfBoundsException}.
     * <p>
     * This scenario simulates a corrupted internal state to ensure the method
     * fails fast rather than causing unpredictable behavior.
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void writeShouldThrowExceptionOnNegativeSegmentLength() {
        // Arrange: Create a ByteArrayBuilder. The initial size is not critical for this test.
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Manually set the current segment length to a negative value,
        // which puts the builder into an invalid state. This is the condition under test.
        builder.setCurrentSegmentLength(-1);

        // Attempt to write a byte array. This should trigger the exception because the
        // internal pointer (_currBlockPtr) is now out of bounds.
        builder.write(new byte[1]);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // declared in the @Test annotation. If no exception is thrown, the test will fail.
    }
}