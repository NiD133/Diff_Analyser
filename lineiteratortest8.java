package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator} focusing on specific configurations.
 * This test suite verifies that creating a LineIterator with a null encoding
 * correctly uses the platform's default encoding for reading files.
 */
public class LineIteratorTest {

    /**
     * A temporary directory for creating test files, managed by JUnit.
     */
    @TempDir
    public File temporaryFolder;

    /**
     * Creates a list of strings to be used as lines in a test file.
     *
     * @param lineCount The number of lines to generate.
     * @return A list of strings, e.g., ["LINE 0", "LINE 1", ...].
     */
    private List<String> createTestLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Creates a test file with the specified lines and encoding.
     *
     * @param file The file to write to.
     * @param encoding The character encoding to use (or null for default).
     * @param lineCount The number of lines to write.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createTestLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Asserts that the iterator provides the exact sequence of expected lines.
     *
     * @param expectedLines The lines we expect the iterator to return.
     * @param iterator The iterator to test.
     */
    private void assertIteratorMatchesLines(final List<String> expectedLines, final LineIterator iterator) {
        for (int i = 0; i < expectedLines.size(); i++) {
            assertTrue(iterator.hasNext(), "Iterator should have another line at index " + i);
            final String actualLine = iterator.next(); // Use non-deprecated next()
            assertEquals(expectedLines.get(i), actualLine, "Line " + i + " should match");
        }
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after reading all lines");
    }

    @Test
    void lineIteratorWithNullEncodingShouldUseDefaultEncoding() throws IOException {
        // ARRANGE
        final File testFile = new File(temporaryFolder, "test-null-encoding.txt");
        final String nullEncoding = null; // Passing null uses the platform's default encoding.
        final int lineCount = 3;
        final List<String> expectedLines = createLinesFile(testFile, nullEncoding, lineCount);

        // ACT & ASSERT
        // Use try-with-resources to ensure the iterator and its underlying reader are always closed.
        try (final LineIterator iterator = FileUtils.lineIterator(testFile, nullEncoding)) {
            assertIteratorMatchesLines(expectedLines, iterator);
        }
    }
}