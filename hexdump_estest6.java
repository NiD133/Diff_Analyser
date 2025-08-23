package org.apache.commons.io;

import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * Tests for the {@link HexDump} class, focusing on exception scenarios.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws a ReadOnlyBufferException when attempting to write
     * to an Appendable that is a read-only buffer.
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void testDumpThrowsExceptionWhenTargetBufferIsReadOnly() {
        // Arrange: Create a sample byte array and a read-only buffer.
        // CharBuffer.wrap(CharSequence) is documented to create a read-only buffer.
        byte[] dataToDump = new byte[16];
        Appendable readOnlyBuffer = CharBuffer.wrap("read-only");

        // Act & Assert: Attempting to dump data into the read-only buffer
        // should throw a ReadOnlyBufferException.
        HexDump.dump(dataToDump, 0L, readOnlyBuffer, 0, dataToDump.length);
    }
}