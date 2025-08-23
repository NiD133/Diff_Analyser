package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ByteUtils}.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(OutputStream, long, int)}
     * correctly propagates an {@link IOException} thrown by the underlying stream.
     */
    @Test(timeout = 4000)
    public void toLittleEndianShouldPropagateIOExceptionFromOutputStream() {
        // Arrange: Create a PipedOutputStream that is not connected to an input stream.
        // Any attempt to write to this stream will result in an IOException.
        PipedOutputStream unconnectedStream = new PipedOutputStream();
        final long valueToWrite = 1L;
        final int length = 8; // Using the size of a long in bytes for clarity.

        // Act & Assert
        try {
            ByteUtils.toLittleEndian(unconnectedStream, valueToWrite, length);
            fail("Expected an IOException to be thrown, but the operation succeeded.");
        } catch (IOException e) {
            // Verify that the propagated exception is the one we expect from the unconnected pipe.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}