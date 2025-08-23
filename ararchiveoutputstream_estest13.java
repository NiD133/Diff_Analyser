package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for the ArArchiveOutputStream class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that attempting to write to the stream before an archive entry
     * has been started throws an IOException.
     */
    @Test
    public void writingToStreamWithoutAnEntryShouldThrowIOException() {
        // Arrange: Create an ArArchiveOutputStream without adding an entry.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);
        byte[] dataToWrite = { 1, 2, 3 };

        // Act & Assert: Attempting to write should fail.
        try {
            arOut.write(dataToWrite, 0, dataToWrite.length);
            fail("Expected an IOException because no archive entry was put into the stream.");
        } catch (final IOException e) {
            // Verify that the correct, specific exception was thrown.
            assertEquals("No current entry to write to", e.getMessage());
        }
    }
}