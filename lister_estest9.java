package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;

/**
 * Test suite for the {@link Lister} class.
 * This class contains the refactored test case.
 */
// The original test extended a scaffolding class. We keep it to maintain structural integrity.
public class Lister_ESTestTest9 extends Lister_ESTest_scaffolding {

    /**
     * Tests that Lister#go() throws an IOException when an explicitly provided
     * archive format is not recognized by the ArchiveStreamFactory.
     */
    @Test
    public void goShouldThrowIOExceptionForUnknownArchiveFormat() {
        // Arrange: Set up command-line arguments with a dummy file path and an
        // unsupported archive format name.
        final String dummyFilePath = "any-file.archive";
        final String unknownArchiveFormat = "unsupported-archive-format";
        final String[] args = {dummyFilePath, unknownArchiveFormat};

        final Lister lister = new Lister(false, args);

        // Act & Assert: Verify that calling go() throws the expected exception
        // with a specific message.
        try {
            lister.go();
            fail("Expected an IOException because the archive format is unknown.");
        } catch (final IOException e) {
            // The underlying ArchiveStreamFactory throws an ArchiveException (a subclass of IOException).
            // We verify the exception type and message to ensure the failure is for the correct reason.
            final String expectedMessage = "Archiver: " + unknownArchiveFormat + " not found.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}