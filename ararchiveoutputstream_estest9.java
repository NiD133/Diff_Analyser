package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that creating an archive entry from a non-existent file results
     * in an entry with a size of zero.
     */
    @Test
    public void createArchiveEntryForNonExistentFileShouldHaveZeroSize() throws IOException {
        // Arrange
        // Use a standard File object for a path that is known not to exist.
        final File nonExistentFile = new File("this-file-does-not-exist.tmp");
        final String entryName = "test-entry.txt";

        // A precondition to ensure the test environment is as expected.
        assertFalse("Test precondition failed: The file should not exist.", nonExistentFile.exists());

        // The ArArchiveOutputStream writes to a byte array, so no actual file is created.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream)) {

            // Act
            // Create an archive entry based on the non-existent file.
            final ArArchiveEntry entry = arOut.createArchiveEntry(nonExistentFile, entryName);

            // Assert
            // The size should be 0, as File.length() returns 0 for non-existent files.
            assertEquals("Entry size should be 0 for a non-existent file.", 0L, entry.getSize());
            // Also verify that the entry name was set correctly.
            assertEquals("Entry name should match the provided name.", entryName, entry.getName());
        }
    }
}