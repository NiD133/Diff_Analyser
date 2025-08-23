package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link ArArchiveOutputStream} class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that when BSD long file mode is enabled, adding an entry with a
     * standard-length filename correctly writes the global and entry headers.
     *
     * This test ensures that the long file mode setting does not alter the
     * output for filenames that do not require special handling (i.e., are shorter
     * than 16 characters).
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void shouldWriteHeaderForShortFilenameWhenBsdLongFileModeIsEnabled() throws IOException {
        // Arrange: Set up the output stream and an archive entry with a short name.
        final String shortFileName = "short-name.txt"; // A name < 16 chars is standard.
        final long fileSize = 0L;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);
        final ArArchiveEntry entry = new ArArchiveEntry(shortFileName, fileSize);

        // Configure the stream to handle long filenames using the BSD method.
        arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

        // Act: Write the archive entry header to the stream.
        arOut.putArchiveEntry(entry);

        // Assert: Check if the total bytes written match the expected header sizes.
        // The total size should be the sum of the global AR header and a standard entry header.
        // Global header: "!<arch>\n" (8 bytes)
        // Entry header: 60 bytes
        final int globalHeaderSize = 8;
        final int standardEntryHeaderSize = 60;
        final int expectedSize = globalHeaderSize + standardEntryHeaderSize;

        assertEquals("The output size should be the sum of global and entry headers.",
                     expectedSize, arOut.getCount());
    }
}