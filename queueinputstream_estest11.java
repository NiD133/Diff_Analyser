package org.apache.commons.io.input;

import org.junit.Test;

/**
 * Contains tests for the {@link QueueInputStream} class, focusing on
 * invalid arguments for the read method.
 */
public class QueueInputStreamTest {

    /**
     * Tests that calling read(byte[], int, int) with a negative offset and
     * a negative length throws an IndexOutOfBoundsException. This behavior is
     * consistent with the general contract of {@link java.io.InputStream#read(byte[], int, int)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithNegativeOffsetAndLengthShouldThrowException() {
        // Arrange: Create an input stream and a buffer for the read operation.
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] buffer = new byte[10];
        final int negativeOffset = -1;
        final int negativeLength = -1;

        // Act: Attempt to read from the stream with invalid parameters.
        // The test expects this line to throw an IndexOutOfBoundsException.
        inputStream.read(buffer, negativeOffset, negativeLength);

        // Assert: The expected exception is declared in the @Test annotation,
        // so no explicit assertion is needed here.
    }
}