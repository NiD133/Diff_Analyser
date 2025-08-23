package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that the constructor, when opening a file from a path,
     * correctly initializes the file pointer to the beginning of the file (position 0).
     *
     * This test covers the deprecated constructor:
     * {@link RandomAccessFileOrArray#RandomAccessFileOrArray(String)}
     */
    @Test
    public void constructorWithFilePath_shouldInitializeFilePointerToZero() throws IOException {
        // Arrange: Create a temporary, empty file that can be opened.
        File tempFile = temporaryFolder.newFile("testfile.data");
        String filePath = tempFile.getAbsolutePath();
        RandomAccessFileOrArray fileOrArray = null;

        try {
            // Act: Create a RandomAccessFileOrArray instance from the file path.
            fileOrArray = new RandomAccessFileOrArray(filePath);

            // Assert: The initial file pointer should be at the start of the file.
            long expectedPosition = 0L;
            long actualPosition = fileOrArray.getFilePointer();
            assertEquals("The file pointer should be at the beginning of the file after construction.", expectedPosition, actualPosition);
        } finally {
            // Cleanup: Ensure the file resource is closed to prevent leaks.
            if (fileOrArray != null) {
                fileOrArray.close();
            }
        }
    }
}