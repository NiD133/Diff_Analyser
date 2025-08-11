package com.google.common.io;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * Tests for CharSequenceReader functionality including reading, marking, 
 * skipping, and error handling scenarios.
 */
public class CharSequenceReaderTest {

    private static final String SAMPLE_TEXT = "Hello World";
    private static final String EMPTY_TEXT = "";
    private static final char[] SAMPLE_CHARS = {'a', 'b', 'c', 'd', 'e', 'f'};
    
    @Test
    public void markSupported_shouldReturnTrue() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        assertTrue("CharSequenceReader should support marking", reader.markSupported());
    }

    @Test
    public void mark_withValidReadAheadLimit_shouldSucceed() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.mark(10);
        
        assertTrue("Mark operation should succeed", reader.markSupported());
    }

    @Test(expected = IllegalArgumentException.class)
    public void mark_withNegativeReadAheadLimit_shouldThrowException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.mark(-1);
    }

    @Test
    public void readSingleCharacter_fromNonEmptySequence_shouldReturnCharacterValue() throws IOException {
        char[] chars = {'g', '\0', '\0'};
        CharSequenceReader reader = createReaderWithCharArray(chars);
        
        int result = reader.read();
        
        assertEquals("Should return ASCII value of 'g'", 103, result);
    }

    @Test
    public void readSingleCharacter_fromEmptySequence_shouldReturnEndOfStream() throws IOException {
        CharSequenceReader reader = createReaderWithText(EMPTY_TEXT);
        
        int result = reader.read();
        
        assertEquals("Should return -1 for end of stream", -1, result);
    }

    @Test
    public void readIntoCharArray_withAvailableData_shouldReturnNumberOfCharsRead() throws IOException {
        char[] sourceChars = {'a', 'b', 'c'};
        CharSequenceReader reader = createReaderWithCharArray(sourceChars);
        char[] buffer = new char[5];
        
        int charsRead = reader.read(buffer);
        
        assertEquals("Should read all available characters", 3, charsRead);
    }

    @Test
    public void readIntoCharArray_withZeroLength_shouldReturnZero() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        char[] buffer = new char[10];
        
        int charsRead = reader.read(buffer, 0, 0);
        
        assertEquals("Reading zero characters should return 0", 0, charsRead);
    }

    @Test
    public void readIntoCharArray_afterEndOfStream_shouldReturnMinusOne() throws IOException {
        char[] sourceChars = {'x'};
        CharSequenceReader reader = createReaderWithCharArray(sourceChars);
        char[] buffer = new char[5];
        
        // Read all available data first
        reader.read(buffer);
        
        // Try to read again
        int result = reader.read(buffer, 0, 0);
        
        assertEquals("Should return -1 when no more data available", -1, result);
    }

    @Test
    public void readIntoCharBuffer_withWritableBuffer_shouldReturnCharsRead() throws IOException {
        char[] sourceChars = {'x'};
        CharSequenceReader reader = createReaderWithCharArray(sourceChars);
        CharBuffer buffer = CharBuffer.allocate(5);
        
        int charsRead = reader.read(buffer);
        
        assertEquals("Should read available characters into buffer", 0, charsRead);
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void readIntoCharBuffer_withReadOnlyBuffer_shouldThrowException() throws IOException {
        char[] sourceChars = {'x'};
        CharSequenceReader reader = createReaderWithCharArray(sourceChars);
        CharBuffer readOnlyBuffer = CharBuffer.wrap("readonly").asReadOnlyBuffer();
        
        reader.read(readOnlyBuffer);
    }

    @Test
    public void skip_withValidCount_shouldReturnNumberOfCharsSkipped() throws IOException {
        CharSequenceReader reader = createReaderWithText("abcdefghij");
        
        long skipped = reader.skip(5);
        
        assertEquals("Should skip requested number of characters", 5L, skipped);
    }

    @Test
    public void skip_withZeroCount_shouldReturnZero() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        long skipped = reader.skip(0);
        
        assertEquals("Skipping zero characters should return 0", 0L, skipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void skip_withNegativeCount_shouldThrowException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.skip(-1L);
    }

    @Test
    public void ready_withOpenReader_shouldReturnTrue() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        boolean isReady = reader.ready();
        
        assertTrue("Open reader should be ready", isReady);
    }

    @Test
    public void reset_withOpenReader_shouldSucceed() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.reset();
        
        assertTrue("Reset should succeed on open reader", reader.markSupported());
    }

    // Tests for closed reader behavior
    @Test(expected = IOException.class)
    public void readSingleChar_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.read();
    }

    @Test(expected = IOException.class)
    public void readIntoCharArray_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.read(new char[5], 0, 0);
    }

    @Test(expected = IOException.class)
    public void readIntoCharBuffer_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.read(CharBuffer.allocate(5));
    }

    @Test(expected = IOException.class)
    public void skip_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.skip(1L);
    }

    @Test(expected = IOException.class)
    public void ready_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.ready();
    }

    @Test(expected = IOException.class)
    public void mark_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.mark(10);
    }

    @Test(expected = IOException.class)
    public void reset_afterClose_shouldThrowIOException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        reader.close();
        
        reader.reset();
    }

    // Tests for invalid arguments
    @Test(expected = NullPointerException.class)
    public void constructor_withNullCharSequence_shouldThrowNullPointerException() {
        new CharSequenceReader(null);
    }

    @Test(expected = NullPointerException.class)
    public void readIntoCharArray_withNullArray_shouldThrowNullPointerException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.read(null, 0, 5);
    }

    @Test(expected = NullPointerException.class)
    public void readIntoCharBuffer_withNullBuffer_shouldThrowNullPointerException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.read((CharBuffer) null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readIntoCharArray_withNegativeOffset_shouldThrowIndexOutOfBoundsException() throws IOException {
        CharSequenceReader reader = createReaderWithText(SAMPLE_TEXT);
        
        reader.read(new char[5], -1, 2);
    }

    // Helper methods for creating test instances
    private CharSequenceReader createReaderWithText(String text) {
        return new CharSequenceReader(text);
    }

    private CharSequenceReader createReaderWithCharArray(char[] chars) {
        CharBuffer buffer = CharBuffer.wrap(chars);
        return new CharSequenceReader(buffer);
    }
}