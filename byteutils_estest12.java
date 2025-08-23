package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for the {@link ByteUtils.OutputStreamByteConsumer} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that the OutputStreamByteConsumer correctly writes a single byte
     * to its underlying OutputStream.
     */
    @Test
    public void outputStreamByteConsumerShouldWriteByteToStream() throws IOException {
        // Arrange: Create a stream to capture output and a consumer that wraps it.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteUtils.ByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(outputStream);
        final int byteToWrite = 1;

        // Act: Pass a byte to the consumer.
        consumer.accept(byteToWrite);

        // Assert: Verify that the byte was written to the underlying stream.
        byte[] expectedBytes = { (byte) byteToWrite };
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}