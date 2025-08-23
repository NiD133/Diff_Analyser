package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator}.
 * This test focuses on the basic iteration functionality using the next() and hasNext() methods.
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of simple string lines for testing.
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings, e.g., ["LINE 0", "LINE 1", ...].
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Creates a test file and writes the specified number of lines to it.
     *
     * @param file The file to create and write to.
     * @param encoding The character encoding to use.
     * @param lineCount The number of lines to write to the file.
     * @return The list of lines that were written to the file.
     * @throws IOException If an I/O error occurs during file writing.
     */
    private List<String> createTestFileWithLines(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> linesToWrite = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, linesToWrite);
        return linesToWrite;
    }

    /**
     * Verifies that the LineIterator correctly iterates through all lines in a file
     * using the hasNext() and next() methods, and that it behaves correctly when exhausted.
     */
    @Test
    void lineIteratorShouldCorrectlyIterateAllLines() throws IOException {
        // Arrange
        final int lineCount = 3;
        final File testFile = new File(temporaryFolder, "test-file.txt");
        final List<String> expectedLines = createTestFileWithLines(testFile, UTF_8, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            // Act & Assert: Iterate through the file and verify each line
            int linesRead = 0;
            while (iterator.hasNext()) {
                final String actualLine = iterator.next();
                assertEquals(expectedLines.get(linesRead), actualLine, "Line " + linesRead + " should match");
                linesRead++;
            }

            assertEquals(lineCount, linesRead, "Should have read the correct number of lines.");

            // Assert: Verify the iterator is exhausted
            assertFalse(iterator.hasNext(), "hasNext() should be false at the end of iteration.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw NoSuchElementException when no more lines are available.");
        }
    }
}