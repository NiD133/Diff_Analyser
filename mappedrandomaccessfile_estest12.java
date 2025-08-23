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

    // Use JUnit's TemporaryFolder rule for robust file handling in tests.
    // This ensures that test files are created in a temporary directory and
    // are automatically cleaned up after the test runs.
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that the close() method is idempotent, meaning it can be called
     * multiple times on the same instance without throwing an exception or
     * causing other adverse effects.
     */
    @Test
    public void close_isIdempotent_andDoesNotThrowException() throws IOException {
        // Arrange: Create a temporary file and instantiate the MappedRandomAccessFile.
        // Using a real file managed by TemporaryFolder is cleaner than using hardcoded names.
        File testFile = temporaryFolder.newFile("test.dat");
        MappedRandomAccessFile mappedFile = new MappedRandomAccessFile(testFile.getAbsolutePath(), "rw");

        // Act: Close the file twice. The test's main purpose is to ensure the second
        // call completes successfully without throwing an exception.
        mappedFile.close();
        mappedFile.close();

        // Assert: Verify that the object's internal state remains consistent.
        // The file pointer should not have been altered from its initial state.
        assertEquals("The file pointer should remain at 0 after multiple close calls.", 0L, mappedFile.getFilePointer());
    }
}