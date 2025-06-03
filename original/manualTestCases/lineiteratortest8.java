package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * This class contains a test case for the FileUtils.lineIterator() method,
 * focusing on scenarios where the encoding is explicitly set to null.
 * It aims to verify the correct behavior of the line iterator when
 * using the default system encoding.
 */
public class LineIteratorWithNullEncodingTest {

    @TempDir
    File temporaryFolder; // JUnit 5 annotation for creating temporary folders.

    /**
     * Tests the `FileUtils.lineIterator()` method with a `null` encoding.
     * It creates a file with some lines, then uses the line iterator with
     * a null encoding to read and verify the lines.
     *
     * @throws IOException if an I/O error occurs during file creation or reading.
     */
    @Test
    public void testLineIteratorWithNullEncoding() throws IOException {
        // 1. Setup: Define the encoding (null in this case) and the filename.
        final String encoding = null; // Null encoding means use the system's default encoding.
        final File testFile = new File(temporaryFolder, "testFile.txt");

        // 2. Create a test file with some lines.
        final List<String> expectedLines = createLinesFile(testFile, encoding, 3);

        // 3. Create a LineIterator using FileUtils with the specified file and null encoding.
        final LineIterator iterator = FileUtils.lineIterator(testFile, encoding);

        // 4. Assertion: Read and compare the lines from the iterator with the expected lines.
        assertLines(expectedLines, iterator);
    }

    // -------------------- Helper Methods (for readability and reusability) --------------------

    /**
     * Creates a file with the specified number of lines, each containing a simple text,
     * using the given encoding.
     *
     * @param file     The file to create.
     * @param encoding The encoding to use (null for system default).
     * @param numberOfLines The number of lines to write to the file.
     * @return A list of the lines that were written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(File file, String encoding, int numberOfLines) throws IOException {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            lines.add("Line " + (i + 1));
        }
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Asserts that the lines read from the LineIterator match the expected lines.
     * It iterates through the LineIterator and compares each line with the
     * corresponding expected line.  It also handles closing the iterator.
     *
     * @param expectedLines The expected lines.
     * @param iterator      The LineIterator to read from.
     */
    private void assertLines(List<String> expectedLines, LineIterator iterator) {
        try {
            int lineIndex = 0;
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                assertEquals(expectedLines.get(lineIndex), line, "Line " + (lineIndex + 1) + " does not match.");
                lineIndex++;
            }
            assertEquals(expectedLines.size(), lineIndex, "Number of lines read does not match expected number.");

            // Verify that NoSuchElementException is thrown if we try to read past the last line.
            assertThrows(NoSuchElementException.class, iterator::nextLine, "NoSuchElementException should be thrown.");
            assertFalse(iterator.hasNext(), "hasNext() should return false after reaching the end.");
        } finally {
            // Ensure the iterator is always closed to release resources.
            LineIterator.closeQuietly(iterator);
        }
    }
}