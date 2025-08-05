/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
 * Tests {@link LineIterator}.
 */
class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    public File temporaryFolder;

    /**
     * Asserts that the iterator returns the expected lines in order.
     * Uses try-with-resources to ensure iterator is always closed.
     */
    private void assertLinesMatch(List<String> expectedLines, LineIterator iterator) {
        try (iterator) {
            for (int i = 0; i < expectedLines.size(); i++) {
                assertTrue(iterator.hasNext(), "Expected more lines at index " + i);
                assertEquals(expectedLines.get(i), iterator.nextLine(), "Line mismatch at index " + i);
            }
            assertFalse(iterator.hasNext(), "Extra lines found after expected content");
        }
    }

    /**
     * Creates a test file with specified lines and encoding.
     * @return The list of lines written to the file
     */
    private List<String> createTestFile(File file, String encoding, int lineCount) throws IOException {
        List<String> lines = generateLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Creates a test file with default encoding.
     * @return The list of lines written to the file
     */
    private List<String> createTestFile(File file, int lineCount) throws IOException {
        List<String> lines = generateLines(lineCount);
        FileUtils.writeLines(file, lines);
        return lines;
    }

    /**
     * Generates a list of test lines with predictable content.
     */
    private List<String> generateLines(int lineCount) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Tests LineIterator functionality with files containing specific line counts.
     */
    private void verifyLineIteratorForLineCount(int lineCount) throws IOException {
        final String encoding = UTF_8;
        final File testFile = new File(temporaryFolder, "LineIterator-" + lineCount + "-lines.txt");
        final List<String> expectedLines = createTestFile(testFile, encoding, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, encoding)) {
            // Verify remove() is unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove, 
                         "remove() should throw UnsupportedOperationException");

            // Verify all lines
            for (int i = 0; i < lineCount; i++) {
                assertTrue(iterator.hasNext(), "Missing line at index " + i);
                assertEquals(expectedLines.get(i), iterator.next(), "Content mismatch at index " + i);
            }
            
            // Verify end of file
            assertFalse(iterator.hasNext(), "Extra content after last line");
            assertThrows(NoSuchElementException.class, iterator::next, 
                         "next() should throw after last line");
            assertThrows(NoSuchElementException.class, iterator::nextLine, 
                         "nextLine() should throw after last line");
        }
    }

    @Test
    void closingIteratorEarly_ShouldPreventFurtherAccess() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-closeEarly.txt");
        createTestFile(testFile, UTF_8, 3);

        LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8);
        try (iterator) {
            // Read first line
            assertNotNull(iterator.next(), "First line should not be null");
            assertTrue(iterator.hasNext(), "Should have more lines after first");
            
            // Close explicitly
            iterator.close();
            
            // Verify post-closure state
            assertFalse(iterator.hasNext(), "Should have no more lines after close");
            assertThrows(NoSuchElementException.class, iterator::next, 
                         "next() should throw after close");
            assertThrows(NoSuchElementException.class, iterator::nextLine, 
                         "nextLine() should throw after close");
        }
    }

    @Test
    void constructorWithNullReader_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null),
                     "Constructor must throw NPE for null Reader");
    }

    /**
     * Tests LineIterator with a custom line filtering implementation.
     */
    private void verifyFilteredLines(List<String> allLines, Reader reader) throws IOException {
        // Create iterator that filters out lines ending with '1' in last digit
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(String line) {
                char lastChar = line.charAt(line.length() - 1);
                return (lastChar - '0') % 3 != 1;  // Filter out lines ending with 1, 4, 7
            }
        }) {
            // Verify removal unsupported
            assertThrows(UnsupportedOperationException.class, iterator::remove);
            
            // Verify filtered lines
            int index = 0;
            int count = 0;
            while (iterator.hasNext()) {
                String actualLine = iterator.next();
                // Skip filtered lines in original list
                while (index % 3 == 1) {
                    index++;
                }
                assertEquals(allLines.get(index), actualLine, "Filtered content mismatch at position " + count);
                index++;
                count++;
            }
            
            // Verify counts
            int expectedFilteredCount = 6;  // 9 total lines, 3 filtered out
            assertEquals(expectedFilteredCount, count, "Filtered line count mismatch");
        }
    }

    @Test
    void filteringLines_WithBufferedReader_ShouldFilterCorrectly() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-BufferedReader.txt");
        List<String> lines = createTestFile(testFile, UTF_8, 9);
        
        try (Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()))) {
            verifyFilteredLines(lines, reader);
        }
    }

    @Test
    void filteringLines_WithFileReader_ShouldFilterCorrectly() throws Exception {
        File testFile = new File(temporaryFolder, "LineIterator-Filter-FileReader.txt");
        List<String> lines = createTestFile(testFile, UTF_8, 9);
        
        try (Reader reader = Files.newBufferedReader(testFile.toPath())) {
            verifyFilteredLines(lines, reader);
        }
    }

    @Test
    void invalidEncoding_ShouldThrowUnsupportedCharsetException() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createTestFile(testFile, UTF_8, 3);

        assertThrows(UnsupportedCharsetException.class, 
                     () -> FileUtils.lineIterator(testFile, "INVALID_ENCODING"),
                     "Should throw for invalid encoding");
    }

    @Test
    void missingFile_ShouldThrowNoSuchFileException() {
        final File testFile = new File(temporaryFolder, "non-existent-file.txt");
        assertThrows(NoSuchFileException.class, 
                     () -> FileUtils.lineIterator(testFile, UTF_8),
                     "Should throw for missing file");
    }

    @Test
    void nextLine_WithDefaultEncoding_ShouldReadLinesCorrectly() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextLine.txt");
        final List<String> expectedLines = createTestFile(testFile, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
            assertLinesMatch(expectedLines, iterator);
        }
    }

    @Test
    void nextLine_WithExplicitUtf8_ShouldReadLinesCorrectly() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-nextLine-utf8.txt");
        final List<String> expectedLines = createTestFile(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            assertLinesMatch(expectedLines, iterator);
        }
    }

    @Test
    void next_ShouldReadAllLines() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-next.txt");
        final List<String> expectedLines = createTestFile(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            for (int i = 0; i < expectedLines.size(); i++) {
                assertTrue(iterator.hasNext());
                assertEquals(expectedLines.get(i), iterator.next(), "next() mismatch at index " + i);
            }
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void hasNext_WhenUnderlyingReaderThrowsIOException_ShouldThrowIllegalStateException() {
        Reader throwingReader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Simulated read error");
            }
        };

        try (LineIterator iterator = new LineIterator(throwingReader)) {
            IllegalStateException ex = assertThrows(IllegalStateException.class, iterator::hasNext);
            assertEquals("Unable to read from reader", ex.getMessage());
        }
    }

    @Test
    void fileWithZeroLines_ShouldBeHandledCorrectly() throws Exception {
        verifyLineIteratorForLineCount(0);
    }

    @Test
    void fileWithOneLine_ShouldBeHandledCorrectly() throws Exception {
        verifyLineIteratorForLineCount(1);
    }

    @Test
    void fileWithTwoLines_ShouldBeHandledCorrectly() throws Exception {
        verifyLineIteratorForLineCount(2);
    }

    @Test
    void fileWithThreeLines_ShouldBeHandledCorrectly() throws Exception {
        verifyLineIteratorForLineCount(3);
    }

    @Test
    void validEncoding_ShouldReadAllLines() throws Exception {
        final File testFile = new File(temporaryFolder, "LineIterator-validEncoding.txt");
        createTestFile(testFile, UTF_8, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8)) {
            int count = 0;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            assertEquals(3, count, "Should read all lines");
        }
    }
}