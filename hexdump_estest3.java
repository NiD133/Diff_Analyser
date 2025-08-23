package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that calling HexDump.dump() with a length of zero produces no output.
     * The method should handle this edge case gracefully without writing anything
     * to the provided Appendable.
     */
    @Test
    public void dumpWithZeroLengthShouldWriteNothing() throws IOException {
        // Arrange
        final byte[] data = new byte[8]; // The content of the data array is irrelevant for this test.
        final StringWriter writer = new StringWriter();
        final long offset = 0L;
        final int startIndex = 0;
        final int length = 0;

        // Act
        HexDump.dump(data, offset, writer, startIndex, length);

        // Assert
        assertEquals("Output should be empty when dump length is zero.", "", writer.toString());
    }
}