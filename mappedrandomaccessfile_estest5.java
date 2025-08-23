package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the MappedRandomAccessFile class.
 */
public class MappedRandomAccessFileTest {

    // Use a TemporaryFolder rule to create files that are automatically cleaned up.
    // This makes the test self-contained and avoids side effects.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that getFilePointer() returns the correct file offset after
     * the position is changed using seek().
     */
    @Test
    public void getFilePointer_shouldReturnCorrectPosition_afterSeek() throws IOException {
        // Arrange
        File testFile = tempFolder.newFile("test.dat");
        long targetPosition = 686L;
        MappedRandomAccessFile mraf = null;

        try {
            // The file is opened in "rw" (read-write) mode.
            mraf = new MappedRandomAccessFile(testFile.getAbsolutePath(), "rw");

            // Act
            // Move the file pointer to a specific position.
            mraf.seek(targetPosition);

            // Get the current position of the file pointer.
            long actualPosition = mraf.getFilePointer();

            // Assert
            // The reported position should match the position we seeked to.
            assertEquals("The file pointer should be at the seeked position.", targetPosition, actualPosition);
        } finally {
            // Cleanup: Ensure the file is closed to release resources.
            if (mraf != null) {
                mraf.close();
            }
        }
    }
}