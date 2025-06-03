package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * This class provides test cases for the {@link LineIterator} class, focusing on
 * scenarios where the iterator is closed prematurely.
 */
public class LineIteratorCloseEarlyTest {

    @TempDir
    File temporaryFolder; // JUnit Jupiter's way to create temporary folders

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    /**
     * Helper method to create a file with a specified number of lines.
     *
     * @param file      The file to create.
     * @param encoding  The character encoding to use.
     * @param lineCount The number of lines to write to the file.
     * @throws IOException If an error occurs while writing to the file.
     */
    private void createLinesFile(File file, String encoding, int lineCount) throws IOException {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("Line " + i);
        }
        FileUtils.writeLines(file, encoding, lines);
    }


    /**
     * Tests the behavior of {@link LineIterator} when it is closed before being fully exhausted.
     * It verifies that after closing the iterator:
     * 1.  `hasNext()` returns `false`.
     * 2.  `next()` throws a {@link NoSuchElementException}.
     * 3.  `nextLine()` throws a {@link NoSuchElementException}.
     * 4.  Closing the iterator again does not cause any errors.
     */
    @Test
    public void testCloseEarly() throws Exception {
        // 1. Setup: Create a file with multiple lines using a helper function.
        final String encoding = UTF_8;
        final File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        final int numberOfLines = 3;
        createLinesFile(testFile, encoding, numberOfLines);


        // 2. Action: Create a LineIterator, read the first line, and then close it prematurely.
        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // Get the first line
            assertNotNull(iterator.next(), "Line expected");  // Sanity check: Ensure we got a line.
            assertTrue(iterator.hasNext(), "More lines expected"); // Verify there are more lines initially

            // Close the iterator *before* reading all lines. This is the core test.
            iterator.close();

            // 3. Verification: After closing, attempts to access elements or check existence
            // should result in specific exceptions or return values.

            // Check that hasNext() now returns false.
            assertFalse(iterator.hasNext(), "No more lines expected after closing.");

            // Check that calling next() throws a NoSuchElementException.
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw NoSuchElementException after close.");

            // Check that calling nextLine() throws a NoSuchElementException.
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw NoSuchElementException after close.");

            // Try closing the iterator again. This should not cause any errors, and the same exceptions should be thrown.
            iterator.close();
            assertThrows(NoSuchElementException.class, iterator::next, "next() should still throw NoSuchElementException after a second close.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should still throw NoSuchElementException after a second close.");
        } // The try-with-resources block ensures the iterator is closed automatically.
    }
}