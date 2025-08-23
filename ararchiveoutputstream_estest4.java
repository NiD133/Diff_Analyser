package org.apache.commons.compress.archivers.ar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains understandable tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamUnderstandableTest {

    // Rule to create temporary files and folders, which are automatically cleaned up.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that writing an entry with a long filename in GNU mode writes the
     * expected number of bytes for the archive and entry headers.
     */
    @Test(timeout = 4000)
    public void putArchiveEntryWithLongNameInGnuModeWritesCorrectHeaderSize() throws IOException {
        // Arrange
        // 1. A filename longer than the standard 16-character limit for AR entries.
        String longFileName = "a-filename-longer-than-16-chars.txt";

        // 2. A source file to be added to the archive. Its content is not important for this test.
        File sourceFile = tempFolder.newFile("source.txt");

        // 3. An in-memory output stream to capture the archive bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(byteArrayOutputStream);

        // 4. Configure the stream to handle long filenames using the GNU extension.
        // The original test used the magic number 1909. The AR format specification
        // and the class javadoc indicate that '2' should be used for GNU mode.
        // Any value other than LONGFILE_ERROR (0) or LONGFILE_BSD (1) enables this behavior.
        final int LONGFILE_GNU = 2;
        arOut.setLongFileMode(LONGFILE_GNU);

        // 5. Create the archive entry for the source file with the long name.
        ArArchiveEntry entry = arOut.createArchiveEntry(sourceFile, longFileName);

        // Act
        // Write the entry to the archive stream. This writes the archive signature
        // (if it's the first entry) and the entry's header.
        arOut.putArchiveEntry(entry);

        // Assert
        // Verify the total bytes written. This should be the sum of the AR archive
        // signature and the fixed-size entry header.
        // - Archive signature: 8 bytes ("!<arch>\n")
        // - Entry header: 60 bytes (fixed size for name, date, uid, etc.)
        // In GNU mode, the long filename itself is not written with the header.
        // Instead, a reference is stored in the header, and the full name is
        // written to a special name table when the archive is finalized via finish().
        final long archiveSignatureSize = 8;
        final long entryHeaderSize = 60;
        final long expectedSize = archiveSignatureSize + entryHeaderSize;

        assertEquals("The number of bytes written should equal the size of the archive signature plus one entry header.",
                expectedSize, arOut.getBytesWritten());
    }
}