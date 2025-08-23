package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests advanced scenarios for the {@link LineIterator} class, including error handling and filtering.
 */
public class LineIteratorAdvancedTest {

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file target file
     * @param encoding the encoding to use while writing the lines
     * @param lineCount number of lines to create
     * @return A list of the lines written to the file.
     * @throws IOException If an I/O error occurs
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a list of distinct string lines for testing.
     *
     * @param lineCount number of lines to create
     * @return a new list of strings.
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    @DisplayName("Test that FileUtils.lineIterator throws an exception for an invalid encoding")
    @Test
    void lineIteratorWithInvalidEncodingThrowsException() throws IOException {
        // Arrange
        final String invalidEncoding = "XXXXXXXX";
        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createLinesFile(testFile, StandardCharsets.UTF_8.name(), 3);

        // Act & Assert
        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, invalidEncoding));
    }

    @DisplayName("Test iterating through files with different numbers of lines")
    @ParameterizedTest(name = "Run #{index} with {0} lines")
    @ValueSource(ints = {0, 1, 10})
    void testFileIteration(final int lineCount) throws IOException {
        // Arrange
        final String fileName = "LineIterator-" + lineCount + "-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> expectedLines = createLinesFile(testFile, StandardCharsets.UTF_8.name(), lineCount);

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
            // Assert that remove() is unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            // Assert that the iterator produces the correct sequence of lines
            int lineNum = 0;
            while (iterator.hasNext()) {
                final String line = iterator.next();
                assertEquals(expectedLines.get(lineNum), line, "Line " + lineNum + " should match");
                lineNum++;
            }
            assertEquals(expectedLines.size(), lineNum, "Should have read the correct number of lines");

            // Assert that the iterator is exhausted
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next, "next() should fail after iteration");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should fail after iteration");
        }
    }

    @DisplayName("Test LineIterator filtering by overriding isValidLine()")
    @Test
    void testFilteringWithOverriddenValidator() {
        // Arrange: Create a list of lines and determine the expected filtered result.
        final List<String> allLines = createStringLines(9);
        // The filter will remove lines where (lineNumber % 3 == 1).
        // So, "LINE 1", "LINE 4", "LINE 7" will be removed.
        final List<String> expectedLines = allLines.stream()
            .filter(line -> {
                final int lineNumber = Integer.parseInt(line.substring(5)); // "LINE ".length() is 5
                return lineNumber % 3 != 1;
            })
            .collect(Collectors.toList());

        final String content = String.join(System.lineSeparator(), allLines);
        final Reader reader = new StringReader(content);

        // Act & Assert
        try (final LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                // This custom validation filters out lines where the trailing number modulo 3 is 1.
                final int lineNumber = Integer.parseInt(line.substring(line.lastIndexOf(' ') + 1));
                return lineNumber % 3 != 1;
            }
        }) {
            // Assert that remove() is unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            // Assert that the iterator produces the correct filtered sequence of lines
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines);

            // Assert that the iterator is exhausted
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next, "next() should fail after iteration");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should fail after iteration");
        }
    }
}