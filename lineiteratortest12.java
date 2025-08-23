package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator}.
 */
class LineIteratorTest {

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings for testing, e.g., ["Line 1", "Line 2", ...].
     *
     * @param lineCount The number of lines to create.
     * @return A list of generated strings.
     */
    private List<String> createTestLines(final int lineCount) {
        if (lineCount == 0) {
            return Collections.emptyList();
        }
        return IntStream.rangeClosed(1, lineCount)
            .mapToObj(i -> "Line " + i)
            .collect(Collectors.toList());
    }

    /**
     * Creates a test file with a specified number of lines using UTF-8 encoding.
     *
     * @param lineCount The number of lines to write to the file.
     * @return A list of the lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createTestFile(final int lineCount) throws IOException {
        final String fileName = "LineIterator-" + lineCount + "-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> lines = createTestLines(lineCount);
        FileUtils.writeLines(testFile, lines, StandardCharsets.UTF_8);
        return lines;
    }

    @Test
    @DisplayName("Iterator for an empty file should have no lines")
    void testIteratorForEmptyFile() throws IOException {
        // Arrange
        final List<String> expectedLines = createTestFile(0);
        final File testFile = new File(temporaryFolder, "LineIterator-0-test.txt");

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8)) {
            assertFalse(iterator.hasNext(), "An iterator for an empty file should not have a next line.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an iterator for an empty file should throw an exception.");
        }
    }

    @Test
    @DisplayName("Iterator for a file with one line should read it correctly")
    void testIteratorForFileWithOneLine() throws IOException {
        // Arrange
        final List<String> expectedLines = createTestFile(1);
        final File testFile = new File(temporaryFolder, "LineIterator-1-test.txt");

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8)) {
            // Check iterator contract
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should be an unsupported operation.");

            // Read lines and verify
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines, "The line read should match the line in the file.");

            // Verify behavior at the end of iteration
            assertFalse(iterator.hasNext(), "hasNext() should be false after the line is read.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw an exception after the line is read.");
        }
    }

    @Test
    @DisplayName("Iterator for a file with multiple lines should read them all in order")
    void testIteratorForFileWithMultipleLines() throws IOException {
        // Arrange
        final List<String> expectedLines = createTestFile(5);
        final File testFile = new File(temporaryFolder, "LineIterator-5-test.txt");

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8)) {
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines, "The lines read should match the lines in the file.");
        }
    }

    @Test
    @DisplayName("Iterator should filter lines based on the isValidLine() method")
    void testIsValidLineFiltering() {
        // Arrange: Create lines and define the expected result after filtering.
        final List<String> allLines = createTestLines(9); // "Line 1" to "Line 9"
        final StringReader reader = new StringReader(String.join(System.lineSeparator(), allLines));

        // isValidLine will filter out lines where the number is even.
        final List<String> expectedLines = allLines.stream()
            .filter(line -> {
                final int number = Integer.parseInt(line.substring(5));
                return number % 2 != 0; // Keep odd-numbered lines
            })
            .collect(Collectors.toList());

        // Act & Assert
        // Create a custom LineIterator that filters out even-numbered lines.
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                // Extracts the number from "Line X"
                final int number = Integer.parseInt(line.substring(5));
                return number % 2 != 0;
            }
        }) {
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines, "Iterator should only return valid (odd-numbered) lines.");
        }
    }
}