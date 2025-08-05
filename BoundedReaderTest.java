/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.TempFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link BoundedReader}.
 */
class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final String TEN_CHARS_STRING = "0123456789";

    @Nested
    @DisplayName("Read method tests")
    class ReadTests {

        @Test
        void read_shouldReadUpToBoundAndThenReturnEof() throws IOException {
            try (final Reader reader = new StringReader(TEN_CHARS_STRING);
                 final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                assertEquals('0', boundedReader.read());
                assertEquals('1', boundedReader.read());
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should return EOF when bound is reached");
            }
        }

        @Test
        void read_shouldReturnEofWhenUnderlyingReaderIsShorterThanBound() throws IOException {
            try (final Reader shortReader = new StringReader("01");
                 final BoundedReader boundedReader = new BoundedReader(shortReader, 3)) {
                assertEquals('0', boundedReader.read());
                assertEquals('1', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should return EOF when underlying reader ends");
            }
        }

        @Test
        void readIntoBuffer_shouldReadUpToBound() throws IOException {
            try (final Reader reader = new StringReader(TEN_CHARS_STRING);
                 final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                final char[] buffer = new char[4];
                Arrays.fill(buffer, 'X');

                final int charsRead = boundedReader.read(buffer, 0, 4);

                assertEquals(3, charsRead);
                assertEquals('0', buffer[0]);
                assertEquals('1', buffer[1]);
                assertEquals('2', buffer[2]);
                assertEquals('X', buffer[3], "Buffer should be untouched past the read count");
            }
        }

        @Test
        void readIntoBufferWithOffset_shouldRespectBoundAndOffset() throws IOException {
            try (final Reader reader = new StringReader(TEN_CHARS_STRING);
                 final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                final char[] buffer = new char[4];
                Arrays.fill(buffer, 'X');

                // Try to read 2 chars into buffer starting at offset 1
                final int charsRead = boundedReader.read(buffer, 1, 2);

                assertEquals(2, charsRead);
                assertEquals('X', buffer[0], "Buffer should be untouched before offset");
                assertEquals('0', buffer[1]);
                assertEquals('1', buffer[2]);
                assertEquals('X', buffer[3], "Buffer should be untouched after read");
            }
        }
    }

    @Nested
    @DisplayName("Mark and Reset method tests")
    class MarkResetTests {

        private Reader reader;

        @BeforeEach
        void setUp() {
            // Use a BufferedReader to ensure mark/reset is supported
            reader = new BufferedReader(new StringReader(TEN_CHARS_STRING));
        }

        @Test
        void markAndReset_shouldAllowReReadingWithinBounds() throws IOException {
            try (final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                boundedReader.mark(3);
                assertEquals('0', boundedReader.read());
                assertEquals('1', boundedReader.read());
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should be at bound");

                boundedReader.reset();

                assertEquals('0', boundedReader.read());
                assertEquals('1', boundedReader.read());
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should be at bound again after reset");
            }
        }

        @Test
        void mark_withReadAheadLimitGreaterThanBound_shouldNotAffectReading() throws IOException {
            try (final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                // Mark with a read-ahead limit larger than the BoundedReader's total limit.
                boundedReader.mark(4);
                assertEquals('0', boundedReader.read());
                assertEquals('1', boundedReader.read());
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should still respect the initial bound");
            }
        }

        @Test
        void mark_afterInitialRead_shouldWorkCorrectly() throws IOException {
            try (final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                boundedReader.read(); // Read '0'
                // Mark with a read-ahead limit that exceeds remaining chars
                boundedReader.mark(3);
                assertEquals('1', boundedReader.read());
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should respect the initial bound");
            }
        }
    }

    @Nested
    @DisplayName("Skip method tests")
    class SkipTests {
        @Test
        void skip_shouldNotExceedBound() throws IOException {
            try (final Reader reader = new StringReader(TEN_CHARS_STRING);
                 final BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                boundedReader.skip(2);
                assertEquals('2', boundedReader.read());
                assertEquals(-1, boundedReader.read(), "Should be at bound after skipping and reading");
            }
        }
    }

    @Nested
    @DisplayName("Close method tests")
    class CloseTests {
        @Test
        void close_shouldPropagateToUnderlyingReader() throws IOException {
            final AtomicBoolean closed = new AtomicBoolean();
            final Reader underlyingReader = new StringReader(TEN_CHARS_STRING) {
                @Override
                public void close() {
                    closed.set(true);
                }
            };

            try (final BoundedReader boundedReader = new BoundedReader(underlyingReader, 3)) {
                // BoundedReader is closed by try-with-resources
            }

            assertTrue(closed.get(), "Underlying reader should be closed");
        }
    }

    /**
     * Tests a specific edge case (see IO-424) where a {@link BufferedReader} can hang in {@code readLine()}
     * if the underlying stream ends without a final newline character. {@link BoundedReader}
     * should prevent this by correctly signaling EOF.
     */
    @Nested
    @DisplayName("BufferedReader integration tests")
    class BufferedReaderIntegrationTests {

        private static final String STRING_END_NO_EOL = "0\n1\n2";
        private static final String STRING_END_EOL = "0\n1\n2\n";

        @Test
        void readLineFromStringReader_shouldNotHang_whenDataEndsWithEol() {
            assertTimeout(TIMEOUT, () -> {
                try (final Reader source = new StringReader(STRING_END_EOL);
                     final LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 1_000_000))) {
                    while (reader.readLine() != null) {
                        // consume all lines
                    }
                    assertEquals(3, reader.getLineNumber());
                }
            });
        }

        @Test
        void readLineFromStringReader_shouldNotHang_whenDataEndsWithoutEol() {
            assertTimeout(TIMEOUT, () -> {
                try (final Reader source = new StringReader(STRING_END_NO_EOL);
                     final LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 1_000_000))) {
                    while (reader.readLine() != null) {
                        // consume all lines
                    }
                    assertEquals(3, reader.getLineNumber());
                }
            });
        }

        @Test
        void readLineFromFileReader_shouldNotHang_whenDataEndsWithEol() throws IOException {
            try (final TempFile tempFile = TempFile.create(getClass().getSimpleName(), ".txt")) {
                final File file = tempFile.toFile();
                FileUtils.write(file, STRING_END_EOL, StandardCharsets.ISO_8859_1);

                assertTimeout(TIMEOUT, () -> {
                    try (final Reader source = Files.newBufferedReader(file.toPath());
                         final LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 1_000_000))) {
                        while (reader.readLine() != null) {
                            // consume all lines
                        }
                        assertEquals(3, reader.getLineNumber());
                    }
                });
            }
        }

        @Test
        void readLineFromFileReader_shouldNotHang_whenDataEndsWithoutEol() throws IOException {
            try (final TempFile tempFile = TempFile.create(getClass().getSimpleName(), ".txt")) {
                final File file = tempFile.toFile();
                FileUtils.write(file, STRING_END_NO_EOL, StandardCharsets.ISO_8859_1);

                assertTimeout(TIMEOUT, () -> {
                    try (final Reader source = Files.newBufferedReader(file.toPath());
                         final LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 1_000_000))) {
                        while (reader.readLine() != null) {
                            // consume all lines
                        }
                        assertEquals(3, reader.getLineNumber());
                    }
                });
            }
        }

        @Test
        void readLine_shouldReturnNullAtBoundary() {
            assertTimeout(TIMEOUT, () -> {
                // Set bound inside the first line
                try (final Reader reader = new StringReader(TEN_CHARS_STRING);
                     final BoundedReader boundedReader = new BoundedReader(reader, 3);
                     final BufferedReader br = new BufferedReader(boundedReader)) {
                    // First readLine() consumes up to the bound and returns the partial line
                    assertEquals("012", br.readLine());
                    // Second readLine() should immediately get EOF from BoundedReader
                    // and return null without hanging.
                    assertNull(br.readLine());
                }
            });
        }
    }
}