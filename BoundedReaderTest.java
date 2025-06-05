/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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
 * <p>
 *   This test suite aims to verify the correct behavior of the {@link BoundedReader} class,
 *   focusing on its ability to limit the number of characters read from an underlying reader.
 *   The tests cover various scenarios, including closing, marking, resetting, reading single characters,
 *   reading multiple characters, skipping, and interaction with other reader types like {@link LineNumberReader}.
 * </p>
 */
public class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String STRING_END_NO_EOL = "0\n1\n2";

    private static final String STRING_END_EOL = "0\n1\n2\n";

    private static final String TEST_STRING = "01234567890";
    private static final int MAX_CHARS = 3;

    private final Reader testStringReader = new BufferedReader(new StringReader(TEST_STRING));

    private final Reader shortStringReader = new BufferedReader(new StringReader("01"));

    @Test
    public void testCloseReleasesUnderlyingResource() throws IOException {
        // Arrange
        final AtomicBoolean closed = new AtomicBoolean(false);
        Reader baseReader = new BufferedReader(new StringReader(TEST_STRING)) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        };

        // Act
        try (BoundedReader boundedReader = new BoundedReader(baseReader, MAX_CHARS)) {
            // Using try-with-resources, the boundedReader will be closed automatically
            // This should also close the underlying reader.
        }

        // Assert
        assertTrue(closed.get(), "Underlying reader should be closed when BoundedReader is closed.");
    }

    /**
     * Helper method to test {@link LineNumberReader} in conjunction with {@link BoundedReader}.
     * It reads all lines from the source reader.
     * @param source The reader to wrap with LineNumberReader and BoundedReader.
     * @throws IOException if an I/O error occurs.
     */
    private void testLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // noop
            }
        }
    }

    /**
     * Helper method to create a temp file with the given data, then test it with LineNumberReader and BoundedReader
     * This focuses on cases where the last line has or doesn't have a EOL character.
     * @param data the data to write in the temp file
     * @throws IOException if an I/O error occurs.
     */
    public void testLineNumberReaderAndFileReaderLastLine(final String data) throws IOException {
        try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader source = Files.newBufferedReader(file.toPath())) {
                testLineNumberReader(source);
            }
        }
    }

    @Test
    public void testLineNumberReaderAndFileReaderWithLastLineWithoutEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_NO_EOL));
    }

    @Test
    public void testLineNumberReaderAndFileReaderWithLastLineWithEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_EOL));
    }

    @Test
    public void testLineNumberReaderAndStringReaderWithLastLineWithoutEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_NO_EOL)));
    }

    @Test
    public void testLineNumberReaderAndStringReaderWithLastLineWithEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_EOL)));
    }

    @Test
    public void testMarkAndResetWithinBounds() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.mark(3); // Mark position, allowing up to 3 characters to be read before reset.
            boundedReader.read(); // Read '0'
            boundedReader.read(); // Read '1'
            boundedReader.read(); // Read '2'
            boundedReader.reset(); // Reset to the marked position.
            boundedReader.read(); // Read '0' again
            boundedReader.read(); // Read '1' again
            boundedReader.read(); // Read '2' again

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }

    @Test
    public void testMarkAndResetFromOffset1() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.mark(3); // Mark position, allowing up to 3 characters to be read before reset.
            boundedReader.read(); // Read '0'
            boundedReader.read(); // Read '1'
            boundedReader.read(); // Read '2'
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
            boundedReader.reset(); // Reset to the marked position.
            boundedReader.mark(1);
            boundedReader.read(); // Read '0' again

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }

    @Test
    public void testMarkAndResetWithMarkMore() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.mark(4); // Mark position, allowing up to 4 characters to be read before reset.
            boundedReader.read(); // Read '0'
            boundedReader.read(); // Read '1'
            boundedReader.read(); // Read '2'
            boundedReader.reset(); // Reset to the marked position.
            boundedReader.read(); // Read '0' again
            boundedReader.read(); // Read '1' again
            boundedReader.read(); // Read '2' again

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }

    @Test
    public void testMarkAndResetBeyondBoundsNoReset() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.mark(4); // Mark position, allowing up to 4 characters to be read before reset.
            boundedReader.read(); // Read '0'
            boundedReader.read(); // Read '1'
            boundedReader.read(); // Read '2'

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }

   @Test
    public void testMarkAndResetWithInitialOffsetBeyondBounds() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.read(); // Read '0', offset = 1
            boundedReader.mark(3); // Mark position, allowing up to 3 characters to be read before reset.
            boundedReader.read(); // Read '1', offset = 2
            boundedReader.read(); // Read '2', offset = 3

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }


    @Test
    public void testReadBeyondEofDoesNotHang() {
        assertTimeout(TIMEOUT, () -> {
            // Arrange
            BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS);
            try (BufferedReader bufferedReader = new BufferedReader(boundedReader)) {
                // Act
                bufferedReader.readLine();
                bufferedReader.readLine(); // Attempt to read past the end of the bounded reader.

                // Assert: No exception thrown, and the timeout is not exceeded.
                // This verifies that the BoundedReader correctly returns EOF and does not cause the BufferedReader to hang indefinitely.
            }
        });
    }

    @Test
    public void testReadCharArray() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            char[] charBuffer = new char[4];
            Arrays.fill(charBuffer, 'X');

            // Act
            int charsRead = boundedReader.read(charBuffer, 0, 4);

            // Assert
            assertEquals(MAX_CHARS, charsRead, "Should have read " + MAX_CHARS + " characters.");
            assertEquals('0', charBuffer[0], "First character should be '0'.");
            assertEquals('1', charBuffer[1], "Second character should be '1'.");
            assertEquals('2', charBuffer[2], "Third character should be '2'.");
            assertEquals('X', charBuffer[3], "Fourth character should remain unchanged.");
        }
    }

    @Test
    public void testReadCharArrayWithOffset() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            char[] charBuffer = new char[4];
            Arrays.fill(charBuffer, 'X');

            // Act
            int charsRead = boundedReader.read(charBuffer, 1, 2);

            // Assert
            assertEquals(2, charsRead, "Should have read 2 characters.");
            assertEquals('X', charBuffer[0], "First character should remain unchanged.");
            assertEquals('0', charBuffer[1], "Second character should be '0'.");
            assertEquals('1', charBuffer[2], "Third character should be '1'.");
            assertEquals('X', charBuffer[3], "Fourth character should remain unchanged.");
        }
    }

    @Test
    public void testReadSingleCharacterUntilEnd() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the allowed characters.");
        }
    }

    @Test
    public void testReadWithUnderlyingShortReader() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(shortStringReader, MAX_CHARS)) {
            // Act
            boundedReader.read();
            boundedReader.read();

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF as underlying reader is exhausted.");
        }
    }

    @Test
    public void testSkipCharacters() throws IOException {
        // Arrange
        try (BoundedReader boundedReader = new BoundedReader(testStringReader, MAX_CHARS)) {
            // Act
            boundedReader.skip(2);
            boundedReader.read();

            // Assert
            assertEquals(-1, boundedReader.read(), "Should return EOF after skipping and reading the allowed characters.");
        }
    }
}