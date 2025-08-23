package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

// The test class name and inheritance are kept from the original to maintain context.
// In a real-world scenario, these might also be renamed for clarity.
public class RandomAccessFileMode_ESTestTest4 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that calling the io() method with a read-write mode on a non-existent
     * file path results in the creation of a new, empty file.
     */
    @Test(timeout = 4000)
    public void ioWithReadWriteModeShouldCreateNewEmptyFile() throws IOException {
        // Arrange: Set up the test conditions.
        // We use a read-write mode that should create a file if it doesn't exist.
        final RandomAccessFileMode readWriteMode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        final String nonExistentFileName = "new_file.txt";
        // The test environment is expected to provide a clean directory, so this file
        // should not exist before the 'Act' phase.

        // Act: Execute the method under test.
        // Using try-with-resources ensures the file is closed automatically after the test.
        try (IORandomAccessFile newFile = readWriteMode.io(nonExistentFileName)) {
            // Assert: Verify the outcome.
            // A newly created file should have a length of zero.
            assertEquals("A newly created file should be empty.", 0L, newFile.length());
        }
    }
}