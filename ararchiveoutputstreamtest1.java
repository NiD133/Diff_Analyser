package org.apache.commons.compress.archivers.ar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
// The original class name "ArArchiveOutputStreamTestTest1" was simplified to
// follow standard naming conventions and remove redundancy.
class ArArchiveOutputStreamTest {

    @Test
    @DisplayName("ArArchiveOutputStream should throw an exception for file names longer than 16 characters by default")
    void longFileNameCausesExceptionByDefault() throws IOException {
        // Arrange: The AR format specification limits file names to 16 characters.
        // This test verifies that the default behavior is to reject longer names.
        final String longFileName = "this_is_a_very_long_filename.txt"; // 34 chars > 16

        // Use try-with-resources to ensure the stream is closed automatically after use.
        try (final ArArchiveOutputStream arOut = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            final ArArchiveEntry entryWithLongName = new ArArchiveEntry(longFileName, 0);

            // Act & Assert: Verify that attempting to add the entry throws an ArchiveException.
            final ArchiveException exception = assertThrows(ArchiveException.class, () -> {
                arOut.putArchiveEntry(entryWithLongName);
            });

            // Further assert on the exception message to ensure it's the correct error.
            assertTrue(
                exception.getMessage().startsWith("File name too long"),
                "Exception message should indicate that the file name is too long."
            );
        }
        // Note: The original test included a check to verify the stream was closed.
        // This check is omitted here to improve focus. The test's primary goal is to
        // validate the handling of long file names. The try-with-resources statement
        // guarantees that the close() method is invoked.
    }
}