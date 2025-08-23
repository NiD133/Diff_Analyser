package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that the dump method correctly formats a partial line of data (less than 16 bytes)
     * with a given offset. It verifies the entire formatted string, including the offset,
     * hexadecimal values, and ASCII representation.
     */
    @Test
    public void testDumpWithOffsetAndPartialLine() throws IOException {
        // Arrange
        final long offset = 0x100; // Use a clear, hexadecimal offset for readability
        final int startIndex = 0;
        // Use meaningful data that includes printable and non-printable characters
        final byte[] dataToDump = new byte[] { 'H', 'e', 'l', 'l', 'o', 0x0A }; // "Hello\n"

        // The expected output format for a line is:
        // [8-char hex offset] [space] [16 bytes in hex] [space] [16 bytes in ASCII] [EOL]
        // Our data is 6 bytes, so the line will be padded.
        final String expectedHexValues = "48 65 6C 6C 6F 0A";
        final String expectedAsciiValues = "Hello."; // 0x0A is a non-printable newline

        // Construct the expected full output line with correct padding.
        final String expectedOutput = String.format(
            "%08X %-48s %-16s%s",
            offset,
            expectedHexValues,
            expectedAsciiValues,
            System.lineSeparator()
        );

        // Use ByteArrayOutputStream to capture the output in memory without using the file system.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        HexDump.dump(dataToDump, offset, outputStream, startIndex);

        // Assert
        // Verify the actual string content, which is more robust than just checking the length.
        final String actualOutput = outputStream.toString(StandardCharsets.UTF_8.name());
        assertEquals(expectedOutput, actualOutput);
    }
}