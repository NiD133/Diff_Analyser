package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.evosuite.runtime.testdata.FileSystemHandling;

/**
 * Test suite for {@link RandomAccessFileMode}.
 * This class contains the refactored test case.
 */
public class RandomAccessFileMode_ESTestTest5 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that the io(String) method successfully opens an existing file in read-only mode.
     */
    @Test(timeout = 4000)
    public void ioShouldOpenExistingFileInReadOnlyMode() throws IOException {
        // Arrange: Create a test file with known content.
        final String testFileName = "test-file.txt";
        final String expectedContent = "Hello, World!";
        FileSystemHandling.createFileWithContent(testFileName, expectedContent);

        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;

        // Act & Assert: Attempt to open the file and verify its contents.
        // The use of try-with-resources ensures the file is properly closed and implicitly
        // asserts that no exception is thrown during the file opening process.
        try (RandomAccessFile raf = readOnlyMode.io(testFileName)) {
            // Assert that the file was opened successfully.
            assertNotNull("The RandomAccessFile object should not be null.", raf);

            // Assert that the file content can be read correctly.
            byte[] buffer = new byte[expectedContent.length()];
            raf.readFully(buffer);
            String actualContent = new String(buffer);

            assertEquals("The content read from the file should match the expected content.", expectedContent, actualContent);
        }
    }
}