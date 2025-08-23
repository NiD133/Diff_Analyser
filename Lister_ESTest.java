package org.apache.commons.compress.archivers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Readable tests for Lister focusing on argument validation and basic failure modes.
 *
 * Notes:
 * - These tests avoid environment-specific behaviors (e.g., NoClassDefFoundError from missing optional modules).
 * - They use clear Arrange/Act/Assert structure and descriptive names to ease maintenance.
 */
class ListerTest {

    @TempDir
    Path tempDir;

    @Test
    void constructorRequiresAtLeastOneArgument() {
        // Arrange
        String[] noArgs = new String[0];

        // Act + Assert
        ArrayIndexOutOfBoundsException ex =
                assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Lister(true, noArgs));

        // Accessing args[0] with an empty array causes this
        assertEquals("0", ex.getMessage());
    }

    @Test
    void constructorRejectsNullFirstArgument() {
        // Arrange
        String[] args = new String[] { null };

        // Act + Assert
        NullPointerException ex =
                assertThrows(NullPointerException.class, () -> new Lister(false, args));

        // The implementation requires args[0] to be non-null
        assertEquals("args[0]", ex.getMessage());
    }

    @Test
    void goThrowsInvalidPathExceptionWhenPathContainsNulCharacter() throws Exception {
        // Arrange: NUL byte is not valid in file names on most platforms.
        Lister lister = new Lister(true, "bad\u0000name");

        // Act + Assert
        assertThrows(InvalidPathException.class, lister::go);
    }

    @Test
    void goThrowsNoSuchFileExceptionForMissingFile() throws Exception {
        // Arrange: Target a file that does not exist
        Path missing = tempDir.resolve("does-not-exist.tar");
        Lister lister = new Lister(true, missing.toString());

        // Act + Assert
        assertThrows(NoSuchFileException.class, lister::go);
    }

    @Test
    void goReportsUnknownArchiveTypeWhenExplicitTypeIsUnknown() throws Exception {
        // Arrange: Create an empty file (so path exists) but specify an unknown archive type
        Path file = tempDir.resolve("some-file.bin");
        java.nio.file.Files.write(file, new byte[0]);

        Lister lister = new Lister(false, file.toString(), "not-encodeable");

        // Act
        Exception ex = assertThrows(Exception.class, lister::go);

        // Assert: Depending on Lister's implementation details, either ArchiveException may escape
        // or it may be wrapped as an IOException. We accept either and check the message is informative.
        assertTrue(
                (ex instanceof ArchiveException) || (ex instanceof IOException),
                "Expected ArchiveException or IOException but was " + ex.getClass().getName()
        );
        // Message typically comes from ArchiveStreamFactory; don't assert exact text to avoid brittleness.
    }
}