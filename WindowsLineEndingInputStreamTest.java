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
package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Named.named;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
@DisplayName("Tests for WindowsLineEndingInputStream")
class WindowsLineEndingInputStreamTest {

    private static final int BUFFER_SIZE = 100;

    /**
     * A functional interface to represent different ways of reading from an InputStream.
     */
    @FunctionalInterface
    private interface TestReader {
        String readAll(InputStream in) throws IOException;
    }

    /**
     * Reads the entire stream byte by byte using read().
     */
    private static String readByteByByte(final InputStream in) throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];
        int i = 0;
        while (i < buf.length) {
            final int b = in.read();
            if (b == -1) {
                break;
            }
            buf[i++] = (byte) b;
        }
        return new String(buf, 0, i, StandardCharsets.UTF_8);
    }

    /**
     * Reads the stream into a buffer using read(byte[]).
     */
    private static String readIntoBuffer(final InputStream in) throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];
        final int bytesRead = in.read(buf);
        return bytesRead == -1 ? "" : new String(buf, 0, bytesRead, StandardCharsets.UTF_8);
    }

    /**
     * Reads the stream into a buffer using read(byte[], int, int).
     */
    private static String readIntoBufferWithOffset(final InputStream in) throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];
        final int bytesRead = in.read(buf, 0, BUFFER_SIZE);
        return bytesRead == -1 ? "" : new String(buf, 0, bytesRead, StandardCharsets.UTF_8);
    }

    /**
     * Provides the different read strategies as arguments for parameterized tests.
     */
    private static Stream<Arguments> readStrategies() {
        return Stream.of(
            Arguments.of(named("read()", (TestReader) WindowsLineEndingInputStreamTest::readByteByByte)),
            Arguments.of(named("read(byte[])", (TestReader) WindowsLineEndingInputStreamTest::readIntoBuffer)),
            Arguments.of(named("read(byte[], int, int)", (TestReader) WindowsLineEndingInputStreamTest::readIntoBufferWithOffset))
        );
    }

    /**
     * Helper method to process an input string through WindowsLineEndingInputStream
     * using a specific reader.
     */
    private String process(final String input, final boolean ensureLineFeedAtEos, final TestReader reader) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder()
                    .setCharSequence(input)
                    .setCharset(StandardCharsets.UTF_8)
                    .get(),
                ensureLineFeedAtEos)) {
            return reader.readAll(stream);
        }
    }

    @Nested
    @DisplayName("When ensuring a line ending at the end of the stream")
    class WhenEnsuringEosLineEnding {
        private static final boolean ENSURE_EOS_LINE_ENDING = true;

        @DisplayName("should add CRLF to a simple string")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldAddCrlfToSimpleString(final TestReader reader) throws IOException {
            assertEquals("abc\r\n", process("abc", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should add CRLF when a CRLF is not at the end")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldAddCrlfWhenCrlfIsNotAtEnd(final TestReader reader) throws IOException {
            assertEquals("a\r\nbc\r\n", process("a\r\nbc", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should add CRLF after multiple blank lines")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldAddCrlfAfterMultipleBlankLines(final TestReader reader) throws IOException {
            assertEquals("a\r\n\r\nbc\r\n", process("a\r\n\r\nbc", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should not add extra CRLF if stream already ends with it")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldNotAddExtraCrlfIfPresent(final TestReader reader) throws IOException {
            assertEquals("a\r\n\r\n", process("a\r\n\r\n", ENSURE_EOS_LINE_ENDING, reader));
        }
    }

    @Nested
    @DisplayName("When not ensuring a line ending at the end of the stream")
    class WhenNotEnsuringEosLineEnding {
        private static final boolean ENSURE_EOS_LINE_ENDING = false;

        @DisplayName("should convert standalone LF to CRLF")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldConvertLfToCrlf(final TestReader reader) throws IOException {
            assertEquals("ab\r\nc", process("ab\nc", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should leave a standalone CR unchanged")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldLeaveLoneCrUnchanged(final TestReader reader) throws IOException {
            assertEquals("a\rbc", process("a\rbc", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should not add CRLF to a simple string")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldNotAddCrlfToSimpleString(final TestReader reader) throws IOException {
            assertEquals("a", process("a", ENSURE_EOS_LINE_ENDING, reader));
        }

        @DisplayName("should preserve existing CRLF sequences")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("org.apache.commons.io.input.WindowsLineEndingInputStreamTest#readStrategies")
        void shouldPreserveExistingCrlf(final TestReader reader) throws IOException {
            assertEquals("a\r\n\r\n", process("a\r\n\r\n", ENSURE_EOS_LINE_ENDING, reader));
        }
    }

    @Nested
    @DisplayName("Unsupported Operations")
    class UnsupportedOperations {
        @Test
        @DisplayName("markSupported() should always return false")
        void markSupportedShouldReturnFalse() {
            assertFalse(new WindowsLineEndingInputStream(new NullInputStream(), true).markSupported());
            assertFalse(new WindowsLineEndingInputStream(new NullInputStream(), false).markSupported());
        }

        @Test
        @DisplayName("mark() should throw UnsupportedOperationException")
        void markShouldThrowException() {
            assertThrows(UnsupportedOperationException.class, () -> new WindowsLineEndingInputStream(new NullInputStream(), true).mark(1));
            assertThrows(UnsupportedOperationException.class, () -> new WindowsLineEndingInputStream(new NullInputStream(), false).mark(1));
        }
    }
}