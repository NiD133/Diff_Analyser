package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path; // Added for Path usage
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LineIteratorTest {  // Renamed class for better context

    @TempDir
    Path temporaryFolder; // Use Path instead of File, more modern

    private static final String UTF_8 = StandardCharsets.UTF_8.name(); // Moved UTF-8 definition

    /**
     * Tests FileUtils.lineIterator() with only UTF-8 encoding, retrieving lines one at a time using nextLine().
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testLineIterator_nextLineOnly_Utf8Encoding() throws Exception { // More descriptive name
        // Arrange: Set up the test environment
        final String encoding = UTF_8;
        Path testFile = temporaryFolder.resolve("LineIterator-nextOnly.txt"); //Use Path
        final List<String> expectedLines = createLinesFile(testFile.toFile(), encoding, 3); //pass file

        // Act: Create a LineIterator and iterate through the file using nextLine()
        final LineIterator lineIterator = FileUtils.lineIterator(testFile.toFile(), encoding);

        // Assert: Verify that the lines read from the iterator match the expected lines
        assertLines(expectedLines, lineIterator);

        LineIterator.closeQuietly(lineIterator); // Important: Close the iterator to release resources.
    }

    /**
     * Creates a file with the specified number of lines, using the given encoding.
     *
     * @param file      The file to create.
     * @param encoding  The encoding to use when writing to the file.
     * @param numLines  The number of lines to write to the file.
     * @return A list containing the lines that were written to the file.
     * @throws IOException if an error occurs while writing to the file.
     */
    private List<String> createLinesFile(File file, String encoding, int numLines) throws IOException { //file is already file
        final List<String> lines = new ArrayList<>();
        try (var writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) { //Use try-with-resources, and Path
            for (int i = 0; i < numLines; i++) {
                String line = "Line " + i;
                lines.add(line);
                writer.write(line);
                writer.newLine();
            }
        }
        return lines;
    }

    /**
     * Asserts that the lines read from the LineIterator match the expected lines.
     * It reads all lines from the iterator and compares them to the expected lines.
     *
     * @param expectedLines The expected lines.
     * @param iterator      The LineIterator to read from.
     */
    private void assertLines(List<String> expectedLines, LineIterator iterator) {
        int lineNum = 0;
        try {
            while (iterator.hasNext()) {
                String actualLine = iterator.nextLine();
                assertEquals(expectedLines.get(lineNum), actualLine, "Line " + lineNum + " does not match.");
                lineNum++;
            }
            assertEquals(expectedLines.size(), lineNum, "Number of lines read does not match expected size."); //Check that all lines are read
            assertFalse(iterator.hasNext(), "Iterator should not have more lines after reading all expected lines.");

        } finally {
            // Always close the iterator, even if an exception occurs.
            LineIterator.closeQuietly(iterator);
        }
    }
}