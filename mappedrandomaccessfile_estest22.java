package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    // The TemporaryFolder Rule ensures that files and folders created for the test
    // are automatically deleted after the test finishes, preventing side effects.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that the file pointer is at position 0 for a newly created file.
     */
    @Test
    public void getFilePointer_shouldBeZero_whenFileIsNewlyCreated() throws IOException {
        // --- Arrange ---
        // Create a new, empty file in a temporary directory.
        File newFile = tempFolder.newFile("test.dat");
        MappedRandomAccessFile mraf = null;

        try {
            // Instantiate the class under test with the new file in read-write mode.
            mraf = new MappedRandomAccessFile(newFile.getAbsolutePath(), "rw");

            // --- Act ---
            // Get the current position of the file pointer.
            long filePointer = mraf.getFilePointer();

            // --- Assert ---
            // The pointer should be at the beginning of the file (position 0).
            assertEquals("The initial file pointer for a new file should be 0.", 0L, filePointer);

        } finally {
            // --- Cleanup ---
            // Ensure the file resource is closed, even if the test fails.
            if (mraf != null) {
                mraf.close();
            }
        }
    }
}