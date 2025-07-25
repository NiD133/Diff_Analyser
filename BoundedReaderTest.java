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
 * Unit tests for {@link BoundedReader}.
 */
class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final String STRING_WITHOUT_EOL = "0\n1\n2";
    private static final String STRING_WITH_EOL = "0\n1\n2\n";

    private final Reader longReader = new BufferedReader(new StringReader("01234567890"));
    private final Reader shortReader = new BufferedReader(new StringReader("01"));

    @Test
    void testReaderClosure() throws IOException {
        final AtomicBoolean isClosed = new AtomicBoolean(false);
        try (Reader reader = new BufferedReader(new StringReader("01234567890")) {
            @Override
            public void close() throws IOException {
                isClosed.set(true);
                super.close();
            }
        }) {
            try (BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                // No operation needed
            }
        }
        assertTrue(isClosed.get(), "Reader should be closed");
    }

    private void readAllLines(LineNumberReader reader) throws IOException {
        while (reader.readLine() != null) {
            // No operation needed
        }
    }

    private void testLineNumberReaderWithFile(String data) throws IOException {
        try (TempFile tempFile = TempFile.create(getClass().getSimpleName(), ".txt")) {
            File file = tempFile.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader fileReader = Files.newBufferedReader(file.toPath())) {
                readAllLines(new LineNumberReader(new BoundedReader(fileReader, 10_000_000)));
            }
        }
    }

    @Test
    void testFileReaderWithoutEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderWithFile(STRING_WITHOUT_EOL));
    }

    @Test
    void testFileReaderWithEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderWithFile(STRING_WITH_EOL));
    }

    @Test
    void testStringReaderWithoutEol() {
        assertTimeout(TIMEOUT, () -> readAllLines(new LineNumberReader(new BoundedReader(new StringReader(STRING_WITHOUT_EOL), 10_000_000))));
    }

    @Test
    void testStringReaderWithEol() {
        assertTimeout(TIMEOUT, () -> readAllLines(new LineNumberReader(new BoundedReader(new StringReader(STRING_WITH_EOL), 10_000_000))));
    }

    @Test
    void testMarkAndReset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            boundedReader.mark(3);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            boundedReader.reset();
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testMarkResetWithOffset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            boundedReader.mark(3);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
            boundedReader.reset();
            boundedReader.mark(1);
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testMarkBeyondLimit() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            boundedReader.mark(4);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testReadToEnd() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testShortReader() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(shortReader, 3)) {
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testSkipFunctionality() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            boundedReader.skip(2);
            boundedReader.read();
            assertEquals(-1, boundedReader.read(), "Should reach EOF");
        }
    }

    @Test
    void testReadMultipleCharacters() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            char[] buffer = new char[4];
            Arrays.fill(buffer, 'X');
            int charsRead = boundedReader.read(buffer, 0, 4);
            assertEquals(3, charsRead, "Should read 3 characters");
            assertEquals('0', buffer[0]);
            assertEquals('1', buffer[1]);
            assertEquals('2', buffer[2]);
            assertEquals('X', buffer[3], "Last character should remain unchanged");
        }
    }

    @Test
    void testReadWithOffset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(longReader, 3)) {
            char[] buffer = new char[4];
            Arrays.fill(buffer, 'X');
            int charsRead = boundedReader.read(buffer, 1, 2);
            assertEquals(2, charsRead, "Should read 2 characters");
            assertEquals('X', buffer[0], "First character should remain unchanged");
            assertEquals('0', buffer[1]);
            assertEquals('1', buffer[2]);
            assertEquals('X', buffer[3], "Last character should remain unchanged");
        }
    }
}