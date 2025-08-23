package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the ArArchiveOutputStream class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that calling closeArchiveEntry() when no entry is currently open
     * throws an IOException.
     */
    @Test
    public void closeArchiveEntryWithoutCurrentEntryThrowsIOException() {
        // Arrange: Create an ArArchiveOutputStream without adding any entries.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);

        // Act & Assert: Attempting to close an entry should fail.
        try {
            arOutputStream.closeArchiveEntry();
            fail("Expected an IOException because no archive entry is open.");
        } catch (final IOException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "No current entry to close";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}