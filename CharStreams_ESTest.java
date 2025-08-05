package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import java.io.*;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CharStreams_ESTest extends CharStreams_ESTest_scaffolding {

    private static final long TIMEOUT = 4000L;
    private static final int BUFFER_SIZE = 2048;
    private static final int CHAR_ARRAY_SIZE = 7;

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithPushbackReader() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        PushbackReader pushbackReader = new PushbackReader(pipedReader, 2408);
        CharStreams.skipFully(pushbackReader, 0L);
    }

    @Test(timeout = TIMEOUT)
    public void testCopyCharBufferToNullWriter() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        CharBuffer charBuffer = CharBuffer.allocate(546);
        CharStreams.copy(charBuffer, writer);
        String result = CharStreams.toString((Readable) charBuffer);
        assertEquals("", result);
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesFromCharBuffer() throws Throwable {
        char[] charArray = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        charBuffer.append('z');
        List<String> lines = CharStreams.readLines((Readable) charBuffer);
        assertTrue(lines.isEmpty());
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToBuilder() throws Throwable {
        StringReader stringReader = new StringReader("com.google.common.primitives.Shorts$ShortConverter");
        StringBuilder stringBuilder = new StringBuilder(607);
        long copiedChars = CharStreams.copyReaderToBuilder(stringReader, stringBuilder);
        assertEquals("com.google.common.primitives.Shorts$ShortConverter", stringBuilder.toString());
        assertEquals(50L, copiedChars);
    }

    @Test(timeout = TIMEOUT)
    public void testToStringWithMalformedInputException() throws Throwable {
        byte[] byteArray = new byte[7];
        byteArray[3] = (byte) (-62);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder charsetDecoder = charset.newDecoder();
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, charsetDecoder);
        try {
            CharStreams.toString((Readable) inputStreamReader);
            fail("Expecting exception: MalformedInputException");
        } catch (MalformedInputException e) {
            verifyException("java.nio.charset.CoderResult", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testToStringWithMockFileReader() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileReader mockFileReader = new MockFileReader(fileDescriptor);
        try {
            CharStreams.toString((Readable) mockFileReader);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testToStringWithNullReader() throws Throwable {
        try {
            CharStreams.toString((Readable) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testToStringWithUnconnectedPipedReader() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        try {
            CharStreams.toString((Readable) pipedReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithMalformedInputException() throws Throwable {
        byte[] byteArray = new byte[9];
        byteArray[1] = (byte) (-1);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder charsetDecoder = charset.newDecoder();
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, charsetDecoder);
        try {
            CharStreams.skipFully(inputStreamReader, 336L);
            fail("Expecting exception: MalformedInputException");
        } catch (MalformedInputException e) {
            verifyException("java.nio.charset.CoderResult", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithMockFileReader() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileReader mockFileReader = new MockFileReader(fileDescriptor);
        try {
            CharStreams.skipFully(mockFileReader, 222L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithNullReader() throws Throwable {
        try {
            CharStreams.skipFully((Reader) null, 20L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithArrayIndexOutOfBoundsException() throws Throwable {
        byte[] byteArray = new byte[9];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, (byte) (-39), (byte) 109);
        Charset charset = Charset.defaultCharset();
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, charset);
        try {
            CharStreams.skipFully(inputStreamReader, (byte) 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithUnconnectedPipedReader() throws Throwable {
        PipedReader pipedReader = new PipedReader(1390);
        try {
            CharStreams.skipFully(pipedReader, 1390);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithNullLineProcessor() throws Throwable {
        char[] charArray = new char[CHAR_ARRAY_SIZE];
        CharArrayReader charArrayReader = new CharArrayReader(charArray);
        try {
            CharStreams.readLines((Readable) charArrayReader, (LineProcessor<Object>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithUnconnectedPipedReader() throws Throwable {
        PipedReader pipedReader = new PipedReader(3212);
        LineProcessor<Object> lineProcessor = mock(LineProcessor.class, new ViolatedAssumptionAnswer());
        try {
            CharStreams.readLines((Readable) pipedReader, lineProcessor);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithNullReader() throws Throwable {
        try {
            CharStreams.readLines((Readable) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithUnconnectedPipedInputStream() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(pipedInputStream);
        try {
            CharStreams.readLines((Readable) inputStreamReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testExhaustWithNullReader() throws Throwable {
        try {
            CharStreams.exhaust((Readable) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.io.CharStreams", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testExhaustWithUnconnectedPipedReader() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        try {
            CharStreams.exhaust(pipedReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToWriterWithNullReader() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        try {
            CharStreams.copyReaderToWriter((Reader) null, writer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToWriterWithUnconnectedPipedWriter() throws Throwable {
        char[] charArray = new char[1];
        CharArrayReader charArrayReader = new CharArrayReader(charArray);
        PipedWriter pipedWriter = new PipedWriter();
        try {
            CharStreams.copyReaderToWriter(charArrayReader, pipedWriter);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedWriter", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToBuilderWithMockFileReader() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileReader mockFileReader = new MockFileReader(fileDescriptor);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            CharStreams.copyReaderToBuilder(mockFileReader, stringBuilder);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToBuilderWithNullReader() throws Throwable {
        StringBuilder stringBuilder = new StringBuilder("com.google.common.collect.FilteredEntryMultimap");
        try {
            CharStreams.copyReaderToBuilder((Reader) null, stringBuilder);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithReadOnlyBufferException() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        CharBuffer readOnlyBuffer = CharBuffer.wrap((CharSequence) charBuffer);
        try {
            CharStreams.copy(charBuffer, readOnlyBuffer);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithBufferOverflowException() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        try {
            CharStreams.copy(charBuffer, charBuffer);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithMockFileReader() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileReader mockFileReader = new MockFileReader(fileDescriptor);
        BufferedReader bufferedReader = new BufferedReader(mockFileReader, 1);
        MockFileWriter mockFileWriter = new MockFileWriter("][!>ptM+tm]l");
        try {
            CharStreams.copy(bufferedReader, mockFileWriter);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithNullReadable() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        try {
            CharStreams.copy((Readable) null, charBuffer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithUnconnectedPipedReader() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        PipedReader pipedReader = new PipedReader(BUFFER_SIZE);
        try {
            CharStreams.copy(pipedReader, writer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testAsWriterWithNullAppendable() throws Throwable {
        try {
            CharStreams.asWriter((Appendable) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testAsWriterWithNullWriter() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        Writer resultWriter = CharStreams.asWriter(writer);
        assertSame(resultWriter, writer);
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithEOFException() throws Throwable {
        byte[] byteArray = new byte[9];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Charset charset = Charset.defaultCharset();
        CharsetDecoder charsetDecoder = charset.newDecoder();
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, charsetDecoder);
        try {
            CharStreams.skipFully(inputStreamReader, 336L);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.common.io.CharStreams", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSkipFullyWithNegativeValue() throws Throwable {
        StringReader stringReader = new StringReader("Funnels.unencodedCharsFunnel()");
        CharStreams.skipFully(stringReader, -1932L);
    }

    @Test(timeout = TIMEOUT)
    public void testExhaustWithCharBuffer() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        long exhaustedChars = CharStreams.exhaust(charBuffer);
        assertEquals(BUFFER_SIZE, charBuffer.position());
        assertEquals(BUFFER_SIZE, exhaustedChars);
    }

    @Test(timeout = TIMEOUT)
    public void testExhaustWithEmptyStringReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        long exhaustedChars = CharStreams.exhaust(stringReader);
        assertEquals(0L, exhaustedChars);
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithLineProcessor() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        LineProcessor<StringBuilder> lineProcessor = mock(LineProcessor.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(lineProcessor).getResult();
        doReturn(false).when(lineProcessor).processLine(anyString());
        CharStreams.readLines((Readable) charBuffer, lineProcessor);
        assertEquals(BUFFER_SIZE, charBuffer.position());
        assertEquals(BUFFER_SIZE, charBuffer.capacity());
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithCharArrayReader() throws Throwable {
        char[] charArray = new char[6];
        CharArrayReader charArrayReader = new CharArrayReader(charArray);
        LineProcessor<Object> lineProcessor = mock(LineProcessor.class, new ViolatedAssumptionAnswer());
        doReturn(charArrayReader).when(lineProcessor).getResult();
        doReturn(true).when(lineProcessor).processLine(anyString());
        Object result = CharStreams.readLines((Readable) charArrayReader, lineProcessor);
        assertSame(charArrayReader, result);
    }

    @Test(timeout = TIMEOUT)
    public void testReadLinesWithCharBuffer() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        CharStreams.readLines((Readable) charBuffer);
        assertEquals(BUFFER_SIZE, charBuffer.position());
        assertEquals(BUFFER_SIZE, charBuffer.limit());
    }

    @Test(timeout = TIMEOUT)
    public void testToStringWithCharArrayReader() throws Throwable {
        char[] charArray = new char[3];
        CharArrayReader charArrayReader = new CharArrayReader(charArray);
        String result = CharStreams.toString((Readable) charArrayReader);
        assertEquals("\u0000\u0000\u0000", result);
    }

    @Test(timeout = TIMEOUT)
    public void testCopyToStringBuilder() throws Throwable {
        StringReader stringReader = new StringReader("|liEWG");
        StringBuilder stringBuilder = new StringBuilder();
        long copiedChars = CharStreams.copy(stringReader, stringBuilder);
        assertEquals("|liEWG", stringBuilder.toString());
        assertEquals(6L, copiedChars);
    }

    @Test(timeout = TIMEOUT)
    public void testCopyToCharBufferWriter() throws Throwable {
        CharBuffer charBuffer = CharStreams.createBuffer();
        StringReader stringReader = new StringReader("");
        Writer writer = CharStreams.asWriter(charBuffer);
        long copiedChars = CharStreams.copy(stringReader, writer);
        assertEquals(0L, copiedChars);
        assertEquals(BUFFER_SIZE, charBuffer.remaining());
    }

    @Test(timeout = TIMEOUT)
    public void testStringBuilderInsertWithOutOfBounds() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        StringBuilder stringBuilder = new StringBuilder(2342);
        try {
            stringBuilder.insert(1, (Object) writer);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testMockPrintWriterAppend() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        CharBuffer charBuffer = CharStreams.createBuffer();
        MockPrintWriter mockPrintWriter = new MockPrintWriter(writer, true);
        mockPrintWriter.append((CharSequence) charBuffer);
        assertEquals(BUFFER_SIZE, charBuffer.remaining());
        assertEquals(BUFFER_SIZE, charBuffer.length());
    }

    @Test(timeout = TIMEOUT)
    public void testCopyReaderToWriterTwice() throws Throwable {
        Writer writer = CharStreams.nullWriter();
        StringReader stringReader = new StringReader("Funnels.unencodedCharsFunnel()");
        long firstCopy = CharStreams.copyReaderToWriter(stringReader, writer);
        assertEquals(30L, firstCopy);

        long secondCopy = CharStreams.copyReaderToWriter(stringReader, writer);
        assertEquals(0L, secondCopy);
    }
}