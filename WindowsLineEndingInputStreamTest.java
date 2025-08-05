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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class WindowsLineEndingInputStreamTest {

    private enum ReadMethod {
        BYTE,
        BYTE_ARRAY,
        BYTE_ARRAY_INDEX
    }

    private String roundtripReadByte(final String msg) throws IOException {
        return roundtripReadByte(msg, true);
    }

    private String roundtripReadByte(final String msg, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            int i = 0;
            while (i < buf.length) {
                final int read = lf.read();
                if (read < 0) {
                    break;
                }
                buf[i++] = (byte) read;
            }
            return new String(buf, 0, i, StandardCharsets.UTF_8);
        }
    }

    private String roundtripReadByteArray(final String msg) throws IOException {
        return roundtripReadByteArray(msg, true);
    }

    private String roundtripReadByteArray(final String msg, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            final int read = lf.read(buf);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }

    private String roundtripReadByteArrayIndex(final String msg) throws IOException {
        return roundtripReadByteArrayIndex(msg, true);
    }

    private String roundtripReadByteArrayIndex(final String msg, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            final int read = lf.read(buf, 0, 100);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }

    private String roundtrip(ReadMethod readMethod, String input, boolean ensure) throws IOException {
        switch (readMethod) {
            case BYTE:
                return roundtripReadByte(input, ensure);
            case BYTE_ARRAY:
                return roundtripReadByteArray(input, ensure);
            case BYTE_ARRAY_INDEX:
                return roundtripReadByteArrayIndex(input, ensure);
            default:
                throw new IllegalArgumentException("Invalid read method");
        }
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testInTheMiddleOfTheLine(ReadMethod readMethod) throws Exception {
        assertEquals("a\r\nbc\r\n", roundtrip(readMethod, "a\r\nbc", true));
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testLinuxLineFeeds(ReadMethod readMethod) throws Exception {
        assertEquals("ab\r\nc", roundtrip(readMethod, "ab\nc", false));
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testMalformed(ReadMethod readMethod) throws Exception {
        assertEquals("a\rbc", roundtrip(readMethod, "a\rbc", false));
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testMultipleBlankLines(ReadMethod readMethod) throws Exception {
        assertEquals("a\r\n\r\nbc\r\n", roundtrip(readMethod, "a\r\n\r\nbc", true));
    }

    @ParameterizedTest(name = "ReadMethod: {0} - With line endings")
    @EnumSource(ReadMethod.class)
    void testRetainLineFeedWithLineEndings(ReadMethod readMethod) throws Exception {
        assertEquals("a\r\n\r\n", roundtrip(readMethod, "a\r\n\r\n", false));
    }

    @ParameterizedTest(name = "ReadMethod: {0} - Without line endings")
    @EnumSource(ReadMethod.class)
    void testRetainLineFeedWithoutLineEndings(ReadMethod readMethod) throws Exception {
        assertEquals("a", roundtrip(readMethod, "a", false));
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testSimpleString(ReadMethod readMethod) throws Exception {
        assertEquals("abc\r\n", roundtrip(readMethod, "abc", true));
    }

    @ParameterizedTest(name = "ReadMethod: {0}")
    @EnumSource(ReadMethod.class)
    void testTwoLinesAtEnd(ReadMethod readMethod) throws Exception {
        assertEquals("a\r\n\r\n", roundtrip(readMethod, "a\r\n\r\n", true));
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMark(final boolean ensureLineFeedAtEndOfFile) {
        assertThrows(UnsupportedOperationException.class, 
            () -> new WindowsLineEndingInputStream(new NullInputStream(), true).mark(1));
    }

    @SuppressWarnings("resource")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkSupported(final boolean ensureLineFeedAtEndOfFile) {
        assertFalse(new WindowsLineEndingInputStream(new NullInputStream(), true).markSupported());
    }
}