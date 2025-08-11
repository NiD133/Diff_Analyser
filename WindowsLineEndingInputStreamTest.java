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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for WindowsLineEndingInputStream which converts line endings to Windows format (CRLF).
 * 
 * The stream has two modes:
 * - ensureLineFeedAtEndOfFile=true: Adds CRLF at end if input doesn't end with line ending
 * - ensureLineFeedAtEndOfFile=false: Only converts existing line endings to CRLF format
 */
class WindowsLineEndingInputStreamTest {

    private static final boolean ENSURE_LINE_FEED_AT_EOF = true;
    private static final boolean DO_NOT_ENSURE_LINE_FEED_AT_EOF = false;
    private static final int BUFFER_SIZE = 100;

    /**
     * Reads input using single-byte read() method with line feed ensured at EOF.
     */
    private String readUsingSingleByteMethod(final String input) throws IOException {
        return readUsingSingleByteMethod(input, ENSURE_LINE_FEED_AT_EOF);
    }

    /**
     * Reads input using single-byte read() method.
     */
    private String readUsingSingleByteMethod(final String input, final boolean ensureLineFeedAtEof) throws IOException {
        try (WindowsLineEndingInputStream stream = createWindowsLineEndingStream(input, ensureLineFeedAtEof)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            
            while (bytesRead < buffer.length) {
                final int nextByte = stream.read();
                if (nextByte < 0) { // End of stream
                    break;
                }
                buffer[bytesRead++] = (byte) nextByte;
            }
            
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads input using read(byte[]) method with line feed ensured at EOF.
     */
    private String readUsingByteArrayMethod(final String input) throws IOException {
        return readUsingByteArrayMethod(input, ENSURE_LINE_FEED_AT_EOF);
    }

    /**
     * Reads input using read(byte[]) method.
     */
    private String readUsingByteArrayMethod(final String input, final boolean ensureLineFeedAtEof) throws IOException {
        try (WindowsLineEndingInputStream stream = createWindowsLineEndingStream(input, ensureLineFeedAtEof)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            final int bytesRead = stream.read(buffer);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads input using read(byte[], offset, length) method with line feed ensured at EOF.
     */
    private String readUsingByteArrayWithOffsetMethod(final String input) throws IOException {
        return readUsingByteArrayWithOffsetMethod(input, ENSURE_LINE_FEED_AT_EOF);
    }

    /**
     * Reads input using read(byte[], offset, length) method.
     */
    private String readUsingByteArrayWithOffsetMethod(final String input, final boolean ensureLineFeedAtEof) throws IOException {
        try (WindowsLineEndingInputStream stream = createWindowsLineEndingStream(input, ensureLineFeedAtEof)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            final int bytesRead = stream.read(buffer, 0, BUFFER_SIZE);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Creates a WindowsLineEndingInputStream from the given input string.
     */
    private WindowsLineEndingInputStream createWindowsLineEndingStream(final String input, final boolean ensureLineFeedAtEof) {
        final CharSequenceInputStream inputStream = CharSequenceInputStream.builder()
                .setCharSequence(input)
                .setCharset(StandardCharsets.UTF_8)
                .get();
        return new WindowsLineEndingInputStream(inputStream, ensureLineFeedAtEof);
    }

    // Tests for simple string conversion (adds CRLF at end)
    
    @Test
    void testSimpleString_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("abc");
        assertEquals("abc\r\n", result, "Simple string should have CRLF appended");
    }

    @Test
    void testSimpleString_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("abc");
        assertEquals("abc\r\n", result, "Simple string should have CRLF appended");
    }

    @Test
    void testSimpleString_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("abc");
        assertEquals("abc\r\n", result, "Simple string should have CRLF appended");
    }

    // Tests for strings that don't end with line endings (adds CRLF at end)
    
    @Test
    void testStringInMiddleOfLine_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("a\r\nbc");
        assertEquals("a\r\nbc\r\n", result, "String not ending with line feed should have CRLF appended");
    }

    @Test
    void testStringInMiddleOfLine_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("a\r\nbc");
        assertEquals("a\r\nbc\r\n", result, "String not ending with line feed should have CRLF appended");
    }

    @Test
    void testStringInMiddleOfLine_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("a\r\nbc");
        assertEquals("a\r\nbc\r\n", result, "String not ending with line feed should have CRLF appended");
    }

    // Tests for Unix line endings conversion (LF -> CRLF)
    
    @Test
    void testUnixLineEndingConversion_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("ab\nc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("ab\r\nc", result, "Unix line ending (LF) should be converted to Windows (CRLF)");
    }

    @Test
    void testUnixLineEndingConversion_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("ab\nc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("ab\r\nc", result, "Unix line ending (LF) should be converted to Windows (CRLF)");
    }

    @Test
    void testUnixLineEndingConversion_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("ab\nc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("ab\r\nc", result, "Unix line ending (LF) should be converted to Windows (CRLF)");
    }

    // Tests for malformed line endings (standalone CR characters remain unchanged)
    
    @Test
    void testStandaloneCarriageReturn_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("a\rbc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\rbc", result, "Standalone CR should remain unchanged");
    }

    @Test
    void testStandaloneCarriageReturn_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("a\rbc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\rbc", result, "Standalone CR should remain unchanged");
    }

    @Test
    void testStandaloneCarriageReturn_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("a\rbc", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\rbc", result, "Standalone CR should remain unchanged");
    }

    // Tests for multiple blank lines (existing CRLF sequences preserved)
    
    @Test
    void testMultipleBlankLines_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("a\r\n\r\nbc");
        assertEquals("a\r\n\r\nbc\r\n", result, "Multiple blank lines should be preserved with CRLF added at end");
    }

    @Test
    void testMultipleBlankLines_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("a\r\n\r\nbc");
        assertEquals("a\r\n\r\nbc\r\n", result, "Multiple blank lines should be preserved with CRLF added at end");
    }

    @Test
    void testMultipleBlankLines_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("a\r\n\r\nbc");
        assertEquals("a\r\n\r\nbc\r\n", result, "Multiple blank lines should be preserved with CRLF added at end");
    }

    // Tests for strings already ending with line endings (no modification when ensureLineFeedAtEof=false)
    
    @Test
    void testStringAlreadyEndingWithCRLF_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("a\r\n\r\n", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\r\n\r\n", result, "String already ending with CRLF should remain unchanged");
        
        String resultNoLineEnding = readUsingSingleByteMethod("a", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a", resultNoLineEnding, "String without line ending should remain unchanged when not ensuring EOF line feed");
    }

    @Test
    void testStringAlreadyEndingWithCRLF_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("a\r\n\r\n", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\r\n\r\n", result, "String already ending with CRLF should remain unchanged");
        
        String resultNoLineEnding = readUsingByteArrayMethod("a", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a", resultNoLineEnding, "String without line ending should remain unchanged when not ensuring EOF line feed");
    }

    @Test
    void testStringAlreadyEndingWithCRLF_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayMethod("a\r\n\r\n", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a\r\n\r\n", result, "String already ending with CRLF should remain unchanged");
        
        String resultNoLineEnding = readUsingByteArrayWithOffsetMethod("a", DO_NOT_ENSURE_LINE_FEED_AT_EOF);
        assertEquals("a", resultNoLineEnding, "String without line ending should remain unchanged when not ensuring EOF line feed");
    }

    // Tests for strings ending with two CRLF sequences (preserved as-is)
    
    @Test
    void testStringEndingWithTwoCRLF_SingleByteRead() throws Exception {
        String result = readUsingSingleByteMethod("a\r\n\r\n");
        assertEquals("a\r\n\r\n", result, "String ending with two CRLF should remain unchanged");
    }

    @Test
    void testStringEndingWithTwoCRLF_ByteArrayRead() throws Exception {
        String result = readUsingByteArrayMethod("a\r\n\r\n");
        assertEquals("a\r\n\r\n", result, "String ending with two CRLF should remain unchanged");
    }

    @Test
    void testStringEndingWithTwoCRLF_ByteArrayWithOffsetRead() throws Exception {
        String result = readUsingByteArrayWithOffsetMethod("a\r\n\r\n");
        assertEquals("a\r\n\r\n", result, "String ending with two CRLF should remain unchanged");
    }

    // Tests for mark/reset functionality (not supported)
    
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkThrowsUnsupportedOperation(final boolean ensureLineFeedAtEndOfFile) {
        WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile);
        
        assertThrows(UnsupportedOperationException.class, 
                () -> stream.mark(1), 
                "mark() should throw UnsupportedOperationException");
    }

    @SuppressWarnings("resource")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkNotSupported(final boolean ensureLineFeedAtEndOfFile) {
        WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile);
        
        assertFalse(stream.markSupported(), "markSupported() should return false");
    }
}