package org.apache.commons.compress.archivers;

import org.junit.Test;

import java.nio.file.NoSuchFileException;

/**
 * Contains improved, understandable tests for the {@link Lister} class.
 */
public class ListerTest {

    /**
     * Verifies that Lister#go() throws a NoSuchFileException when the specified
     * archive file does not exist.
     *
     * This test ensures that the application provides clear feedback for a common
     * user error (providing an invalid file path), which is essential for robust
     * error handling.
     */
    @Test(expected = NoSuchFileException.class)
    public void goShouldThrowNoSuchFileExceptionWhenArchiveFileDoesNotExist() throws Exception {
        // Arrange: Set up the Lister with arguments pointing to a file that is
        // guaranteed not to exist. The archive type is specified, but the
        // failure should occur before any format-specific processing.
        String nonExistentFilePath = "path/to/a/surely/non-existent-file.zip";
        String archiveFormat = "zip";
        String[] args = {nonExistentFilePath, archiveFormat};

        Lister lister = new Lister(true, args);

        // Act: Execute the go() method.
        // The @Test(expected=...) annotation will automatically assert that a
        // NoSuchFileException is thrown, failing the test if it is not.
        lister.go();
    }
}