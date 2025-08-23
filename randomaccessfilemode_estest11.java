package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

// The class name and inheritance are kept from the original for context.
// In a real-world scenario, the class would be renamed to RandomAccessFileModeTest.
public class RandomAccessFileMode_ESTestTest11 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that RandomAccessFileMode.create(String) returns a file object
     * initialized with the correct access mode.
     */
    @Test
    public void createWithStringFileNameShouldReturnFileWithCorrectMode() throws IOException {
        // Arrange: Define the test context and expected outcomes.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        final String expectedModeString = "rws";
        // Use a descriptive and unambiguous file name to avoid confusion.
        final File testFile = new MockFile("test-file.tmp");
        testFile.deleteOnExit(); // Ensure cleanup.

        // Act & Assert: Execute the method and verify its behavior.
        // Use a try-with-resources statement to ensure the file is closed automatically.
        try (RandomAccessFile raf = mode.create(testFile.getPath())) {
            // The create() method is expected to return an IORandomAccessFile,
            // which provides the getMode() method. Verify this assumption.
            assertTrue("The created object should be an instance of IORandomAccessFile.",
                raf instanceof IORandomAccessFile);
            final IORandomAccessFile ioRaf = (IORandomAccessFile) raf;

            // Verify that the file was created with the mode defined by the enum constant.
            assertEquals("The file's access mode should match the enum's mode string.",
                expectedModeString, ioRaf.getMode());
        }
    }
}