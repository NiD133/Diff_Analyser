package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedOutputStream;
import org.junit.Test;

/**
 * Tests for {@link HexDump}.
 */
public class HexDumpTest {

    /**
     * Verifies that calling HexDump.dump() with an unconnected PipedOutputStream
     * results in an IOException, as the underlying stream cannot be written to.
     */
    @Test
    public void testDumpToUnconnectedPipeThrowsIOException() {
        // Arrange: Create test data and an output stream that is not connected to an input stream.
        final byte[] data = new byte[16]; // The data content is irrelevant for this test.
        final PipedOutputStream unconnectedPipe = new PipedOutputStream();

        // Act & Assert: Expect an IOException when attempting to write to the unconnected pipe.
        // The assertThrows method checks that the provided lambda throws the specified exception.
        final IOException thrown = assertThrows(IOException.class, () -> {
            HexDump.dump(data, 0L, unconnectedPipe, 0);
        });

        // Further verify that the exception message is what we expect, making the test more robust.
        assertEquals("Pipe not connected", thrown.getMessage());
    }
}