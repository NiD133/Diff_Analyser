package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link LineIterator}.
 */
class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Asserts that the lines from the iterator match the expected lines.
     *
     * @param expectedLines the expected lines
     * @param iterator the line iterator
     */
    private void assertLinesMatch(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                assertEquals(expectedLines.get(i), iterator.nextLine(), "Line mismatch at index " + i);
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines");
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Creates a file with a specified number of lines.
     *
     * @param file the target file
     * @param encoding the encoding to use
     * @param lineCount the number of lines
     * @return the list of lines created
     * @throws IOException if an I/O error occurs
     */
    private List<String> createFileWithLines(final File file, final String encoding, final int lineCount) throws IOException {
        List<String> lines = generateLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Generates a list of lines with the format "LINE X".
     *
     * @param lineCount the number of lines to generate
     * @return the list of generated lines
     */
    private List<String> generateLines(final int lineCount) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Tests reading a file with a specified number of lines.
     *
     * @param lineCount the number of lines in the test file
     * @throws IOException if an I/O error occurs
     */
    private void testFileWithLines(final int lineCount) throws IOException {
        File testFile = new File(temporaryFolder, "LineIterator-" + lineCount + "-test.txt");
        List<String> lines = createFileWithLines(testFile, UTF_8, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            int idx = 0;
            while (iterator.hasNext()) {
                assertEquals(lines.get(idx), iterator.next(), "Line mismatch at index " + idx);
                idx++;
            }
            assertEquals(lines.size(), idx, "Line count mismatch");

            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    void testCloseIteratorEarly() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createFileWithLines(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertNotNull(iterator.next(), "Expected a line");
            assertTrue(iterator.hasNext(), "Expected more lines");

            iterator.close();
            assertFalse(iterator.hasNext(), "No more lines expected");
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);

            iterator.close(); // Ensure no exception on double close
        }
    }

    @Test
    void testConstructorWithNullReader() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null));
    }

    private void testLineFiltering(final List<String> lines, final Reader reader) throws IOException {
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                return (line.charAt(line.length() - 1) - '0') % 3 != 1;
            }
        }) {
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            int idx = 0;
            int actualLines = 0;
            while (iterator.hasNext()) {
                assertEquals(lines.get(idx), iterator.next(), "Line mismatch at index " + idx);
                actualLines++;
                idx++;
                if (idx % 3 == 1) {
                    idx++;
                }
            }
            assertEquals(9, lines.size(), "Expected line count mismatch");
            assertEquals(6, actualLines, "Actual line count mismatch");

            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    void testFilteringWithBufferedReader() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-test.txt");
        List<String> lines = createFileWithLines(testFile, UTF_8, 9);

        try (Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()))) {
            testLineFiltering(lines, reader);
        }
    }

    @Test
    void testFilteringWithFileReader() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-test.txt");
        List<String> lines = createFileWithLines(testFile, UTF_8, 9);

        try (Reader reader = Files.newBufferedReader(testFile.toPath())) {
            testLineFiltering(lines, reader);
        }
    }

    @Test
    void testInvalidEncodingThrowsException() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createFileWithLines(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, "INVALID_ENCODING"));
    }

    @Test
    void testMissingFileThrowsException() throws Exception {
        File missingFile = new File(temporaryFolder, "nonexistent-file.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(missingFile, UTF_8));
    }

    @Test
    void testNextLineOnlyWithDefaultEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    void testNextLineOnlyWithNullEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, null)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    void testNextLineOnlyWithUtf8Encoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    void testNextMethod() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, null)) {
            for (int i = 0; i < lines.size(); i++) {
                assertEquals(lines.get(i), iterator.next(), "Line mismatch at index " + i);
            }
            assertFalse(iterator.hasNext(), "No more lines expected");
        }
    }

    @Test
    void testNextMethodThrowsExceptionOnIOException() throws Exception {
        Reader faultyReader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };

        try (LineIterator iterator = new LineIterator(faultyReader)) {
            assertThrows(IllegalStateException.class, iterator::hasNext);
        }
    }

    @Test
    void testSingleLineFile() throws Exception {
        testFileWithLines(1);
    }

    @Test
    void testThreeLineFile() throws Exception {
        testFileWithLines(3);
    }

    @Test
    void testTwoLineFile() throws Exception {
        testFileWithLines(2);
    }

    @Test
    void testValidEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createFileWithLines(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            int count = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next());
                count++;
            }
            assertEquals(3, count, "Line count mismatch");
        }
    }

    @Test
    void testZeroLineFile() throws Exception {
        testFileWithLines(0);
    }
}