package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that attempting to add a new entry when the previous entry's data
     * has not been fully written results in an IOException.
     */
    @Test
    public void addingEntryWhenPreviousEntrySizeMismatchesThrowsIOException() throws IOException {
        // Arrange: Create an archive entry that expects 1 byte of data, but for which
        // no data will be written.
        final String entryName = "test_entry.txt";
        final long declaredSize = 1L;
        ArArchiveEntry firstEntry = new ArArchiveEntry(entryName, declaredSize);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        // Write the header for the first entry. The stream now expects 1 byte of data.
        arOut.putArchiveEntry(firstEntry);

        // Act & Assert: Attempt to add a second entry. This implicitly closes the first
        // entry, triggering a check on its size. Since no data was written, the
        // actual size (0) mismatches its declared size (1), causing an IOException.
        try {
            ArArchiveEntry secondEntry = new ArArchiveEntry("another_entry.txt", 0);
            arOut.putArchiveEntry(secondEntry);
            fail("Expected an IOException because the previous entry's size was not met.");
        } catch (final IOException e) {
            // Verify the exception message clearly states the size mismatch.
            final String expectedMessage = "Length does not match entry (0 != 1)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}