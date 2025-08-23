package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws a BufferOverflowException when the target
     * Appendable (a CharBuffer) has insufficient capacity to hold the formatted output.
     */
    @Test(expected = BufferOverflowException.class)
    public void dumpShouldThrowBufferOverflowExceptionWhenAppendableIsTooSmall() throws IOException {
        // Arrange: Create data to be dumped. The hex dump output for 10 bytes
        // is significantly larger than the 2-character buffer we provide.
        byte[] dataToDump = new byte[10];
        CharBuffer insufficientBuffer = CharBuffer.wrap(new char[2]);

        // Act: Attempt to dump the data into the undersized buffer.
        // Assert: The @Test(expected) annotation asserts that a BufferOverflowException is thrown.
        HexDump.dump(dataToDump, insufficientBuffer);
    }
}