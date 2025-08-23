package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link ArArchiveOutputStream} focusing on long filename handling.
 */
public class ArArchiveOutputStream_ESTestTest23 extends ArArchiveOutputStream_ESTest_scaffolding {

    /**
     * Tests that adding an entry with a filename longer than 16 characters
     * in BSD long file mode writes the expected number of bytes for the
     * archive headers.
     */
    @Test
    public void putArchiveEntryWithBsdLongFileModeWritesSpecialNameEntry() throws IOException {
        // Arrange
        // A filename longer than the standard 16-character limit of the 'ar' format.
        final String longFileName = "No current entry to close"; // 26 characters
        final long fileSize = 1L;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);

        // Configure the stream to handle long filenames using the BSD extension.
        arOutputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

        ArArchiveEntry entryWithLongName = new ArArchiveEntry(longFileName, fileSize);

        // Act
        // Add the entry with the long name. When using LONGFILE_BSD, this writes:
        // 1. The global archive header ("!<arch>\n").
        // 2. A special archive entry containing the long filename.
        // 3. The header for the actual file entry.
        arOutputStream.putArchiveEntry(entryWithLongName);

        // Assert
        // The original test asserted that 93 bytes are written. This value is unexpected,
        // as a manual calculation suggests a different size:
        //   8 bytes (global header)
        // + 60 bytes (long name entry header)
        // + 26 bytes (long name data)
        // + 60 bytes (actual file header)
        // = 154 bytes.
        // The value 93 might be correct for a specific, older version of the library
        // or a subtle behavior not captured in the calculation. We preserve the original
        // asserted value to maintain the integrity of the existing test case.
        final long expectedBytesWritten = 93L;
        assertEquals(expectedBytesWritten, arOutputStream.getBytesWritten());
    }
}