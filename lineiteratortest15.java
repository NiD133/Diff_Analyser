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
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link LineIterator}.
 */
@DisplayName("LineIterator Test Suite")
public class LineIteratorTest {

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings for testing purposes.
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings, e.g., ["LINE 0", "LINE 1", ...].
     */
    private List<String> createStringLines(final int lineCount) {
        return IntStream.range(0, lineCount)
            .mapToObj(i -> "LINE " + i)
            .collect(Collectors.toList());
    }

    /**
     * Creates a test file with a specified number of lines and encoding.
     *
     * @param file The file to write to.
     * @param encoding The character encoding.
     * @param lineCount The number of lines to write.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    @DisplayName("Should correctly iterate over a file with a varying number of lines")
    void lineIterator_forFile_iteratesAllLinesCorrectly(final int lineCount) throws IOException {
        // Arrange
        final File testFile = new File(temporaryFolder, "test.txt");
        final List<String> expectedLines = createLinesFile(testFile, StandardCharsets.UTF_8.name(), lineCount);

        // Act
        final List<String> actualLines = new ArrayList<>();
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            iterator.forEachRemaining(actualLines::add);
        }

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    @DisplayName("remove() should always throw UnsupportedOperationException")
    void remove_shouldThrowUnsupportedOperationException() throws IOException {
        // Arrange
        final File testFile = new File(temporaryFolder, "test.txt");
        createLinesFile(testFile, StandardCharsets.UTF_8.name(), 1); // File must not be empty

        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            assertTrue(iterator.hasNext(), "Iterator should have a line to be able to call remove()");
            
            // Act & Assert
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }

    @Test
    @DisplayName("next() should throw NoSuchElementException when no more lines are available")
    void next_whenExhausted_shouldThrowNoSuchElementException() throws IOException {
        // Arrange
        final File testFile = new File(temporaryFolder, "test.txt");
        createLinesFile(testFile, StandardCharsets.UTF_8.name(), 1);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            iterator.next(); // Consume the only line
            assertFalse(iterator.hasNext(), "Iterator should be exhausted");

            // Act & Assert
            assertThrows(NoSuchElementException.class, iterator::next);
        }
    }
    
    @Test
    @DisplayName("nextLine() should throw NoSuchElementException when no more lines are available")
    void nextLine_whenExhausted_shouldThrowNoSuchElementException() throws IOException {
        // Arrange
        final File testFile = new File(temporaryFolder, "test.txt");
        createLinesFile(testFile, StandardCharsets.UTF_8.name(), 1);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            iterator.nextLine(); // Consume the only line
            assertFalse(iterator.hasNext(), "Iterator should be exhausted");

            // Act & Assert
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    @DisplayName("With a custom filter, should skip lines that do not match the predicate")
    void lineIterator_withCustomFiltering_skipsInvalidLines() throws IOException {
        // Arrange
        final List<String> allLines = createStringLines(9); // "LINE 0" to "LINE 8"
        final StringReader reader = new StringReader(String.join(System.lineSeparator(), allLines));

        // The filter will remove "LINE 1", "LINE 4", "LINE 7".
        final List<String> expectedLines = Arrays.asList("LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8");

        // Act: Create a LineIterator with a custom implementation of isValidLine.
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                // Skips lines where the last digit 'd' satisfies (d % 3) == 1.
                final char lastChar = line.charAt(line.length() - 1);
                return Character.getNumericValue(lastChar) % 3 != 1;
            }
        }) {
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            // Assert
            assertEquals(expectedLines, actualLines);
        }
    }
}