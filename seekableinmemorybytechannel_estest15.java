package org.apache.commons.compress.utils;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the read() method throws a NullPointerException
     * when called with a null ByteBuffer argument.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullBufferThrowsNullPointerException() {
        // Arrange: Create an empty channel.
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act: Attempt to read into a null buffer.
        // The test expects this call to throw a NullPointerException.
        channel.read((ByteBuffer) null);

        // Assert: The expected exception is verified by the @Test annotation.
    }
}