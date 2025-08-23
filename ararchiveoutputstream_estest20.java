package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the ArArchiveOutputStream class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that getBytesWritten() accurately reflects the number of bytes
     * written for an archive entry, including the global header, entry header,
     * and entry data. It also verifies the count is correctly updated after
     * padding is added upon closing the entry.
     */
    @Test
    public void getBytesWrittenIsAccurateAfterWritingEntry() throws IOException {
        // Arrange
        final String entryName = "test_entry.txt";
        // Use an odd size to ensure padding is tested
        final int entrySize = 1215;
        final byte[] entryData = new byte[entrySize];

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Use try-with-resources to ensure the stream is closed automatically
        try (ArArchiveOutputStream arOut = new ArArchiveOutputStream(byteArrayOutputStream)) {
            final ArArchiveEntry entry = new ArArchiveEntry(entryName, entrySize);

            // Act
            arOut.putArchiveEntry(entry);
            arOut.write(entryData);

            // Assert: Check byte count before the entry is closed and padded.
            // The total should be the sum of the global AR header (8 bytes),
            // the entry header (60 bytes), and the data written so far.
            final long expectedBytesBeforePadding = 8L + 60L + entrySize;
            assertEquals("Byte count should be correct before closing the entry",
                expectedBytesBeforePadding, arOut.getBytesWritten());

            // Act: Close the entry, which will trigger padding
            arOut.closeArchiveEntry();

            // Assert: Check byte count after the entry is closed.
            // Since the entry size (1215) is odd, one padding byte is added.
            final long expectedBytesAfterPadding = expectedBytesBeforePadding + 1;
            assertEquals("Byte count should include padding byte after closing the entry",
                expectedBytesAfterPadding, arOut.getBytesWritten());
        }
    }
}