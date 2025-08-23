package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator}.
 * This class focuses on filtering capabilities and basic file iteration.
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Tests that the iterator correctly reads all lines from a file.
     */
    @Test
    void testIterationOverFile() throws IOException {
        // Arrange: Create a test file with a known set of lines.
        final int lineCount = 4;
        final File testFile = new File(temporaryFolder, "testIteration.txt");
        final List<String> expectedLines = createLinesFile(testFile, UTF_8, lineCount);

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            // Collect all lines from the iterator
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            // Verify the collected lines match the expected lines
            assertEquals(expectedLines, actualLines);

            // Verify that the iterator is exhausted
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next, "next() should fail after iteration is complete");

            // Verify that the unsupported remove() method throws an exception
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }

    /**
     * Tests the custom filtering feature by subclassing LineIterator and overriding isValidLine().
     */
    @Test
    void testCustomFilteringWithIsValidLine() throws IOException {
        // Arrange: Create a test file with lines "LINE 0" through "LINE 8".
        final File testFile = new File(temporaryFolder, "testFiltering.txt");
        createLinesFile(testFile, UTF_8, 9);

        // The filter will skip lines where the last digit (d) satisfies (d % 3 == 1).
        // This means lines ending in 1, 4, and 7 will be removed.
        final List<String> expectedLines = Arrays.asList(
            "LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8");

        // Act & Assert
        try (Reader reader = Files.newBufferedReader(testFile.toPath(), StandardCharsets.UTF_8);
             LineIterator iterator = new LineIterator(reader) {
                 @Override
                 protected boolean isValidLine(final String line) {
                     // A more readable way to implement the filtering logic.
                     final char lastChar = line.charAt(line.length() - 1);
                     final int digit = Character.getNumericValue(lastChar);
                     return digit % 3 != 1;
                 }
             }) {

            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            assertEquals(expectedLines, actualLines);

            // Verify that the iterator is exhausted
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next);
        }
    }

    // --- Helper Methods ---

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file      The file to write to.
     * @param encoding  The encoding to use.
     * @param lineCount The number of lines to create.
     * @return A list of the lines that were written to the file.
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
}