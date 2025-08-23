package org.apache.commons.io.input;

import org.junit.Test;

/**
 * Tests for {@link QueueInputStream} focusing on invalid arguments for the read method.
 */
public class QueueInputStreamTest {

    /**
     * Tests that calling the read(byte[], int, int) method with a negative length
     * throws an IndexOutOfBoundsException, as specified by the InputStream contract.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithNegativeLengthShouldThrowIndexOutOfBoundsException() {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] buffer = new byte[10];
        final int validOffset = 0;
        final int negativeLength = -1;

        // Act
        // This call is expected to fail because the length parameter is negative.
        inputStream.read(buffer, validOffset, negativeLength);

        // Assert: The test passes if the expected IndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}