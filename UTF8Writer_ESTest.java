package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on correctness,
 * edge cases, and error handling.
 */
public class UTF8WriterTest {

    // Character constants for different UTF-8 byte lengths
    private static final int ASCII_CHAR_VAL = 'A'; // U+0041 -> 1 byte
    private static final int TWO_BYTE_CHAR_VAL = 0x00A2; // Cent sign ¢ -> 2 bytes
    private static final int THREE_BYTE_CHAR_VAL = 0x20AC; // Euro sign € -> 3 bytes
    private static final int FOUR_BYTE_CODE_POINT = 0x10400; // Deseret Long I -> 4 bytes

    // Surrogate character constants
    private static final int HIGH_SURROGATE_FIRST = 0xD800;
    private static final int LOW_SURROGATE_FIRST = 0xDC00;
    private static final int LOW_SURROGATE_LAST = 0xDFFF;

    private BufferRecycler bufferRecycler;
    private ByteArrayOutputStream outputStream;
    private IOContext ioContext;
    private UTF8Writer writer;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        outputStream = new ByteArrayOutputStream();
        ioContext = createContext(false);
        writer = new UTF8Writer(ioContext, outputStream);
    }

    private IOContext createContext(boolean resourceManaged) {
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                resourceManaged
        );
    }

    /*
    /**********************************************************
    /* Constructor and Setup Tests
    /**********************************************************
    */

    @Test(expected = NullPointerException.class)
    public void constructor_whenContextIsNull_throwsNullPointerException() {
        new UTF8Writer(null, outputStream);
    }

    /*
    /**********************************************************
    /* Tests for write(int)
    /**********************************************************
    */

    @Test
    public void write_withAsciiChar_writesOneByte() throws IOException {
        writer.write(ASCII_CHAR_VAL);
        writer.flush();
        assertArrayEquals(new byte[]{'A'}, outputStream.toByteArray());
    }

    @Test
    public void write_withTwoByteChar_writesTwoBytes() throws IOException {
        writer.write(TWO_BYTE_CHAR_VAL);
        writer.flush();
        assertArrayEquals(new byte[]{(byte) 0xC2, (byte) 0xA2}, outputStream.toByteArray());
    }

    @Test
    public void write_withThreeByteChar_writesThreeBytes() throws IOException {
        writer.write(THREE_BYTE_CHAR_VAL);
        writer.flush();
        assertArrayEquals(new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC}, outputStream.toByteArray());
    }

    @Test
    public void write_withFourByteCodePoint_writesFourBytes() throws IOException {
        writer.write(FOUR_BYTE_CODE_POINT);
        writer.flush();
        assertArrayEquals(new byte[]{(byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x80}, outputStream.toByteArray());
    }

    /**
     * A valid surrogate pair consists of a high surrogate (U+D800-U+DBFF) followed by
     * a low surrogate (U+DC00-U+DFFF). This test verifies that writing them sequentially
     * produces the correct 4-byte UTF-8 character.
     */
    @Test
    public void write_withValidSurrogatePair_writesFourByteCodePoint() throws IOException {
        // U+10400 is represented by surrogates D801 and DC00
        writer.write(0xD801);
        writer.write(0xDC00);
        writer.flush();
        assertArrayEquals(new byte[]{(byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x80}, outputStream.toByteArray());
    }

    /*
    /**********************************************************
    /* Error handling for write(int)
    /**********************************************************
    */

    /**
     * The UTF-16 surrogate range is U+D800 to U+DFFF. These characters are invalid
     * in isolation and must appear in pairs. Writing a lone low surrogate (U+DC00 to U+DFFF)
     * should fail.
     */
    @Test(expected = IOException.class)
    public void write_whenGivenUnmatchedLowSurrogate_throwsIOException() throws IOException {
        writer.write(LOW_SURROGATE_LAST);
    }

    /**
     * Writing a high surrogate should store it and expect a low surrogate next.
     * Writing another high surrogate instead should result in an error.
     */
    @Test(expected = IOException.class)
    public void write_whenGivenHighSurrogateAfterHighSurrogate_throwsIOException() throws IOException {
        writer.write(HIGH_SURROGATE_FIRST);
        writer.write(HIGH_SURROGATE_FIRST); // Invalid sequence
    }

    /**
     * A high surrogate must be followed by a low surrogate. Following it with a
     * non-surrogate character is an error.
     */
    @Test(expected = IOException.class)
    public void write_whenGivenInvalidCharacterAfterHighSurrogate_throwsIOException() throws IOException {
        writer.write(HIGH_SURROGATE_FIRST);
        writer.write('A'); // Invalid sequence
    }

    @Test(expected = IOException.class)
    public void write_whenGivenCodePointAboveLegalMaximum_throwsIOException() throws IOException {
        // The maximum legal Unicode code point is U+10FFFF.
        writer.write(0x110000);
    }

    /*
    /**********************************************************
    /* Tests for write(String) and write(char[])
    /**********************************************************
    */

    @Test
    public void writeString_writesCorrectUTF8Bytes() throws IOException {
        String text = "Hello, World! ¢€𐐀"; // Mix of 1, 2, 3, and 4-byte chars
        writer.write(text);
        writer.flush();
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), outputStream.toByteArray());
    }

    @Test
    public void writeCharArray_writesCorrectUTF8Bytes() throws IOException {
        String text = "Hello, World! ¢€𐐀";
        writer.write(text.toCharArray());
        writer.flush();
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), outputStream.toByteArray());
    }

    @Test(expected = NullPointerException.class)
    public void write_withNullString_throwsNullPointerException() throws IOException {
        writer.write((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void write_withNullCharArray_throwsNullPointerException() throws IOException {
        writer.write((char[]) null);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void writeString_withOutOfBoundsRequest_throwsStringIndexOutOfBoundsException() throws IOException {
        writer.write("abc", 1, 3); // Tries to read past the end of the string
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void writeCharArray_withOutOfBoundsRequest_throwsArrayIndexOutOfBoundsException() throws IOException {
        writer.write(new char[]{'a', 'b', 'c'}, 1, 3); // Tries to read past the end of the array
    }

    @Test
    public void write_withZeroLength_doesNothing() throws IOException {
        writer.write("abc", 1, 0);
        writer.write(new char[]{'a', 'b', 'c'}, 1, 0);
        writer.flush();
        assertEquals(0, outputStream.size());
    }

    /*
    /**********************************************************
    /* Buffer, flush, and close behavior
    /**********************************************************
    */

    @Test
    public void write_whenBufferOverflows_flushesToStream() throws IOException {
        // The default buffer size is 2000.
        int bufferSize = 2000;
        char[] longArray = new char[bufferSize];
        Arrays.fill(longArray, 'A');

        // Write enough to fill the buffer, but not trigger a flush
        writer.write(longArray, 0, bufferSize - 4);
        assertEquals(0, outputStream.size());

        // Write more to trigger an automatic flush
        writer.write(longArray, 0, 4);
        assertTrue("Stream should have been flushed automatically", outputStream.size() > 0);

        writer.flush();
        assertEquals(bufferSize, outputStream.size());
    }

    @Test
    public void flush_writesBufferedOutputToStream() throws IOException {
        writer.write('X');
        // Data should be in the buffer, not the stream yet
        assertEquals(0, outputStream.size());

        writer.flush();
        // After flush, data should be in the stream
        assertEquals(1, outputStream.size());
        assertEquals("X", outputStream.toString());
    }

    @Test
    public void flush_onWriterWithNullStream_doesNothing() throws IOException {
        UTF8Writer nullStreamWriter = new UTF8Writer(createContext(false), null);
        nullStreamWriter.flush(); // Should not throw
    }

    @Test
    public void close_flushesBufferAndIsIdempotent() throws IOException {
        writer.write('Z');
        writer.close();
        assertEquals(1, outputStream.size());

        // Calling close again should not cause an error
        writer.close();
    }

    /**
     * Tests that writing to a writer with a null output stream works until the
     * internal buffer is full, at which point a flush is triggered, causing a
     * NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void write_whenOutputStreamIsNullAndBufferOverflows_throwsNullPointerException() throws IOException {
        IOContext context = createContext(false);
        // The default buffer size is 2000.
        byte[] buffer = context.allocWriteEncodingBuffer();
        int bufferSize = buffer.length;
        context.releaseWriteEncodingBuffer(buffer);

        UTF8Writer nullStreamWriter = new UTF8Writer(context, null);

        // Write enough characters to force a flush, which will fail on the null stream
        for (int i = 0; i < bufferSize; i++) {
            nullStreamWriter.write('A');
        }
    }

    /*
    /**********************************************************
    /* Static helper method tests
    /**********************************************************
    */

    @Test
    public void illegalSurrogateDesc_forHighSurrogate_returnsCorrectMessage() {
        String msg = UTF8Writer.illegalSurrogateDesc(HIGH_SURROGATE_FIRST);
        assertEquals("Unmatched first part of surrogate pair (0xd800)", msg);
    }

    @Test
    public void illegalSurrogate_forUnmatchedSecondPart_throwsIOException() throws IOException {
        try {
            UTF8Writer.illegalSurrogate(LOW_SURROGATE_FIRST);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unmatched second part of surrogate pair"));
        }
    }
}