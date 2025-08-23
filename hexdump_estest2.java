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
     * Tests that the dump method correctly formats a byte array into a hexadecimal string representation.
     * This test covers a partial line of output and includes non-printable characters.
     */
    @Test
    public void testDumpByteArray() throws IOException {
        // Arrange: Set up the input data and a writer to capture the output.
        // The input is a 7-byte array. The first byte, 0xFF (decimal -1), is a non-printable character.
        final byte[] dataToDump = {(byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        final StringWriter outputWriter = new StringWriter();

        // Arrange: Define the expected output string.
        // The output should include the offset, the hex values padded to a full 16-byte line,
        // and the ASCII representation (with '.' for non-printable characters), followed by a newline.
        final String EOL = System.lineSeparator();
        final String expectedOutput = "00000000  FF 00 00 00 00 00 00                                 ......." + EOL;

        // Act: Call the method under test.
        HexDump.dump(dataToDump, outputWriter);

        // Assert: Verify that the captured output matches the expected format.
        assertEquals(expectedOutput, outputWriter.toString());
    }
}