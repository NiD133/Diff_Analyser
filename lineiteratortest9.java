package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    @TempDir
    private File temporaryFolder;

    /**
     * Creates a list of strings for testing purposes.
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings, e.g., ["LINE 0", "LINE 1", ...].
     */
    private List<String> createTestLines(final int lineCount) {
        return IntStream.range(0, lineCount)
                .mapToObj(i -> "LINE " + i)
                .collect(Collectors.toList());
    }

    /**
     * Creates a test file with the given lines and encoding.
     *
     * @param file The file to write to.
     * @param encoding The character encoding.
     * @param lineCount The number of lines to write.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createTestFileWithLines(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createTestLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    @Test
    void lineIteratorShouldReadAllLinesFromUtf8File() throws IOException {
        // Arrange
        final String encoding = StandardCharsets.UTF_8.name();
        final File testFile = new File(temporaryFolder, "LineIteratorTest.txt");
        final List<String> expectedLines = createTestFileWithLines(testFile, encoding, 3);
        final List<String> actualLines = new ArrayList<>();

        // Act: Use try-with-resources to ensure the iterator is closed automatically.
        try (final LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            while (iterator.hasNext()) {
                actualLines.add(iterator.nextLine());
            }
            // Assert that the iterator is exhausted after the loop.
            assertFalse(iterator.hasNext(), "Iterator should be exhausted after reading all lines.");
        }

        // Assert: Verify that the lines read match the lines written.
        assertEquals(expectedLines, actualLines);
    }
}