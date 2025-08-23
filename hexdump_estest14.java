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
     * Tests that HexDump.dump correctly formats a subset of a byte array,
     * respecting the given offset, index, and length parameters.
     */
    @Test
    public void testDumpWithOffsetAndLengthDumpsCorrectRange() throws IOException {
        // Arrange
        // Create a data array with more bytes than we will dump to ensure
        // the method respects the length parameter. All elements are 0x00 by default.
        final byte[] data = new byte[40];

        // The dump will start from the second byte (index 1) of the data array.
        final int startIndex = 1;
        // We will dump 32 bytes, which should result in two full 16-byte lines of output.
        final int dumpLength = 32;
        // The hex dump's address column will start at this offset.
        final long initialOffset = 1L;

        final StringWriter writer = new StringWriter();

        // Define the expected output. The dump should produce two lines.
        // The first line's offset is `initialOffset`.
        // The second line's offset is `initialOffset + 16`.
        final String lineSeparator = System.lineSeparator();
        final String expectedOutput =
                "00000001 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ................" + lineSeparator +
                "00000011 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ................" + lineSeparator;

        // Act
        HexDump.dump(data, initialOffset, writer, startIndex, dumpLength);

        // Assert
        // Verify that the actual output matches the expected hex dump format.
        assertEquals(expectedOutput, writer.toString());
    }
}