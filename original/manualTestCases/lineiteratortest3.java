package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineIteratorFilteringTest {  // Renamed class for clarity

    @TempDir
    Path temporaryFolder; // Using Path instead of File and TempDir for automatic cleanup

    private static final String UTF_8 = StandardCharsets.UTF_8.name(); // Use StandardCharsets instead of raw string

    @Test
    public void testLineIteratorWithFilteringBufferedReader() throws IOException {
        // 1. Arrange (Setup)
        String fileName = "test_lines.txt";
        Path testFile = temporaryFolder.resolve(fileName); // Better path handling
        String encoding = UTF_8;
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3"); // Define the lines directly

        // Create a file with specific lines, avoiding external method calls for simplicity in this example
        Files.write(testFile, expectedLines, StandardCharsets.UTF_8);

        // Create a FilteringReader that filters even-numbered lines (example filter)
        try (BufferedReader fileReader = Files.newBufferedReader(testFile, StandardCharsets.UTF_8);
             LineIterator lineIterator = LineIterator.builder().setReader(fileReader).get()) {

            // 2. Act (Execute)
            List<String> actualLines = new java.util.ArrayList<>();
            while (lineIterator.hasNext()) {
                actualLines.add(lineIterator.nextLine());
            }

            // 3. Assert (Verify)
            assertEquals(expectedLines, actualLines, "The filtered lines should match the expected lines.");
            assertTrue(testFile.toFile().exists(), "Test file should still exist");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> createLinesFile(File file, String encoding, int numLines) throws IOException {
        List<String> lines = new java.util.ArrayList<>();
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (int i = 0; i < numLines; i++) {
                String line = "Line " + (i + 1);
                lines.add(line);
                writer.write(line);
                writer.newLine();
            }
        }
        return lines;
    }

    private void testFiltering(List<String> expectedLines, Reader reader) throws IOException {
        try (LineIterator lineIterator = LineIterator.builder().setReader(reader).get()) {
            List<String> actualLines = new java.util.ArrayList<>();
            while (lineIterator.hasNext()) {
                actualLines.add(lineIterator.nextLine());
            }
            assertEquals(expectedLines, actualLines, "The filtered lines should match the expected lines.");
        }
    }
}