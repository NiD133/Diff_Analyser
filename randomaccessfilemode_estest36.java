package org.apache.commons.io;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that attempting to open a non-existent file in read-only ('r') mode
     * throws a {@link FileNotFoundException}, as this mode does not create files.
     */
    @Test
    public void ioWithReadOnlyModeThrowsExceptionForNonexistentFile() {
        // Arrange: Define the mode and a file that we know does not exist.
        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;
        final String nonExistentFileName = "non_existent_file.txt";

        // Act & Assert: Execute the method and verify that it throws the correct exception.
        // The 'r' mode is for reading only and will not create a file if it's missing.
        FileNotFoundException thrown = assertThrows(
            FileNotFoundException.class,
            () -> readOnlyMode.io(nonExistentFileName)
        );

        // Further Assert: Verify the exception message for more specific and robust testing.
        // The exact message can vary by JVM/OS, so checking for the file name is a good practice.
        assertTrue(
            "The exception message should contain the name of the file that was not found.",
            thrown.getMessage().contains(nonExistentFileName)
        );
    }
}