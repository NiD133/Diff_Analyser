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
     * Tests that HexDump.dump() correctly formats a single byte from an array,
     * respecting the given offset, index, and length parameters.
     */
    @Test
    public void testDumpSingleByteWithOffsetAndLength() throws IOException {
        // Arrange: Set up the input data and expected output.
        final long offset = 110L; // The starting offset for the hex dump (0x6E).
        final int index = 0;      // The starting index within the data array.
        final int length = 1;     // The number of bytes to dump.

        // The input data array. Only the byte at the specified index is relevant.
        final byte[] data = new byte[37];
        data[index] = 'n'; // The byte to be dumped (ASCII 110, hex 6E).

        final StringWriter outputWriter = new StringWriter();

        // The expected output line consists of the offset, the byte's hex value,
        // padding to fill a standard 16-byte line, the byte's ASCII representation,
        // and a line separator.
        final String EOL = System.lineSeparator();
        final String expectedOutput = "0000006E: 6E                                               n" + EOL;

        // Act: Call the method under test.
        HexDump.dump(data, offset, outputWriter, index, length);

        // Assert: Verify that the actual output matches the expected format.
        assertEquals(expectedOutput, outputWriter.toString());
    }
}