package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator} focusing on error handling scenarios.
 *
 * <p>This class verifies the behavior of factory methods like
 * {@link FileUtils#lineIterator(File, String)} when encountering exceptional
 * conditions, such as a missing file.
 * </p>
 */
class LineIteratorTest {

    @TempDir
    private File temporaryFolder;

    /**
     * Tests that creating a {@link LineIterator} for a non-existent file
     * via {@link FileUtils#lineIterator(File, String)} throws a
     * {@link NoSuchFileException}.
     */
    @Test
    void lineIterator_whenFileDoesNotExist_throwsNoSuchFileException() {
        // Arrange: Define a path to a file that is guaranteed not to exist.
        final File nonExistentFile = new File(temporaryFolder, "non-existent-file.txt");
        final String encoding = StandardCharsets.UTF_8.name();

        // Act & Assert: Verify that attempting to create a LineIterator for the missing file
        // throws the expected exception.
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(nonExistentFile, encoding));
    }
}