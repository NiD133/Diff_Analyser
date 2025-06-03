package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LineIteratorExampleTest {

    @TempDir
    Path temporaryFolder; // JUnit 5 annotation for creating a temporary directory

    // Helper method to create a file with specified lines
    private File createLinesFile(Path filePath, String encoding, int numberOfLines) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            lines.add("Line " + i);
        }

        if (encoding == null) {
            Files.write(filePath, lines); // Uses default charset
        } else {
            Files.write(filePath, lines, StandardCharsets.UTF_8); //example of a different encoding
        }
        return filePath.toFile();
    }

    @Test
    public void testLineIteratorNextOnly() throws IOException {
        // GIVEN: A file with a few lines
        String encoding = null; // Use default system encoding
        Path testFilePath = temporaryFolder.resolve("testFile.txt"); // Resolve a path within the temp folder
        File testFile = createLinesFile(testFilePath, encoding, 3);
        List<String> expectedLines = Files.readAllLines(testFile.toPath());


        // WHEN: Iterating through the file using LineIterator.next()
        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // THEN: Each line should match the expected content
            int lineNumber = 0;
            while (iterator.hasNext()) {
              String actualLine = iterator.next();
              assertEquals(expectedLines.get(lineNumber), actualLine, "Line " + lineNumber + " should match.");
              lineNumber++;
            }


            // THEN:  After reading all lines, hasNext() should return false.
            assertFalse(iterator.hasNext(), "Iterator should have no more lines.");
        }
    }
}