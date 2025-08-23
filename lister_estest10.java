package org.apache.commons.compress.archivers;

import org.junit.Test;
import java.nio.file.InvalidPathException;

/**
 * Tests for the {@link Lister} command-line application wrapper.
 */
public class ListerTest {

    /**
     * Verifies that calling go() on a Lister instance created without a file path
     * throws an InvalidPathException. This simulates running the application
     * with an empty or missing file argument.
     *
     * The deprecated default constructor `new Lister()` is used here as it
     * directly creates this invalid state.
     */
    @Test(expected = InvalidPathException.class)
    public void goShouldThrowExceptionWhenInitializedWithEmptyFilePath() throws Exception {
        // Arrange: Create a Lister instance using the default constructor.
        // This sets up the internal state with an empty, and therefore invalid, file path.
        Lister lister = new Lister();

        // Act & Assert: Calling go() should immediately fail when it tries to
        // process the empty file path, throwing an InvalidPathException.
        lister.go();
    }
}