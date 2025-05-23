/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Tests for the {@link LineIterator} class.
 * This class provides an iterator to read lines from a file or reader.
 */
public class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Asserts that the lines returned by the LineIterator match the expected lines.
     *
     * @param expectedLines The expected lines.
     * @param iterator The LineIterator to test.
     */
    private void assertLines(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String actualLine = iterator.nextLine();
                assertEquals(expectedLines.get(i), actualLine, "nextLine() line " + i + " should match.");
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines.");
        } finally {
            // Ensure the iterator is closed to release resources.
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Creates a test file with the specified number of lines, using the default encoding.
     *
     * @param file The file to create.
     * @param lineCount The number of lines to write to the file.
     * @return A list of the lines that were written to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    private List<String> createLinesFile(final File file, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, lines);
        return lines;
    }

    /**
     * Creates a test file with the specified number of lines, using the specified encoding.
     *
     * @param file The file to create.
     * @param encoding The encoding to use when writing to the file.
     * @param lineCount The number of lines to write to the file.
     * @return A list of the lines that were written to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    private List<String> createLinesFile(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = createStringLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a list of Strings, each representing a line of text.
     * Each line is in the format "LINE " + i, where i is the line number.
     *
     * @param lineCount The number of lines to create.
     * @return A list of Strings representing the lines.
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Creates a file with a specified number of lines and then tests
     * iterating over those lines using a LineIterator.
     *
     * @param lineCount The number of lines to create in the test file.
     * @throws IOException If an I/O error occurs while creating the file.
     */
    private void doTestFileWithSpecifiedLines(final int lineCount) throws IOException {
        final String encoding = UTF_8;

        final String fileName = "LineIterator-" + lineCount + "-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> lines = createLinesFile(testFile, encoding, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // LineIterator does not support the remove operation.
            assertThrows(UnsupportedOperationException.class, iterator::remove, "remove() should throw UnsupportedOperationException");

            int lineIndex = 0;
            while (iterator.hasNext()) {
                final String line = iterator.next();
                assertEquals(lines.get(lineIndex), line, "Line at index " + lineIndex + " should match.");
                assertTrue(lineIndex < lines.size(), "Expected index " + lineIndex + " should be within bounds (size=" + lines.size() + ")");
                lineIndex++;
            }
            assertEquals(lineIndex, lines.size(), "The number of lines read should match the number of lines written.");

            // After all lines are read, next() and nextLine() should throw NoSuchElementException.
            assertThrows(NoSuchElementException.class, iterator::next, "next() after end should throw NoSuchElementException");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() after end should throw NoSuchElementException");
        }
    }

    /**
     * Tests that the LineIterator can be closed early, before all lines are read.
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testCloseEarly() throws Exception {
        final String encoding = UTF_8;

        final File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // Get the first line.
            assertNotNull("Line expected", iterator.next());
            assertTrue(iterator.hasNext(), "More lines expected");

            // Close the iterator.
            iterator.close();
            assertFalse(iterator.hasNext(), "No more lines expected after close");
            assertThrows(NoSuchElementException.class, iterator::next, "next() after close should throw NoSuchElementException");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() after close should throw NoSuchElementException");

            // Try closing again (should be safe).
            iterator.close();
            assertThrows(NoSuchElementException.class, iterator::next, "next() after close (again) should throw NoSuchElementException");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() after close (again) should throw NoSuchElementException");
        }
    }

    /**
     * Tests that the constructor throws a NullPointerException when passed a null Reader.
     */
    @Test
    public void testConstructor() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null), "Constructor with null Reader should throw NullPointerException");
    }

    /**
     * Tests that lines can be filtered based on a custom filtering logic.
     *
     * @param lines The expected lines in the file.
     * @param reader The Reader to iterate over.
     * @throws IOException If an I/O error occurs while reading from the Reader.
     */
    private void testFiltering(final List<String> lines, final Reader reader) throws IOException {
        try (LineIterator iterator = new LineIterator(reader) {
            // Override isValidLine to filter lines based on a custom criteria.
            @Override
            protected boolean isValidLine(final String line) {
                final char c = line.charAt(line.length() - 1);
                // Accept only lines where the last digit, minus 48, is not 1 (i.e., the last digit is not 1, 4, or 7).
                return (c - 48) % 3 != 1;
            }
        }) {
            // LineIterator does not support the remove operation.
            assertThrows(UnsupportedOperationException.class, iterator::remove, "remove() should throw UnsupportedOperationException");

            int lineIndex = 0;
            int actualLines = 0;
            while (iterator.hasNext()) {
                final String line = iterator.next();
                actualLines++;
                assertEquals(lines.get(lineIndex), line, "Line at index " + lineIndex + " should match.");
                assertTrue(lineIndex < lines.size(), "Expected index " + lineIndex + " should be within bounds (size=" + lines.size() + ")");
                lineIndex++;
                // Skip the lines that were filtered out.  This matches the
                // custom logic defined in isValidLine() in the anonymous
                // LineIterator class.
                if (lineIndex % 3 == 1) {
                    lineIndex++;
                }
            }
            assertEquals(9, lines.size(), "Line Count doesn't match");
            assertEquals(9, lineIndex, "Line Count doesn't match");
            assertEquals(6, actualLines, "Line Count doesn't match");

            // After all lines are read, next() and nextLine() should throw NoSuchElementException.
            assertThrows(NoSuchElementException.class, iterator::next, "next() after end should throw NoSuchElementException");
            assertThrows(NoSuchElementException.class, iterator::nextLine, "nextLine() after end should throw NoSuchElementException");
        }
    }

    /**
     * Tests filtering with a BufferedReader.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testFilteringBufferedReader() throws Exception {
        final String encoding = UTF_8;

        final String fileName = "LineIterator-Filter-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> lines = createLinesFile(testFile, encoding, 9);

        final Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()));
        testFiltering(lines, reader);
    }

    /**
     * Tests filtering with a FileReader.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testFilteringFileReader() throws Exception {
        final String encoding = UTF_8;

        final String fileName = "LineIterator-Filter-test.txt";
        final File testFile = new File(temporaryFolder, fileName);
        final List<String> lines = createLinesFile(testFile, encoding, 9);

        final Reader reader = Files.newBufferedReader(testFile.toPath());
        testFiltering(lines, reader);
    }

    /**
     * Tests that an UnsupportedCharsetException is thrown when an invalid encoding is used.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testInvalidEncoding() throws Exception {
        final String encoding = "XXXXXXXX";

        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createLinesFile(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, encoding), "Invalid encoding should throw UnsupportedCharsetException");
    }

    /**
     * Tests that a NoSuchFileException is thrown when trying to iterate over a missing file.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testMissingFile() throws Exception {
        final File testFile = new File(temporaryFolder, "dummy-missing-file.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(testFile, UTF_8), "Missing file should throw NoSuchFileException");
    }

    /**
     * Tests reading lines using nextLine() with the default encoding.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testNextLineOnlyDefaultEncoding() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile);
        assertLines(lines, iterator);
    }

    /**
     * Tests reading lines using nextLine() with a null encoding (should default to system encoding).
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testNextLineOnlyNullEncoding() throws Exception {
        final String encoding = null;

        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, encoding, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, encoding);
        assertLines(lines, iterator);
    }

    /**
     * Tests reading lines using nextLine() with UTF-8 encoding.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testNextLineOnlyUtf8Encoding() throws Exception {
        final String encoding = UTF_8;

        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, encoding, 3);

        final LineIterator iterator = FileUtils.lineIterator(testFile, encoding);
        assertLines(lines, iterator);
    }

    /**
     * Tests reading lines using next() with a null encoding (should default to system encoding).
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testNextOnly() throws Exception {
        final String encoding = null;

        final File testFile = new File(temporaryFolder, "LineIterator-nextOnly.txt");
        final List<String> lines = createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            for (int i = 0; i < lines.size(); i++) {
                final String line = iterator.next();
                assertEquals(lines.get(i), line, "next() line " + i + " should match.");
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines.");
        }
    }

    /**
     * Tests that an IllegalStateException is thrown when hasNext() throws an IOException during the call to readLine().
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testNextWithException() throws Exception {
        final Reader reader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("hasNext");
            }
        };
        try (LineIterator li = new LineIterator(reader)) {
            assertThrows(IllegalStateException.class, li::hasNext, "IOException during hasNext() should throw IllegalStateException");
        }
    }

    /**
     * Tests reading a file with one line.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testOneLines() throws Exception {
        doTestFileWithSpecifiedLines(1);
    }

    /**
     * Tests reading a file with three lines.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testThreeLines() throws Exception {
        doTestFileWithSpecifiedLines(3);
    }

    /**
     * Tests reading a file with two lines.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testTwoLines() throws Exception {
        doTestFileWithSpecifiedLines(2);
    }

    /**
     * Tests reading a file with a valid encoding specified.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testValidEncoding() throws Exception {
        final String encoding = UTF_8;

        final File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createLinesFile(testFile, encoding, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            int count = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next());
                count++;
            }
            assertEquals(3, count, "Should read 3 lines");
        }
    }

    /**
     * Tests reading a file with zero lines.
     *
     * @throws Exception If an error occurs during file creation or iteration.
     */
    @Test
    public void testZeroLines() throws Exception {
        doTestFileWithSpecifiedLines(0);
    }
}