package org.apache.commons.compress.archivers.ar;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that creating an archive entry from a Path on a non-POSIX file system
     * (or when POSIX attributes are otherwise unavailable) results in a default
     * user ID of 0.
     */
    @Test(timeout = 4000)
    public void createArchiveEntryFromPathShouldUseDefaultUserIdOnNonPosixFileSystem() throws IOException {
        // This test is only meaningful on file systems that do not support PosixFileAttributes.
        // On POSIX-compliant systems, the ArArchiveEntry constructor would read the actual user ID.
        // This assumption ensures the test verifies the fallback behavior without failing on POSIX systems.
        Assume.assumeFalse("Skipping test on a POSIX-compliant file system",
            FileSystems.getDefault().supportedFileAttributeViews().contains("posix"));

        // Arrange
        // The output stream is required for the constructor but not used for creating the entry metadata.
        final ArArchiveOutputStream arOut = new ArArchiveOutputStream(new ByteArrayOutputStream());
        final File tempFile = tempFolder.newFile("test-file.txt");
        final Path entryPath = tempFile.toPath();
        final String entryName = "archive-entry.txt";

        // Act
        final ArArchiveEntry archiveEntry = arOut.createArchiveEntry(entryPath, entryName, LinkOption.NOFOLLOW_LINKS);

        // Assert
        // On non-POSIX systems, the ArArchiveEntry constructor falls back to a default user ID of 0.
        assertEquals("Expected default user ID of 0 on a non-POSIX file system", 0, archiveEntry.getUserId());
    }
}