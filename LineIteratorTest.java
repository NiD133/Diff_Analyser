package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Tests {@link LineIterator}.
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Asserts that the lines in the iterator match the expected lines.
     *
     * @param expectedLines the expected lines
     * @param iterator the iterator to test
     */
    private void assertLines(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String line = iterator.nextLine();
                assertEquals(expectedLines.get(i), line, "nextLine() line " + i);
            }
            assertFalse(iterator.hasNext(), "No more expected");
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Creates a test file with a specified number of lines.
     *
     * @param file the target file
     * @param lineCount the number of lines to create
     * @return the list of lines written to the file
     * @throws IOException if an I/O error occurs
     */
    private List<String> createLinesFile(final File file, final int lineCount) throws IOException {
        return createLinesFile(file, UTF_8, lineCount);
    }

    /**
     * Creates a test file with a specified number of lines and encoding.
     *
     * @param file the target file
     * @param encoding the encoding to use
     * @param lineCount the number of lines to create
     * @return the list of lines written to the file
     * @throws IOException if an I/O error occurs
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a list of string lines.
     *
     * @param lineCount the number of lines to create
     * @return the list of lines
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Tests a file with a specified number of lines.
     *
     * @param lineCount the number of lines to create
     * @throws IOException if an I/O error occurs
     */
    private void testFileWithSpecifiedLines(final int lineCount) throws IOException {
        final File testFile = new File(temporaryFolder, "LineIterator-" + lineCount + "-test.txt");
        final List<String> lines = createLinesFile(testFile, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            int idx = 0;
            while (iterator.hasNext()) {
                final String line = iterator.next();
                assertEquals(lines.get(idx), line, "Comparing line " + idx);
                assertTrue(idx < lines.size(), "Exceeded expected idx=" + idx + " size=" + lines.size());
                idx++;
            }
            assertEquals(idx, lines.size(), "Line Count doesn't match");

            // try calling next() after file processed
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testCloseEarly() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createLinesFile(testFile, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            // get
            assertNotNull("Line expected", iterator.next());
            assertTrue(iterator.hasNext(), "More expected");

            // close
            iterator.close();
            assertFalse(iterator.hasNext(), "No more expected");
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
            // try closing again
            iterator.close();
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testConstructor() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null));
    }

    @Test
    public void testFiltering() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-Filter-test.txt");
        final List<String> lines = createLinesFile(testFile, 9);

        final Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()));
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                final char c = line.charAt(line.length() - 1);
                return (c - 48) % 3 != 1;
            }
        }) {
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            int idx = 0;
            int actualLines = 0;
            while (iterator.hasNext()) {
                final String line = iterator.next();
                actualLines++;
                assertEquals(lines.get(idx), line, "Comparing line " + idx);
                assertTrue(idx < lines.size(), "Exceeded expected idx=" + idx + " size=" + lines.size());
                idx++;
                if (idx % 3 == 1) {
                    idx++;
                }
            }
            assertEquals(9, lines.size(), "Line Count doesn't match");
            assertEquals(9, idx, "Line Count doesn't match");
            assertEquals(6, actualLines, "Line Count doesn't match");

            // try calling next() after file processed
            assertThrows(NoSuchElementException.class, iterator::next);
            assertThrows(NoSuchElementException.class, iterator::nextLine);
        }
    }

    @Test
    public void testInvalidEncoding() throws Exception {
        final String encoding = "XXXXXXXX";

        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createLinesFile(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, encoding));
    }

    @Test
    public void testMissingFile() throws Exception {
        final File testFile = new File(temporaryFolder, "dummy-missing-file.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(testFile, UTF_8));
    }

    @Test
    public void testNextLineOnlyDefaultEncoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile);
        assertLines(lines, iterator);
    }

    @Test
    public void testNextLineOnlyNullEncoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, null, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, null);
        assertLines(lines, iterator);
    }

    @Test
    public void testNextLineOnlyUtf8Encoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, UTF_8, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8);
        assertLines(lines, iterator);
    }

    @Test
    public void testNextOnly() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, null)) {
            for (int i = 0; i < lines.size(); i++) {
                final String line = iterator.next();
                assertEquals(lines.get(i), line, "next() line " + i);
            }
            assertFalse(iterator.hasNext(), "No more expected");
        }
    }

    @Test
    public void testNextWithException() throws Exception {
        final Reader reader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("hasNext");
            }
        };
        try (LineIterator li = new LineIterator(reader)) {
            assertThrows(IllegalStateException.class, li::hasNext);
        }
    }

    @Test
    public void testOneLine() throws Exception {
        testFileWithSpecifiedLines(1);
    }

    @Test
    public void testThreeLines() throws Exception {
        testFileWithSpecifiedLines(3);
    }

    @Test
    public void testTwoLines() throws Exception {
        testFileWithSpecifiedLines(2);
    }

    @Test
    public void testValidEncoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createLinesFile(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            int count = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next());
                count++;
            }
            assertEquals(3, count);
        }
    }

    @Test
    public void testZeroLines() throws Exception {
        testFileWithSpecifiedLines(0);
    }
}