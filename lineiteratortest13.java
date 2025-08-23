package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the {@link LineIterator} class.
 *
 * This suite focuses on the core functionality: iterating over lines from a file,
 * handling exceptions, and custom line filtering.
 */
@DisplayName("Tests for LineIterator")
class LineIteratorTest {

    /** A temporary directory for creating test files. */
    @TempDir
    private File temporaryFolder;

    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File(temporaryFolder, "test.txt");
    }

    /**
     * Tests that the iterator correctly reads all lines from a file in order.
     */
    @Test
    @DisplayName("Should iterate through all lines of a file correctly")
    void testBasicFileIteration() throws IOException {
        // Arrange
        final List<String> expectedLines = createLinesFile(testFile, 3);

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            assertIteratorMatches(expectedLines, iterator);
        }
    }

    /**
     * Tests that calling remove() on the iterator always throws an UnsupportedOperationException,
     * as the iterator is read-only.
     */
    @Test
    @DisplayName("remove() should throw UnsupportedOperationException")
    void testRemove_throwsUnsupportedOperationException() throws IOException {
        // Arrange
        createLinesFile(testFile, 1); // File needs at least one line

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            assertTrue(iterator.hasNext(), "Iterator should have a line to read.");
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() is not supported and should throw.");
        }
    }

    /**
     * Tests that calling next() or nextLine() after the iterator is exhausted
     * throws a NoSuchElementException.
     */
    @Test
    @DisplayName("next() on an exhausted iterator should throw NoSuchElementException")
    void testNext_onExhaustedIterator_throwsNoSuchElementException() throws IOException {
        // Arrange
        createLinesFile(testFile, 1); // A single line is sufficient

        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            // Act: exhaust the iterator
            assertTrue(iterator.hasNext());
            iterator.next(); // Consume the only line

            // Assert
            assertFalse(iterator.hasNext(), "Iterator should be exhausted.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw when no elements are left.");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should also throw when no elements are left.");
        }
    }

    /**
     * Tests the filtering mechanism by subclassing LineIterator and overriding isValidLine().
     */
    @Test
    @DisplayName("isValidLine() should correctly filter lines from the iterator")
    void testIsValidLine_filtersAsExpected() {
        // Arrange: Create a list of lines and define the expected result after filtering.
        final List<String> inputLines = List.of(
            "LINE 0", "LINE 1", "LINE 2",
            "LINE 3", "LINE 4", "LINE 5"
        );
        final List<String> expectedLines = List.of(
            "LINE 0", "LINE 2", "LINE 3", "LINE 5"
        );
        final StringReader reader = new StringReader(String.join("\n", inputLines));

        // Act: Create a custom iterator that filters out lines ending with a digit 'd' where d % 3 == 1.
        final LineIterator filteringIterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                final char lastChar = line.charAt(line.length() - 1);
                // Keep the line only if the final digit is not 1, 4, 7, etc.
                return (lastChar - '0') % 3 != 1;
            }
        };

        // Assert
        assertIteratorMatches(expectedLines, filteringIterator);
    }

    // --- Helper Methods ---

    /**
     * Asserts that the lines from the iterator match the expected list of strings.
     * This helper consumes and closes the iterator.
     *
     * @param expectedLines The list of lines we expect to read.
     * @param iterator The LineIterator to test.
     */
    private void assertIteratorMatches(final List<String> expectedLines, final LineIterator iterator) {
        try {
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines);
        } finally {
            // Ensure the iterator and its underlying reader are always closed.
            iterator.close();
        }
    }

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file The file to write to.
     * @param lineCount The number of lines to generate.
     * @return A list of the strings that were written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), lines);
        return lines;
    }

    /**
     * Generates a list of strings for testing purposes (e.g., "LINE 0", "LINE 1", ...).
     *
     * @param lineCount The number of lines to generate.
     * @return A new list of strings.
     */
    private List<String> createStringLines(final int lineCount) {
        return IntStream.range(0, lineCount)
            .mapToObj(i -> "LINE " + i)
            .collect(Collectors.toList());
    }
}