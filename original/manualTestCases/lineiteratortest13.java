package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test case demonstrating a simple file reading scenario using Apache Commons IO.
 * This test specifically reads a file and verifies that it contains a certain number of lines.
 */
public class SimpleFileReadingTest {

    @TempDir
    Path temporaryFolder; // JUnit 5 annotation for creating a temporary directory

    /**
     * Tests reading a file containing three lines.
     *
     * @throws IOException if an error occurs during file creation or reading.
     */
    @Test
    public void testReadFileWithThreeLines() throws IOException {
        int expectedLineCount = 3;
        File testFile = createTestFile(expectedLineCount); // Create a file with 3 lines.

        List<String> lines = Files.readAllLines(testFile.toPath()); // Read all lines from the file.

        assertEquals(expectedLineCount, lines.size(), "The file should contain " + expectedLineCount + " lines.");
    }

    /**
     * Creates a test file with a specified number of lines in the temporary directory.
     *
     * @param numberOfLines The number of lines to write to the file.
     * @return The created file.
     * @throws IOException if an error occurs during file creation or writing.
     */
    private File createTestFile(int numberOfLines) throws IOException {
        File testFile = temporaryFolder.resolve("testfile.txt").toFile(); // Create the file within the temp directory.
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            lines.add("Line " + (i + 1));
        }
        Files.write(testFile.toPath(), lines); // Write the lines to the file.
        return testFile;
    }
}