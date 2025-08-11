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
 * Tests for {@link BoundedReader}.
 * 
 * The tests use short, self-contained input sources (StringReader/BufferedReader)
 * and helper methods to make each scenario explicit and easy to follow.
 */
class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String DIGITS = "01234567890";
    private static final String SHORT_DIGITS = "01";
    private static final int LIMIT_THREE = 3;

    private static final String STRING_END_NO_EOL = "0\n1\n2";
    private static final String STRING_END_EOL = "0\n1\n2\n";

    // ------------- Helper factory methods -------------

    private static Reader newDigitsReader() {
        // BufferedReader wraps StringReader to exercise buffering code paths.
        return new BufferedReader(new StringReader(DIGITS));
    }

    private static Reader newShortDigitsReader() {
        return new BufferedReader(new StringReader(SHORT_DIGITS));
    }

    private static BoundedReader newBounded(final Reader target, final int maxChars) {
        return new BoundedReader(target, maxChars);
    }

    // ------------- Helper I/O methods for line-number scenarios -------------

    private static void readAllLinesWithLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(newBounded(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // drain all lines
            }
        }
    }

    private static void readAllLinesFromFile(final String data) throws IOException {
        try (TempFile path = TempFile.create(BoundedReaderTest.class.getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader source = Files.newBufferedReader(file.toPath())) {
                readAllLinesWithLineNumberReader(source);
            }
        }
    }

    // ------------- Tests -------------

    @Test
    void testClosePropagatesToUnderlyingReader() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean(false);

        try (Reader underlying = new BufferedReader(new StringReader(DIGITS)) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        }) {
            try (BoundedReader bounded = newBounded(underlying, LIMIT_THREE)) {
                // no-op, just close bounded and ensure it closes underlying
            }
        }

        assertTrue(closed.get(), "Closing BoundedReader must close the underlying Reader");
    }

    @Test
    void testReadSingleTillEndWithinBound() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            assertEquals(-1, bounded.read(), "Expected EOF after reaching the character limit");
        }
    }

    @Test
    void testReadFromShortUnderlyingReader() throws IOException {
        try (BoundedReader bounded = newBounded(newShortDigitsReader(), LIMIT_THREE)) {
            bounded.read(); // '0'
            bounded.read(); // '1'
            assertEquals(-1, bounded.read(), "Underlying reader exhausted before limit should produce EOF");
        }
    }

    @Test
    void testReadMultiNoOffset() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            final char[] buffer = new char[4];
            Arrays.fill(buffer, 'X');

            final int read = bounded.read(buffer, 0, 4);

            assertEquals(3, read, "Should read up to the configured limit only");
            assertEquals('0', buffer[0], "First char");
            assertEquals('1', buffer[1], "Second char");
            assertEquals('2', buffer[2], "Third char");
            assertEquals('X', buffer[3], "Unwritten cells must remain untouched");
        }
    }

    @Test
    void testReadMultiWithOffset() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            final char[] buffer = new char[4];
            Arrays.fill(buffer, 'X');

            final int read = bounded.read(buffer, 1, 2);

            assertEquals(2, read, "Should read requested amount within the limit");
            assertEquals('X', buffer[0], "Data before offset must remain untouched");
            assertEquals('0', buffer[1], "First char at offset");
            assertEquals('1', buffer[2], "Second char at offset+1");
            assertEquals('X', buffer[3], "Data after written region must remain untouched");
        }
    }

    @Test
    void testSkipWithinBounds() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.skip(2); // skip '0','1'
            bounded.read();  // consume '2'
            assertEquals(-1, bounded.read(), "After skipping 2 and reading 1, limit should be exhausted");
        }
    }

    @Test
    void testReadBytesEOFWithBufferedReaderAndReadLine() {
        assertTimeout(TIMEOUT, () -> {
            try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE);
                 BufferedReader br = new BufferedReader(bounded)) {
                br.readLine(); // read first (and only) line from limited stream
                br.readLine(); // should hit EOF cleanly
            }
        });
    }

    // -------- mark/reset scenarios --------

    @Test
    void testMarkResetWithinLimit() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.mark(LIMIT_THREE);
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            bounded.reset(); // back to mark
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            assertEquals(-1, bounded.read(), "After re-reading 3 chars post-reset, we should hit EOF");
        }
    }

    @Test
    void testMarkResetThenMarkWithSmallerReadAhead() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.mark(LIMIT_THREE);
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            assertEquals(-1, bounded.read(), "Limit reached");
            bounded.reset(); // back to start
            bounded.mark(1); // smaller readAheadLimit (within bounds)
            bounded.read(); // '0'
            assertEquals(-1, bounded.read(), "After reading 1 char with limit 1, next must be EOF");
        }
    }

    @Test
    void testMarkGreaterThanBoundStillBehavesWithinBoundAfterReset() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.mark(LIMIT_THREE + 1); // request more than bound; bound must still apply
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            bounded.reset(); // back to start
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            assertEquals(-1, bounded.read(), "Read limit must not be exceeded even if mark readAheadLimit is larger");
        }
    }

    @Test
    void testMarkOutsideBoundedMaxThenReadTillEOF() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.mark(LIMIT_THREE + 1); // beyond bound
            bounded.read(); // '0'
            bounded.read(); // '1'
            bounded.read(); // '2'
            assertEquals(-1, bounded.read(), "No more chars available once the bounded limit is reached");
        }
    }

    @Test
    void testMarkWithInitialOffsetThenReadTillEOF() throws IOException {
        try (BoundedReader bounded = newBounded(newDigitsReader(), LIMIT_THREE)) {
            bounded.read();      // consume '0'
            bounded.mark(3);     // mark after first char
            bounded.read();      // '1'
            bounded.read();      // '2'
            assertEquals(-1, bounded.read(), "Limit should be reached after consuming remaining chars");
        }
    }

    // -------- LineNumberReader edge cases (end-of-line present/absent) --------

    @Test
    void testLineNumberReaderAndFileReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> readAllLinesFromFile(STRING_END_NO_EOL));
    }

    @Test
    void testLineNumberReaderAndFileReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> readAllLinesFromFile(STRING_END_EOL));
    }

    @Test
    void testLineNumberReaderAndStringReaderLastLineEolNo() {
        assertTimeout(TIMEOUT, () -> readAllLinesWithLineNumberReader(new StringReader(STRING_END_NO_EOL)));
    }

    @Test
    void testLineNumberReaderAndStringReaderLastLineEolYes() {
        assertTimeout(TIMEOUT, () -> readAllLinesWithLineNumberReader(new StringReader(STRING_END_EOL)));
    }
}