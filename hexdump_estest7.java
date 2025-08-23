package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Tests for {@link HexDump}.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump throws a BufferOverflowException when the provided
     * Appendable (a CharBuffer in this case) has insufficient capacity to hold
     * the formatted hex output.
     */
    @Test(expected = BufferOverflowException.class)
    public void testDumpThrowsBufferOverflowExceptionForInsufficientlySizedAppendable() throws IOException {
        // Arrange: Create data to dump and a buffer that is too small for the output.
        byte[] dataToDump = { 0x00, 0x01, 0x02 };

        // The hex dump format includes an offset, hex codes, and ASCII representation,
        // which requires significant space. A buffer of size 3 is guaranteed to be too small
        // as even the initial offset string (e.g., "00000000 ") exceeds this capacity.
        final int insufficientCapacity = 3;
        Appendable smallBuffer = CharBuffer.allocate(insufficientCapacity);

        long initialOffset = 0L;
        int startIndex = 0;
        int bytesToDump = 1; // We only need to attempt to dump one byte to trigger the overflow.

        // Act & Assert: Calling dump with the small buffer is expected to throw the exception.
        HexDump.dump(dataToDump, initialOffset, smallBuffer, startIndex, bytesToDump);
    }
}