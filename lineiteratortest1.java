package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings for testing.
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings.
     */
    private static List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file      The file to write to.
     * @param lineCount The number of lines to create.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, lines);
        return lines;
    }

    /**
     * Creates a test file with a specified number of lines and encoding.
     *
     * @param file      The file to write to.
     * @param encoding  The encoding to use.
     * @param lineCount The number of lines to create.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    @Test
    void testCloseEarly() throws Exception {
        // Arrange
        final String encoding = StandardCharsets.UTF_8.name();
        final File testFile = new File(temporaryFolder, "test-close-early.txt");
        createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // Act: Read one line, then close the iterator before reaching the end.
            assertNotNull(iterator.next(), "First line should be readable");
            assertTrue(iterator.hasNext(), "Iterator should have more lines before close");
            iterator.close();

            // Assert: After closing, the iterator should be finished.
            assertFalse(iterator.hasNext(), "Iterator should have no more lines after close");
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw after close");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw after close");

            // Act: Closing again should have no effect.
            iterator.close();

            // Assert: State remains finished.
            assertFalse(iterator.hasNext(), "Iterator should still have no lines after second close");
            assertThrows(NoSuchElementException.class, iterator::next, "next() should still throw after second close");
        }
    }

    /**
     * Tests that a file can be iterated over completely. Also checks behavior
     * after the iteration is complete.
     *
     * @param lineCount The number of lines to test with.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void testFileIteration(final int lineCount) throws IOException {
        // Arrange
        final String encoding = StandardCharsets.UTF_8.name();
        final String fileName = "test-file-iteration-" + lineCount + ".txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> expectedLines = createLinesFile(testFile, encoding, lineCount);

        // Act & Assert
        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // The remove() operation is not supported.
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines, "The lines read from the iterator should match the expected lines.");

            // After iteration, hasNext() should be false.
            assertFalse(iterator.hasNext(), "Iterator should be exhausted");

            // Calling next() or nextLine() should throw an exception.
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw when no elements are left");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw when no elements are left");
        }
    }

    /**
     * Tests the filtering capabilities of LineIterator by subclassing it and
     * overriding the isValidLine() method.
     */
    @Test
    void testFilteringWithIsValidLine() {
        // Arrange: Create lines "LINE 0" through "LINE 8".
        final List<String> inputLines = createStringLines(9);
        final String testData = String.join(System.lineSeparator(), inputLines);

        // Lines ending with a digit 'd' where (d % 3 == 1) are considered invalid.
        // This means lines ending in 1, 4, and 7 will be filtered out.
        final List<String> expectedLines = inputLines.stream()
            .filter(line -> {
                final char lastChar = line.charAt(line.length() - 1);
                return Character.getNumericValue(lastChar) % 3 != 1;
            })
            .collect(Collectors.toList());
        
        // Sanity check the expected lines
        assertEquals(Arrays.asList("LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8"), expectedLines);

        // Act & Assert: Create a custom LineIterator that filters lines.
        try (LineIterator iterator = new LineIterator(new StringReader(testData)) {
            @Override
            protected boolean isValidLine(final String line) {
                final char lastChar = line.charAt(line.length() - 1);
                final int lastDigit = Character.getNumericValue(lastChar);
                return lastDigit % 3 != 1;
            }
        }) {
            // The remove() operation is not supported.
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines, "The filtered lines should match the expected lines.");

            // After iteration, hasNext() should be false.
            assertFalse(iterator.hasNext(), "Iterator should be exhausted");

            // Calling next() or nextLine() should throw an exception.
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw when no elements are left");
        }
    }
}