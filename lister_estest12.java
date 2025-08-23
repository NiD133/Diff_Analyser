package org.apache.commons.compress.archivers;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Unit tests for the {@link Lister} class.
 */
public class ListerTest {

    /**
     * Verifies that the go() method throws a NoSuchFileException
     * when the specified archive file does not exist.
     */
    @Test(expected = NoSuchFileException.class)
    public void goShouldThrowExceptionWhenFileDoesNotExist() throws ArchiveException, IOException {
        // Arrange: Define arguments for a non-existent file.
        String[] args = {"non-existent-archive.zip"};
        // The 'quiet' flag is set to true to prevent console output during the test run.
        Lister lister = new Lister(true, args);

        // Act: Attempt to list the contents of the non-existent file.
        // This call is expected to throw a NoSuchFileException.
        lister.go();

        // Assert: The test passes if the expected NoSuchFileException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}