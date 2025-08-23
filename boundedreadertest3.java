package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Tests that a {@link LineNumberReader} wrapping a {@link BoundedReader}
     * does not hang when reading a file that ends with a newline character.
     * This is a regression test for a potential infinite loop scenario that can occur
     * in this specific reader chain configuration.
     *
     * @param tempDir A temporary directory provided by JUnit to create test files.
     */
    @Test
    void whenReadingLinesFromFileWithTrailingNewline_shouldNotHang(@TempDir final Path tempDir) throws IOException {
        // ARRANGE: Create a test file with content ending in a newline.
        final String fileContent = "0\n1\n2\n";
        final Path testFile = tempDir.resolve("testWithTrailingNewline.txt");
        FileUtils.writeStringToFile(testFile.toFile(), fileContent, StandardCharsets.UTF_8);

        // A bound large enough that it won't be hit, to test the wrapping behavior.
        final int largeBound = 10_000_000;

        // ACT & ASSERT: Ensure reading the entire file through the reader chain completes within the timeout.
        assertTimeout(TIMEOUT, () -> {
            // The test verifies that the combination of LineNumberReader -> BoundedReader -> FileReader
            // correctly handles the end of the stream and terminates.
            try (Reader fileReader = Files.newBufferedReader(testFile, StandardCharsets.UTF_8);
                 Reader boundedReader = new BoundedReader(fileReader, largeBound);
                 LineNumberReader lineNumberReader = new LineNumberReader(boundedReader)) {

                // Read all lines to ensure the stream is fully consumed without hanging.
                while (lineNumberReader.readLine() != null) {
                    // Intentionally empty: the goal is to check for termination, not content.
                }
            }
        });
    }
}