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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the filtering capabilities of {@link LineIterator} by overriding the
 * {@code isValidLine(String)} method.
 */
public class LineIteratorFilteringTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings for testing, e.g., ["LINE 0", "LINE 1", ...].
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings.
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Creates a test file with a specified number of lines and a given encoding.
     *
     * @param file The target file.
     * @param encoding The encoding to use.
     * @param lineCount The number of lines to create.
     * @throws IOException If an I/O error occurs.
     */
    private void createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
    }

    @Test
    void testIsValidLineOverrideFiltersLines() throws IOException {
        // --- ARRANGE ---

        // 1. Define the expected lines after filtering.
        // The custom filter will remove lines where the last digit (as a number)
        // modulo 3 equals 1. For lines 0-8, this removes lines 1, 4, and 7.
        final List<String> expectedLines = Arrays.asList(
            "LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8"
        );

        // 2. Create a test file with 9 lines ("LINE 0" to "LINE 8").
        final File testFile = new File(temporaryFolder, "filtered-lines.txt");
        createLinesFile(testFile, UTF_8, 9);

        // --- ACT & ASSERT ---

        // Use a try-with-resources block to ensure the reader and iterator are closed.
        try (Reader reader = Files.newBufferedReader(testFile.toPath());
             // Create a LineIterator with a custom filtering rule.
             LineIterator iterator = new LineIterator(reader) {
                 @Override
                 protected boolean isValidLine(final String line) {
                     // Keep lines where the last digit (as an int) mod 3 is not 1.
                     final char lastChar = line.charAt(line.length() - 1);
                     final int lastDigit = Character.getNumericValue(lastChar);
                     return lastDigit % 3 != 1;
                 }
             }) {

            // Test that remove() is unsupported at any time.
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should be an unsupported operation.");

            // Collect the filtered lines from the iterator.
            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            // Verify the filtered lines match the expected lines.
            assertEquals(expectedLines, actualLines,
                "The filtered lines should match the expected lines.");

            // Verify the iterator contract after all elements have been consumed.
            assertFalse(iterator.hasNext(), "hasNext() should be false after consuming all lines.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw NoSuchElementException after all lines are read.");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw NoSuchElementException after all lines are read.");
        }
    }
}