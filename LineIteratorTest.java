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
public class LineIteratorTest {

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
                assertEquals(expectedLines.get(i), iterator.nextLine(), "Mismatch at line " + i);
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines");
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Creates a file with a specified number of lines and returns the lines.
     *
     * @param file the file to write to
     * @param lineCount the number of lines to create
     * @return the list of lines created
     * @throws IOException if an I/O error occurs
     */
    private List<String> createFileWithLines(final File file, final int lineCount) throws IOException {
        return createFileWithLines(file, UTF_8, lineCount);
    }

    /**
     * Creates a file with a specified number of lines and encoding, and returns the lines.
     *
     * @param file the file to write to
     * @param encoding the encoding to use
     * @param lineCount the number of lines to create
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
     * @param lineCount the number of lines in the file
     * @throws IOException if an I/O error occurs
     */
    private void testFileWithLines(final int lineCount) throws IOException {
        File testFile = new File(temporaryFolder, "LineIterator-" + lineCount + "-test.txt");
        List<String> lines = createFileWithLines(testFile, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            int idx = 0;
            while (iterator.hasNext()) {
                assertEquals(lines.get(idx), iterator.next(), "Mismatch at line " + idx);
                idx++;
            }
            assertEquals(lines.size(), idx, "Line count mismatch");

            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testCloseEarly() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createFileWithLines(testFile, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertNotNull(iterator.next(), "Expected a line");
            assertTrue(iterator.hasNext(), "Expected more lines");

            iterator.close();
            assertFalse(iterator.hasNext(), "No more lines expected");
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);

            iterator.close(); // Ensure no exception on double close
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testConstructorWithNullReader() {
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
                assertEquals(lines.get(idx), iterator.next(), "Mismatch at line " + idx);
                actualLines++;
                idx++;
                if (idx % 3 == 1) {
                    idx++;
                }
            }
            assertEquals(9, lines.size(), "Expected 9 lines");
            assertEquals(9, idx, "Expected 9 lines");
            assertEquals(6, actualLines, "Expected 6 valid lines");

            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testFilteringWithBufferedReader() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-test.txt");
        List<String> lines = createFileWithLines(testFile, 9);

        try (Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()))) {
            testLineFiltering(lines, reader);
        }
    }

    @Test
    public void testFilteringWithFileReader() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-test.txt");
        List<String> lines = createFileWithLines(testFile, 9);

        try (Reader reader = Files.newBufferedReader(testFile.toPath())) {
            testLineFiltering(lines, reader);
        }
    }

    @Test
    public void testInvalidEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createFileWithLines(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, "XXXXXXXX"));
    }

    @Test
    public void testMissingFile() {
        File testFile = new File(temporaryFolder, "dummy-missing-file.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(testFile, UTF_8));
    }

    @Test
    public void testNextLineOnlyWithDefaultEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    public void testNextLineOnlyWithNullEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, null)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    public void testNextLineOnlyWithUtf8Encoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertLinesMatch(lines, iterator);
        }
    }

    @Test
    public void testNextOnly() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        List<String> lines = createFileWithLines(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, null)) {
            for (int i = 0; i < lines.size(); i++) {
                assertEquals(lines.get(i), iterator.next(), "Mismatch at line " + i);
            }
            assertFalse(iterator.hasNext(), "No more lines expected");
        }
    }

    @Test
    public void testNextWithException() {
        Reader reader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("hasNext");
            }
        };
        try (LineIterator iterator = new LineIterator(reader)) {
            assertThrows(IllegalStateException.class, iterator::hasNext);
        }
    }

    @Test
    public void testOneLine() throws Exception {
        testFileWithLines(1);
    }

    @Test
    public void testThreeLines() throws Exception {
        testFileWithLines(3);
    }

    @Test
    public void testTwoLines() throws Exception {
        testFileWithLines(2);
    }

    @Test
    public void testValidEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createFileWithLines(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            int count = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next(), "Expected a line");
                count++;
            }
            assertEquals(3, count, "Expected 3 lines");
        }
    }

    @Test
    public void testZeroLines() throws Exception {
        testFileWithLines(0);
    }
}