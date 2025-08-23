package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Verifies that when the 'lineFeedAtEos' flag is true, a CRLF sequence (\r\n)
     * is appended to an input stream that does not already end with a line ending.
     */
    @Test
    public void readShouldAppendCRLFAtEndOfStreamWhenFlagIsTrue() throws IOException {
        // Arrange: Create an input stream with simple text that has no line endings.
        final String inputString = "Hello World";
        final byte[] inputBytes = inputString.getBytes(StandardCharsets.US_ASCII);
        final InputStream sourceStream = new ByteArrayInputStream(inputBytes);

        // The class under test is configured to ensure a line feed at the end of the stream.
        final boolean ensureLineFeedAtEos = true;
        final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(sourceStream, ensureLineFeedAtEos);

        // The expected output is the original string followed by a Windows line ending.
        final byte[] expectedBytes = (inputString + "\r\n").getBytes(StandardCharsets.US_ASCII);

        // Act: Read the entire stream to trigger the end-of-stream behavior.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = windowsStream.read()) != -1) {
            outputStream.write(byteRead);
        }
        final byte[] actualBytes = outputStream.toByteArray();

        // Assert: The actual output should match the expected output with the appended CRLF.
        assertArrayEquals("The stream should have a CRLF appended at the end.", expectedBytes, actualBytes);
    }
}