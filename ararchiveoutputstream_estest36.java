package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that calling createArchiveEntry with a null Path argument
     * results in a NullPointerException.
     *
     * The underlying implementation delegates to `java.nio.file.Files`,
     * which throws a NullPointerException when given a null path.
     */
    @Test(expected = NullPointerException.class)
    public void createArchiveEntryWithNullPathShouldThrowNullPointerException() throws IOException {
        // Arrange
        // The underlying output stream can be null for this test because createArchiveEntry
        // only reads file metadata and does not write to the stream.
        final ArArchiveOutputStream arOutStream = new ArArchiveOutputStream(null);
        final String entryName = "test-entry.txt";

        // Act & Assert
        // This call is expected to throw a NullPointerException because the path is null.
        arOutStream.createArchiveEntry((Path) null, entryName, (LinkOption[]) null);
    }
}