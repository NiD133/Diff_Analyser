package org.apache.commons.io.input;

import org.junit.Test;

/**
 * Unit tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that {@link QueueInputStream#read(byte[], int, int)} throws a
     * {@link NullPointerException} when the provided buffer is null, as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullBufferShouldThrowNullPointerException() {
        // Arrange: Create an instance of the stream under test.
        final QueueInputStream inputStream = new QueueInputStream();

        // Act & Assert: Call the method with a null buffer.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        // The offset and length values are irrelevant for this test as the null check on the
        // buffer is performed first.
        inputStream.read(null, 0, 10);
    }
}