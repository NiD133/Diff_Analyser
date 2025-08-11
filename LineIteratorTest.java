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

    private static final String UTF_8_ENCODING = StandardCharsets.UTF_8.name();
    private static final String INVALID_ENCODING = "XXXXXXXX";
    
    @TempDir
    public File temporaryFolder;

    // ========== Constructor Tests ==========
    
    @Test
    void testConstructor_WithNullReader_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null));
    }

    // ========== Basic Iteration Tests ==========
    
    @Test
    void testIterateEmptyFile() throws Exception {
        testFileWithSpecifiedNumberOfLines(0);
    }

    @Test
    void testIterateSingleLineFile() throws Exception {
        testFileWithSpecifiedNumberOfLines(1);
    }

    @Test
    void testIterateTwoLineFile() throws Exception {
        testFileWithSpecifiedNumberOfLines(2);
    }

    @Test
    void testIterateThreeLineFile() throws Exception {
        testFileWithSpecifiedNumberOfLines(3);
    }

    // ========== Encoding Tests ==========
    
    @Test
    void testIterateWithDefaultEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "default-encoding-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, 3);

        LineIterator iterator = FileUtils.lineIterator(testFile);
        assertIteratorReturnsExpectedLines(expectedLines, iterator);
    }

    @Test
    void testIterateWithNullEncoding() throws Exception {
        File testFile = new File(temporaryFolder, "null-encoding-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, null, 3);

        LineIterator iterator = FileUtils.lineIterator(testFile, (String) null);
        assertIteratorReturnsExpectedLines(expectedLines, iterator);
    }

    @Test
    void testIterateWithUtf8Encoding() throws Exception {
        File testFile = new File(temporaryFolder, "utf8-encoding-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING);
        assertIteratorReturnsExpectedLines(expectedLines, iterator);
    }

    @Test
    void testIterateWithValidEncoding_CountsLinesCorrectly() throws Exception {
        File testFile = new File(temporaryFolder, "valid-encoding-test.txt");
        createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING)) {
            int actualLineCount = 0;
            while (iterator.hasNext()) {
                assertNotNull(iterator.next(), "Each line should not be null");
                actualLineCount++;
            }
            assertEquals(3, actualLineCount, "Should count exactly 3 lines");
        }
    }

    @Test
    void testIterateWithInvalidEncoding_ThrowsUnsupportedCharsetException() throws Exception {
        File testFile = new File(temporaryFolder, "invalid-encoding-test.txt");
        createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        assertThrows(UnsupportedCharsetException.class, 
            () -> FileUtils.lineIterator(testFile, INVALID_ENCODING),
            "Should throw exception for invalid encoding");
    }

    // ========== Iterator Behavior Tests ==========
    
    @Test
    void testNextMethod_IteratesThroughAllLines() throws Exception {
        File testFile = new File(temporaryFolder, "next-method-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, null, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, (String) null)) {
            for (int i = 0; i < expectedLines.size(); i++) {
                String actualLine = iterator.next();
                assertEquals(expectedLines.get(i), actualLine, "Line " + i + " should match expected content");
            }
            assertFalse(iterator.hasNext(), "Should have no more lines after processing all");
        }
    }

    @Test
    void testNextLineMethod_IteratesThroughAllLines() throws Exception {
        File testFile = new File(temporaryFolder, "nextline-method-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING);
        assertIteratorReturnsExpectedLinesUsingNextLine(expectedLines, iterator);
    }

    @Test
    void testRemoveMethod_ThrowsUnsupportedOperationException() throws Exception {
        File testFile = new File(temporaryFolder, "remove-test.txt");
        createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING)) {
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() method should not be supported");
        }
    }

    @Test
    void testNextAfterExhaustion_ThrowsNoSuchElementException() throws Exception {
        testFileWithSpecifiedNumberOfLines(1); // This test includes exhaustion behavior
    }

    @Test
    void testHasNextWithIOException_ThrowsIllegalStateException() throws Exception {
        Reader faultyReader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Simulated IO error during hasNext");
            }
        };
        
        try (LineIterator iterator = new LineIterator(faultyReader)) {
            assertThrows(IllegalStateException.class, iterator::hasNext,
                "hasNext() should wrap IOException in IllegalStateException");
        }
    }

    // ========== Resource Management Tests ==========
    
    @Test
    void testEarlyClose_PreventsSubsequentOperations() throws Exception {
        File testFile = new File(temporaryFolder, "early-close-test.txt");
        createTestFileWithLines(testFile, UTF_8_ENCODING, 3);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING)) {
            // Read first line successfully
            assertNotNull(iterator.next(), "First line should be readable");
            assertTrue(iterator.hasNext(), "Should have more lines before closing");

            // Close iterator early
            iterator.close();
            
            // Verify iterator is properly closed
            assertFalse(iterator.hasNext(), "Should have no more lines after closing");
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw exception after closing");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw exception after closing");
            
            // Verify multiple closes are safe
            iterator.close(); // Should not throw exception
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should still throw exception after multiple closes");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should still throw exception after multiple closes");
        }
    }

    // ========== File System Tests ==========
    
    @Test
    void testMissingFile_ThrowsNoSuchFileException() throws Exception {
        File nonExistentFile = new File(temporaryFolder, "this-file-does-not-exist.txt");
        
        assertThrows(NoSuchFileException.class, 
            () -> FileUtils.lineIterator(nonExistentFile, UTF_8_ENCODING),
            "Should throw exception when file does not exist");
    }

    // ========== Line Filtering Tests ==========
    
    @Test
    void testCustomLineFiltering_WithBufferedReader() throws Exception {
        File testFile = new File(temporaryFolder, "filter-buffered-test.txt");
        List<String> allLines = createTestFileWithLines(testFile, UTF_8_ENCODING, 9);

        Reader reader = new BufferedReader(Files.newBufferedReader(testFile.toPath()));
        testLineFilteringBehavior(allLines, reader);
    }

    @Test
    void testCustomLineFiltering_WithFileReader() throws Exception {
        File testFile = new File(temporaryFolder, "filter-file-test.txt");
        List<String> allLines = createTestFileWithLines(testFile, UTF_8_ENCODING, 9);

        Reader reader = Files.newBufferedReader(testFile.toPath());
        testLineFilteringBehavior(allLines, reader);
    }

    // ========== Helper Methods ==========

    /**
     * Creates a test file with the specified number of lines and tests complete iteration behavior.
     * This includes testing that the iterator properly handles exhaustion and throws appropriate exceptions.
     */
    private void testFileWithSpecifiedNumberOfLines(final int lineCount) throws IOException {
        File testFile = new File(temporaryFolder, "line-count-" + lineCount + "-test.txt");
        List<String> expectedLines = createTestFileWithLines(testFile, UTF_8_ENCODING, lineCount);

        try (LineIterator iterator = FileUtils.lineIterator(testFile, UTF_8_ENCODING)) {
            // Verify remove() is not supported
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should not be supported");

            // Iterate through all lines and verify content
            int actualLineCount = 0;
            while (iterator.hasNext()) {
                String actualLine = iterator.next();
                assertEquals(expectedLines.get(actualLineCount), actualLine, 
                    "Line " + actualLineCount + " content should match expected");
                assertTrue(actualLineCount < expectedLines.size(), 
                    "Should not exceed expected line count. Current: " + actualLineCount + ", Max: " + expectedLines.size());
                actualLineCount++;
            }
            
            assertEquals(expectedLines.size(), actualLineCount, 
                "Actual line count should match expected line count");

            // Verify proper behavior after exhaustion
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw exception when no more lines available");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw exception when no more lines available");
        }
    }

    /**
     * Tests custom line filtering logic that skips lines ending with digits where (digit % 3 == 1).
     * This tests the isValidLine() override functionality.
     */
    private void testLineFilteringBehavior(final List<String> allLines, final Reader reader) throws IOException {
        try (LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                // Skip lines ending with digits where (digit - 48) % 3 == 1
                // This filters out lines ending with '1', '4', '7', etc.
                final char lastChar = line.charAt(line.length() - 1);
                return (lastChar - 48) % 3 != 1;
            }
        }) {
            assertThrows(UnsupportedOperationException.class, iterator::remove,
                "remove() should not be supported even with filtering");

            int expectedLineIndex = 0;
            int actualFilteredLineCount = 0;
            
            while (iterator.hasNext()) {
                String actualLine = iterator.next();
                actualFilteredLineCount++;
                
                assertEquals(allLines.get(expectedLineIndex), actualLine, 
                    "Filtered line should match expected line at index " + expectedLineIndex);
                assertTrue(expectedLineIndex < allLines.size(), 
                    "Should not exceed total line count. Index: " + expectedLineIndex + ", Size: " + allLines.size());
                
                expectedLineIndex++;
                // Skip the next line if it would be filtered (every 3rd line starting from index 1)
                if (expectedLineIndex % 3 == 1) {
                    expectedLineIndex++;
                }
            }
            
            // Verify expected counts based on filtering logic
            assertEquals(9, allLines.size(), "Test should use exactly 9 lines");
            assertEquals(9, expectedLineIndex, "Should have processed correct number of line indices");
            assertEquals(6, actualFilteredLineCount, "Should have 6 lines after filtering (9 - 3 filtered)");

            // Verify proper behavior after exhaustion
            assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw exception after filtering is complete");
            assertThrows(NoSuchElementException.class, iterator::nextLine,
                "nextLine() should throw exception after filtering is complete");
        }
    }

    /**
     * Verifies that the iterator returns exactly the expected lines using nextLine() method.
     */
    private void assertIteratorReturnsExpectedLinesUsingNextLine(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String actualLine = iterator.nextLine();
                assertEquals(expectedLines.get(i), actualLine, "nextLine() should return expected content for line " + i);
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines after processing all expected lines");
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Verifies that the iterator returns exactly the expected lines using next() method.
     */
    private void assertIteratorReturnsExpectedLines(final List<String> expectedLines, final LineIterator iterator) {
        try {
            for (int i = 0; i < expectedLines.size(); i++) {
                final String actualLine = iterator.nextLine();
                assertEquals(expectedLines.get(i), actualLine, "Line " + i + " should match expected content");
            }
            assertFalse(iterator.hasNext(), "Iterator should have no more lines after processing all expected lines");
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Creates a test file with the specified number of lines using default encoding.
     */
    private List<String> createTestFileWithLines(final File file, final int lineCount) throws IOException {
        final List<String> lines = generateTestLines(lineCount);
        FileUtils.writeLines(file, lines);
        return lines;
    }

    /**
     * Creates a test file with the specified number of lines using the given encoding.
     */
    private List<String> createTestFileWithLines(final File file, final String encoding, final int lineCount) throws IOException {
        final List<String> lines = generateTestLines(lineCount);
        FileUtils.writeLines(file, encoding, lines);
        return lines;
    }

    /**
     * Generates test line content with predictable format: "LINE 0", "LINE 1", etc.
     */
    private List<String> generateTestLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }
}