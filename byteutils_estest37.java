package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(InputStream, int)} throws an IOException
     * when the stream contains fewer bytes than requested.
     */
    @Test
    public void fromLittleEndianInputStreamShouldThrowIOExceptionWhenStreamIsTooShort() {
        // Arrange: Set up an input stream that has fewer bytes than we attempt to read.
        final int bytesToRead = 8;
        final int availableBytes = 6; // Intentionally less than bytesToRead
        final byte[] inputData = new byte[availableBytes];
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        // Act & Assert: Verify that an IOException is thrown with the correct message.
        // We use a try-catch block to assert the specific exception message,
        // which is more precise than using @Test(expected = IOException.class).
        try {
            ByteUtils.fromLittleEndian(inputStream, bytesToRead);
            fail("Expected an IOException because the stream ended prematurely.");
        } catch (final IOException e) {
            // The method is expected to throw an IOException with a specific message
            // when it cannot read the requested number of bytes.
            assertEquals("Premature end of data", e.getMessage());
        }
    }
}