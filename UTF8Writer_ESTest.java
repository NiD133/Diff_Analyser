package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.UTF8Writer;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.*;
import java.nio.CharBuffer;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class UTF8Writer_ESTest extends UTF8Writer_ESTest_scaffolding {

    static class TestSetup {
        static final StreamReadConstraints DEFAULT_READ_CONSTRAINTS = StreamReadConstraints.defaults();
        static final StreamWriteConstraints DEFAULT_WRITE_CONSTRAINTS = StreamWriteConstraints.defaults();
        static final ErrorReportConfiguration DEFAULT_ERROR_CONFIG = ErrorReportConfiguration.defaults();
        static final BufferRecycler BUFFER_RECYCLER = new BufferRecycler();
        static final ContentReference UNKNOWN_CONTENT = ContentReference.unknown();
        static final ContentReference REDACTED_CONTENT = ContentReference.redacted();

        static IOContext createIOContext(ContentReference contentRef, boolean managed) {
            return new IOContext(
                DEFAULT_READ_CONSTRAINTS,
                DEFAULT_WRITE_CONSTRAINTS,
                DEFAULT_ERROR_CONFIG,
                BUFFER_RECYCLER,
                contentRef,
                managed
            );
        }
    }

    // Helper method for common output stream setup
    private ByteArrayOutputStream createByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    /* ================================================
     * Tests for illegal surrogate handling
     * ================================================
     */

    @Test
    public void testIllegalSurrogateDesc_FirstPart() throws Throwable {
        String description = UTF8Writer.illegalSurrogateDesc(56319);
        assertEquals("Unmatched first part of surrogate pair (0xdbff)", description);
    }

    @Test(expected = IOException.class)
    public void testIllegalSurrogate_UnmatchedSecondPart() throws Throwable {
        UTF8Writer.illegalSurrogate(1114111); // 0x10FFFF
    }

    @Test
    public void testIllegalSurrogateDesc_UnmatchedFirst() throws Throwable {
        String description = UTF8Writer.illegalSurrogateDesc(55296); // 0xD800
        assertEquals("Unmatched first part of surrogate pair (0xd800)", description);
    }

    @Test(expected = IOException.class)
    public void testIllegalSurrogate_InvalidCharacter() throws Throwable {
        UTF8Writer.illegalSurrogate(-554); // Negative value
    }

    /* ================================================
     * Tests for surrogate conversion
     * ================================================
     */

    @Test
    public void testConvertSurrogate_ValidSecondPart() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        UTF8Writer writer = new UTF8Writer(context, null);
        int result = writer.convertSurrogate(56320); // Valid surrogate
        assertEquals(-56557568, result);
    }

    @Test(expected = IOException.class)
    public void testConvertSurrogate_InvalidSurrogatePair() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        UTF8Writer writer = new UTF8Writer(context, null);
        writer.convertSurrogate(20000000); // Invalid pair
    }

    @Test
    public void testConvertSurrogate_ValidSecondPartHigh() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, new PipedOutputStream());
        int result = writer.convertSurrogate(57343); // Valid surrogate
        assertEquals(-56556545, result);
    }

    /* ================================================
     * Tests for write operations
     * ================================================
     */

    @Test
    public void testWriteChar_ValidNonSurrogate() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.write(128); // Valid non-surrogate
        writer.flush();
        assertEquals(1, out.size());
    }

    @Test(expected = IOException.class)
    public void testWriteChar_UnmatchedSurrogate() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, new ByteArrayOutputStream());
        writer.write(57343); // Unmatched surrogate
    }

    @Test(expected = IOException.class)
    public void testWriteChar_InvalidHighCharacter() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        UTF8Writer writer = new UTF8Writer(context, new PipedOutputStream());
        writer.write(20000000); // Character beyond valid range
    }

    @Test
    public void testWriteChar_ValidHighCharacter() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        ContentReference contentRef = ContentReference.rawReference(true, null);
        IOContext context = TestSetup.createIOContext(contentRef, true);
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.write(1114111); // Valid high character
        writer.flush();
        assertEquals(4, out.size()); // UTF-8 encoding for 0x10FFFF
    }

    @Test
    public void testWriteChar_ValidSupplementary() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        ContentReference contentRef = ContentReference.rawReference(true, new Object());
        IOContext context = TestSetup.createIOContext(contentRef, false);
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.write(2048); // Valid supplementary character
        writer.flush();
        assertEquals(2, out.size()); // UTF-8 encoding for U+0800
    }

    @Test(expected = IOException.class)
    public void testWriteChar_BrokenSurrogatePair() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        StreamReadConstraints readConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints writeConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorConfig = ErrorReportConfiguration.defaults();
        BufferRecycler recycler = new BufferRecycler();
        ContentReference contentRef = ContentReference.unknown();
        IOContext context = new IOContext(readConstraints, writeConstraints, errorConfig, recycler, contentRef, true);
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.write(55296); // First part of surrogate
        writer.write(55296); // Invalid second part (should be low surrogate)
    }

    @Test
    public void testWriteCharArray_ValidCharacters() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        UTF8Writer writer = new UTF8Writer(context, out);
        char[] data = new char[7];
        writer.write(data, 0, 2); // Valid write
        writer.flush();
        assertEquals(0, out.size()); // Only writes when buffer full or flushed
    }

    @Test(expected = NullPointerException.class)
    public void testWriteCharArray_NullArray() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, new PipedOutputStream());
        writer.write((char[]) null); // Null array
    }

    @Test
    public void testWriteString_ValidString() throws Throwable {
        ByteArrayOutputStream out = createByteArrayOutputStream();
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.write("@"); // Valid ASCII
        writer.flush();
        assertEquals(1, out.size());
    }

    @Test(expected = NullPointerException.class)
    public void testWriteString_NullString() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, new PipedOutputStream());
        writer.write((String) null); // Null string
    }

    @Test
    public void testWriteString_EmptyString() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        UTF8Writer writer = new UTF8Writer(context, null);
        writer.write("", -19, -1981); // No-op write
    }

    /* ================================================
     * Tests for append operations
     * ================================================
     */

    @Test
    public void testAppendCharSequence_ValidData() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, true);
        CharBuffer buffer = CharBuffer.allocate(2);
        buffer.put('\u0080'); // Valid character
        buffer.flip();
        UTF8Writer writer = new UTF8Writer(context, new PipedOutputStream());
        writer.append(buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendCharSequence_NullContext() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, false);
        CharBuffer buffer = CharBuffer.allocate(7989);
        UTF8Writer writer = new UTF8Writer(context, null);
        writer.append(buffer);
        writer.append('C');
        writer.write(1000);
        writer.append('\u0000');
        writer.write(1);
        writer.write(2937);
        writer.append('+'); // Triggers NPE due to null output
    }

    /* ================================================
     * Tests for edge cases and error handling
     * ================================================
     */

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullContext() throws Throwable {
        new UTF8Writer(null, null); // Null IOContext
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_DuplicateAllocation() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        PipedOutputStream out = new PipedOutputStream();
        new UTF8Writer(context, out);
        new UTF8Writer(context, out); // Second allocation should fail
    }

    @Test(expected = NullPointerException.class)
    public void testFlush_NullOutputStream() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.REDACTED_CONTENT, true);
        UTF8Writer writer = new UTF8Writer(context, null);
        writer.flush(); // NPE on flush
    }

    @Test
    public void testClose_AlreadyClosed() throws Throwable {
        IOContext context = TestSetup.createIOContext(TestSetup.UNKNOWN_CONTENT, false);
        PipedOutputStream out = new PipedOutputStream();
        UTF8Writer writer = new UTF8Writer(context, out);
        writer.close();
        writer.close(); // Should be safe to close twice
    }
}