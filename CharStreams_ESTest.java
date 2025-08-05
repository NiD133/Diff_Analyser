package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileReader;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CharStreams_ESTest extends CharStreams_ESTest_scaffolding {

    // ============================= skipFully Tests =================================
    @Test(timeout = 4000)
    public void test_skipFully_withZero_doesNothing() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        PushbackReader pushbackReader = new PushbackReader(pipedReader, 2408);
        CharStreams.skipFully(pushbackReader, 0L);
    }

    @Test(timeout = 4000)
    public void test_skipFully_withNegative_doesNothing() throws Throwable {
        StringReader reader = new StringReader("Funnels.unencodedCharsFunnel()");
        CharStreams.skipFully(reader, -1932L);
    }

    @Test(timeout = 4000)
    public void test_skipFully_withInsufficientData_throwsEOFException() throws Throwable {
        byte[] byteArray = new byte[9];
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        InputStreamReader reader = new InputStreamReader(byteStream, decoder);
        try {
            CharStreams.skipFully(reader, 336L);
            fail("Expected EOFException");
        } catch (EOFException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_skipFully_withMalformedInput_throwsException() throws Throwable {
        byte[] byteArray = new byte[9];
        byteArray[1] = (byte) (-1);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        InputStreamReader reader = new InputStreamReader(byteStream, decoder);
        try {
            CharStreams.skipFully(reader, 336L);
            fail("Expected MalformedInputException");
        } catch (MalformedInputException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_skipFully_withMockFileReader_throwsNullPointerException() throws Throwable {
        FileDescriptor fd = new FileDescriptor();
        MockFileReader reader = new MockFileReader(fd);
        try {
            CharStreams.skipFully(reader, 222L);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_skipFully_withNullReader_throwsNullPointerException() throws Throwable {
        try {
            CharStreams.skipFully((Reader) null, 20L);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_skipFully_withInvalidArrayIndex_throwsException() throws Throwable {
        byte[] byteArray = new byte[9];
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray, -39, 109);
        Charset charset = Charset.defaultCharset();
        InputStreamReader reader = new InputStreamReader(byteStream, charset);
        try {
            CharStreams.skipFully(reader, 1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_skipFully_withUnconnectedPipe_throwsIOException() throws Throwable {
        PipedReader reader = new PipedReader(1390);
        try {
            CharStreams.skipFully(reader, 1390);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ============================= copy Tests ======================================
    @Test(timeout = 4000)
    public void test_copy_charBufferToNullWriter_clearsBuffer() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        CharBuffer buffer = CharBuffer.allocate(546);
        CharStreams.copy(buffer, writer);
        String result = CharStreams.toString(buffer);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void test_copy_stringReaderToStringBuilder_returnsContent() throws Throwable {
        StringReader reader = new StringReader("com.google.common.primitives.Shorts$ShortConverter");
        StringBuilder builder = new StringBuilder(607);
        long copied = CharStreams.copy(reader, builder);
        assertEquals("com.google.common.primitives.Shorts$ShortConverter", builder.toString());
        assertEquals(50L, copied);
    }

    @Test(timeout = 4000)
    public void test_copy_emptyStringReader_returnsZero() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        StringReader reader = new StringReader("");
        Writer writer = CharStreams.asWriter(buffer);
        long copied = CharStreams.copy(reader, writer);
        assertEquals(0L, copied);
        assertEquals(2048, buffer.remaining());
    }

    @Test(timeout = 4000)
    public void test_copy_toReadOnlyBuffer_throwsException() throws Throwable {
        CharBuffer src = CharStreams.createBuffer();
        CharBuffer dest = CharBuffer.wrap((CharSequence) src);
        try {
            CharStreams.copy(src, dest);
            fail("Expected ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copy_charBufferToItself_throwsException() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        try {
            CharStreams.copy(buffer, buffer);
            fail("Expected BufferOverflowException");
        } catch (BufferOverflowException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copy_withMockFileReader_throwsException() throws Throwable {
        FileDescriptor fd = new FileDescriptor();
        MockFileReader reader = new MockFileReader(fd);
        BufferedReader buffered = new BufferedReader(reader, 1);
        MockFileWriter writer = new MockFileWriter("][!>ptM+tm]l");
        try {
            CharStreams.copy(buffered, writer);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copy_withNullSource_throwsNullPointerException() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        try {
            CharStreams.copy((Readable) null, buffer);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copy_withUnconnectedPipe_throwsIOException() throws Throwable {
        PipedReader reader = new PipedReader(2048);
        Writer writer = CharStreams.nullWriter();
        try {
            CharStreams.copy(reader, writer);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ====================== copyReaderToBuilder Tests =============================
    @Test(timeout = 4000)
    public void test_copyReaderToBuilder_returnsCorrectContent() throws Throwable {
        StringReader reader = new StringReader("com.google.common.primitives.Shorts$ShortConverter");
        StringBuilder builder = new StringBuilder(607);
        long copied = CharStreams.copyReaderToBuilder(reader, builder);
        assertEquals("com.google.common.primitives.Shorts$ShortConverter", builder.toString());
        assertEquals(50L, copied);
    }

    @Test(timeout = 4000)
    public void test_copyReaderToBuilder_withMockFileReader_throwsException() throws Throwable {
        FileDescriptor fd = new FileDescriptor();
        MockFileReader reader = new MockFileReader(fd);
        StringBuilder builder = new StringBuilder();
        try {
            CharStreams.copyReaderToBuilder(reader, builder);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copyReaderToBuilder_withNullReader_throwsNullPointerException() throws Throwable {
        StringBuilder builder = new StringBuilder("com.google.common.collect.FilteredEntryMultimap");
        try {
            CharStreams.copyReaderToBuilder(null, builder);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // ====================== copyReaderToWriter Tests ==============================
    @Test(timeout = 4000)
    public void test_copyReaderToWriter_withNullWriter_returnsCorrectByteCount() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        StringReader reader = new StringReader("|liEWG");
        long copied = CharStreams.copyReaderToWriter(reader, writer);
        assertEquals(6L, copied);
        long nextCopy = CharStreams.copyReaderToWriter(reader, writer);
        assertEquals(0L, nextCopy);
    }

    @Test(timeout = 4000)
    public void test_copyReaderToWriter_withNullReader_throwsNullPointerException() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        try {
            CharStreams.copyReaderToWriter(null, writer);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_copyReaderToWriter_withUnconnectedPipe_throwsIOException() throws Throwable {
        char[] data = new char[1];
        CharArrayReader reader = new CharArrayReader(data);
        PipedWriter writer = new PipedWriter();
        try {
            CharStreams.copyReaderToWriter(reader, writer);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ============================= toString Tests ==================================
    @Test(timeout = 4000)
    public void test_toString_withCharArrayReader_returnsContent() throws Throwable {
        char[] data = new char[3];
        CharArrayReader reader = new CharArrayReader(data);
        String result = CharStreams.toString(reader);
        assertEquals("\u0000\u0000\u0000", result);
    }

    @Test(timeout = 4000)
    public void test_toString_withMalformedInput_throwsException() throws Throwable {
        byte[] byteArray = new byte[7];
        byteArray[3] = (byte) (-62);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        InputStreamReader reader = new InputStreamReader(byteStream, decoder);
        try {
            CharStreams.toString(reader);
            fail("Expected MalformedInputException");
        } catch (MalformedInputException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_toString_withMockFileReader_throwsException() throws Throwable {
        FileDescriptor fd = new FileDescriptor();
        MockFileReader reader = new MockFileReader(fd);
        try {
            CharStreams.toString(reader);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_toString_withNullReader_throwsNullPointerException() throws Throwable {
        try {
            CharStreams.toString((Readable) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_toString_withUnconnectedPipe_throwsIOException() throws Throwable {
        PipedReader reader = new PipedReader();
        try {
            CharStreams.toString(reader);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ============================= readLines Tests ================================
    @Test(timeout = 4000)
    public void test_readLines_charBufferWithOneChar_returnsEmptyList() throws Throwable {
        char[] data = new char[1];
        CharBuffer buffer = CharBuffer.wrap(data);
        buffer.append('z');
        List<String> lines = CharStreams.readLines(buffer);
        assertTrue(lines.isEmpty());
    }

    @Test(timeout = 4000)
    public void test_readLines_charBuffer_returnsLines() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        List<String> lines = CharStreams.readLines(buffer);
        assertEquals(2048, buffer.position());
        assertEquals(2048, buffer.limit());
    }

    @Test(timeout = 4000)
    public void test_readLines_withLineProcessorReturningFalse_stopsEarly() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        LineProcessor<StringBuilder> processor = mock(LineProcessor.class);
        doReturn(null).when(processor).getResult();
        doReturn(false).when(processor).processLine(anyString());
        CharStreams.readLines(buffer, processor);
        assertEquals(2048, buffer.position());
        assertEquals(2048, buffer.capacity());
    }

    @Test(timeout = 4000)
    public void test_readLines_withLineProcessorReturningTrue_returnsResult() throws Throwable {
        char[] data = new char[6];
        CharArrayReader reader = new CharArrayReader(data);
        LineProcessor<Object> processor = mock(LineProcessor.class);
        doReturn(reader).when(processor).getResult();
        doReturn(true).when(processor).processLine(anyString());
        Object result = CharStreams.readLines(reader, processor);
        assertSame(reader, result);
    }

    @Test(timeout = 4000)
    public void test_readLines_withNullProcessor_throwsNullPointerException() throws Throwable {
        char[] data = new char[7];
        CharArrayReader reader = new CharArrayReader(data);
        try {
            CharStreams.readLines(reader, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_readLines_withUnconnectedPipe_throwsIOException() throws Throwable {
        PipedReader reader = new PipedReader(3212);
        LineProcessor<Object> processor = mock(LineProcessor.class);
        try {
            CharStreams.readLines(reader, processor);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_readLines_withNullReadable_throwsNullPointerException() throws Throwable {
        try {
            CharStreams.readLines((Readable) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_readLines_withUnconnectedInputStream_throwsIOException() throws Throwable {
        PipedInputStream pipe = new PipedInputStream();
        InputStreamReader reader = new InputStreamReader(pipe);
        try {
            CharStreams.readLines(reader);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ============================= exhaust Tests ===================================
    @Test(timeout = 4000)
    public void test_exhaust_charBuffer_returnsCharacterCount() throws Throwable {
        CharBuffer buffer = CharStreams.createBuffer();
        long count = CharStreams.exhaust(buffer);
        assertEquals(2048, buffer.position());
        assertEquals(2048L, count);
    }

    @Test(timeout = 4000)
    public void test_exhaust_emptyReader_returnsZero() throws Throwable {
        StringReader reader = new StringReader("");
        long count = CharStreams.exhaust(reader);
        assertEquals(0L, count);
    }

    @Test(timeout = 4000)
    public void test_exhaust_withNullReadable_throwsNullPointerException() throws Throwable {
        try {
            CharStreams.exhaust(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_exhaust_withUnconnectedPipe_throwsIOException() throws Throwable {
        PipedReader reader = new PipedReader();
        try {
            CharStreams.exhaust(reader);
            fail("Expected IOException: Pipe not connected");
        } catch (IOException e) {
            // Expected
        }
    }

    // ============================= asWriter Tests ==================================
    @Test(timeout = 4000)
    public void test_asWriter_withNullAppendable_throwsNullPointerException() throws Throwable {
        try {
            CharStreams.asWriter(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_asWriter_withNullWriter_returnsSameInstance() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        Writer wrapped = CharStreams.asWriter(writer);
        assertSame(writer, wrapped);
    }

    // ============================= Misc Tests ======================================
    @Test(timeout = 4000)
    public void test_stringBuilderInsertOutOfBounds_throwsException() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        StringBuilder builder = new StringBuilder(2342);
        try {
            builder.insert(1, writer);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void test_appendCharBufferToPrintWriter_doesNotChangeBuffer() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        CharBuffer buffer = CharStreams.createBuffer();
        MockPrintWriter printWriter = new MockPrintWriter(writer, true);
        printWriter.append(buffer);
        assertEquals(2048, buffer.remaining());
        assertEquals(2048, buffer.length());
    }
}