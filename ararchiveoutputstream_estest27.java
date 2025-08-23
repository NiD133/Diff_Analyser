package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that calling finish() on an archive with an unclosed entry
     * throws an IOException. An entry is considered "open" after putArchiveEntry()
     * has been called, and it is "closed" by calling closeArchiveEntry().
     */
    @Test
    public void finishShouldThrowIOExceptionForUnclosedEntry() throws IOException {
        // Arrange: Create an archive stream and add an entry without closing it.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);
        final ArArchiveEntry entry = new ArArchiveEntry("test_entry.txt", 0);

        arOutputStream.putArchiveEntry(entry);

        // Act & Assert: Attempting to finish the stream should fail.
        try {
            arOutputStream.finish();
            fail("Expected an IOException because an entry was left open.");
        } catch (final IOException e) {
            // Verify that the correct, descriptive error message is thrown.
            final String expectedMessage = "This archive contains unclosed entries.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}