package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that creating a MappedRandomAccessFile for a non-existent file path
     * in "rw" mode results in a new, empty file with a length and file pointer of 0.
     */
    @Test
    public void constructor_withReadWriteModeForNewFile_initializesLengthAndPointerToZero() throws IOException {
        // Arrange: Define a path for a new file that does not yet exist.
        File newFile = new File(tempFolder.getRoot(), "newFile.dat");
        String filePath = newFile.getAbsolutePath();
        MappedRandomAccessFile mraf = null;

        try {
            // Act: Create the MappedRandomAccessFile. This should also create the physical file.
            mraf = new MappedRandomAccessFile(filePath, "rw");

            // Assert: Verify the initial state of the new file.
            assertEquals("A new file should have a length of 0.", 0L, mraf.length());
            assertEquals("The file pointer should be at the beginning for a new file.", 0L, mraf.getFilePointer());
        } finally {
            // Cleanup: Ensure the file resource is always closed.
            if (mraf != null) {
                mraf.close();
            }
        }
    }
}