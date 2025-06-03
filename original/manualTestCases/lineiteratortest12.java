package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class focuses on verifying the behavior of the IOUtils.readLines() method
 * when reading files with a single line.  It uses temporary files to ensure
 * isolation and repeatability of the tests.
 */
public class ReadLinesSingleLineTest {

    @TempDir
    File tempDir;  // JUnit 5 annotation to create a temporary directory for each test

    /**
     * Tests reading a file with exactly one line using IOUtils.readLines().
     *
     * @throws IOException if an I/O error occurs while creating or reading the file.
     */
    @Test
    public void testReadSingleLineFile() throws IOException {
        // 1.  Setup: Create a temporary file and write a single line of text to it.
        File testFile = new File(tempDir, "single_line_file.txt");
        String expectedLine = "This is the only line in the file.";
        Files.write(testFile.toPath(), List.of(expectedLine), StandardCharsets.UTF_8);

        // 2.  Execution: Read the lines from the file using IOUtils.readLines().
        List<String> lines = IOUtils.readLines(testFile.toURI().toURL(), StandardCharsets.UTF_8);

        // 3.  Verification:  Assert that the list contains exactly one element,
        //     and that the element is equal to the expected line.
        assertNotNull(lines, "The list of lines should not be null.");
        assertEquals(1, lines.size(), "The list should contain only one line.");
        assertEquals(expectedLine, lines.get(0), "The read line should match the expected line.");
    }

    /**
     * This test ensures that a file with one empty line is correctly read.
     * @throws IOException
     */
     @Test
    public void testReadSingleEmptyLineFile() throws IOException {
        // 1. Setup: Create a temporary file and write a single empty line to it.
        File testFile = new File(tempDir, "empty_line_file.txt");
        Files.write(testFile.toPath(), List.of(""), StandardCharsets.UTF_8);

        // 2. Execution: Read the lines from the file using IOUtils.readLines().
        List<String> lines = IOUtils.readLines(testFile.toURI().toURL(), StandardCharsets.UTF_8);

        // 3. Verification: Assert that the list contains exactly one element, and that the element is an empty string.
        assertNotNull(lines, "The list of lines should not be null.");
        assertEquals(1, lines.size(), "The list should contain only one line.");
        assertTrue(lines.get(0).isEmpty(), "The read line should be an empty string.");
    }
}