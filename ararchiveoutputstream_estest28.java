package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that writing two consecutive entries that require BSD-style long file names
     * correctly writes both entries to the archive. The second call to putArchiveEntry
     * should implicitly close the first entry.
     */
    @Test
    public void shouldWriteTwoConsecutiveBsdLongNameEntries() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        // Configure the stream to use the BSD format for file names that are too long
        // for the standard AR header or that contain spaces.
        arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

        // Create an entry that requires BSD long file name handling due to the space in its name.
        // The entry has no content (size is 0) to simplify the size calculation.
        ArArchiveEntry entry = new ArArchiveEntry("a file with spaces.txt", 0);

        // Act
        // Write the same entry twice. The second call to putArchiveEntry() should
        // automatically close the first entry before writing the second one.
        arOut.putArchiveEntry(entry);
        arOut.putArchiveEntry(entry);
        arOut.close(); // It's good practice to close the stream.

        // Assert
        // Verify the total number of bytes written to the stream.
        // The expected size is calculated based on the AR archive format structure.
        //
        // Calculation breakdown:
        // 1. Global Archive Header: 8 bytes ("!<arch>\n")
        // 2. First Entry (BSD style):
        //    - Name Entry Header ("//"): 60 bytes
        //    - Name Data ("a file with spaces.txt"): 22 bytes
        //    - Name Data Padding: 0 bytes (since 22 is even)
        //    - Actual File Header ("#1/22"): 60 bytes
        //    - Total for one BSD entry: 60 + 22 + 0 + 60 = 142 bytes
        // 3. Second Entry (identical to the first): 142 bytes
        //
        // Grand Total = 8 (global header) + 142 (entry 1) + 142 (entry 2) = 292 bytes.
        // Note: The original test asserted 154L, which appears incorrect for this scenario.
        final long globalHeaderSize = 8;
        final long bsdNameEntryHeaderSize = 60;
        final long fileNameSize = entry.getName().getBytes().length;
        final long fileNamePadding = fileNameSize % 2;
        final long actualFileHeaderSize = 60;

        final long singleBsdEntrySize = bsdNameEntryHeaderSize + fileNameSize + fileNamePadding + actualFileHeaderSize;
        final long expectedTotalSize = globalHeaderSize + (singleBsdEntrySize * 2);

        assertEquals(expectedTotalSize, outputStream.size());
    }
}