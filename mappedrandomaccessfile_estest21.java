package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the MappedRandomAccessFile class.
 */
public class MappedRandomAccessFileTest {

    // Use JUnit's TemporaryFolder rule to create and clean up test files automatically.
    // This makes the test self-contained and prevents side effects on the file system.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that calling getChannel() on a newly created file
     * does not advance the file pointer from its initial position of 0.
     */
    @Test
    public void getChannel_onNewFile_shouldNotChangeFilePointer() throws IOException {
        // Arrange: Create a temporary file and open it with MappedRandomAccessFile.
        File tempFile = tempFolder.newFile("test.data");
        
        // Using a try-with-resources statement ensures the file is closed automatically,
        // preventing resource leaks.
        try (MappedRandomAccessFile mappedFile = new MappedRandomAccessFile(tempFile.getAbsolutePath(), "rw")) {
            
            // Act: Call the method under test.
            FileChannel channel = mappedFile.getChannel();

            // Assert: Verify the post-conditions.
            // 1. The channel should be successfully retrieved (not null).
            assertNotNull("The returned FileChannel should not be null.", channel);
            
            // 2. The file pointer should remain at the beginning of the file.
            long expectedPosition = 0L;
            long actualPosition = mappedFile.getFilePointer();
            assertEquals("Calling getChannel() should not move the file pointer.", expectedPosition, actualPosition);
        }
    }
}