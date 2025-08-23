package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the ArArchiveOutputStream class, focusing on entry writing behavior.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that writing an archive entry with a long filename, using the BSD long file mode,
     * writes the correct number of bytes for the header and the filename itself.
     *
     * The AR format has a standard 60-byte header. In BSD mode, long filenames are written
     * immediately after this header. This test verifies that the total bytes written by
     * putArchiveEntry() equals the header size plus the filename length.
     */
    @Test
    public void putArchiveEntryWithBsdLongFileModeWritesHeaderAndName() throws IOException {
        // Arrange
        final String longFileName = "a-very-long-filename-that-exceeds-16-chars.txt"; // 51 chars
        final long fileSize = 1024L;
        final int arHeaderSize = 60;
        final long expectedBytesWritten = arHeaderSize + longFileName.length(); // 60 + 51 = 111

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream)) {
            // Configure the stream to handle long filenames using the BSD extension.
            arOutputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

            ArArchiveEntry entryWithLongName = new ArArchiveEntry(longFileName, fileSize);

            // Act
            // This should write the 60-byte header followed by the 51-byte filename.
            arOutputStream.putArchiveEntry(entryWithLongName);

            // Assert
            assertEquals("Bytes written should be header size + filename length",
                         expectedBytesWritten, arOutputStream.getBytesWritten());
        }
    }
}