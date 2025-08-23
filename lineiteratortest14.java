package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link LineIterator} created via {@link FileUtils#lineIterator(File, String)}.
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    private File temporaryFolder;

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file      The file to write to.
     * @param encoding  The encoding to use.
     * @param lineCount The number of lines to create.
     * @return A list of the lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a list of strings for testing, e.g., ["LINE 0", "LINE 1", ...].
     *
     * @param lineCount The number of lines to create.
     * @return A new list of strings.
     */
    private List<String> createStringLines(final int lineCount) {
        return IntStream.range(0, lineCount)
            .mapToObj(i -> "LINE " + i)
            .collect(Collectors.toList());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5})
    void fileLineIterator_shouldCorrectlyIterateAllLines(final int lineCount) throws IOException {
        // Arrange
        final String fileName = "LineIterator-" + lineCount + "-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> expectedLines = createLinesFile(testFile, UTF_8, lineCount);

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            // Test iterator contract: remove() is unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should be an unsupported operation.");

            int actualLineCount = 0;
            while (iterator.hasNext()) {
                final String actualLine = iterator.next();
                // Ensure we don't read more lines than expected, which would cause an
                // IndexOutOfBoundsException on the next line.
                assertTrue(actualLineCount < expectedLines.size(),
                    "Iterator produced more lines than expected.");
                assertEquals(expectedLines.get(actualLineCount), actualLine,
                    () -> "Line content mismatch at index " + actualLineCount);
                actualLineCount++;
            }

            // Verify that all expected lines were iterated
            assertEquals(expectedLines.size(), actualLineCount, "Line count should match the file content.");

            // Test iterator contract: calling next() when hasNext() is false
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw when no more elements exist.");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw when no more elements exist.");
        }
    }

    @Test
    void lineIterator_shouldThrowException_forNonExistentFile() {
        // Arrange
        final File nonExistentFile = new File(temporaryFolder, "nonExistent.txt");

        // Act & Assert
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(nonExistentFile, UTF_8),
            "Expected a NoSuchFileException for a non-existent file.");
    }

    @Test
    void lineIterator_shouldThrowException_forUnsupportedEncoding() {
        // Arrange
        final File testFile = new File(temporaryFolder, "test.txt");
        final String invalidEncoding = "INVALID-ENCODING";

        // Act & Assert
        assertThrows(UnsupportedCharsetException.class, () -> {
            // The iterator must be closed even if the factory method throws.
            // However, in this case, the exception is thrown during construction,
            // so there is no iterator to close.
            FileUtils.lineIterator(testFile, invalidEncoding);
        }, "Expected an UnsupportedCharsetException for an invalid encoding.");
    }
}