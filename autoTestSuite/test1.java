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
 * Tests for the {@link BoundedReader} class.
 *
 * <p>These tests verify that the {@link BoundedReader} correctly limits the
 * number of characters read from the underlying reader.</p>
 */
public class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String STRING_END_NO_EOL = "0\n1\n2";

    private static final String STRING_END_EOL = "0\n1\n2\n";

    private final Reader sr = new BufferedReader(new StringReader("01234567890"));

    private final Reader shortReader = new BufferedReader(new StringReader("01"));

    /**
     * Tests that the underlying reader is closed when the {@link BoundedReader} is closed.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testCloseTest() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean(false); // Flag to track if the underlying reader is closed.

        // Create a Reader that sets the 'closed' flag when its close() method is called.
        try (Reader sr = new BufferedReader(new StringReader("01234567890")) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        }) {
            // Wrap the reader with a BoundedReader and close it.
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                // Intentionally empty. The try-with-resources statement closes 'mr' automatically.
            }
        }

        // Assert that the underlying reader's close() method was called.
        assertTrue(closed.get(), "Underlying reader should be closed when BoundedReader is closed.");
    }

    /**
     * Helper method to test {@link LineNumberReader} with a {@link BoundedReader}. Reads all lines from the source.
     * @param source The source reader.
     * @throws IOException If an I/O error occurs.
     */
    private void testLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // noop
            }
        }
    }

    /**
     * Helper method to test {@link LineNumberReader} and {@link Files#newBufferedReader} combination with a file.
     * @param data The data to write to the file.
     * @throws IOException If an I/O error occurs.
     */
    public void testLineNumberReaderAndFileReaderLastLine(final String data) throws IOException {
        try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1); // Write data to a temporary file.

            try (Reader source = Files.newBufferedReader(file.toPath())) { // Create a BufferedReader from the file.
                testLineNumberReader(source); // Test the LineNumberReader with the file reader.
            }
        }
    }

    /**
     * Tests {@link LineNumberReader} and {@link Files#newBufferedReader} with data that ends without an end-of-line character.
     */
    @Test
    public void testLineNumberReaderAndFileReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_NO_EOL));
    }

    /**
     * Tests {@link LineNumberReader} and {@link Files#newBufferedReader} with data that ends with an end-of-line character.
     */
    @Test
    public void testLineNumberReaderAndFileReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_EOL));
    }

    /**
     * Tests {@link LineNumberReader} and {@link StringReader} with data that ends without an end-of-line character.
     */
    @Test
    public void testLineNumberReaderAndStringReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_NO_EOL)));
    }

    /**
     * Tests {@link LineNumberReader} and {@link StringReader} with data that ends with an end-of-line character.
     */
    @Test
    public void testLineNumberReaderAndStringReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_EOL)));
    }

    /**
     * Tests the {@link BoundedReader#mark(int)}, {@link BoundedReader#read()}, and {@link BoundedReader#reset()} methods.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMarkReset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(3); // Mark the current position.
            mr.read(); // Read a character.
            mr.read(); // Read a character.
            mr.read(); // Read a character.
            mr.reset(); // Reset to the marked position.
            mr.read(); // Read the first character again.
            mr.read(); // Read the second character again.
            mr.read(); // Read the third character again.
            assertEquals(-1, mr.read(), "Should return EOF after reading the bound"); // Verify that EOF is reached.
        }
    }

    /**
     * Tests the {@link BoundedReader#mark(int)}, {@link BoundedReader#read()}, and {@link BoundedReader#reset()} methods from a specific offset.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMarkResetFromOffset1() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(3);
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read());
            mr.reset();
            mr.mark(1);
            mr.read();
            assertEquals(-1, mr.read());
        }
    }

    /**
     * Tests the {@link BoundedReader#mark(int)}, {@link BoundedReader#read()}, and {@link BoundedReader#reset()} methods when marking more characters than available.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMarkResetMarkMore() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(4);
            mr.read();
            mr.read();
            mr.read();
            mr.reset();
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read());
        }
    }

    /**
     * Tests the {@link BoundedReader#mark(int)}, {@link BoundedReader#read()}, and {@link BoundedReader#reset()} methods with mark outside the bounded reader max.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMarkResetWithMarkOutsideBoundedReaderMax() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(4);
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read());
        }
    }

    /**
     * Tests the {@link BoundedReader#mark(int)}, {@link BoundedReader#read()}, and {@link BoundedReader#reset()} methods with mark outside the bounded reader max and an initial offset.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testMarkResetWithMarkOutsideBoundedReaderMaxAndInitialOffset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.read();
            mr.mark(3);
            mr.read();
            mr.read();
            assertEquals(-1, mr.read());
        }
    }

    /**
     * Tests the {@link BoundedReader} when reading bytes until the end of the stream.
     */
    @Test
    public void testReadBytesEOF() {
        assertTimeout(TIMEOUT, () -> {
            final BoundedReader mr = new BoundedReader(sr, 3);
            try (BufferedReader br = new BufferedReader(mr)) {
                br.readLine();
                br.readLine();
            }
        });
    }

    /**
     * Tests the {@link BoundedReader#read(char[], int, int)} method to read multiple characters at once.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testReadMulti() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            final char[] cbuf = new char[4]; // Create a character buffer.
            Arrays.fill(cbuf, 'X'); // Fill the buffer with 'X' to detect changes.
            final int read = mr.read(cbuf, 0, 4); // Read up to 4 characters into the buffer.

            assertEquals(3, read, "Should read 3 characters."); // Verify that 3 characters were read.
            assertEquals('0', cbuf[0], "First character should be '0'."); // Verify the characters in the buffer.
            assertEquals('1', cbuf[1], "Second character should be '1'.");
            assertEquals('2', cbuf[2], "Third character should be '2'.");
            assertEquals('X', cbuf[3], "Fourth character should remain 'X'."); // Verify that the rest of the buffer is unchanged.
        }
    }

    /**
     * Tests the {@link BoundedReader#read(char[], int, int)} method with an offset in the character buffer.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testReadMultiWithOffset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            final char[] cbuf = new char[4]; // Create a character buffer.
            Arrays.fill(cbuf, 'X'); // Fill the buffer with 'X' to detect changes.
            final int read = mr.read(cbuf, 1, 2); // Read up to 2 characters into the buffer, starting at index 1.

            assertEquals(2, read, "Should read 2 characters."); // Verify that 2 characters were read.
            assertEquals('X', cbuf[0], "First character should remain 'X'."); // Verify the characters in the buffer.
            assertEquals('0', cbuf[1], "Second character should be '0'.");
            assertEquals('1', cbuf[2], "Third character should be '1'.");
            assertEquals('X', cbuf[3], "Fourth character should remain 'X'."); // Verify that the rest of the buffer is unchanged.
        }
    }

    /**
     * Tests that the {@link BoundedReader} stops reading after the specified limit.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testReadTillEnd() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.read(); // Read a character.
            mr.read(); // Read a character.
            mr.read(); // Read a character.
            assertEquals(-1, mr.read(), "Should return EOF after reading the bound"); // Verify that EOF is reached.
        }
    }

    /**
     * Tests that the {@link BoundedReader} works correctly with a short underlying reader.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testShortReader() throws IOException {
        try (BoundedReader mr = new BoundedReader(shortReader, 3)) {
            mr.read(); // Read a character.
            mr.read(); // Read a character.
            assertEquals(-1, mr.read(), "Should return EOF after reading all chars in the short reader"); // Verify that EOF is reached.
        }
    }

    /**
     * Tests the {@link BoundedReader#skip(long)} method.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testSkipTest() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.skip(2); // Skip 2 characters.
            mr.read(); // Read the next character.
            assertEquals(-1, mr.read(), "Should return EOF after reading the bound"); // Verify that EOF is reached.
        }
    }
}