package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the ArArchiveOutputStream class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that writing the first entry to an AR archive correctly writes
     * both the global archive header and the entry's header.
     */
    @Test
    public void putFirstArchiveEntryWritesArchiveAndEntryHeaders() throws IOException {
        // Arrange
        // The AR format has a global "magic" header and a header for each entry.
        // 1. Global AR magic header: "!<arch>\n" is 8 bytes.
        // 2. Standard AR entry header: 60 bytes.
        final long expectedHeaderSize = 8L + 60L;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Use a try-with-resources statement to ensure the stream is closed automatically.
        try (ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream)) {
            // Create a simple entry. The specific metadata values are not critical for this test,
            // as long as the name is short enough to not trigger long filename handling.
            ArArchiveEntry entry = new ArArchiveEntry("test_entry.txt", 1024L);

            // Act
            arOut.putArchiveEntry(entry);

            // Assert
            // The total bytes written should equal the sum of the global and entry header sizes.
            assertEquals("Bytes written should be the sum of archive magic and entry header sizes.",
                    expectedHeaderSize, arOut.getBytesWritten());

            // For completeness, also verify the underlying stream received the correct number of bytes.
            assertEquals("The underlying stream's size should match the expected header size.",
                    (int) expectedHeaderSize, outputStream.size());
        }
    }
}