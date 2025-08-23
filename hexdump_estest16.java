package org.apache.commons.io;

import org.junit.Test;
import java.io.IOException;
import java.io.PipedWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link HexDump}.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws an IOException when attempting to write
     * to an Appendable that is not ready for writing, such as an unconnected PipedWriter.
     */
    @Test
    public void dump_toUnconnectedPipedWriter_shouldThrowIOException() {
        // Arrange: Create some data and an unconnected PipedWriter.
        // A PipedWriter must be connected to a PipedReader before it can be used.
        final byte[] dataToDump = new byte[]{0x00, 0x01, 0x7F};
        final PipedWriter unconnectedWriter = new PipedWriter();

        // Act & Assert: Verify that calling dump with the unconnected writer throws an IOException.
        final IOException exception = assertThrows(
                IOException.class,
                () -> HexDump.dump(dataToDump, unconnectedWriter)
        );

        // Assert: Check the exception message for more specific verification.
        assertEquals("Pipe not connected", exception.getMessage());
    }
}