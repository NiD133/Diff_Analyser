package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.function.IOFunction;

// Note: The original test class structure is maintained.
// Unused and EvoSuite-specific imports have been removed for clarity.
public class RandomAccessFileMode_ESTestTest20 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that calling {@link RandomAccessFileMode#apply(Path, IOFunction)} with
     * {@link RandomAccessFileMode#READ_ONLY} on a non-existent file throws a
     * {@link FileNotFoundException}. This is the correct behavior, as read-only
     * mode should not create a file if it is missing.
     */
    @Test
    public void applyWithReadOnlyModeOnNonExistentFileThrowsFileNotFoundException() {
        // Arrange: Define a path for a file that is guaranteed not to exist
        // and select the READ_ONLY mode.
        final File nonExistentFile = new File("this-file-should-not-exist.tmp");
        // Defensive cleanup: ensure the file doesn't exist from a previous failed run.
        if (nonExistentFile.exists()) {
            nonExistentFile.delete();
        }
        final Path nonExistentPath = nonExistentFile.toPath();
        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;

        // Act & Assert: Verify that a FileNotFoundException is thrown.
        try {
            readOnlyMode.apply(nonExistentPath, randomAccessFile -> {
                // This function should never be executed because opening the file will fail first.
                fail("The IOFunction should not have been invoked for a non-existent file in read-only mode.");
                return null; // Return value is irrelevant.
            });
            // If the 'apply' method does not throw an exception, this test should fail.
            fail("Expected a FileNotFoundException to be thrown, but no exception occurred.");
        } catch (final FileNotFoundException e) {
            // Success: The expected exception was caught. The test passes.
            // The type of the exception is the primary assertion.
        } catch (final IOException e) {
            // Fail the test if any other unexpected IO-related exception is thrown.
            fail("Caught an unexpected IOException instead of FileNotFoundException: " + e.getMessage());
        }
    }
}