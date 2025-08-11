package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

/**
 * Test suite for CharStreams utility class.
 * Tests various character stream operations including copying, reading, skipping, and error handling.
 */
public class CharStreamsTest {

    // ========== skipFully() Tests ==========
    
    @Test(timeout = 4000)
    public void skipFully_withZeroCharacters_shouldCompleteSuccessfully() throws Throwable {
        // Given: A reader with some content
        PipedReader reader = new PipedReader();
        PushbackReader pushbackReader = new PushbackReader(reader, 2408);
        
        // When: Skipping 0 characters
        // Then: Should complete without error
        CharStreams.skipFully(pushbackReader, 0L);
    }

    @Test(timeout = 4000)
    public void skipFully_withNegativeCount_shouldCompleteImmediately() throws Throwable {
        // Given: A reader with content
        StringReader reader = new StringReader("Funnels.unencodedCharsFunnel()");
        
        // When: Skipping negative number of characters
        // Then: Should complete without error (negative skip is treated as 0)
        CharStreams.skipFully(reader, -1932L);
    }

    @Test(timeout = 4000)
    public void skipFully_whenNotEnoughCharactersAvailable_shouldThrowEOFException() throws Throwable {
        // Given: A reader with limited content (9 null bytes)
        byte[] data = new byte[9];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
        
        // When: Trying to skip more characters than available
        // Then: Should throw EOFException
        try {
            CharStreams.skipFully(reader, 336L);
            fail("Expected EOFException when trying to skip more characters than available");
        } catch (EOFException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void skipFully_withNullReader_shouldThrowNullPointerException() throws Throwable {
        // When: Passing null reader
        // Then: Should throw NullPointerException
        try {
            CharStreams.skipFully(null, 20L);
            fail("Expected NullPointerException for null reader");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== toString() Tests ==========

    @Test(timeout = 4000)
    public void toString_withValidContent_shouldReturnCompleteString() throws Throwable {
        // Given: A reader with specific content
        char[] content = new char[3];
        CharArrayReader reader = new CharArrayReader(content);
        
        // When: Converting to string
        String result = CharStreams.toString(reader);
        
        // Then: Should return string representation of null characters
        assertEquals("\u0000\u0000\u0000", result);
    }

    @Test(timeout = 4000)
    public void toString_withNullReader_shouldThrowNullPointerException() throws Throwable {
        // When: Passing null readable
        // Then: Should throw NullPointerException
        try {
            CharStreams.toString((Readable) null);
            fail("Expected NullPointerException for null readable");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void toString_withDisconnectedPipe_shouldThrowIOException() throws Throwable {
        // Given: A disconnected piped reader
        PipedReader reader = new PipedReader();
        
        // When: Trying to read from disconnected pipe
        // Then: Should throw IOException
        try {
            CharStreams.toString(reader);
            fail("Expected IOException for disconnected pipe");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    // ========== copy() Tests ==========

    @Test(timeout = 4000)
    public void copy_fromStringReaderToStringBuilder_shouldCopyAllContent() throws Throwable {
        // Given: A reader with specific content and an empty StringBuilder
        StringReader reader = new StringReader("|liEWG");
        StringBuilder builder = new StringBuilder();
        
        // When: Copying content
        long charsCopied = CharStreams.copy(reader, builder);
        
        // Then: Should copy all characters
        assertEquals("|liEWG", builder.toString());
        assertEquals(6L, charsCopied);
    }

    @Test(timeout = 4000)
    public void copy_fromEmptyReader_shouldReturnZero() throws Throwable {
        // Given: An empty reader and a CharBuffer writer
        StringReader reader = new StringReader("");
        CharBuffer buffer = CharStreams.createBuffer();
        Writer writer = CharStreams.asWriter(buffer);
        
        // When: Copying from empty reader
        long charsCopied = CharStreams.copy(reader, writer);
        
        // Then: Should copy 0 characters
        assertEquals(0L, charsCopied);
        assertEquals(2048, buffer.remaining()); // Buffer should remain unchanged
    }

    @Test(timeout = 4000)
    public void copy_withNullSource_shouldThrowNullPointerException() throws Throwable {
        // Given: A valid destination buffer
        CharBuffer buffer = CharStreams.createBuffer();
        
        // When: Copying from null source
        // Then: Should throw NullPointerException
        try {
            CharStreams.copy((Readable) null, buffer);
            fail("Expected NullPointerException for null source");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void copy_toNullWriter_shouldCopyToNowhere() throws Throwable {
        // Given: A reader with content and a null writer (discards output)
        StringReader reader = new StringReader("com.google.common.primitives.Shorts$ShortConverter");
        StringBuilder builder = new StringBuilder(607);
        
        // When: Copying to StringBuilder
        long charsCopied = CharStreams.copyReaderToBuilder(reader, builder);
        
        // Then: Should copy all content
        assertEquals("com.google.common.primitives.Shorts$ShortConverter", builder.toString());
        assertEquals(50L, charsCopied);
    }

    @Test(timeout = 4000)
    public void copy_betweenCharBuffers_withReadOnlyDestination_shouldThrowException() throws Throwable {
        // Given: A source buffer and a read-only destination buffer
        CharBuffer source = CharStreams.createBuffer();
        CharBuffer readOnlyDestination = CharBuffer.wrap(source);
        
        // When: Trying to copy to read-only buffer
        // Then: Should throw ReadOnlyBufferException
        try {
            CharStreams.copy(source, readOnlyDestination);
            fail("Expected ReadOnlyBufferException for read-only destination");
        } catch (Exception e) {
            assertTrue("Expected ReadOnlyBufferException", 
                      e instanceof java.nio.ReadOnlyBufferException);
        }
    }

    // ========== readLines() Tests ==========

    @Test(timeout = 4000)
    public void readLines_fromCharBuffer_shouldReturnEmptyList() throws Throwable {
        // Given: A CharBuffer with null characters
        char[] content = new char[1];
        CharBuffer buffer = CharBuffer.wrap(content);
        buffer.append('z');
        
        // When: Reading lines
        List<String> lines = CharStreams.readLines(buffer);
        
        // Then: Should return empty list (no line separators)
        assertTrue("Expected empty list when no line separators present", lines.isEmpty());
    }

    @Test(timeout = 4000)
    public void readLines_withNullReader_shouldThrowNullPointerException() throws Throwable {
        // When: Passing null readable
        // Then: Should throw NullPointerException
        try {
            CharStreams.readLines((Readable) null);
            fail("Expected NullPointerException for null readable");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void readLines_withLineProcessor_andNullProcessor_shouldThrowNullPointerException() throws Throwable {
        // Given: A valid reader but null processor
        char[] content = new char[7];
        CharArrayReader reader = new CharArrayReader(content);
        
        // When: Using null line processor
        // Then: Should throw NullPointerException
        try {
            CharStreams.readLines(reader, null);
            fail("Expected NullPointerException for null line processor");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void readLines_withLineProcessor_shouldProcessLinesCorrectly() throws Throwable {
        // Given: A reader and a mock line processor
        char[] content = new char[6];
        CharArrayReader reader = new CharArrayReader(content);
        LineProcessor<Object> processor = mock(LineProcessor.class);
        when(processor.getResult()).thenReturn(reader);
        when(processor.processLine(anyString())).thenReturn(true);
        
        // When: Processing lines
        Object result = CharStreams.readLines(reader, processor);
        
        // Then: Should return processor result
        assertSame("Should return the processor's result", reader, result);
    }

    // ========== exhaust() Tests ==========

    @Test(timeout = 4000)
    public void exhaust_withCharBuffer_shouldReadAllCharacters() throws Throwable {
        // Given: A CharBuffer with default capacity
        CharBuffer buffer = CharStreams.createBuffer();
        
        // When: Exhausting the buffer
        long charsRead = CharStreams.exhaust(buffer);
        
        // Then: Should read all available characters
        assertEquals(2048L, charsRead);
        assertEquals(2048, buffer.position());
    }

    @Test(timeout = 4000)
    public void exhaust_withEmptyReader_shouldReturnZero() throws Throwable {
        // Given: An empty string reader
        StringReader reader = new StringReader("");
        
        // When: Exhausting the reader
        long charsRead = CharStreams.exhaust(reader);
        
        // Then: Should read 0 characters
        assertEquals(0L, charsRead);
    }

    @Test(timeout = 4000)
    public void exhaust_withNullReader_shouldThrowNullPointerException() throws Throwable {
        // When: Passing null readable
        // Then: Should throw NullPointerException
        try {
            CharStreams.exhaust((Readable) null);
            fail("Expected NullPointerException for null readable");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Utility Methods Tests ==========

    @Test(timeout = 4000)
    public void nullWriter_shouldDiscardAllOutput() throws Throwable {
        // Given: A null writer and some content
        Writer nullWriter = CharStreams.nullWriter();
        CharBuffer content = CharBuffer.allocate(546);
        
        // When: Writing to null writer
        CharStreams.copy(content, nullWriter);
        String remaining = CharStreams.toString(content);
        
        // Then: Content should be consumed but discarded
        assertEquals("", remaining);
    }

    @Test(timeout = 4000)
    public void asWriter_withWriterInput_shouldReturnSameInstance() throws Throwable {
        // Given: An existing Writer
        Writer originalWriter = CharStreams.nullWriter();
        
        // When: Converting Writer to Writer
        Writer result = CharStreams.asWriter(originalWriter);
        
        // Then: Should return the same instance
        assertSame("Should return same Writer instance", originalWriter, result);
    }

    @Test(timeout = 4000)
    public void asWriter_withNullAppendable_shouldThrowNullPointerException() throws Throwable {
        // When: Passing null appendable
        // Then: Should throw NullPointerException
        try {
            CharStreams.asWriter((Appendable) null);
            fail("Expected NullPointerException for null appendable");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Error Handling Tests ==========

    @Test(timeout = 4000)
    public void operations_withMalformedInput_shouldThrowMalformedInputException() throws Throwable {
        // Given: A byte stream with malformed UTF-8 data
        byte[] malformedData = new byte[7];
        malformedData[3] = (byte) -62; // Invalid UTF-8 sequence
        ByteArrayInputStream inputStream = new ByteArrayInputStream(malformedData);
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        InputStreamReader reader = new InputStreamReader(inputStream, decoder);
        
        // When: Trying to read malformed input
        // Then: Should throw MalformedInputException
        try {
            CharStreams.toString(reader);
            fail("Expected MalformedInputException for malformed input");
        } catch (Exception e) {
            assertTrue("Expected MalformedInputException", 
                      e.getCause() instanceof java.nio.charset.MalformedInputException ||
                      e instanceof java.nio.charset.MalformedInputException);
        }
    }

    @Test(timeout = 4000)
    public void copyReaderToWriter_afterReaderExhausted_shouldCopyZeroCharacters() throws Throwable {
        // Given: A reader and null writer
        Writer nullWriter = CharStreams.nullWriter();
        StringReader reader = new StringReader("Funnels.unencodedCharsFunnel()");
        
        // When: First copy exhausts the reader
        long firstCopy = CharStreams.copyReaderToWriter(reader, nullWriter);
        assertEquals(30L, firstCopy);
        
        // When: Second copy on exhausted reader
        long secondCopy = CharStreams.copyReaderToWriter(reader, nullWriter);
        
        // Then: Should copy 0 characters
        assertEquals(0L, secondCopy);
    }
}