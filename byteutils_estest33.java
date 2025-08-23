package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PipedInputStream;

/**
 * Test suite for {@link ByteUtils}.
 * This specific test class focuses on edge cases and error handling.
 */
// The original class name and inheritance are preserved to maintain compatibility
// with the auto-generation tool's test infrastructure.
public class ByteUtils_ESTestTest33 extends ByteUtils_ESTest_scaffolding {

    /**
     * Verifies that fromLittleEndian throws an IOException when attempting to read
     * from a stream that is not ready, such as an unconnected PipedInputStream.
     */
    @Test(expected = IOException.class, timeout = 4000)
    public void fromLittleEndianOnUnconnectedStreamThrowsIOException() throws IOException {
        // Arrange: Create a PipedInputStream that is not connected to an output stream.
        // According to its contract, any attempt to read from it will throw an IOException.
        PipedInputStream unconnectedPipe = new PipedInputStream();
        DataInput dataInput = new DataInputStream(unconnectedPipe);

        // Act & Assert: Attempt to read from the unconnected stream.
        // The @Test(expected = IOException.class) annotation asserts that the expected
        // exception is thrown.
        ByteUtils.fromLittleEndian(dataInput, 1);
    }
}