package com.fasterxml.jackson.core.io;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.UTF8Writer;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.nio.CharBuffer;

/**
 * Test suite for UTF8Writer functionality including:
 * - Basic character writing operations
 * - Unicode surrogate pair handling
 * - Error conditions and edge cases
 * - Stream operations (flush, close)
 */
public class UTF8Writer_Test {

    private IOContext ioContext;
    private ByteArrayOutputStream outputStream;
    private UTF8Writer writer;

    @Before
    public void setUp() {
        StreamReadConstraints readConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints writeConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorConfig = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentRef = ContentReference.unknown();
        
        ioContext = new IOContext(readConstraints, writeConstraints, errorConfig, 
                                 bufferRecycler, contentRef, false);
        outputStream = new ByteArrayOutputStream();
        writer = new UTF8Writer(ioContext, outputStream);
    }

    // ========== Surrogate Pair Tests ==========

    @Test
    public void testSurrogateDescriptionForFirstPart() throws Exception {
        String description = UTF8Writer.illegalSurrogateDesc(0xDBFF); // High surrogate
        assertEquals("Unmatched first part of surrogate pair (0xdbff)", description);
    }

    @Test
    public void testSurrogateDescriptionForSecondPart() throws Exception {
        String description = UTF8Writer.illegalSurrogateDesc(0xD800); // High surrogate
        assertEquals("Unmatched first part of surrogate pair (0xd800)", description);
    }

    @Test(expected = IOException.class)
    public void testIllegalSurrogateThrowsException() throws Exception {
        UTF8Writer.illegalSurrogate(0x10FFFF); // Invalid surrogate value
    }

    @Test
    public void testValidSurrogateConversion() throws Exception {
        int result = writer.convertSurrogate(0xDC00); // Low surrogate
        assertEquals(-56557568, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidSurrogateConversion() throws Exception {
        writer.convertSurrogate(20000000); // Invalid surrogate value
    }

    @Test
    public void testValidSurrogateConversionWithHighValue() throws Exception {
        int result = writer.convertSurrogate(0xDFFF); // High low surrogate
        assertEquals(-56556545, result);
    }

    // ========== Character Writing Tests ==========

    @Test
    public void testWriteSingleAsciiCharacter() throws Exception {
        writer.write('A');
        writer.flush();
        assertEquals("A", outputStream.toString());
    }

    @Test
    public void testWriteUnicodeCharacter() throws Exception {
        writer.write(0x80); // Non-ASCII character
        writer.flush();
        // Verify UTF-8 encoding occurred (exact bytes depend on encoding)
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testWrite2ByteUnicodeCharacter() throws Exception {
        writer.write(0x800); // 2-byte UTF-8 character
        writer.flush();
        assertTrue(outputStream.size() >= 2);
    }

    @Test
    public void testWrite3ByteUnicodeCharacter() throws Exception {
        writer.write(0x800); // 3-byte UTF-8 character range
        writer.flush();
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testWrite4ByteUnicodeCharacter() throws Exception {
        writer.write(0x10FFFF); // Maximum valid Unicode code point
        writer.flush();
        assertTrue(outputStream.size() > 0);
    }

    // ========== Error Condition Tests ==========

    @Test(expected = IOException.class)
    public void testWriteInvalidHighSurrogate() throws Exception {
        writer.write(0xDFFF); // Unmatched low surrogate
    }

    @Test(expected = IOException.class)
    public void testWriteCodePointTooLarge() throws Exception {
        writer.write(20000000); // Exceeds maximum Unicode value
    }

    @Test(expected = IOException.class)
    public void testWriteBrokenSurrogatePair() throws Exception {
        writer.write(0xD800); // High surrogate
        writer.write(0xD800); // Another high surrogate (invalid pair)
    }

    @Test(expected = IOException.class)
    public void testWriteHighSurrogateFollowedByNormalChar() throws Exception {
        writer.write(0xD800); // High surrogate
        writer.write('c'); // Normal character (breaks surrogate pair)
    }

    // ========== Array Writing Tests ==========

    @Test
    public void testWriteCharArray() throws Exception {
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        writer.write(chars);
        writer.flush();
        assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testWriteCharArrayWithUnicode() throws Exception {
        char[] chars = {0x80, 0x00}; // Mixed ASCII and Unicode
        writer.write(chars);
        writer.flush();
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testWriteCharArraySubset() throws Exception {
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        writer.write(chars, 1, 3); // Write "BCD"
        writer.flush();
        assertEquals("BCD", outputStream.toString());
    }

    @Test
    public void testWriteEmptyCharArraySubset() throws Exception {
        char[] chars = new char[5];
        writer.write(chars, 1000, -230); // Negative length should be handled
        // Should complete without error
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testWriteCharArrayBeyondBounds() throws Exception {
        char[] chars = new char[4];
        writer.write(chars, 0, 6); // Length exceeds array size
    }

    // ========== String Writing Tests ==========

    @Test
    public void testWriteString() throws Exception {
        writer.write("Hello World");
        writer.flush();
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    public void testWriteStringSubset() throws Exception {
        writer.write("Hello World", 6, 5); // Write "World"
        writer.flush();
        assertEquals("World", outputStream.toString());
    }

    @Test
    public void testWriteEmptyStringSubset() throws Exception {
        writer.write("", -19, -1981); // Edge case with negative values
        // Should complete without error
    }

    @Test(expected = NullPointerException.class)
    public void testWriteNullString() throws Exception {
        writer.write((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteNullStringWithOffset() throws Exception {
        writer.write((String) null, 0, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteNullCharArray() throws Exception {
        writer.write((char[]) null);
    }

    // ========== CharSequence Append Tests ==========

    @Test
    public void testAppendCharBuffer() throws Exception {
        CharBuffer buffer = CharBuffer.wrap("Test");
        writer.append(buffer);
        writer.flush();
        assertEquals("Test", outputStream.toString());
    }

    @Test
    public void testAppendCharBufferWithUnicode() throws Exception {
        char[] chars = {0x80, 0x00};
        CharBuffer buffer = CharBuffer.wrap(chars);
        writer.append(buffer);
        writer.flush();
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testAppendSingleCharacter() throws Exception {
        writer.append('X');
        writer.flush();
        assertEquals("X", outputStream.toString());
    }

    // ========== Stream Operation Tests ==========

    @Test
    public void testFlushEmptyBuffer() throws Exception {
        writer.flush(); // Should not throw exception
    }

    @Test
    public void testFlushWithContent() throws Exception {
        writer.write("test");
        writer.flush();
        assertEquals("test", outputStream.toString());
    }

    @Test
    public void testCloseWriter() throws Exception {
        writer.write("test");
        writer.close();
        assertEquals("test", outputStream.toString());
    }

    @Test
    public void testCloseWriterTwice() throws Exception {
        writer.close();
        writer.close(); // Should not throw exception
    }

    // ========== Constructor and Initialization Tests ==========

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullIOContext() throws Exception {
        new UTF8Writer(null, outputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void testReuseIOContextBuffer() throws Exception {
        // First writer uses the buffer
        new UTF8Writer(ioContext, outputStream);
        // Second writer should fail because buffer is already allocated
        new UTF8Writer(ioContext, new ByteArrayOutputStream());
    }

    // ========== Edge Case Tests ==========

    @Test
    public void testWriteWithNullOutputStream() throws Exception {
        IOContext nullStreamContext = new IOContext(
            StreamReadConstraints.defaults(),
            StreamWriteConstraints.defaults(), 
            ErrorReportConfiguration.defaults(),
            new BufferRecycler(),
            ContentReference.unknown(),
            false
        );
        
        UTF8Writer nullWriter = new UTF8Writer(nullStreamContext, null);
        
        // Writing should work, but flushing will fail
        nullWriter.write("test");
        
        try {
            nullWriter.flush();
            fail("Expected NullPointerException when flushing to null stream");
        } catch (NullPointerException expected) {
            // Expected behavior
        }
    }

    @Test
    public void testSurrogateConstant() {
        assertEquals(-56613888, UTF8Writer.SURROGATE_BASE);
    }
}