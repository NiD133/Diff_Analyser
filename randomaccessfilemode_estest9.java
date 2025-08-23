package org.apache.commons.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link RandomAccessFileMode#create(Path)}.
 */
public class RandomAccessFileModeTest {

    // Use a JUnit rule to create and automatically clean up a temporary folder and files.
    // This is a standard, reliable way to handle file-based tests.
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that creating a RandomAccessFile from a Path using a specific mode
     * successfully opens the file and initializes the file pointer at position 0.
     */
    @Test
    public void createFromPathShouldReturnFileWithPointerAtStart() throws IOException {
        // Arrange: Define the mode and create a temporary file to work with.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        final Path testFile = temporaryFolder.newFile("testfile.txt").toPath();

        // Act & Assert: Use a try-with-resources block to ensure the file is closed automatically.
        try (RandomAccessFile raf = mode.create(testFile)) {
            // Assert that the file was created and opened successfully.
            assertNotNull("The created RandomAccessFile should not be null.", raf);

            // Assert that the file pointer is at the beginning of the newly created file.
            assertEquals("File pointer should be at the start of the file.", 0L, raf.getFilePointer());
        }
    }
}