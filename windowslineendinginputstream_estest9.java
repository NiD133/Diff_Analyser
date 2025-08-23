package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link WindowsLineEndingInputStream} class.
 * This test focuses on behavior with an empty input stream.
 */
// Note: Removed EvoSuite-specific runner and scaffolding for clarity.
// A standard JUnit runner can execute this test.
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that an empty input stream, when configured with 'lineFeedAtEos' as true,
     * correctly injects a CRLF ("\r\n") sequence before signaling the end of the stream.
     */
    @Test
    public void read_onEmptyStreamWithEnsureLineFeedAtEos_returnsCRLFThenEOF() throws IOException {
        // Arrange: Create an empty input stream and wrap it with WindowsLineEndingInputStream,
        // configured to ensure a line ending at the end of the stream.
        final byte[] emptyInputData = new byte[0];
        final InputStream emptyStream = new ByteArrayInputStream(emptyInputData);
        final boolean ensureLineFeedAtEos = true;
        
        try (final WindowsLineEndingInputStream windowsStream =
                     new WindowsLineEndingInputStream(emptyStream, ensureLineFeedAtEos)) {

            // Act & Assert:
            // 1. The stream should first inject a Carriage Return ('\r').
            assertEquals("Expected the first byte to be a Carriage Return (CR).",
                    '\r', windowsStream.read());

            // 2. The stream should then inject a Line Feed ('\n').
            assertEquals("Expected the second byte to be a Line Feed (LF).",
                    '\n', windowsStream.read());

            // 3. Finally, the stream should report the end of the file.
            assertEquals("Expected the end of the stream.",
                    -1, windowsStream.read());
        }
    }
}