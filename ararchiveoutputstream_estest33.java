package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link ArArchiveOutputStream} class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that calling putArchiveEntry() writes the correct number of bytes for
     * the archive header and the first entry's header.
     */
    @Test
    public void putArchiveEntryWritesArchiveAndEntryHeaders() throws IOException {
        // Arrange: Set up the output stream and an archive entry.
        // The entry name is exactly 16 characters, the maximum for the standard AR format.
        final String entryName = "standard_name_16"; // 16 characters long
        final long entrySize = 1L;
        ArArchiveEntry entry = new ArArchiveEntry(entryName, entrySize);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);

        // Act: Write the first archive entry. This triggers writing the global
        // archive header followed by this entry's header.
        arOutputStream.putArchiveEntry(entry);

        // Assert: Verify that the total bytes written match the expected header sizes.
        final long archiveMagicHeaderSize = 8; // Size of "!<arch>\n"
        final long entryHeaderSize = 60;       // Size of a standard AR entry header
        final long expectedBytesWritten = archiveMagicHeaderSize + entryHeaderSize;

        assertEquals("The total bytes written should be the sum of the archive and entry header sizes.",
                expectedBytesWritten, arOutputStream.getBytesWritten());
    }
}