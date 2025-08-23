package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 *
 * This class contains tests for the behavior of creating a RandomAccessFile
 * with different access modes.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that attempting to open a non-existent file in read-only mode ("r")
     * correctly throws a FileNotFoundException. Read-only mode cannot create files.
     */
    @Test
    public void testCreateWithReadOnlyModeForNonexistentFileThrowsException() {
        // Arrange: Set up the test conditions.
        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;
        // Use a mock file to represent a file that does not exist on the file system.
        final File nonexistentFile = new MockFile("nonexistent_file.txt");

        // Act & Assert: Perform the action and verify the outcome.
        try {
            readOnlyMode.create(nonexistentFile);
            fail("Expected a FileNotFoundException because the file does not exist and the mode is read-only.");
        } catch (final FileNotFoundException e) {
            // This is the expected behavior.
            // The mock RandomAccessFile provides a specific message that we can verify.
            final String expectedMessage = "File does not exist, and RandomAccessFile is not open in write mode";
            assertEquals("The exception message should explain why the operation failed.", expectedMessage, e.getMessage());
        }
    }
}