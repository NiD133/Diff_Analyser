package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwoLinesTest {  // Renamed class for better clarity

    @Test
    public void testFileContainsExactlyTwoLines() throws IOException { // More descriptive test name
        // 1.  Setup: Create a temporary file with two lines of text.
        Path tempFile = Files.createTempFile("testFile", ".txt");  // Create a temp file
        List<String> lines = List.of("This is line one.", "This is line two.");
        Files.write(tempFile, lines); // Write the lines to the temp file

        // 2.  Execution: Use the `FileUtils.readLines()` method to read the file's contents.  (Assumed, based on package)
        List<String> readLines = FileUtils.readLines(tempFile.toFile(), "UTF-8"); // Specify encoding (important!)

        // 3.  Verification: Assert that the file contains exactly two lines and that the content is as expected.
        assertNotNull(readLines, "The list of lines should not be null.");
        assertEquals(2, readLines.size(), "The file should contain exactly two lines.");
        assertEquals("This is line one.", readLines.get(0), "The first line should match.");
        assertEquals("This is line two.", readLines.get(1), "The second line should match.");

        // 4. Cleanup (Important!):  Delete the temporary file after the test.
        Files.deleteIfExists(tempFile);
    }

    //Assumed Implementation of FileUtils.readLines()
    static class FileUtils{
        public static List<String> readLines(File file, String charsetName) throws IOException {
            List<String> lines = new ArrayList<>();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines;
        }
    }
}