package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Contains tests for the {@link ArArchiveOutputStream} class.
 * This class retains the original test's scaffolding for context.
 */
public class ArArchiveOutputStream_ESTestTest10 {

    /**
     * Tests that an ArArchiveEntry created from an empty file has a length of 0.
     *
     * This test verifies that ArArchiveOutputStream#createArchiveEntry correctly
     * reflects the size of the source file (0 bytes in this case) in the
     * resulting ArArchiveEntry object.
     */
    @Test
    public void createArchiveEntryFromEmptyFileShouldResultInZeroLengthEntry() throws IOException {
        // Arrange: Set up an ArArchiveOutputStream and an empty source file.
        // A ByteArrayOutputStream is used as a standard in-memory sink for the archive data.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(byteArrayOutputStream);

        // Create a temporary, empty file using MockFile to avoid actual disk I/O.
        File emptyFile = MockFile.createTempFile("empty-file", ".tmp");
        emptyFile.deleteOnExit(); // Ensure the mock file is cleaned up.

        // The entry name within the archive. The original test used an empty string.
        String entryNameInArchive = "";

        // Act: Create an archive entry from the empty file.
        ArArchiveEntry archiveEntry = arOutputStream.createArchiveEntry(emptyFile, entryNameInArchive);

        // Assert: Verify that the created entry's length is 0 and its name is correct.
        assertEquals("The length of an archive entry created from an empty file should be 0.",
                0L, archiveEntry.getLength());
        assertEquals("The entry name should match the provided name.",
                entryNameInArchive, archiveEntry.getName());

        arOutputStream.close();
    }
}