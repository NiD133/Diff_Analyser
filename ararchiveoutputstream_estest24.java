package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that attempting to add an archive entry with a name longer than 16 characters
     * throws an IOException when using the default long file mode (LONGFILE_ERROR).
     */
    @Test(timeout = 4000)
    public void putArchiveEntryWithLongNameFailsByDefault() {
        // Arrange
        String longFileName = "a-file-with-a-very-long-name.txt"; // 31 chars > 16
        ArArchiveEntry entryWithLongName = new ArArchiveEntry(longFileName, 0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        // Act & Assert
        // The ArArchiveOutputStream is expected to throw an IOException because the file name
        // exceeds the 16-character limit and the default behavior is to error out.
        IOException exception = assertThrows(IOException.class, () -> {
            arOut.putArchiveEntry(entryWithLongName);
        });

        // Verify the exception message is as expected.
        String expectedMessage = "File name too long, > 16 chars: " + longFileName;
        assertEquals(expectedMessage, exception.getMessage());
    }
}