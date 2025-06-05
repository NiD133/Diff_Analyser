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
 * Tests for {@link LineIterator}.
 *
 * <p>These tests verify the functionality of the {@link LineIterator} class,
 * ensuring it correctly iterates over lines in a file or reader, handles
 * different encodings, and gracefully manages exceptions.</p>
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Asserts that the {@link LineIterator} iterates over the expected lines.
     *
     * @param expectedLines The list of expected lines.
     * @param iterator The LineIterator to test.
     */
    private void assertLines(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String actualLine = iterator.nextLine();
                assertEquals(expectedLines.get(i), actualLine, "Line " + i + " should match expected value.");
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines.");
        } finally {
            IOUtils.closeQuietly(iterator); // Ensure the iterator is closed, even if the test fails.
        }
    }

    /**
     * Creates a test file with the specified number of lines, using the default UTF-8 encoding.
     *
     * @param file The file to create.
     * @param lineCount The number of lines to create.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final int lineCount) throws IOException {
        return createLinesFile(file, UTF_8, lineCount);
    }

    /**
     * Creates a test file with the specified number of lines and encoding.
     *
     * @param file The file to create.
     * @param encoding The encoding to use when writing the lines.
     * @param lineCount The number of lines to create.
     * @return The list of lines written to the file.
     * @throws IOException If an I/O error occurs.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a list of strings, each representing a line of text.
     *
     * @param lineCount The number of lines to create.
     * @return A list of strings representing the lines.
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Tests the {@link LineIterator} with a file containing a specified number of lines.
     * This method covers basic iteration, line comparison, and exception handling.
     *
     * @param lineCount The number of lines the test file should contain.
     * @throws IOException If an I/O error occurs.
     */
    private void doTestFileWithSpecifiedLines(final int lineCount) throws IOException {
        final String encoding = UTF_8;
        final String fileName = "LineIterator-" + lineCount + "-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> expectedLines = createLinesFile(testFile, encoding, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            assertThrows(UnsupportedOperationException.class, iterator::remove, "remove() should throw UnsupportedOperationException");

            int lineNumber = 0;
            while (iterator.hasNext()) {
                final String actualLine = iterator.next();
                assertEquals(expectedLines.get(lineNumber), actualLine, "Line " + lineNumber + " should match expected value.");
                assertTrue(lineNumber < expectedLines.size(), "Line number should not exceed the number of lines.");
                lineNumber++;
            }
            assertEquals(expectedLines.size(), lineNumber, "Number of lines read should match the number of lines in the file.");

            // Verify that calling next() or nextLine() after the end of the file throws an exception.
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw NoSuchElementException after the last line.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw NoSuchElementException after the last line.");
        }
    }

    /**
     * Tests closing the {@link LineIterator} before reaching the end of the file.
     * This verifies that resources are properly released when the iterator is closed early.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testCloseEarly() throws Exception {
        final String encoding = UTF_8;
        final File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // Get the first line.
            assertNotNull(iterator.next(), "A line should be returned.");
            assertTrue(iterator.hasNext(), "More lines should be available.");

            // Close the iterator.
            iterator.close();
            assertFalse(iterator.hasNext(), "No more lines should be available after closing.");
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw NoSuchElementException after closing.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw NoSuchElementException after closing.");

            // Verify that closing the iterator again has no effect.
            iterator.close();
            assertThrows(NoSuchElementException.class, iterator::next, "next() should still throw NoSuchElementException after closing again.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should still throw NoSuchElementException after closing again.");
        }
    }

    /**
     * Tests the constructor of {@link LineIterator} with a null reader.
     * This verifies that a NullPointerException is thrown when a null reader is provided.
     */
    @Test
    public void testConstructor() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null), "Constructor should throw NullPointerException when reader is null.");
    }

    /**
     * Tests the {@link LineIterator} with a custom filter to validate lines.
     *
     * @param expectedLines The list of expected lines *after* filtering.
     * @param reader The Reader to iterate over.
     * @throws IOException If an I/O error occurs.
     */
    private void testFiltering(final List<String> expectedLines, final Reader reader) throws IOException {
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                final char c = line.charAt(line.length() - 1);
                return (c - 48) % 3 != 1; // Filter lines where the last digit % 3 == 1.
            }
        }) {
            assertThrows(UnsupportedOperationException.class, iterator::remove, "remove() should throw UnsupportedOperationException");

            int expectedLineIndex = 0;
            int actualLineCount = 0;

            while (iterator.hasNext()) {
                final String actualLine = iterator.next();
                actualLineCount++;
                assertEquals(expectedLines.get(expectedLineIndex), actualLine, "Line " + expectedLineIndex + " should match expected value.");
                assertTrue(expectedLineIndex < expectedLines.size(), "Index should not exceed list size.");
                expectedLineIndex++;
            }
            //Verify correct number of expected lines according to filtering:
            assertEquals(expectedLines.size(), 6, "Expected Line Count doesn't match"); //6 lines out of 9 should satisfy the filter condition
            assertEquals(expectedLines.size(), actualLineCount, "Actual Line Count doesn't match");

            // try calling next() after file processed
            assertThrows(NoSuchElementException.class, iterator::next, "next() should throw NoSuchElementException after the last line.");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() should throw NoSuchElementException after the last line.");
        }
    }

    /**
     * Tests filtering lines from a file using a BufferedReader.
     * @throws Exception If an I/O error occurs.
     */
    @Test
    public void testFilteringBufferedReader() throws Exception {
        final String encoding = UTF_8;
        final String fileName = "LineIterator-Filter-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> allLines = createLinesFile(testFile, encoding, 9);
        List<String> expectedFilteredLines = new ArrayList<>();
        for (int i = 0; i < allLines.size(); i++) {
            if ((allLines.get(i).charAt(allLines.get(i).length() - 1) - 48) % 3 != 1) {
                expectedFilteredLines.add(allLines.get(i));
            }
        }


        final Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()));
        testFiltering(expectedFilteredLines, reader);
    }

    /**
     * Tests filtering lines from a file using a FileReader.
     * @throws Exception If an I/O error occurs.
     */
    @Test
    public void testFilteringFileReader() throws Exception {
        final String encoding = UTF_8;
        final String fileName = "LineIterator-Filter-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> allLines = createLinesFile(testFile, encoding, 9);
        List<String> expectedFilteredLines = new ArrayList<>();
        for (int i = 0; i < allLines.size(); i++) {
            if ((allLines.get(i).charAt(allLines.get(i).length() - 1) - 48) % 3 != 1) {
                expectedFilteredLines.add(allLines.get(i));
            }
        }

        final Reader reader = Files.newBufferedReader(testFile.toPath());
        testFiltering(expectedFilteredLines, reader);
    }

    /**
     * Tests the {@link LineIterator} with an invalid encoding.
     * This verifies that an UnsupportedCharsetException is thrown.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testInvalidEncoding() throws Exception {
        final String invalidEncoding = "XXXXXXXX";
        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createLinesFile(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, invalidEncoding),
                "lineIterator() should throw UnsupportedCharsetException for invalid encoding.");
    }

    /**
     * Tests the {@link LineIterator} with a missing file.
     * This verifies that a NoSuchFileException is thrown.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMissingFile() throws Exception {
        final File testFile = new File(temporaryFolder, "dummy-missing-file.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(testFile, UTF_8),
                "lineIterator() should throw NoSuchFileException for missing file.");
    }

    /**
     * Tests iterating over lines using only {@link LineIterator#nextLine()} and the default encoding.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testNextLineOnlyDefaultEncoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> expectedLines = createLinesFile(testFile, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile);
        assertLines(expectedLines, iterator);
    }

    /**
     * Tests iterating over lines using only {@link LineIterator#nextLine()} and a null encoding (system default).
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testNextLineOnlyNullEncoding() throws Exception {
        final String encoding = null;
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> expectedLines = createLinesFile(testFile, encoding, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, encoding);
        assertLines(expectedLines, iterator);
    }

    /**
     * Tests iterating over lines using only {@link LineIterator#nextLine()} and UTF-8 encoding.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testNextLineOnlyUtf8Encoding() throws Exception {
        final String encoding = UTF_8;
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> expectedLines = createLinesFile(testFile, encoding, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, encoding);
        assertLines(expectedLines, iterator);
    }

    /**
     * Tests iterating over lines using only {@link LineIterator#next()} and a null encoding (system default).
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testNextOnly() throws Exception {
        final String encoding = null;
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> expectedLines = createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String line = iterator.next();
                assertEquals(expectedLines.get(i), line, "next() line " + i);
            }
            assertFalse(iterator.hasNext(), "No more expected");
        }
    }

    /**
     * Tests the scenario where {@link BufferedReader#readLine()} throws an IOException during {@link LineIterator#hasNext()}.
     *
     * @throws Exception If an I/O error occurs.
     */
    @Test
    public void testNextWithException() throws Exception {
        final Reader reader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Simulated IOException in readLine()");
            }
        };
        try (LineIterator iterator = new LineIterator(reader)) {
            assertThrows(IllegalStateException.class, iterator::hasNext, "hasNext() should throw IllegalStateException when readLine() throws IOException.");
        }
    }

    /**
     * Tests the {@link LineIterator} with a file containing one line.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testOneLines() throws Exception {
        doTestFileWithSpecifiedLines(1);
    }

    /**
     * Tests the {@link LineIterator} with a file containing three lines.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testThreeLines() throws Exception {
        doTestFileWithSpecifiedLines(3);
    }

    /**
     * Tests the {@link LineIterator} with a file containing two lines.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testTwoLines() throws Exception {
        doTestFileWithSpecifiedLines(2);
    }

    /**
     * Tests the {@link LineIterator} with a valid encoding.
     * This verifies that the iterator can read the file without errors.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testValidEncoding() throws Exception {
        final String encoding = UTF_8;
        final File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            int lineCount = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next(), "Line should not be null.");
                lineCount++;
            }
            assertEquals(3, lineCount, "Should have iterated over 3 lines.");
        }
    }

    /**
     * Tests the {@link LineIterator} with a file containing zero lines.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testZeroLines() throws Exception {
        doTestFileWithSpecifiedLines(0);
    }
}