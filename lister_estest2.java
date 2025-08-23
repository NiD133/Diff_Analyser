package org.apache.commons.compress.archivers;

import org.junit.Test;
import java.nio.file.InvalidPathException;

/**
 * Tests for the {@link Lister} command-line application.
 */
public class ListerTest {

    /**
     * Verifies that the Lister#go() method throws an InvalidPathException
     * when the input file path argument contains a null character, which is illegal in file paths.
     */
    @Test(expected = InvalidPathException.class)
    public void goShouldThrowInvalidPathExceptionForPathContainingNullCharacter() throws Exception {
        // Arrange: Create command-line arguments with a file path containing a null character.
        // This is an invalid path on most operating systems.
        final String invalidPathWithNullChar = "archive-name\u0000.tar";
        final String[] args = { invalidPathWithNullChar };

        // The 'quiet' flag is set to true, but it's not relevant to this test's outcome.
        final Lister lister = new Lister(true, args);

        // Act & Assert: Calling go() should attempt to create a Path object from the invalid
        // string, which is expected to throw an InvalidPathException.
        lister.go();
    }
}