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
import org.junit.jupiter.api.Test;

/**
 * Tests {@link BoundedReader}.
 */
class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final String SAMPLE_DATA = "01234567890";
    private static final String SHORT_DATA = "01";
    private static final String MULTILINE_DATA_WITHOUT_FINAL_EOL = "0\n1\n2";
    private static final String MULTILINE_DATA_WITH_FINAL_EOL = "0\n1\n2\n";
    private static final int EOF = -1;
    private static final int MAX_CHARS_LIMIT = 3;
    private static final char PLACEHOLDER_CHAR = 'X';

    @Test
    void shouldCloseUnderlyingReaderWhenBoundedReaderIsClosed() throws IOException {
        // Given: A reader that tracks whether it has been closed
        final AtomicBoolean readerClosed = new AtomicBoolean(false);
        Reader trackingReader = createReaderThatTracksClose(SAMPLE_DATA, readerClosed);

        // When: BoundedReader is closed (which should close the underlying reader)
        try (Reader reader = trackingReader;
             BoundedReader boundedReader = new BoundedReader(reader, MAX_CHARS_LIMIT)) {
            // BoundedReader is automatically closed here
        }

        // Then: The underlying reader should be closed
        assertTrue(readerClosed.get(), "Underlying reader should be closed when BoundedReader is closed");
    }

    @Test
    void shouldReadExactlyThreeCharactersWhenLimitIsThree() throws IOException {
        // Given: A reader with sample data and a limit of 3 characters
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Reading characters one by one until EOF
            char firstChar = (char) boundedReader.read();
            char secondChar = (char) boundedReader.read();
            char thirdChar = (char) boundedReader.read();
            int fourthRead = boundedReader.read();

            // Then: Should read exactly 3 characters then return EOF
            assertEquals('0', firstChar);
            assertEquals('1', secondChar);
            assertEquals('2', thirdChar);
            assertEquals(EOF, fourthRead, "Should return EOF after reading the maximum allowed characters");
        }
    }

    @Test
    void shouldReturnEofWhenUnderlyingReaderIsShorterThanLimit() throws IOException {
        // Given: A reader with only 2 characters but a limit of 3
        Reader shortReader = new BufferedReader(new StringReader(SHORT_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(shortReader, MAX_CHARS_LIMIT)) {
            // When: Reading all available characters
            char firstChar = (char) boundedReader.read();
            char secondChar = (char) boundedReader.read();
            int thirdRead = boundedReader.read();

            // Then: Should read available characters then return EOF
            assertEquals('0', firstChar);
            assertEquals('1', secondChar);
            assertEquals(EOF, thirdRead, "Should return EOF when underlying reader is exhausted");
        }
    }

    @Test
    void shouldReadMultipleCharactersIntoBuffer() throws IOException {
        // Given: A reader with sample data and a buffer larger than the limit
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        char[] buffer = new char[4];
        Arrays.fill(buffer, PLACEHOLDER_CHAR);
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Reading multiple characters into buffer
            int charactersRead = boundedReader.read(buffer, 0, 4);

            // Then: Should read only up to the limit and preserve unused buffer space
            assertEquals(3, charactersRead, "Should read exactly 3 characters due to limit");
            assertEquals('0', buffer[0]);
            assertEquals('1', buffer[1]);
            assertEquals('2', buffer[2]);
            assertEquals(PLACEHOLDER_CHAR, buffer[3], "Unused buffer space should remain unchanged");
        }
    }

    @Test
    void shouldReadMultipleCharactersIntoBufferWithOffset() throws IOException {
        // Given: A reader with sample data and a buffer with offset
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        char[] buffer = new char[4];
        Arrays.fill(buffer, PLACEHOLDER_CHAR);
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Reading 2 characters starting at offset 1
            int charactersRead = boundedReader.read(buffer, 1, 2);

            // Then: Should read characters at the specified offset
            assertEquals(2, charactersRead);
            assertEquals(PLACEHOLDER_CHAR, buffer[0], "Buffer before offset should remain unchanged");
            assertEquals('0', buffer[1]);
            assertEquals('1', buffer[2]);
            assertEquals(PLACEHOLDER_CHAR, buffer[3], "Buffer after read should remain unchanged");
        }
    }

    @Test
    void shouldSkipCharactersAndRespectLimit() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Skipping 2 characters then reading 1
            boundedReader.skip(2);
            char readChar = (char) boundedReader.read();
            int nextRead = boundedReader.read();

            // Then: Should read the character after skip and then EOF
            assertEquals('2', readChar, "Should read character at position 2 after skipping 2");
            assertEquals(EOF, nextRead, "Should return EOF after reaching limit");
        }
    }

    @Test
    void shouldSupportMarkAndResetWithinLimit() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Marking, reading to limit, then resetting and reading again
            boundedReader.mark(3);
            boundedReader.read(); // '0'
            boundedReader.read(); // '1'
            boundedReader.read(); // '2'
            boundedReader.reset();
            
            char firstCharAfterReset = (char) boundedReader.read();
            char secondCharAfterReset = (char) boundedReader.read();
            char thirdCharAfterReset = (char) boundedReader.read();
            int fourthReadAfterReset = boundedReader.read();

            // Then: Should be able to re-read the same characters
            assertEquals('0', firstCharAfterReset);
            assertEquals('1', secondCharAfterReset);
            assertEquals('2', thirdCharAfterReset);
            assertEquals(EOF, fourthReadAfterReset);
        }
    }

    @Test
    void shouldSupportMarkAndResetWithLimitedReadAhead() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Reading to limit, resetting, then marking with smaller read-ahead
            boundedReader.mark(3);
            boundedReader.read(); // '0'
            boundedReader.read(); // '1'
            boundedReader.read(); // '2'
            assertEquals(EOF, boundedReader.read());
            
            boundedReader.reset();
            boundedReader.mark(1); // Smaller read-ahead limit
            char charAfterReset = (char) boundedReader.read();
            int nextRead = boundedReader.read();

            // Then: Should respect the new smaller read-ahead limit
            assertEquals('0', charAfterReset);
            assertEquals(EOF, nextRead, "Should return EOF due to BoundedReader limit");
        }
    }

    @Test
    void shouldHandleMarkWithReadAheadLargerThanBoundedLimit() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Marking with read-ahead larger than BoundedReader limit
            boundedReader.mark(4); // Larger than MAX_CHARS_LIMIT
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            boundedReader.reset();
            
            char firstChar = (char) boundedReader.read();
            char secondChar = (char) boundedReader.read();
            char thirdChar = (char) boundedReader.read();
            int fourthRead = boundedReader.read();

            // Then: Should still respect BoundedReader limit
            assertEquals('0', firstChar);
            assertEquals('1', secondChar);
            assertEquals('2', thirdChar);
            assertEquals(EOF, fourthRead);
        }
    }

    @Test
    void shouldRespectBoundedLimitEvenWithLargeMarkReadAhead() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Setting mark with read-ahead larger than limit
            boundedReader.mark(4);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            int fourthRead = boundedReader.read();

            // Then: Should return EOF at the bounded limit
            assertEquals(EOF, fourthRead, "Should respect BoundedReader limit regardless of mark read-ahead");
        }
    }

    @Test
    void shouldHandleMarkAfterInitialRead() throws IOException {
        // Given: A reader with sample data
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        try (BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT)) {
            // When: Reading one character, then marking
            boundedReader.read(); // '0'
            boundedReader.mark(3);
            boundedReader.read(); // '1'
            boundedReader.read(); // '2'
            int nextRead = boundedReader.read();

            // Then: Should return EOF after reaching limit
            assertEquals(EOF, nextRead, "Should return EOF after reaching bounded limit");
        }
    }

    @Test
    void shouldNotHangWhenReadingLinesWithBufferedReader() {
        // Given: A BoundedReader wrapped in BufferedReader
        Reader sourceReader = new BufferedReader(new StringReader(SAMPLE_DATA));
        
        // When & Then: Reading lines should complete within timeout (no infinite loop)
        assertTimeout(TIMEOUT, () -> {
            BoundedReader boundedReader = new BoundedReader(sourceReader, MAX_CHARS_LIMIT);
            try (BufferedReader bufferedReader = new BufferedReader(boundedReader)) {
                bufferedReader.readLine();
                bufferedReader.readLine(); // This should not hang
            }
        }, "Reading lines should not cause infinite loop or hang");
    }

    @Test
    void shouldHandleLineNumberReaderWithStringDataEndingWithoutEol() {
        // When & Then: Processing multiline data without final EOL should complete within timeout
        assertTimeout(TIMEOUT, () -> {
            processMultilineDataWithLineNumberReader(new StringReader(MULTILINE_DATA_WITHOUT_FINAL_EOL));
        }, "LineNumberReader should handle data without final EOL without hanging");
    }

    @Test
    void shouldHandleLineNumberReaderWithStringDataEndingWithEol() {
        // When & Then: Processing multiline data with final EOL should complete within timeout
        assertTimeout(TIMEOUT, () -> {
            processMultilineDataWithLineNumberReader(new StringReader(MULTILINE_DATA_WITH_FINAL_EOL));
        }, "LineNumberReader should handle data with final EOL without hanging");
    }

    @Test
    void shouldHandleLineNumberReaderWithFileDataEndingWithoutEol() {
        // When & Then: Processing file data without final EOL should complete within timeout
        assertTimeout(TIMEOUT, () -> {
            processFileDataWithLineNumberReader(MULTILINE_DATA_WITHOUT_FINAL_EOL);
        }, "LineNumberReader should handle file data without final EOL without hanging");
    }

    @Test
    void shouldHandleLineNumberReaderWithFileDataEndingWithEol() {
        // When & Then: Processing file data with final EOL should complete within timeout
        assertTimeout(TIMEOUT, () -> {
            processFileDataWithLineNumberReader(MULTILINE_DATA_WITH_FINAL_EOL);
        }, "LineNumberReader should handle file data with final EOL without hanging");
    }

    // Helper methods

    private Reader createReaderThatTracksClose(String data, AtomicBoolean closedFlag) {
        return new BufferedReader(new StringReader(data)) {
            @Override
            public void close() throws IOException {
                closedFlag.set(true);
                super.close();
            }
        };
    }

    private void processMultilineDataWithLineNumberReader(Reader sourceReader) throws IOException {
        int largeLimit = 10_000_000; // Large limit to avoid interference with test logic
        try (LineNumberReader lineReader = new LineNumberReader(new BoundedReader(sourceReader, largeLimit))) {
            while (lineReader.readLine() != null) {
                // Process all lines - this tests that BoundedReader doesn't cause hanging
            }
        }
    }

    private void processFileDataWithLineNumberReader(String testData) throws IOException {
        try (TempFile tempFile = TempFile.create(getClass().getSimpleName(), ".txt")) {
            File file = tempFile.toFile();
            FileUtils.write(file, testData, StandardCharsets.ISO_8859_1);
            
            try (Reader fileReader = Files.newBufferedReader(file.toPath())) {
                processMultilineDataWithLineNumberReader(fileReader);
            }
        }
    }
}