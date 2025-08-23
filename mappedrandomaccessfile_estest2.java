package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link MappedRandomAccessFile}.
 * This class contains the refactored test case.
 */
public class MappedRandomAccessFile_ESTestTest2 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that attempting to read from a position beyond the end of the file
     * immediately returns -1 (end-of-file), even if the other read parameters
     * (offset, length) are invalid.
     */
    @Test
    public void read_whenFilePointerIsPastEndOfFile_returnsNegativeOne() throws IOException {
        // Arrange: Create an empty temporary file to ensure its length is zero.
        File emptyFile = temporaryFolder.newFile("empty.dat");

        // The buffer to read into. Its size is not critical for this test.
        byte[] buffer = new byte[10];
        
        // These parameters are intentionally invalid to demonstrate that the
        // end-of-file check takes precedence over parameter validation.
        final int invalidBufferOffset = -1;
        final int anyLength = 5;

        // Use a try-with-resources block to ensure the file is closed automatically.
        try (MappedRandomAccessFile mraf = new MappedRandomAccessFile(emptyFile.getAbsolutePath(), "r")) {
            // The file is empty, so its length is 0.
            // Seek to any position greater than 0, which is beyond the end of the file.
            long positionPastEndOfFile = 100L;
            mraf.seek(positionPastEndOfFile);

            // Act: Attempt to read from the position past the end of the file.
            int bytesRead = mraf.read(buffer, invalidBufferOffset, anyLength);

            // Assert: The read method should return -1 to indicate end-of-file.
            // This behavior is expected because the file pointer check happens before
            // the buffer offset/length validation.
            assertEquals("Should return -1 for end-of-file, even with an invalid buffer offset", -1, bytesRead);
        }
    }
}