package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that attempting to close an ArArchiveOutputStream while an entry is still
     * "open" (i.e., put but not yet closed) results in an IOException.
     * This ensures the stream is not left in an inconsistent state.
     */
    @Test
    public void closingStreamWithOpenEntryThrowsException() throws IOException {
        // Arrange: Set up an AR output stream and add an entry without closing it.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);

        // An entry with a size greater than 0.
        ArArchiveEntry entry = new ArArchiveEntry("test_entry.txt", 10);
        arOutputStream.putArchiveEntry(entry);

        // Act & Assert: Attempting to close the main stream should fail.
        try {
            arOutputStream.close();
            fail("Expected an IOException because an archive entry was not closed.");
        } catch (final IOException e) {
            // Verify that the exception message is the one we expect.
            final String expectedMessage = "This archive contains unclosed entries.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}