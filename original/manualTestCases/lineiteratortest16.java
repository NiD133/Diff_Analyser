package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneratedTestCase {

    @Test
    public void testZeroLinesFromFile() throws IOException {
        // 1. Arrange: Create a temporary file (empty for this test).
        Path tempFile = Files.createTempFile("testZeroLines", ".txt");
        File file = tempFile.toFile();
        file.deleteOnExit(); // Ensure the file is deleted after the test runs.

        // 2. Act: Read all lines from the file using Files.readAllLines.
        List<String> lines = Files.readAllLines(tempFile);

        // 3. Assert: Verify that the list of lines is empty.
        assertEquals(0, lines.size(), "The file should contain zero lines.");
    }
}