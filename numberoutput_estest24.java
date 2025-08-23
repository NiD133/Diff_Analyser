package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class contains an improved version of an auto-generated test case
 * for the {@link NumberOutput} class.
 *
 * The original class name `NumberOutput_ESTestTest24` and its scaffolding parent
 * are preserved to maintain compatibility with the existing test suite structure.
 */
public class NumberOutput_ESTestTest24 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that calling {@code outputLong} with a byte array buffer that is
     * too small to hold the number's string representation throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongThrowsExceptionWhenBufferIsTooSmall() {
        // Arrange: A long value that requires more bytes than the buffer provides.
        // The number 7,174,648,137,343,063,403L is 19 digits long and requires 19 bytes.
        long largeNumber = 7174648137343063403L;
        byte[] insufficientBuffer = new byte[5]; // The buffer is intentionally too small.

        // Act & Assert: Attempt to write the long into the buffer.
        // The test expects an ArrayIndexOutOfBoundsException, which is declared
        // in the @Test annotation above.
        NumberOutput.outputLong(largeNumber, insufficientBuffer, 0);
    }
}