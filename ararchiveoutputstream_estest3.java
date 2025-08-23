package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link ArArchiveOutputStream}.
 * This refactored test replaces an auto-generated one to improve clarity.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that an IOException is thrown when adding a new entry if the
     * previously written entry's content size does not match its declared size.
     */
    @Test
    public void writingMismatchedSizeToEntryThrowsExceptionOnNextEntry() throws IOException {
        // Arrange
        final String entryName = "test_entry.txt";
        final long declaredEntrySize = 100L;
        final byte[] actualData = "This data is definitely not 100 bytes long.".getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream)) {
            // Create an entry declaring a size of 100 bytes.
            ArArchiveEntry entry = new ArArchiveEntry(entryName, declaredEntrySize);
            arOut.putArchiveEntry(entry);

            // Act: Write a different number of bytes than declared in the entry header.
            arOut.write(actualData);

            // Assert: Attempting to add a new entry should trigger a size check on the
            // previous entry, which will fail and throw an IOException.
            try {
                ArArchiveEntry nextEntry = new ArArchiveEntry("next_entry.txt", 0);
                arOut.putArchiveEntry(nextEntry);
                fail("Expected an IOException because the written data size does not match the entry's declared size.");
            } catch (final IOException e) {
                final String expectedMessage = "Length does not match entry (" + actualData.length + " != " + declaredEntrySize;
                assertTrue("The exception message should report the size mismatch.", e.getMessage().startsWith(expectedMessage));
            }
        }
    }
}