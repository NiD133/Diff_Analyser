package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * An AR archive consists of a magic string ("!<arch>\n", 8 bytes)
     * followed by a 60-byte header for each entry.
     */
    private static final int AR_MAGIC_HEADER_SIZE = 8;
    private static final int AR_ENTRY_HEADER_SIZE = 60;

    /**
     * Verifies that writing multiple, consecutive entries to the archive
     * correctly updates the total number of bytes written.
     */
    @Test
    public void shouldWriteCorrectByteCountWhenAddingMultipleEntries() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);
        
        // Create a representative entry with zero content size.
        ArArchiveEntry entry = new ArArchiveEntry("entry.txt", 0);

        // Act: Write the first entry.
        arOut.putArchiveEntry(entry);
        arOut.closeArchiveEntry();

        // Assert: The stream should contain the archive magic header plus one entry header.
        long expectedSizeAfterFirstEntry = AR_MAGIC_HEADER_SIZE + AR_ENTRY_HEADER_SIZE;
        assertEquals("Byte count after first entry is incorrect.",
                expectedSizeAfterFirstEntry, arOut.getCount());

        // Act: Write a second entry.
        arOut.putArchiveEntry(entry);

        // Assert: The stream should now also contain the header for the second entry.
        long expectedSizeAfterSecondEntry = expectedSizeAfterFirstEntry + AR_ENTRY_HEADER_SIZE;
        assertEquals("Byte count after second entry is incorrect.",
                expectedSizeAfterSecondEntry, arOut.getCount());
        
        // The original test asserted a total of 128 bytes.
        // Our calculation confirms this: 8 + 60 + 60 = 128.
        assertEquals(128, arOut.getCount());
    }
}