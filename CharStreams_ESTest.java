/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;

/**
 * This test suite is a refactored version of an EvoSuite-generated test class.
 * The goal of the refactoring is to improve understandability, maintainability,
 * and to clearly express the intended behavior of the {@link CharStreams} class.
 */
@RunWith(JUnit4.class)
public class CharStreamsTest {

    private static final String TEST_STRING = "The quick brown fox jumped over the lazy dog.";

    //<editor-fold desc="copy tests">

    @Test
    public void testCopy_fromReadableToAppendable_copiesAllContent() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);
        StringBuilder builder = new StringBuilder();

        // Act
        long count = CharStreams.copy(reader, builder);

        // Assert
        assertThat(count).isEqualTo(TEST_STRING.length());
        assertThat(builder.toString()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testCopy_fromEmptyReader_copiesNothing() throws IOException {
        // Arrange
        StringReader reader = new StringReader("");
        StringBuilder builder = new StringBuilder();

        // Act
        long count = CharStreams.copy(reader, builder);

        // Assert
        assertThat(count).isEqualTo(0);
        assertThat(builder.toString()).isEmpty();
    }

    @Test(expected = BufferOverflowException.class)
    public void testCopy_toSelf_throwsBufferOverflowException() throws IOException {
        // Arrange
        CharBuffer buffer = CharBuffer.allocate(10).put("12345");
        buffer.flip();

        // Act
        CharStreams.copy(buffer, buffer);
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void testCopy_toReadOnlyBuffer_throwsReadOnlyBufferException() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);
        CharBuffer buffer = CharBuffer.allocate(TEST_STRING.length());
        CharBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        // Act
        CharStreams.copy(reader, readOnlyBuffer);
    }

    @Test(expected = NullPointerException.class)
    public void testCopy_withNullReadable_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.copy(null, new StringBuilder());
    }

    @Test(expected = NullPointerException.class)
    public void testCopy_withNullAppendable_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.copy(new StringReader(TEST_STRING), null);
    }

    //</editor-fold>

    //<editor-fold desc="copyReaderToWriter tests">

    @Test
    public void testCopyReaderToWriter_copiesAllContent() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);
        StringBuilderWriter writer = new StringBuilderWriter();

        // Act
        long count = CharStreams.copyReaderToWriter(reader, writer);

        // Assert
        assertThat(count).isEqualTo(TEST_STRING.length());
        assertThat(writer.toString()).isEqualTo(TEST_STRING);
    }

    @Test
    public void testCopyReaderToWriter_fromExhaustedReader_copiesNothing() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);
        Writer writer = CharStreams.nullWriter();
        CharStreams.copyReaderToWriter(reader, writer); // Exhaust the reader

        // Act
        long count = CharStreams.copyReaderToWriter(reader, writer);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    //</editor-fold>

    //<editor-fold desc="toString tests">

    @Test
    public void testToString_withContent_returnsFullString() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);

        // Act
        String result = CharStreams.toString(reader);

        // Assert
        assertThat(result).isEqualTo(TEST_STRING);
    }

    @Test
    public void testToString_withEmptyReader_returnsEmptyString() throws IOException {
        // Arrange
        StringReader reader = new StringReader("");

        // Act
        String result = CharStreams.toString(reader);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void testToString_withNull_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.toString(null);
    }

    //</editor-fold>

    //<editor-fold desc="readLines tests">

    @Test
    public void testReadLines_withMultipleLines_returnsListOfLines() throws IOException {
        // Arrange
        String content = "line 1\nline 2\r\nline 3\r";
        StringReader reader = new StringReader(content);

        // Act
        List<String> lines = CharStreams.readLines(reader);

        // Assert
        assertThat(lines).containsExactly("line 1", "line 2", "line 3").inOrder();
    }

    @Test
    public void testReadLines_withTrailingNewline_doesNotIncludeEmptyLine() throws IOException {
        // Arrange
        String content = "line 1\nline 2\n";
        StringReader reader = new StringReader(content);

        // Act
        List<String> lines = CharStreams.readLines(reader);

        // Assert
        assertThat(lines).containsExactly("line 1", "line 2").inOrder();
    }

    @Test
    public void testReadLines_onConsumedReadable_returnsEmptyList() throws IOException {
        // Arrange
        CharBuffer buffer = CharBuffer.wrap("some data");
        buffer.position(buffer.limit()); // Consume the buffer

        // Act
        List<String> lines = CharStreams.readLines(buffer);

        // Assert
        assertThat(lines).isEmpty();
    }

    @Test
    public void testReadLines_withProcessor_processesAllLines() throws IOException {
        // Arrange
        String content = "line 1\nline 2\nline 3";
        StringReader reader = new StringReader(content);
        @SuppressWarnings("unchecked")
        LineProcessor<List<String>> processor = mock(LineProcessor.class);
        List<String> resultList = Arrays.asList("L1", "L2", "L3");

        when(processor.processLine("line 1")).thenReturn(true);
        when(processor.processLine("line 2")).thenReturn(true);
        when(processor.processLine("line 3")).thenReturn(true);
        when(processor.getResult()).thenReturn(resultList);

        // Act
        List<String> result = CharStreams.readLines(reader, processor);

        // Assert
        InOrder inOrder = inOrder(processor);
        inOrder.verify(processor).processLine("line 1");
        inOrder.verify(processor).processLine("line 2");
        inOrder.verify(processor).processLine("line 3");
        inOrder.verify(processor).getResult();
        assertThat(result).isSameInstanceAs(resultList);
    }

    @Test
    public void testReadLines_withProcessorStoppingEarly_stopsReading() throws IOException {
        // Arrange
        String content = "line 1\nline 2\nline 3";
        StringReader reader = new StringReader(content);
        @SuppressWarnings("unchecked")
        LineProcessor<String> processor = mock(LineProcessor.class);

        when(processor.processLine("line 1")).thenReturn(true);
        when(processor.processLine("line 2")).thenReturn(false); // Stop here
        when(processor.getResult()).thenReturn("stopped");

        // Act
        String result = CharStreams.readLines(reader, processor);

        // Assert
        InOrder inOrder = inOrder(processor);
        inOrder.verify(processor).processLine("line 1");
        inOrder.verify(processor).processLine("line 2");
        inOrder.verify(processor).getResult();
        inOrder.verifyNoMoreInteractions(); // Verifies processLine("line 3") was not called
        assertThat(result).isEqualTo("stopped");
    }

    @Test(expected = NullPointerException.class)
    public void testReadLines_withNull_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.readLines(null);
    }

    @Test(expected = NullPointerException.class)
    public void testReadLines_withNullProcessor_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.readLines(new StringReader(""), null);
    }

    //</editor-fold>

    //<editor-fold desc="skipFully tests">

    @Test
    public void testSkipFully_skipsCorrectNumberOfChars() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);
        int charsToSkip = 10;

        // Act
        CharStreams.skipFully(reader, charsToSkip);

        // Assert
        String remaining = CharStreams.toString(reader);
        assertThat(remaining).isEqualTo(TEST_STRING.substring(charsToSkip));
    }

    @Test
    public void testSkipFully_whenSkippingZero_doesNothing() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);

        // Act
        CharStreams.skipFully(reader, 0L);

        // Assert
        assertThat(CharStreams.toString(reader)).isEqualTo(TEST_STRING);
    }

    @Test
    public void testSkipFully_whenSkippingNegative_doesNothing() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);

        // Act
        CharStreams.skipFully(reader, -10L);

        // Assert
        assertThat(CharStreams.toString(reader)).isEqualTo(TEST_STRING);
    }

    @Test(expected = EOFException.class)
    public void testSkipFully_moreThanAvailable_throwsEofException() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);

        // Act
        CharStreams.skipFully(reader, TEST_STRING.length() + 1);
    }

    @Test(expected = NullPointerException.class)
    public void testSkipFully_withNull_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.skipFully(null, 10L);
    }

    //</editor-fold>

    //<editor-fold desc="exhaust tests">

    @Test
    public void testExhaust_consumesAllCharsAndReturnsCount() throws IOException {
        // Arrange
        StringReader reader = new StringReader(TEST_STRING);

        // Act
        long count = CharStreams.exhaust(reader);

        // Assert
        assertThat(count).isEqualTo(TEST_STRING.length());
        assertThat(reader.read()).isEqualTo(-1); // Verify stream is at EOF
    }

    @Test
    public void testExhaust_onEmptyReader_returnsZero() throws IOException {
        // Arrange
        StringReader reader = new StringReader("");

        // Act
        long count = CharStreams.exhaust(reader);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test(expected = NullPointerException.class)
    public void testExhaust_withNull_throwsNullPointerException() throws IOException {
        // Act
        CharStreams.exhaust(null);
    }

    //</editor-fold>

    //<editor-fold desc="nullWriter tests">

    @Test
    public void testNullWriter_discardsAllWrites() {
        // Arrange
        Writer nullWriter = CharStreams.nullWriter();

        // Act & Assert
        // These should all be no-ops and not throw exceptions
        try {
            nullWriter.write('a');
            nullWriter.write("test");
            nullWriter.write(new char[] {'a', 'b', 'c'});
            nullWriter.append("sequence");
            nullWriter.flush();
            nullWriter.close(); // Should also be a no-op
        } catch (Exception e) {
            fail("CharStreams.nullWriter() should not throw exceptions. Got: " + e);
        }
    }

    //</editor-fold>

    //<editor-fold desc="asWriter tests">

    @Test
    public void testAsWriter_whenGivenWriter_returnsSameInstance() {
        // Arrange
        Writer writer = new StringBuilderWriter();

        // Act
        Writer result = CharStreams.asWriter(writer);

        // Assert
        assertSame(writer, result);
    }

    @Test
    public void testAsWriter_whenGivenAppendable_returnsFunctionalWriter() throws IOException {
        // Arrange
        StringBuilder builder = new StringBuilder();
        Writer writer = CharStreams.asWriter(builder);

        // Act
        writer.write(TEST_STRING);
        writer.flush(); // Should be a no-op for StringBuilder
        writer.close(); // Should be a no-op for StringBuilder

        // Assert
        assertThat(builder.toString()).isEqualTo(TEST_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAsWriter_withNull_throwsNullPointerException() {
        // Act
        CharStreams.asWriter(null);
    }

    //</editor-fold>

    //<editor-fold desc="Exception Propagation tests">

    private Reader createMalformedInputReader() {
        // A byte sequence that is invalid in UTF-8
        byte[] invalidBytes = new byte[] {(byte) 0xC0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidBytes);
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

    @Test(expected = MalformedInputException.class)
    public void testToString_withMalformedInput_throwsException() throws IOException {
        // Arrange
        Reader reader = createMalformedInputReader();
        // Act
        CharStreams.toString(reader);
    }

    @Test(expected = IOException.class)
    public void testCopy_whenReaderThrows_propagatesException() throws IOException {
        // Arrange
        Reader throwingReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test exception");
            }
            @Override
            public void close() {}
        };
        // Act
        CharStreams.copy(throwingReader, new StringBuilder());
    }

    //</editor-fold>

    /** A simple Writer that writes to a StringBuilder, for testing. */
    private static final class StringBuilderWriter extends Writer {
        private final StringBuilder builder = new StringBuilder();

        @Override
        public void write(char[] cbuf, int off, int len) {
            builder.append(cbuf, off, len);
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}