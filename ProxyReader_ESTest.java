package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.apache.commons.io.input.CloseShieldReader;
import org.apache.commons.io.input.TaggedReader;
import org.apache.commons.io.input.TeeReader;

/**
 * Test suite for ProxyReader and its concrete implementations.
 * Tests the basic functionality, error handling, and edge cases.
 */
public class ProxyReader_ESTest {

    private static final String SAMPLE_TEXT = "Hello World";
    private static final String EMPTY_TEXT = "";
    private static final int BUFFER_SIZE = 1024;

    // ========== Hook Method Tests ==========
    
    @Test(timeout = 4000)
    public void testBeforeReadHook_shouldNotThrowException() throws Throwable {
        // Given: A TeeReader with valid input and output streams
        PipedReader input = new PipedReader(BUFFER_SIZE);
        CharArrayWriter output = new CharArrayWriter(BUFFER_SIZE);
        TeeReader teeReader = new TeeReader(input, output);
        
        // When: beforeRead hook is called
        // Then: No exception should be thrown
        teeReader.beforeRead(0);
    }

    @Test(timeout = 4000)
    public void testAfterReadHook_shouldNotThrowException() throws Throwable {
        // Given: A CloseShieldReader wrapping an empty StringReader
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        
        // When: afterRead hook is called
        // Then: No exception should be thrown
        reader.afterRead(1139);
    }

    // ========== Mark and Reset Tests ==========

    @Test(timeout = 4000)
    public void testMark_withSupportedReader_shouldSucceed() throws Throwable {
        // Given: A TaggedReader wrapping a StringReader (which supports mark)
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: mark is called
        // Then: No exception should be thrown
        reader.mark(47);
    }

    @Test(timeout = 4000)
    public void testReset_withSupportedReader_shouldSucceed() throws Throwable {
        // Given: A TaggedReader wrapping a StringReader with marked position
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: reset is called
        // Then: No exception should be thrown
        reader.reset();
    }

    @Test(timeout = 4000)
    public void testMarkSupported_withSupportingReader_shouldReturnTrue() throws Throwable {
        // Given: A TeeReader wrapping a StringReader (which supports mark)
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        CharArrayWriter output = new CharArrayWriter(BUFFER_SIZE);
        TeeReader reader = new TeeReader(stringReader, output);
        
        // When: markSupported is called
        boolean result = reader.markSupported();
        
        // Then: Should return true
        assertTrue("StringReader supports mark, so TeeReader should too", result);
    }

    @Test(timeout = 4000)
    public void testMarkSupported_withNonSupportingReader_shouldReturnFalse() throws Throwable {
        // Given: A TaggedReader wrapping a PipedReader (which doesn't support mark)
        PipedReader pipedReader = new PipedReader();
        TaggedReader reader = new TaggedReader(pipedReader);
        
        // When: markSupported is called
        boolean result = reader.markSupported();
        
        // Then: Should return false
        assertFalse("PipedReader doesn't support mark, so TaggedReader shouldn't either", result);
    }

    // ========== Skip Tests ==========

    @Test(timeout = 4000)
    public void testSkip_withAvailableCharacters_shouldReturnActualSkipped() throws Throwable {
        // Given: A TaggedReader with 11 characters available
        StringReader stringReader = new StringReader(SAMPLE_TEXT); // "Hello World" = 11 chars
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: Attempting to skip more characters than available
        long skipped = reader.skip(75);
        
        // Then: Should return actual number of characters skipped
        assertEquals("Should skip all 11 available characters", 11L, skipped);
    }

    @Test(timeout = 4000)
    public void testSkip_withNegativeValue_shouldReturnZero() throws Throwable {
        // Given: A TaggedReader with empty content
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: Attempting to skip negative number of characters
        long skipped = reader.skip(-1);
        
        // Then: Should return 0
        assertEquals("Skipping negative characters should return 0", 0L, skipped);
    }

    // ========== Read Tests ==========

    @Test(timeout = 4000)
    public void testRead_singleCharacter_shouldReturnCharacterValue() throws Throwable {
        // Given: A CloseShieldReader with known content
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        
        // When: Reading single character
        int result = reader.read();
        
        // Then: Should return ASCII value of '$'
        assertEquals("Should return ASCII value of '$'", 36, result);
    }

    @Test(timeout = 4000)
    public void testRead_fromEmptyStream_shouldReturnMinusOne() throws Throwable {
        // Given: A TaggedReader with empty content
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: Reading from empty stream
        int result = reader.read();
        
        // Then: Should return -1 (EOF)
        assertEquals("Reading from empty stream should return -1", -1, result);
    }

    @Test(timeout = 4000)
    public void testRead_intoCharArray_shouldFillBufferAndReturnCount() throws Throwable {
        // Given: A TaggedReader with known content
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        char[] buffer = new char[6];
        
        // When: Reading into character array
        int charsRead = reader.read(buffer);
        
        // Then: Should read 6 characters and return count
        assertEquals("Should read 6 characters", 6, charsRead);
    }

    @Test(timeout = 4000)
    public void testRead_intoCharArrayFromEmptyStream_shouldReturnMinusOne() throws Throwable {
        // Given: A TaggedReader with empty content
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        char[] buffer = new char[3];
        
        // When: Reading into character array from empty stream
        int charsRead = reader.read(buffer);
        
        // Then: Should return -1 (EOF)
        assertEquals("Reading from empty stream should return -1", -1, charsRead);
    }

    @Test(timeout = 4000)
    public void testRead_intoCharArrayWithOffsetAndLength_shouldRespectBounds() throws Throwable {
        // Given: A CloseShieldReader with known content
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        CloseShieldReader reader = CloseShieldReader.wrap(new TaggedReader(stringReader));
        char[] buffer = new char[4];
        
        // When: Reading 1 character at offset 1
        int charsRead = reader.read(buffer, 1, 1);
        
        // Then: Should read 1 character at the correct position
        assertEquals("Should read 1 character", 1, charsRead);
        assertArrayEquals("Character should be placed at offset 1", 
                         new char[] {'\u0000', 'H', '\u0000', '\u0000'}, buffer);
    }

    @Test(timeout = 4000)
    public void testRead_zeroLengthRequest_shouldReturnZero() throws Throwable {
        // Given: A TaggedReader with content
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        char[] buffer = new char[1];
        
        // When: Requesting to read 0 characters
        int charsRead = reader.read(buffer, 0, 0);
        
        // Then: Should return 0
        assertEquals("Reading 0 characters should return 0", 0, charsRead);
    }

    // ========== CharBuffer Read Tests ==========

    @Test(timeout = 4000)
    public void testRead_intoCharBuffer_shouldReturnCharacterCount() throws Throwable {
        // Given: A TaggedReader with known content
        StringReader stringReader = new StringReader("org.apache.commons.io.input.ProxyReader");
        TaggedReader reader = new TaggedReader(stringReader);
        CharBuffer buffer = CharBuffer.allocate(1366);
        
        // When: Reading into CharBuffer
        int charsRead = reader.read(buffer);
        
        // Then: Should read all 39 characters
        assertEquals("Should read all 39 characters", 39, charsRead);
    }

    @Test(timeout = 4000)
    public void testRead_intoEmptyCharBuffer_shouldReturnZero() throws Throwable {
        // Given: A TaggedReader with empty content and empty CharBuffer
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        CharBuffer buffer = CharBuffer.wrap("");
        
        // When: Reading into empty CharBuffer
        int charsRead = reader.read(buffer);
        
        // Then: Should return 0 (no space in buffer)
        assertEquals("Reading into empty CharBuffer should return 0", 0, charsRead);
    }

    @Test(timeout = 4000)
    public void testRead_intoCharBufferFromEmptyStream_shouldReturnMinusOne() throws Throwable {
        // Given: A TaggedReader with empty content and available buffer space
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        CharBuffer buffer = CharBuffer.wrap(new char[4]);
        
        // When: Reading into CharBuffer from empty stream
        int charsRead = reader.read(buffer);
        
        // Then: Should return -1 (EOF)
        assertEquals("Reading from empty stream should return -1", -1, charsRead);
    }

    // ========== Ready Tests ==========

    @Test(timeout = 4000)
    public void testReady_withAvailableData_shouldReturnTrue() throws Throwable {
        // Given: A CloseShieldReader with available data
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        
        // When: Checking if reader is ready
        boolean isReady = reader.ready();
        
        // Then: Should return true
        assertTrue("Reader with available data should be ready", isReady);
    }

    @Test(timeout = 4000)
    public void testReady_afterClose_shouldReturnFalse() throws Throwable {
        // Given: A CloseShieldReader that has been closed
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        reader.close();
        
        // When: Checking if closed reader is ready
        boolean isReady = reader.ready();
        
        // Then: Should return false
        assertFalse("Closed reader should not be ready", isReady);
    }

    // ========== Close Tests ==========

    @Test(timeout = 4000)
    public void testClose_shouldNotThrowException() throws Throwable {
        // Given: A TaggedReader wrapping a PipedReader
        PipedReader pipedReader = new PipedReader();
        TaggedReader reader = new TaggedReader(pipedReader);
        
        // When: Closing the reader
        // Then: No exception should be thrown
        reader.close();
    }

    @Test(timeout = 4000)
    public void testRead_afterClose_shouldReturnMinusOne() throws Throwable {
        // Given: A CloseShieldReader that has been closed
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader reader = CloseShieldReader.wrap(pipedReader);
        char[] buffer = new char[4];
        reader.close();
        
        // When: Attempting to read after close
        int result = reader.read(buffer, 0, 1041);
        
        // Then: Should return -1
        assertEquals("Reading after close should return -1", -1, result);
    }

    // ========== Error Handling Tests ==========

    @Test(timeout = 4000, expected = IOException.class)
    public void testSkip_withDisconnectedPipe_shouldThrowIOException() throws Throwable {
        // Given: A TaggedReader wrapping a disconnected PipedReader
        PipedReader pipedReader = new PipedReader(BUFFER_SIZE);
        TaggedReader reader = new TaggedReader(pipedReader);
        
        // When: Attempting to skip on disconnected pipe
        // Then: Should throw IOException
        reader.skip(BUFFER_SIZE);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testSkip_withNegativeValue_onPipedReader_shouldThrowIllegalArgumentException() throws Throwable {
        // Given: A TaggedReader wrapping a PipedReader
        PipedReader pipedReader = new PipedReader();
        TaggedReader reader = new TaggedReader(pipedReader);
        
        // When: Attempting to skip negative number of characters
        // Then: Should throw IllegalArgumentException
        reader.skip(-1L);
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void testMark_withNegativeReadAheadLimit_shouldThrowIllegalArgumentException() throws Throwable {
        // Given: A TaggedReader wrapping a StringReader
        StringReader stringReader = new StringReader(SAMPLE_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        
        // When: Calling mark with negative read-ahead limit
        // Then: Should throw IllegalArgumentException
        reader.mark(-1520);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testRead_withNullCharArray_shouldThrowNullPointerException() throws Throwable {
        // Given: A CloseShieldReader with content
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        
        // When: Attempting to read into null array
        // Then: Should throw NullPointerException
        reader.read((char[]) null, 63, 63);
    }

    @Test(timeout = 4000, expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidArrayBounds_shouldThrowIndexOutOfBoundsException() throws Throwable {
        // Given: A CloseShieldReader and empty char array
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        CloseShieldReader reader = CloseShieldReader.wrap(stringReader);
        char[] emptyArray = new char[0];
        
        // When: Attempting to read beyond array bounds
        // Then: Should throw IndexOutOfBoundsException
        reader.read(emptyArray, 49, 49);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testRead_withNullCharBuffer_shouldThrowNullPointerException() throws Throwable {
        // Given: A TaggedReader
        PipedReader pipedReader = new PipedReader();
        TaggedReader reader = new TaggedReader(pipedReader);
        
        // When: Attempting to read into null CharBuffer
        // Then: Should throw NullPointerException
        reader.read((CharBuffer) null);
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void testHandleIOException_shouldRethrowException() throws Throwable {
        // Given: A TaggedReader and an IOException
        PipedReader pipedReader = new PipedReader(2398);
        TaggedReader reader = new TaggedReader(pipedReader);
        IOException testException = new IOException("Test exception message");
        
        // When: Handling an IOException
        // Then: Should rethrow the exception
        reader.handleIOException(testException);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testHandleIOException_withNullException_shouldThrowNullPointerException() throws Throwable {
        // Given: A CloseShieldReader
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader reader = CloseShieldReader.wrap(pipedReader);
        
        // When: Handling a null IOException
        // Then: Should throw NullPointerException
        reader.handleIOException(null);
    }

    // ========== Edge Case Tests ==========

    @Test(timeout = 4000)
    public void testRead_emptyCharArray_shouldReturnZero() throws Throwable {
        // Given: A TaggedReader with empty content and empty char array
        StringReader stringReader = new StringReader(EMPTY_TEXT);
        TaggedReader reader = new TaggedReader(stringReader);
        char[] emptyArray = new char[0];
        
        // When: Reading into empty array
        int result = reader.read(emptyArray);
        
        // Then: Should return 0
        assertEquals("Reading into empty array should return 0", 0, result);
    }
}