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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link LineIterator}.
 */
class LineIteratorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @TempDir
    File tempDir;

    // ----------------------------- test helpers -----------------------------

    private static List<String> buildLines(final int count) {
        final List<String> lines = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    private static void writeLines(final File file, final List<String> lines) throws IOException {
        FileUtils.writeLines(file, lines);
    }

    private static void writeLines(final File file, final String encoding, final List<String> lines) throws IOException {
        FileUtils.writeLines(file, encoding, lines);
    }

    private static File newTempFile(final File dir, final String name) {
        return new File(dir, name);
    }

    private static void assertFullyExhausted(final LineIterator iterator) {
        assertFalse(iterator.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(NoSuchElementException.class, iterator::nextLine);
    }

    private static void assertReadsUsingNext(final LineIterator iterator, final List<String> expected) {
        int index = 0;
        while (iterator.hasNext()) {
            assertTrue(index < expected.size(), "Iterator returned more lines than expected");
            assertEquals(expected.get(index), iterator.next(), "Mismatch at line index " + index);
            index++;
        }
        assertEquals(expected.size(), index, "Unexpected number of lines read");
    }

    private static void assertReadsUsingNextLine(final LineIterator iterator, final List<String> expected) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), iterator.nextLine(), "Mismatch at line index " + i);
        }
        assertFalse(iterator.hasNext(), "Iterator should be exhausted");
    }

    // ------------------------------- tests ---------------------------------

    @Test
    @DisplayName("Constructor rejects null Reader")
    void constructorRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null));
    }

    @Test
    @DisplayName("Close early: hasNext becomes false and next/nextLine throw")
    void closeEarly() throws Exception {
        final File file = newTempFile(tempDir, "close-early.txt");
        writeLines(file, UTF_8, buildLines(3));

        try (LineIterator it = FileUtils.lineIterator(file, UTF_8)) {
            assertNotNull(it.next(), "Expected first line");
            assertTrue(it.hasNext(), "Expected more lines");

            it.close(); // explicit early close

            assertFullyExhausted(it);

            // closing again is safe
            it.close();
            assertFullyExhausted(it);
        }
    }

    @Test
    @DisplayName("Valid UTF-8 encoding iterates all lines")
    void validEncodingIteratesAllLines() throws Exception {
        final File file = newTempFile(tempDir, "valid-encoding.txt");
        writeLines(file, UTF_8, buildLines(3));

        try (LineIterator it = FileUtils.lineIterator(file, UTF_8)) {
            int count = 0;
            while (it.hasNext()) {
                assertNotNull(it.next());
                count++;
            }
            assertEquals(3, count);
        }
    }

    @Test
    @DisplayName("Invalid encoding throws UnsupportedCharsetException")
    void invalidEncodingThrows() throws Exception {
        final File file = newTempFile(tempDir, "invalid-encoding.txt");
        writeLines(file, UTF_8, buildLines(3));

        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(file, "XXXXXXXX"));
    }

    @Test
    @DisplayName("Missing file throws NoSuchFileException")
    void missingFileThrows() {
        final File missing = newTempFile(tempDir, "missing.txt");
        assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(missing, UTF_8));
    }

    @ParameterizedTest(name = "Reads {0} lines using hasNext()/next()")
    @ValueSource(ints = {0, 1, 2, 3})
    void iteratesExpectedNumberOfLines(final int count) throws Exception {
        final File file = newTempFile(tempDir, "iterate-" + count + ".txt");
        final List<String> expected = buildLines(count);
        writeLines(file, UTF_8, expected);

        try (LineIterator it = FileUtils.lineIterator(file, UTF_8)) {
            assertThrows(UnsupportedOperationException.class, it::remove);

            assertReadsUsingNext(it, expected);

            // After exhaustion, next/nextLine must throw
            assertThrows(NoSuchElementException.class, it::next);
            assertThrows(NoSuchElementException.class, it::nextLine);
        }
    }

    @Test
    @DisplayName("nextLine() with default encoding")
    void nextLineOnlyWithDefaultEncoding() throws Exception {
        final File file = newTempFile(tempDir, "nextLine-default.txt");
        final List<String> expected = buildLines(3);
        writeLines(file, expected);

        try (LineIterator it = FileUtils.lineIterator(file)) {
            assertReadsUsingNextLine(it, expected);
        }
    }

    @Test
    @DisplayName("nextLine() with null encoding (platform default)")
    void nextLineOnlyWithNullEncoding() throws Exception {
        final File file = newTempFile(tempDir, "nextLine-null.txt");
        final List<String> expected = buildLines(3);
        writeLines(file, null, expected);

        try (LineIterator it = FileUtils.lineIterator(file, null)) {
            assertReadsUsingNextLine(it, expected);
        }
    }

    @Test
    @DisplayName("nextLine() with UTF-8 encoding")
    void nextLineOnlyWithUtf8Encoding() throws Exception {
        final File file = newTempFile(tempDir, "nextLine-utf8.txt");
        final List<String> expected = buildLines(3);
        writeLines(file, UTF_8, expected);

        try (LineIterator it = FileUtils.lineIterator(file, UTF_8)) {
            assertReadsUsingNextLine(it, expected);
        }
    }

    @Test
    @DisplayName("hasNext throws IllegalStateException when reader.readLine throws")
    void hasNextThrowsWhenReaderThrows() throws Exception {
        final Reader throwingReader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("forced failure");
            }
        };
        try (LineIterator it = new LineIterator(throwingReader)) {
            assertThrows(IllegalStateException.class, it::hasNext);
        }
    }

    @Test
    @DisplayName("next() only (no explicit nextLine calls)")
    void nextOnly() throws Exception {
        final File file = newTempFile(tempDir, "next-only.txt");
        final List<String> expected = buildLines(3);
        writeLines(file, null, expected);

        try (LineIterator it = FileUtils.lineIterator(file, (String) null)) {
            assertReadsUsingNext(it, expected);
            assertFalse(it.hasNext(), "Iterator should be exhausted");
        }
    }

    @Test
    @DisplayName("Filtering via isValidLine: accept all lines except those with index % 3 == 1")
    void filteringWithBufferedReader() throws Exception {
        final File file = newTempFile(tempDir, "filter-buffered.txt");
        final List<String> allLines = buildLines(9);
        writeLines(file, UTF_8, allLines);

        try (LineIterator it = new LineIterator(new BufferedReader(Files.newBufferedReader(file.toPath()))) {
            @Override
            protected boolean isValidLine(final String line) {
                // last char is the line index; skip indices where (index % 3) == 1
                final char last = line.charAt(line.length() - 1);
                final int index = last - '0';
                return (index % 3) != 1;
            }
        }) {
            assertThrows(UnsupportedOperationException.class, it::remove);

            int expectedIndex = 0;
            int actualCount = 0;
            while (it.hasNext()) {
                assertEquals(allLines.get(expectedIndex), it.next(), "Mismatch at index " + expectedIndex);
                actualCount++;
                expectedIndex++;
                if (expectedIndex % 3 == 1) {
                    expectedIndex++; // skip the filtered-out element
                }
            }

            final int expectedKept = 6; // from 0..8, indices kept: 0,2,3,5,6,8
            assertEquals(9, allLines.size(), "Test precondition");
            assertEquals(expectedKept, actualCount, "Unexpected number of lines after filtering");

            assertThrows(NoSuchElementException.class, it::next);
            assertThrows(NoSuchElementException.class, it::nextLine);
        }
    }

    @Test
    @DisplayName("Filtering via isValidLine works with unbuffered Reader too")
    void filteringWithFileReader() throws Exception {
        final File file = newTempFile(tempDir, "filter-file-reader.txt");
        final List<String> allLines = buildLines(9);
        writeLines(file, UTF_8, allLines);

        try (LineIterator it = new LineIterator(Files.newBufferedReader(file.toPath())) {
            @Override
            protected boolean isValidLine(final String line) {
                final char last = line.charAt(line.length() - 1);
                final int index = last - '0';
                return (index % 3) != 1;
            }
        }) {
            int count = 0;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            assertEquals(6, count, "Unexpected number of lines after filtering");
        }
    }
}