package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Lister} class.
 */
public class ListerTest {

    /**
     * Tests that Lister.go() correctly propagates a NoClassDefFoundError if a
     * dependency of the specified archiver (e.g., TarFile) fails to initialize.
     *
     * This is an edge-case test that simulates a problematic runtime environment
     * where a required class cannot be loaded, ensuring the application fails fast.
     */
    @Test
    public void goWithTarFormatShouldPropagateClassInitializationError() {
        // Arrange: Prepare command-line arguments to list a "tar" archive.
        // The file path is intentionally empty to trigger the archive handling logic.
        String[] commandLineArgs = {"", "tar"};
        Lister lister = new Lister(false, commandLineArgs);

        // Act & Assert
        try {
            lister.go();
            fail("Expected a NoClassDefFoundError to be thrown due to a class initialization failure.");
        } catch (NoClassDefFoundError e) {
            // This error is expected in a specific test setup where a dependency of
            // TarFile (org.apache.commons.compress.archivers.zip.ZipEncodingHelper)
            // is configured to fail during its static initialization.
            // We verify the error message to confirm the cause of the failure.
            String expectedMessage = "Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}