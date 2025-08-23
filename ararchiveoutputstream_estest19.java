package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for ArArchiveOutputStream.
 * This class focuses on verifying the output stream's behavior, such as archive size and content.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that creating an archive with a single empty file entry and then
     * finishing the stream results in the correct total size.
     * The expected size is the sum of the AR magic header and the single entry's header.
     */
    @Test
    public void finishWithSingleEmptyEntryWritesCorrectArchiveSize() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        // Create a temporary file to be used for the archive entry.
        // The file's content is not used, only its metadata (like size=0).
        File emptyFile = File.createTempFile("test-entry", ".txt");
        emptyFile.deleteOnExit();
        
        String entryName = "empty.txt";
        ArchiveEntry entry = arOut.createArchiveEntry(emptyFile, entryName);

        // Act
        arOut.putArchiveEntry(entry);
        arOut.closeArchiveEntry();
        arOut.finish();
        arOut.close();

        // Assert
        // The total size should be the AR magic header (8 bytes) + one entry header (60 bytes).
        final long arMagicHeaderSize = 8;
        final long arEntryHeaderSize = 60;
        final long expectedSize = arMagicHeaderSize + arEntryHeaderSize;

        assertEquals("The total bytes written should match the expected archive size.",
                expectedSize, arOut.getBytesWritten());
        assertEquals("The underlying stream size should also match the expected archive size.",
                expectedSize, outputStream.size());
    }
}