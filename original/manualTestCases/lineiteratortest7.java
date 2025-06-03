package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LineIteratorSimpleTest {

    @TempDir
    File temporaryFolder; // JUnit provides a temporary folder for tests

    @Test
    void testReadLinesFromFile_DefaultEncoding() throws IOException {
        // 1. Setup: Create a temporary file with known content.
        File testFile = new File(temporaryFolder, "testFile.txt");
        List<String> expectedLines = createLinesFile(testFile, 3); // Creates the file and returns the lines

        // 2. Execute: Use FileUtils.lineIterator to read the file line by line.
        LineIterator lineIterator = FileUtils.lineIterator(testFile);

        // 3. Verify: Assert that the lines read by the iterator match the expected lines.
        assertLines(expectedLines, lineIterator);

        // 4. Cleanup: While not strictly necessary because of @TempDir, explicitly closing the iterator
        // makes the test more robust.  In a real application, this is crucial.
        LineIterator.closeQuietly(lineIterator);
    }

    /**
     * Creates a file with a specified number of lines, each containing "Line " + lineNumber.
     *
     * @param file The file to create.
     * @param numberOfLines The number of lines to write.
     * @return A list containing the expected lines.
     * @throws IOException If an error occurs during file creation.
     */
    private List<String> createLinesFile(File file, int numberOfLines) throws IOException {
        List<String> lines = new ArrayList<>();
        List<String> linesToWrite = new ArrayList<>();

        for (int i = 1; i <= numberOfLines; i++) {
            String line = "Line " + i;
            lines.add(line);
            linesToWrite.add(line + System.lineSeparator()); // Add newline character for writing to file
        }

        java.nio.file.Files.write(file.toPath(), linesToWrite, java.nio.charset.StandardCharsets.UTF_8);
        return lines;
    }

    /**
     * Asserts that the lines read from the LineIterator match the expected lines.
     *
     * @param expectedLines The expected lines.
     * @param iterator The LineIterator to read from.
     */
    private void assertLines(List<String> expectedLines, LineIterator iterator) {
        int lineNumber = 0;
        while (iterator.hasNext()) {
            String actualLine = iterator.nextLine();
            assertEquals(expectedLines.get(lineNumber), actualLine, "Line " + (lineNumber + 1) + " should match.");
            lineNumber++;
        }
        assertEquals(expectedLines.size(), lineNumber, "The number of lines read should match the expected number.");
    }
}