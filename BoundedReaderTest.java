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
 *
 * <p>
 *   This class contains test cases for verifying the behavior of the {@link BoundedReader} class,
 *   which limits the number of characters that can be read from an underlying reader.
 * </p>
 */
class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String STRING_END_NO_EOL = "0\n1\n2";

    private static final String STRING_END_EOL = "0\n1\n2\n";

    private final Reader sr = new BufferedReader(new StringReader("01234567890"));

    private final Reader shortReader = new BufferedReader(new StringReader("01"));

    /**
     * Tests that the {@link BoundedReader} closes the underlying reader when it is closed.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testCloseTest() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean();
        Reader sr = new BufferedReader(new StringReader("01234567890")) { // Use local variable to avoid shadowing
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        };

        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            // The try-with-resources block ensures that mr is closed.
        }

        assertTrue(closed.get(), "Underlying reader should be closed when BoundedReader is closed.");
    }

    /**
     * Helper method to test {@link LineNumberReader} with a {@link BoundedReader}.
     * @param source The source reader to use.
     * @throws IOException If an I/O error occurs.
     */
    private void testLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // noop: Reads lines until the end of the stream is reached
            }
        }
    }

    /**
     * Helper method to test {@link LineNumberReader} and reading from a file, specifically focusing on the last line.
     * @param data The data to write to the file.
     * @throws IOException If an I/O error occurs.
     */
    void testLineNumberReaderAndFileReaderLastLine(final String data) throws IOException {
        try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader source = Files.newBufferedReader(file.toPath())) {
                testLineNumberReader(source);
            }
        }
    }

    /**
     * Tests {@link LineNumberReader} with a file ending without a newline character.
     */
    @Test
    void testLineNumberReaderAndFileReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_NO_EOL));
    }

    /**
     * Tests {@link LineNumberReader} with a file ending with a newline character.
     */
    @Test
    void testLineNumberReaderAndFileReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReaderLastLine(STRING_END_EOL));
    }

    /**
     * Tests {@link LineNumberReader} with a string ending without a newline character.
     */
    @Test
    void testLineNumberReaderAndStringReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_NO_EOL)));
    }

    /**
     * Tests {@link LineNumberReader} with a string ending with a newline character.
     */
    @Test
    void testLineNumberReaderAndStringReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_EOL)));
    }

    /**
     * Tests the {@link BoundedReader#mark(int)} and {@link BoundedReader#reset()} methods.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testMarkReset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(3);
            mr.read();
            mr.read();
            mr.read();
            mr.reset();
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 characters after reset.");
        }
    }

    /**
     * Tests mark and reset functionality when marking from an offset.
     * @throws IOException
     */
    @Test
    void testMarkResetFromOffset1() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(3);
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 chars");
            mr.reset();
            mr.mark(1);
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 1 char");
        }
    }

    /**
     * Tests mark and reset where the mark limit is more than what's left.
     * @throws IOException
     */
    @Test
    void testMarkResetMarkMore() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(4);
            mr.read();
            mr.read();
            mr.read();
            mr.reset();
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 characters after reset.");
        }
    }

    /**
     * Tests marking beyond the boundary.
     * @throws IOException
     */
    @Test
    void testMarkResetWithMarkOutsideBoundedReaderMax() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(4);
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 chars");
        }
    }

    /**
     * Tests that mark works correctly when marking after an initial offset.
     * @throws IOException
     */
    @Test
    void testMarkResetWithMarkOutsideBoundedReaderMaxAndInitialOffset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.read();
            mr.mark(3);
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 chars");
        }
    }

    /**
     * Tests reading bytes until EOF is reached.
     */
    @Test
    void testReadBytesEOF() {
        assertTimeout(TIMEOUT, () -> {
            final BoundedReader mr = new BoundedReader(sr, 3);
            try (BufferedReader br = new BufferedReader(mr)) {
                br.readLine();
                br.readLine();
            }
        });
    }

    /**
     * Tests reading multiple characters into a character array.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testReadMulti() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            final char[] cbuf = new char[4];
            Arrays.fill(cbuf, 'X');
            final int read = mr.read(cbuf, 0, 4);
            assertEquals(3, read, "Should read 3 characters.");
            assertEquals('0', cbuf[0], "First character should be '0'.");
            assertEquals('1', cbuf[1], "Second character should be '1'.");
            assertEquals('2', cbuf[2], "Third character should be '2'.");
            assertEquals('X', cbuf[3], "Fourth character should remain 'X'.");
        }
    }

    /**
     * Tests reading multiple characters into a character array with an offset.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testReadMultiWithOffset() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            final char[] cbuf = new char[4];
            Arrays.fill(cbuf, 'X');
            final int read = mr.read(cbuf, 1, 2);
            assertEquals(2, read, "Should read 2 characters.");
            assertEquals('X', cbuf[0], "First character should remain 'X'.");
            assertEquals('0', cbuf[1], "Second character should be '0'.");
            assertEquals('1', cbuf[2], "Third character should be '1'.");
            assertEquals('X', cbuf[3], "Fourth character should remain 'X'.");
        }
    }

    /**
     * Tests reading until the end of the bounded reader.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testReadTillEnd() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 3 characters.");
        }
    }

    /**
     * Tests the behavior of the {@link BoundedReader} with a short reader.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testShortReader() throws IOException {
        try (BoundedReader mr = new BoundedReader(shortReader, 3)) {
            mr.read();
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after reading 2 characters.");
        }
    }

    /**
     * Tests the {@link BoundedReader#skip(long)} method.
     * @throws IOException If an I/O error occurs.
     */
    @Test
    void testSkipTest() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.skip(2);
            mr.read();
            assertEquals(-1, mr.read(), "End of stream should be reached after skipping 2 characters and reading 1.");
        }
    }
}