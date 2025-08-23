package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * A temporary directory for creating test files.
     */
    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings for testing purposes, e.g., ["LINE 0", "LINE 1", ...].
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings.
     */
    private List<String> createTestLines(final int lineCount) {
        return IntStream.range(0, lineCount)
            .mapToObj(i -> "LINE " + i)
            .collect(Collectors.toList());
    }

    /**
     * Creates a test file with the given lines using the default encoding.
     *
     * @param file      The file to write to.
     * @param lineCount The number of lines to create in the file.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createTestFileWithLines(final File file, final int lineCount) throws IOException {
        final List<String> lines = createTestLines(lineCount);
        FileUtils.writeLines(file, lines);
        return lines;
    }

    /**
     * Creates a test file with the given lines using a specific encoding.
     *
     * @param file      The file to write to.
     * @param encoding  The character encoding to use.
     * @param lineCount The number of lines to create in the file.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createTestFileWithLines(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createTestLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Tests the complete lifecycle of a LineIterator, from creation to exhaustion,
     * for files with different numbers of lines.
     *
     * @param lineCount The number of lines to include in the test file.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    void testFullIterationLifecycle(final int lineCount) throws IOException {
        // Arrange
        final String encoding = StandardCharsets.UTF_8.name();
        final File testFile = new File(temporaryFolder, "test-file-" + lineCount + "-lines.txt");
        final List<String> expectedLines = createTestFileWithLines(testFile, encoding, lineCount);

        // Act & Assert
        try (final LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // 1. Test that remove() is unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should be unsupported");

            // 2. Test that the iterator correctly reads all lines
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertEquals(expectedLines, actualLines,
                "The lines read from the file should match the expected lines.");

            // 3. Test behavior after the iterator is exhausted
            assertFalse(iterator.hasNext(), "hasNext() should be false after iteration");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw an exception after iteration is complete.");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw an exception after iteration is complete.");
        }
    }

    /**
     * Tests the filtering mechanism provided by overriding the isValidLine() method.
     */
    @Test
    void testFilteringWithIsValidLine() {
        // Arrange: Create a list of 9 lines ("LINE 0" to "LINE 8")
        final List<String> inputLines = createTestLines(9);
        final StringReader reader = new StringReader(String.join(System.lineSeparator(), inputLines));

        // We will filter out lines where the line number modulo 3 is 1 (i.e., LINE 1, LINE 4, LINE 7).
        final List<String> expectedLines = inputLines.stream()
            .filter(line -> {
                final int lineNumber = Integer.parseInt(line.substring(line.lastIndexOf(' ') + 1));
                return lineNumber % 3 != 1;
            })
            .collect(Collectors.toList());

        // Act: Create a custom LineIterator that filters lines
        try (final LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                final char lastChar = line.charAt(line.length() - 1);
                final int lineNumber = Character.getNumericValue(lastChar);
                return lineNumber % 3 != 1;
            }
        }) {
            // Assert: Collect the results and verify against the expected list
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            assertEquals(expectedLines, actualLines, "Iterator should return only valid lines.");
            assertEquals(6, actualLines.size(), "There should be 6 lines after filtering.");
        }
    }

    /**
     * Verifies that the deprecated nextLine() method works correctly.
     * This test is important for ensuring backward compatibility.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    @SuppressWarnings("deprecation") // Suppressing warning as we are intentionally testing a deprecated method.
    void testDeprecatedNextLineMethod() throws IOException {
        // Arrange
        final File testFile = new File(temporaryFolder, "test-file-default-encoding.txt");
        final List<String> expectedLines = createTestFileWithLines(testFile, 3);

        // Act & Assert
        try (final LineIterator iterator = FileUtils.lineIterator(testFile)) {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String expectedLine = expectedLines.get(i);
                final String actualLine = iterator.nextLine(); // Using the deprecated method
                assertEquals(expectedLine, actualLine, "Line " + i + " should match");
            }
            assertFalse(iterator.hasNext(), "Iterator should be exhausted after reading all lines.");
        }
    }
}