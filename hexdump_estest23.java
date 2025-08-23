package org.apache.commons.io;

import org.junit.Test;

import java.io.OutputStream;

/**
 * Tests for the {@link HexDump} utility class.
 * This test focuses on verifying argument validation.
 */
public class HexDump_ESTestTest23 { // Note: The class name is kept from the original for consistency.

    /**
     * Tests that HexDump.dump() throws a NullPointerException when the provided
     * OutputStream is null. The method is expected to validate its arguments
     * before attempting to process any data.
     */
    @Test(expected = NullPointerException.class)
    public void dumpWithNullOutputStreamShouldThrowNullPointerException() {
        // Arrange: Define valid inputs for all parameters except for the stream.
        final byte[] data = new byte[16];
        final long offset = 0L;
        final int index = 0;
        final OutputStream nullStream = null;

        // Act & Assert: This call is expected to throw a NullPointerException
        // because the output stream is null. The assertion is handled by the
        // 'expected' attribute of the @Test annotation.
        HexDump.dump(data, offset, nullStream, index);
    }
}