package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * Test suite for the MappedRandomAccessFile class.
 */
public class MappedRandomAccessFileTest {

    // Use a JUnit Rule to create and manage a temporary folder for test files.
    // This ensures tests are isolated and don't leave files behind.
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that the read() method throws an ArrayIndexOutOfBoundsException
     * when called with a negative offset.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void read_withNegativeOffset_shouldThrowException() throws IOException {
        // Arrange: Create a temporary file and instantiate the class under test.
        File tempFile = temporaryFolder.newFile("test.data");
        MappedRandomAccessFile mappedFile = null;

        try {
            mappedFile = new MappedRandomAccessFile(tempFile.getAbsolutePath(), "rw");
            byte[] buffer = new byte[10];
            int negativeOffset = -1; // The invalid offset that should cause the exception.
            int readLength = 5;

            // Act: Attempt to read from the file using the invalid negative offset.
            // This call is expected to throw the exception.
            mappedFile.read(buffer, negativeOffset, readLength);

        } finally {
            // Assert: The test passes if the expected ArrayIndexOutOfBoundsException is thrown.
            // This is handled by the @Test(expected=...) annotation.

            // Cleanup: Ensure the file resource is always closed.
            if (mappedFile != null) {
                mappedFile.close();
            }
        }
    }
}