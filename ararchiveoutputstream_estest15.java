package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;

/**
 * Tests for the {@link ArArchiveOutputStream} class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that attempting to create a new archive entry after the stream
     * has been finished will result in an IOException.
     */
    @Test
    public void shouldThrowIOExceptionWhenCreatingEntryAfterFinish() throws IOException {
        // Arrange: Set up an archive stream and finish it.
        // Using a real, in-memory stream is clearer than using a null stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream archiveOutputStream = new ArArchiveOutputStream(outputStream);

        // Finalize the archive. No more entries should be allowed.
        archiveOutputStream.finish();

        // Act & Assert: Attempt to add an entry to the finished stream.
        try {
            // The specific parameters for createArchiveEntry do not matter,
            // as the check for a finished stream happens first.
            archiveOutputStream.createArchiveEntry((Path) null, "new-entry.txt");
            fail("Expected an IOException because the stream has already been finished.");
        } catch (final IOException e) {
            // Verify that the correct exception and message are thrown.
            // This behavior is defined in the superclass ArchiveOutputStream.
            assertEquals("Stream has already been finished.", e.getMessage());
        }
    }
}