package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * This class provides a test case for validating the behavior of the
 * {@link FileUtils#lineIterator(File, String)} method when reading files with a valid encoding.
 * It focuses on verifying that the iterator correctly reads all lines from a file
 * created using a specific encoding (UTF-8 in this case).
 */
public class LineIteratorValidEncodingTest {

    // Use a temporary directory for creating test files.  JUnit creates and cleans this up.
    @TempDir
    File temporaryFolder;

    /**
     * Tests that the {@link FileUtils#lineIterator(File, String)} method correctly reads
     * all lines from a file when provided with a valid encoding (UTF-8).
     *
     * @throws Exception if an error occurs during file creation or reading.  Specifically,
     *         an IOException could occur if the file cannot be created or read.
     */
    @Test
    public void testLineIteratorReadsAllLinesWithValidEncoding() throws Exception {
        // 1. Setup:  Define the encoding, filename, and number of lines.
        final String encoding = StandardCharsets.UTF_8.name(); // Explicitly use the name of the charset
        final String filename = "LineIterator-validEncoding.txt";
        final File testFile = new File(temporaryFolder, filename);
        final int numberOfLines = 3;

        // 2. Arrange: Create a test file with the specified encoding and number of lines.
        createLinesFile(testFile, encoding, numberOfLines);

        // 3. Act:  Obtain a LineIterator for the test file.
        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // 4. Assert: Iterate through the lines and verify that each line is not null and that the
            //    correct number of lines are read.
            int lineCount = 0;
            while (iterator.hasNext()) {
                String line = iterator.next();
                assertNotNull(line, "Line should not be null"); // More descriptive message
                lineCount++;
            }
            assertEquals(numberOfLines, lineCount, "Number of lines read should match the expected value."); // More descriptive message
        }
    }

    /**
     * Helper method to create a file with the specified encoding and number of lines.
     *
     * @param file The file to create.
     * @param encoding The encoding to use.
     * @param numberOfLines The number of lines to write to the file.
     * @throws IOException if an error occurs during file creation.
     */
    private void createLinesFile(File file, String encoding, int numberOfLines) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            lines.add("Line " + i);
        }
        FileUtils.writeLines(file, encoding, lines);
    }
}