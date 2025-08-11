package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;

import static org.junit.Assert.*;

public class UTF8WriterTest {

    // -- Test helpers ---------------------------------------------------------------------------

    private static IOContext newIOContext(boolean managedResource) {
        StreamReadConstraints read = StreamReadConstraints.defaults();
        StreamWriteConstraints write = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errs = ErrorReportConfiguration.defaults();
        BufferRecycler recycler = new BufferRecycler();
        ContentReference content = ContentReference.unknown();
        return new IOContext(read, write, errs, recycler, content, managedResource);
    }

    private static UTF8Writer newWriter(OutputStream out) {
        return new UTF8Writer(newIOContext(true), out);
    }

    // -- Happy-path encoding --------------------------------------------------------------------

    @Test
    public void writesAsciiChar() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act
        w.write('A');
        w.flush();

        // Assert
        assertArrayEquals(new byte[] { 0x41 }, out.toByteArray());
    }

    @Test
    public void writesTwoByteUtf8ForLatin1() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act
        w.write("ö"); // U+00F6
        w.flush();

        // Assert
        assertArrayEquals(new byte[] { (byte) 0xC3, (byte) 0xB6 }, out.toByteArray());
    }

    @Test
    public void writesFourByteUtf8FromSurrogatePair() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);
        String grinningFace = "\uD83D\uDE00"; // U+1F600

        // Act
        w.write(grinningFace);
        w.flush();

        // Assert
        assertArrayEquals(new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80 }, out.toByteArray());
    }

    @Test
    public void appendCharSequenceWritesCharacters() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);
        CharBuffer cb = CharBuffer.wrap(new char[] { 'A', '\u00F6' });

        // Act
        w.append(cb);
        w.flush();

        // Assert
        assertArrayEquals(new byte[] { 0x41, (byte) 0xC3, (byte) 0xB6 }, out.toByteArray());
    }

    // -- Error handling around surrogates and code points ---------------------------------------

    @Test
    public void unmatchedLowSurrogateThrows() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act / Assert
        try {
            w.write(0xDFFF); // low surrogate without preceding high surrogate
            fail("Expected IOException for unmatched low surrogate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unmatched second part of surrogate pair"));
            assertTrue(e.getMessage().contains("0xdfff"));
        }
    }

    @Test
    public void highSurrogateFollowedByNonLowSurrogateThrows() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act
        w.write(0xD800); // high surrogate

        // Next char breaks the pair
        try {
            w.append('M');
            fail("Expected IOException for broken surrogate pair");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Broken surrogate pair"));
            assertTrue(e.getMessage().contains("0xd800"));
        }
    }

    @Test
    public void illegalCodePointTooHighThrows() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);
        int tooHigh = 0x1312D00; // > 0x10FFFF

        // Act / Assert
        try {
            w.write(tooHigh);
            fail("Expected IOException for illegal code point");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Illegal character point"));
            assertTrue(e.getMessage().contains("0x1312d00"));
        }
    }

    @Test
    public void convertSurrogateWithNoStoredFirstPartThrows() throws Exception {
        // Arrange
        UTF8Writer w = new UTF8Writer(newIOContext(false), new ByteArrayOutputStream());

        // Act / Assert
        try {
            // Call with an arbitrary value that is not a valid low surrogate
            w.convertSurrogate(0x1312D00);
            fail("Expected IOException due to missing first surrogate");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Broken surrogate pair"));
            assertTrue(e.getMessage().contains("first char 0x0"));
        }
    }

    // -- Static helpers -------------------------------------------------------------------------

    @Test
    public void illegalSurrogateHelpersProduceClearMessages() throws Exception {
        assertEquals("Unmatched first part of surrogate pair (0xd800)", UTF8Writer.illegalSurrogateDesc(0xD800));
        assertEquals("Unmatched first part of surrogate pair (0xdbff)", UTF8Writer.illegalSurrogateDesc(0xDBFF));

        try {
            UTF8Writer.illegalSurrogate(0x10FFFF);
            fail("Expected IOException for unmatched second surrogate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unmatched second part of surrogate pair"));
            assertTrue(e.getMessage().contains("0x10ffff"));
        }

        try {
            UTF8Writer.illegalSurrogate(-554);
            fail("Expected IOException for illegal code point");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Illegal character point"));
            assertTrue(e.getMessage().contains("0xfffffdd6"));
        }
    }

    // -- Resource management --------------------------------------------------------------------

    @Test
    public void closeIsIdempotentAndFlushesBuffer() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act
        w.write('B');
        w.close();
        w.close(); // should be a no-op

        // Assert
        assertArrayEquals(new byte[] { 0x42 }, out.toByteArray());
    }

    @Test
    public void ioContextBufferIsAllocatedOnlyOnce() {
        // Arrange
        IOContext ctx = newIOContext(true);

        // Act
        new UTF8Writer(ctx, new ByteArrayOutputStream());

        // Allocating another UTF8Writer with the same IOContext should fail
        try {
            new UTF8Writer(ctx, new ByteArrayOutputStream());
            fail("Expected IllegalStateException due to double allocation in IOContext");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Trying to call same allocXxx() method second time"));
        }
    }

    // -- No-op inputs ---------------------------------------------------------------------------

    @Test
    public void writingEmptyInputsProducesNoOutput() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = newWriter(out);

        // Act
        w.write(new char[0]);
        w.write("");
        w.flush();

        // Assert
        assertEquals(0, out.size());
    }

    // -- Null argument validation ---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void writingNullStringThrowsNpe() throws Exception {
        UTF8Writer w = newWriter(new ByteArrayOutputStream());
        w.write((String) null);
    }
}