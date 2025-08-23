package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that attempting to create a new archive entry after the stream
     * has been closed results in an IOException.
     */
    @Test
    public void createArchiveEntryOnClosedStreamThrowsIOException() throws IOException {
        // Arrange: Create a dummy file and an ArArchiveOutputStream, then close the stream.
        final File dummyFile = tempFolder.newFile("test-file.txt");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);
        arOut.close();

        // Act & Assert: Attempting to create an entry should throw an IOException.
        try {
            arOut.createArchiveEntry(dummyFile, "archive-entry-name.txt");
            fail("Expected an IOException because the stream is already closed.");
        } catch (final IOException e) {
            // Verify the exception message is correct.
            final String expectedMessage = "Stream has already been finished.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}