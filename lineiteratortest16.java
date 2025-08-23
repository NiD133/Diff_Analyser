package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the {@link LineIterator} class, focusing on its core functionality
 * including file reading, empty file handling, and custom line filtering.
 */
@DisplayName("LineIterator Functionality Test")
class LineIteratorFunctionalityTest {

    @TempDir
    private File temporaryFolder;
    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File(temporaryFolder, "test.txt");
    }

    /**
     * Creates a list of strings for testing, e.g., ["LINE 0", "LINE 1", ...].
     *
     * @param lineCount The number of lines to generate.
     * @return A list of generated strings.
     */
    private List<String> generateTestLines(final int lineCount) {
        return IntStream.range(0, lineCount)
            .mapToObj(i -> "LINE " + i)
            .collect(Collectors.toList());
    }

    @Test
    @DisplayName("Iterator on an empty file should be empty")
    void lineIterator_onEmptyFile_isEmpty() throws IOException {
        // Arrange
        // An empty file is created by default via the @BeforeEach setup.
        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            assertFalse(iterator.hasNext(), "Iterator for an empty file should not have a next line.");
            assertThrows(NoSuchElementException.class, iterator::next, "Calling next() on an exhausted iterator should throw.");
        }
    }

    @Test
    @DisplayName("Iterator should correctly read all lines from a non-empty file")
    void lineIterator_onNonEmptyFile_readsAllLinesCorrectly() throws IOException {
        // Arrange
        final List<String> expectedLines = generateTestLines(3);
        FileUtils.writeLines(testFile, expectedLines, StandardCharsets.UTF_8);
        final List<String> actualLines = new ArrayList<>();

        // Act
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            // Assert basic iterator properties
            assertThrows(UnsupportedOperationException.class, iterator::remove, "remove() should be an unsupported operation.");

            iterator.forEachRemaining(actualLines::add);
        }

        // Assert
        assertIterableEquals(expectedLines, actualLines, "The lines read should match the lines written.");
    }

    @Test
    @DisplayName("Iterator should correctly handle post-iteration calls")
    void lineIterator_afterIteration_throwsExceptionOnNext() throws IOException {
        // Arrange
        final List<String> expectedLines = generateTestLines(2);
        FileUtils.writeLines(testFile, expectedLines, StandardCharsets.UTF_8);

        // Act
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            // Exhaust the iterator
            while (iterator.hasNext()) {
                iterator.next();
            }

            // Assert
            assertFalse(iterator.hasNext(), "hasNext() should be false after iteration.");
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw after iteration.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw after iteration.");
        }
    }

    @Test
    @DisplayName("Iterator with a custom filter should only return valid lines")
    void lineIterator_withCustomFilter_skipsInvalidLines() throws IOException {
        // Arrange
        final List<String> inputLines = generateTestLines(9); // "LINE 0" through "LINE 8"
        final Reader reader = new StringReader(String.join(System.lineSeparator(), inputLines));

        // Lines are filtered if the last digit (d) satisfies (d % 3 == 1).
        // This means lines ending in 1, 4, 7 will be skipped.
        final List<String> expectedLines = Arrays.asList("LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8");
        final List<String> actualLines = new ArrayList<>();

        // Act
        // Create a LineIterator with a custom implementation of isValidLine.
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                // This custom filter logic is now clear and well-documented.
                final char lastChar = line.charAt(line.length() - 1);
                if (Character.isDigit(lastChar)) {
                    final int lastDigit = Character.getNumericValue(lastChar);
                    return lastDigit % 3 != 1;
                }
                return true; // Keep lines that don't end in a digit
            }
        }) {
            iterator.forEachRemaining(actualLines::add);
        }

        // Assert
        assertEquals(6, actualLines.size(), "The number of lines returned after filtering should be correct.");
        assertIterableEquals(expectedLines, actualLines, "The filtered lines should match the expected output.");
    }
}