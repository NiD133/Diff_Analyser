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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.TempFile;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link BoundedReader}.
 */
class BoundedReaderTest {

    @Nested
    class CloseTests {
        /**
         * Tests that closing the BoundedReader also closes the underlying reader.
         */
        @Test
        void close_shouldCloseUnderlyingReader() throws IOException {
            final AtomicBoolean closed = new AtomicBoolean();
            try (Reader sr = new BufferedReader(new StringReader("01234567890")) {
                @Override
                public void close() throws IOException {
                    closed.set(true);
                    super.close();
                }
            }) {
                try (BoundedReader mr = new BoundedReader(sr, 3)) {
                    // No operation - just testing close behavior
                }
            }
            assertTrue(closed.get());
        }
    }

    @Nested
    class LineNumberReaderTests {
        private static final String STRING_END_NO_EOL = "0\n1\n2";
        private static final String STRING_END_EOL = "0\n1\n2\n";

        /**
         * Helper method to test reading all lines from a source using LineNumberReader.
         */
        private void testLineNumberReader(final Reader source) throws IOException {
            try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000)) {
                while (reader.readLine() != null) {
                    // Read all lines
                }
            }
        }

        /**
         * Tests reading from a file source with different EOL endings.
         */
        @ParameterizedTest
        @ValueSource(strings = {STRING_END_NO_EOL, STRING_END_EOL})
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void withFileReader(String data) throws IOException {
            try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
                final File file = path.toFile();
                FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
                try (Reader source = Files.newBufferedReader(file.toPath())) {
                    testLineNumberReader(source);
                }
            }
        }

        /**
         * Tests reading from a string source with different EOL endings.
         */
        @ParameterizedTest
        @ValueSource(strings = {STRING_END_NO_EOL, STRING_END_EOL})
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void withStringReader(String data) throws IOException {
            testLineNumberReader(new StringReader(data));
        }
    }

    @Nested
    class MarkResetTests {
        private Reader sr = new BufferedReader(new StringReader("01234567890"));

        /**
         * Tests mark/reset within the bounded limit.
         */
        @Test
        void markReset_shouldWorkWithinLimit() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.mark(3);
                mr.read();
                mr.read();
                mr.read();
                mr.reset();
                assertEquals('0', mr.read());
                assertEquals('1', mr.read());
                assertEquals('2', mr.read());
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests reset after reading all characters.
         */
        @Test
        void markReset_shouldWorkAfterReadingAll() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.mark(3);
                mr.read();
                mr.read();
                mr.read();
                assertEquals(-1, mr.read());
                mr.reset();
                mr.mark(1);
                assertEquals('0', mr.read());
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests mark with limit larger than bounded size.
         */
        @Test
        void markReset_shouldWorkWhenMarkLargerThanLimit() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.mark(4);
                mr.read();
                mr.read();
                mr.read();
                mr.reset();
                assertEquals('0', mr.read());
                assertEquals('1', mr.read());
                assertEquals('2', mr.read());
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests mark outside bounded limit without reading offset.
         */
        @Test
        void markReset_shouldWorkWhenMarkOutsideLimit() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.mark(4);
                mr.read();
                mr.read();
                mr.read();
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests mark outside bounded limit after partial read.
         */
        @Test
        void markReset_shouldWorkWhenMarkOutsideLimitAfterPartialRead() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.read();
                mr.mark(3);
                mr.read();
                mr.read();
                assertEquals(-1, mr.read());
            }
        }
    }

    @Nested
    class ReadTests {
        private Reader sr = new BufferedReader(new StringReader("01234567890"));
        private Reader shortReader = new BufferedReader(new StringReader("01"));

        /**
         * Tests reading beyond bounded limit doesn't hang.
         */
        @Test
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void read_shouldNotHangWhenReadingBeyondLimit() throws IOException {
            final BoundedReader mr = new BoundedReader(sr, 3);
            try (BufferedReader br = new BufferedReader(mr)) {
                br.readLine();
                br.readLine();
            }
        }

        /**
         * Tests full buffer read within bounded limit.
         */
        @Test
        void read_shouldFillBufferWithinLimit() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                final char[] cbuf = new char[4];
                Arrays.fill(cbuf, 'X');
                final int read = mr.read(cbuf, 0, 4);
                assertEquals(3, read);
                assertEquals('0', cbuf[0]);
                assertEquals('1', cbuf[1]);
                assertEquals('2', cbuf[2]);
                assertEquals('X', cbuf[3]);
            }
        }

        /**
         * Tests partial buffer read with offset.
         */
        @Test
        void read_shouldFillBufferWithOffset() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                final char[] cbuf = new char[4];
                Arrays.fill(cbuf, 'X');
                final int read = mr.read(cbuf, 1, 2);
                assertEquals(2, read);
                assertEquals('X', cbuf[0]);
                assertEquals('0', cbuf[1]);
                assertEquals('1', cbuf[2]);
                assertEquals('X', cbuf[3]);
            }
        }

        /**
         * Tests reading till bounded limit.
         */
        @Test
        void read_shouldReturnMinusOneWhenLimitReached() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                assertEquals('0', mr.read());
                assertEquals('1', mr.read());
                assertEquals('2', mr.read());
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests reading from source shorter than bounded limit.
         */
        @Test
        void read_shouldHandleShortSource() throws IOException {
            try (BoundedReader mr = new BoundedReader(shortReader, 3)) {
                assertEquals('0', mr.read());
                assertEquals('1', mr.read());
                assertEquals(-1, mr.read());
            }
        }

        /**
         * Tests skipping within bounded limit.
         */
        @Test
        void skip_shouldSkipCharacters() throws IOException {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                mr.skip(2);
                assertEquals('2', mr.read());
                assertEquals(-1, mr.read());
            }
        }
    }
}