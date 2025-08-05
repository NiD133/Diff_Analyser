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
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link LineIterator}.
 *
 * <p>This test class is organized using {@link Nested} classes to group tests
 * by the specific behavior they target (e.g., iteration, error handling, closing).
 * This improves clarity and makes it easier to locate tests for a particular feature.
 * Repetitive tests for different line counts have been consolidated into a single
 * {@link ParameterizedTest}.
 * </p>
 */
class LineIteratorTest {

    @TempDir
    private File temporaryFolder;

    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File(temporaryFolder, "test-file.txt");
    }

    /**
     * Creates a test file with a specified number of lines.
     * Each line is of the form "LINE X".
     */
    private List<String> createTestFile(final int lineCount) throws IOException {
        final List<String> lines = IntStream.range(0, lineCount)
                .mapToObj(i -> "LINE " + i)
                .collect(Collectors.toList());
        FileUtils.writeLines(testFile, lines, StandardCharsets.UTF_8);
        return lines;
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTest {
        @Test
        void shouldThrowNullPointerExceptionWhenReaderIsNull() {
            assertThrows(NullPointerException.class, () -> new LineIterator(null),
                "Constructor should not accept a null reader.");
        }
    }

    @Nested
    @DisplayName("Iteration Tests")
    class IterationTest {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 5})
        @DisplayName("Should correctly iterate over a file")
        void shouldCorrectlyIterateOverFile(final int lineCount) throws IOException {
            // Arrange
            final List<String> expectedLines = createTestFile(lineCount);

            // Act & Assert
            try (LineIterator iterator = FileUtils.lineIterator(testFile, StandardCharsets.UTF_8.name())) {
                final List<String> actualLines = new ArrayList<>();
                iterator.forEachRemaining(actualLines::add);
                assertIterableEquals(expectedLines, actualLines);
            }
        }

        @Test
        @DisplayName("next() should throw NoSuchElementException when iteration is finished")
        void shouldThrowNoSuchElementExceptionWhenFinished() throws IOException {
            // Arrange
            createTestFile(2); // Create a file with a few lines

            try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
                // Act
                while (iterator.hasNext()) {
                    iterator.next(); // Exhaust the iterator
                }

                // Assert
                assertFalse(iterator.hasNext());
                assertThrows(NoSuchElementException.class, iterator::next,
                    "Calling next() on an exhausted iterator should throw.");
                assertThrows(NoSuchElementException.class, iterator::nextLine,
                    "Calling nextLine() on an exhausted iterator should throw.");
            }
        }

        @Test
        @DisplayName("remove() should throw UnsupportedOperationException")
        void shouldThrowUnsupportedOperationExceptionOnRemove() throws IOException {
            // Arrange
            createTestFile(1);
            try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
                // Assert
                assertTrue(iterator.hasNext());
                assertThrows(UnsupportedOperationException.class, iterator::remove,
                    "remove() is not supported.");
            }
        }

        @Test
        @DisplayName("Deprecated nextLine() should behave like next()")
        void deprecatedNextLineShouldBehaveLikeNext() throws IOException {
            // Arrange
            final List<String> expectedLines = createTestFile(3);

            // Act
            final List<String> actualLines = new ArrayList<>();
            try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
                while (iterator.hasNext()) {
                    actualLines.add(iterator.nextLine());
                }
            }

            // Assert
            assertIterableEquals(expectedLines, actualLines);
        }
    }

    @Nested
    @DisplayName("Filtering Tests")
    class FilteringTest {

        @Test
        @DisplayName("Should filter lines when isValidLine() is overridden")
        void shouldFilterLinesWhenIsValidLineIsOverridden() throws IOException {
            // Arrange
            createTestFile(9); // Creates lines "LINE 0" through "LINE 8"

            // The filter will exclude lines where the line number modulo 3 is 1.
            // This corresponds to "LINE 1", "LINE 4", "LINE 7".
            final List<String> expectedLines = Arrays.asList(
                "LINE 0", "LINE 2", "LINE 3", "LINE 5", "LINE 6", "LINE 8");

            // Act
            final List<String> actualLines = new ArrayList<>();
            try (Reader reader = Files.newBufferedReader(testFile.toPath());
                 LineIterator iterator = new LineIterator(reader) {
                     @Override
                     protected boolean isValidLine(final String line) {
                         final char lastChar = line.charAt(line.length() - 1);
                         return (lastChar - '0') % 3 != 1;
                     }
                 }) {
                iterator.forEachRemaining(actualLines::add);
            }

            // Assert
            assertIterableEquals(expectedLines, actualLines);
        }
    }

    @Nested
    @DisplayName("Closing and Resource Management")
    class ClosingTest {

        @Test
        @DisplayName("close() should stop iteration and prevent further access")
        void closeShouldStopIteration() throws IOException {
            // Arrange
            createTestFile(3);

            try (LineIterator iterator = FileUtils.lineIterator(testFile)) {
                // Act
                assertTrue(iterator.hasNext(), "Should have lines before close()");
                assertNotNull(iterator.next(), "Should return first line");

                iterator.close();

                // Assert
                assertFalse(iterator.hasNext(), "Should have no more lines after close()");
                assertThrows(NoSuchElementException.class, iterator::next);
                assertThrows(NoSuchElementException.class, iterator::nextLine);

                // Calling close() again should not cause an error
                iterator.close();
            }
        }
    }

    @Nested
    @DisplayName("Error Condition Tests")
    class ErrorConditionTest {

        @Test
        @DisplayName("hasNext() should throw IllegalStateException if underlying reader fails")
        void hasNextShouldThrowIllegalStateExceptionOnReaderError() {
            // Arrange
            final Reader failingReader = new BufferedReader(new StringReader("")) {
                @Override
                public String readLine() throws IOException {
                    throw new IOException("Test Exception");
                }
            };

            // Act & Assert
            try (LineIterator iterator = new LineIterator(failingReader)) {
                final IllegalStateException ex = assertThrows(IllegalStateException.class, iterator::hasNext);
                assertEquals(IOException.class, ex.getCause().getClass());
            }
        }

        @Test
        @DisplayName("FileUtils.lineIterator() should throw for a non-existent file")
        void shouldThrowForMissingFile() {
            final File nonExistentFile = new File(temporaryFolder, "non-existent-file.txt");
            assertThrows(NoSuchFileException.class, () -> FileUtils.lineIterator(nonExistentFile));
        }

        @Test
        @DisplayName("FileUtils.lineIterator() should throw for an invalid encoding")
        void shouldThrowForInvalidEncoding() throws IOException {
            createTestFile(1);
            final String invalidEncoding = "INVALID-ENCODING";
            assertThrows(UnsupportedCharsetException.class,
                () -> FileUtils.lineIterator(testFile, invalidEncoding));
        }
    }
}